/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.identity;

import cart.ai.shopping.infrastructure.in.rest.BaseFlowIT;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.RegisterRestRequest;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.UpdateUserRestRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for User management endpoints.
 * <p>
 * Extends {@link BaseFlowIT} to reuse shared infrastructure and JWT session helpers.
 * Each role block performs a real login to verify the full auth flow.
 * <p>
 * Roles tested: ADMIN, VENDOR, CUSTOMER
 *
 * @author Roberto Díaz
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserFlowIT extends BaseFlowIT {

    @AfterAll
    void tearDown() {
        // Remove users created by this IT that are not bootstrap users.
        // The vendor name change is left as-is since bootstrap users are reset on next test run.
        removeTestUsers("newcustomer@test.com", "disposable@test.com");
    }

    // =========================================================================
    // ADMIN BLOCK
    // =========================================================================

    @Test
    @Order(1)
    void adminCanLoginSuccessfully() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new cart.ai.shopping.infrastructure.in.rest.identity.dtos.LoginRestRequest(ADMIN_EMAIL, ADMIN_PASS))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.userId").isNotEmpty())
                .andExpect(jsonPath("$.roles", hasItem("ADMIN")))
                .andReturn();

        var response = objectMapper.readTree(result.getResponse().getContentAsString());
        adminToken = response.get("token").asText();
        adminUserId = response.get("userId").asText();
    }

    @Test
    @Order(2)
    void adminCanListAllUsers() throws Exception {
        ensureAdminLoggedIn();
        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    @Order(3)
    void adminCanGetAnyUser() throws Exception {
        ensureAdminLoggedIn();
        ensureVendorLoggedIn();
        mockMvc.perform(get("/api/users/" + vendorUserId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(VENDOR_EMAIL));
    }

    @Test
    @Order(4)
    void adminCanUpdateAnyUser() throws Exception {
        ensureAdminLoggedIn();
        ensureVendorLoggedIn();
        UpdateUserRestRequest request = new UpdateUserRestRequest(
                vendorUserId, "Updated Vendor Name", VENDOR_EMAIL, Set.of("VENDOR"), null
        );
        mockMvc.perform(put("/api/users/" + vendorUserId)
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Vendor Name"));
    }

    // =========================================================================
    // VENDOR BLOCK
    // =========================================================================

    @Test
    @Order(5)
    void vendorCanLoginSuccessfully() throws Exception {
        vendorToken = null; // Force re-login to assert the login response
        var response = login(VENDOR_EMAIL, VENDOR_PASS);
        vendorToken = response.get("token").asText();
        vendorUserId = response.get("userId").asText();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new cart.ai.shopping.infrastructure.in.rest.identity.dtos.LoginRestRequest(VENDOR_EMAIL, VENDOR_PASS))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.roles", hasItem("VENDOR")));
    }

    @Test
    @Order(6)
    void vendorCanGetOwnProfile() throws Exception {
        ensureVendorLoggedIn();
        mockMvc.perform(get("/api/users/" + vendorUserId)
                        .header("Authorization", "Bearer " + vendorToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(VENDOR_EMAIL));
    }

    @Test
    @Order(7)
    void vendorCanUpdateOwnProfile() throws Exception {
        ensureVendorLoggedIn();
        UpdateUserRestRequest request = new UpdateUserRestRequest(
                vendorUserId, "Vendor Self Update", VENDOR_EMAIL, Set.of("VENDOR"), null
        );
        mockMvc.perform(put("/api/users/" + vendorUserId)
                        .header("Authorization", "Bearer " + vendorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Vendor Self Update"));
    }

    @Test
    @Order(8)
    void vendorCannotListAllUsers() throws Exception {
        ensureVendorLoggedIn();
        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer " + vendorToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(9)
    void vendorCannotGetAnotherUsersProfile() throws Exception {
        ensureAdminLoggedIn();
        ensureVendorLoggedIn();
        mockMvc.perform(get("/api/users/" + adminUserId)
                        .header("Authorization", "Bearer " + vendorToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(10)
    void vendorCannotDeleteUsers() throws Exception {
        ensureAdminLoggedIn();
        ensureVendorLoggedIn();
        mockMvc.perform(delete("/api/users/" + adminUserId)
                        .header("Authorization", "Bearer " + vendorToken))
                .andExpect(status().isForbidden());
    }

    // =========================================================================
    // CUSTOMER BLOCK
    // =========================================================================

    @Test
    @Order(11)
    void customerCanRegisterSuccessfully() throws Exception {
        RegisterRestRequest request = new RegisterRestRequest(
                "New Customer", "newcustomer@test.com", "password123", null
        );
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.userId").isNotEmpty())
                .andExpect(jsonPath("$.roles", hasItem("CUSTOMER")));
    }

    @Test
    @Order(12)
    void customerCanLoginSuccessfully() throws Exception {
        customerToken = null; // Force re-login to assert the login response
        var response = login(CUSTOMER_EMAIL, CUSTOMER_PASS);
        customerToken = response.get("token").asText();
        customerUserId = response.get("userId").asText();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new cart.ai.shopping.infrastructure.in.rest.identity.dtos.LoginRestRequest(CUSTOMER_EMAIL, CUSTOMER_PASS))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.roles", hasItem("CUSTOMER")));
    }

    @Test
    @Order(13)
    void customerCanGetOwnProfile() throws Exception {
        ensureCustomerLoggedIn();
        mockMvc.perform(get("/api/users/" + customerUserId)
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(CUSTOMER_EMAIL));
    }

    @Test
    @Order(14)
    void customerCanUpdateOwnProfile() throws Exception {
        ensureCustomerLoggedIn();
        UpdateUserRestRequest request = new UpdateUserRestRequest(
                customerUserId, "Customer Self Update", CUSTOMER_EMAIL, Set.of("CUSTOMER"), null
        );
        mockMvc.perform(put("/api/users/" + customerUserId)
                        .header("Authorization", "Bearer " + customerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Customer Self Update"));
    }

    @Test
    @Order(15)
    void customerCannotListAllUsers() throws Exception {
        ensureCustomerLoggedIn();
        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(16)
    void customerCannotGetAnotherUsersProfile() throws Exception {
        ensureAdminLoggedIn();
        ensureCustomerLoggedIn();
        mockMvc.perform(get("/api/users/" + adminUserId)
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(17)
    void customerCannotUpdateAnotherUsersProfile() throws Exception {
        ensureAdminLoggedIn();
        ensureCustomerLoggedIn();
        UpdateUserRestRequest request = new UpdateUserRestRequest(
                adminUserId, "Hacked Name", ADMIN_EMAIL, Set.of("ADMIN"), null
        );
        mockMvc.perform(put("/api/users/" + adminUserId)
                        .header("Authorization", "Bearer " + customerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(18)
    void customerCannotDeleteUsers() throws Exception {
        ensureAdminLoggedIn();
        ensureCustomerLoggedIn();
        mockMvc.perform(delete("/api/users/" + adminUserId)
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(19)
    void customerCanLogout() throws Exception {
        ensureCustomerLoggedIn();
        String tokenToInvalidate = customerToken;
        String userIdAtLogout = customerUserId;

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + tokenToInvalidate))
                .andExpect(status().isOk());

        // After logout the token must be blacklisted
        mockMvc.perform(get("/api/users/" + userIdAtLogout)
                        .header("Authorization", "Bearer " + tokenToInvalidate))
                .andExpect(status().isForbidden());

        // Reset so subsequent ITs can re-authenticate as CUSTOMER with a fresh token
        invalidateCustomerSession();
    }

    // =========================================================================
    // ADMIN — Delete (must be last to not affect other tests)
    // =========================================================================

    @Test
    @Order(20)
    void adminCanDeleteUser() throws Exception {
        ensureAdminLoggedIn();
        RegisterRestRequest register = new RegisterRestRequest(
                "Disposable User", "disposable@test.com", "pass123", null
        );
        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk())
                .andReturn();

        String disposableId = objectMapper.readTree(result.getResponse().getContentAsString())
                .get("userId").asText();

        mockMvc.perform(delete("/api/users/" + disposableId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/users/" + disposableId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }
}
