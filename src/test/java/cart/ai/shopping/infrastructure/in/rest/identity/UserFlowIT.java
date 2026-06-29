/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.in.rest.identity;

import cart.ai.shopping.infrastructure.config.TestStorageConfig;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.LoginRestRequest;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.RegisterRestRequest;
import cart.ai.shopping.infrastructure.in.rest.identity.dtos.UpdateUserRestRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for User management endpoints.
 * <p>
 * Strategy: Each role block performs a real login to obtain a JWT token,
 * then uses that token in subsequent requests — testing both authentication
 * and authorization in a realistic end-to-end flow.
 * <p>
 * Roles tested: ADMIN, VENDOR, CUSTOMER
 *
 * @author Roberto Díaz
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestStorageConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserFlowIT {

    // Credentials seeded by bootstrap (see test/resources/application.properties)
    private static final String ADMIN_EMAIL = "admin@test.com";
    private static final String ADMIN_PASS = "adminpass";
    private static final String VENDOR_EMAIL = "vendor@test.com";
    private static final String VENDOR_PASS = "vendorpass";
    private static final String CUSTOMER_EMAIL = "customer@test.com";
    private static final String CUSTOMER_PASS = "customerpass";
    // Shared state across tests — tokens and IDs obtained during login/register
    private static String adminToken;
    private static String adminUserId;
    private static String vendorToken;
    private static String vendorUserId;
    private static String customerToken;
    private static String customerUserId;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Ensures admin is logged in — safe to call multiple times.
     */
    private void ensureAdminLoggedIn() throws Exception {
        if (adminToken != null) return;
        LoginRestRequest req = new LoginRestRequest(ADMIN_EMAIL, ADMIN_PASS);
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk()).andReturn();
        var response = objectMapper.readTree(result.getResponse().getContentAsString());
        adminToken = response.get("token").asText();
        adminUserId = response.get("userId").asText();
    }

    /**
     * Ensures vendor is logged in — safe to call multiple times.
     */
    private void ensureVendorLoggedIn() throws Exception {
        if (vendorToken != null) return;
        LoginRestRequest req = new LoginRestRequest(VENDOR_EMAIL, VENDOR_PASS);
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk()).andReturn();
        var response = objectMapper.readTree(result.getResponse().getContentAsString());
        vendorToken = response.get("token").asText();
        vendorUserId = response.get("userId").asText();
    }

    /**
     * Ensures customer is logged in — safe to call multiple times.
     */
    private void ensureCustomerLoggedIn() throws Exception {
        if (customerToken != null) return;
        LoginRestRequest req = new LoginRestRequest(CUSTOMER_EMAIL, CUSTOMER_PASS);
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk()).andReturn();
        var response = objectMapper.readTree(result.getResponse().getContentAsString());
        customerToken = response.get("token").asText();
        customerUserId = response.get("userId").asText();
    }

    // =========================================================================
    // ADMIN BLOCK
    // =========================================================================

    @Test
    @Order(1)
    void adminCanLoginSuccessfully() throws Exception {
        LoginRestRequest request = new LoginRestRequest(ADMIN_EMAIL, ADMIN_PASS);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
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

        // Admin fetches vendor profile
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
        ensureVendorLoggedIn();
        // Re-login to get fresh token and assert response
        vendorToken = null;
        LoginRestRequest request = new LoginRestRequest(VENDOR_EMAIL, VENDOR_PASS);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.roles", hasItem("VENDOR")))
                .andReturn();

        var response = objectMapper.readTree(result.getResponse().getContentAsString());
        vendorToken = response.get("token").asText();
        vendorUserId = response.get("userId").asText();
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

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.userId").isNotEmpty())
                .andExpect(jsonPath("$.roles", hasItem("CUSTOMER")))
                .andReturn();

        var response = objectMapper.readTree(result.getResponse().getContentAsString());
        customerToken = response.get("token").asText();
        customerUserId = response.get("userId").asText();
    }

    @Test
    @Order(12)
    void customerCanLoginSuccessfully() throws Exception {
        ensureCustomerLoggedIn();
        // Re-login to get fresh token and assert response
        customerToken = null;
        LoginRestRequest request = new LoginRestRequest(CUSTOMER_EMAIL, CUSTOMER_PASS);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.roles", hasItem("CUSTOMER")))
                .andReturn();

        // Refresh customerToken and ID to the seeded customer for remaining tests
        var response = objectMapper.readTree(result.getResponse().getContentAsString());
        customerToken = response.get("token").asText();
        customerUserId = response.get("userId").asText();
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
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isOk());

        // After logout, the token should be blacklisted — requests must be rejected
        mockMvc.perform(get("/api/users/" + customerUserId)
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isForbidden());
    }

    // =========================================================================
    // ADMIN — Delete (must be last to not affect other tests)
    // =========================================================================

    @Test
    @Order(20)
    void adminCanDeleteUser() throws Exception {
        ensureAdminLoggedIn();
        // Create a disposable user to delete
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

        // Admin deletes the disposable user
        mockMvc.perform(delete("/api/users/" + disposableId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());

        // Verify the user no longer exists
        mockMvc.perform(get("/api/users/" + disposableId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }
}
