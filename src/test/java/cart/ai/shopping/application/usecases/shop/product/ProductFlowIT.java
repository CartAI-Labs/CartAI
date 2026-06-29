/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.application.usecases.shop.product;

import cart.ai.shopping.infrastructure.config.TestStorageConfig;
import cart.ai.shopping.infrastructure.in.rest.shop.dtos.CreateProductRestRequest;
import cart.ai.shopping.infrastructure.in.rest.shop.dtos.UpdateProductRestRequest;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Roberto Díaz
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestStorageConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductFlowIT {

    private static String sharedProductId;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    @WithMockUser(roles = "CUSTOMER")
    void shouldDenyCreateProductForCustomer() throws Exception {
        CreateProductRestRequest request = new CreateProductRestRequest("Laptop", "Desc", new BigDecimal("1500.00"), 10, List.of());
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(2)
    @WithMockUser(roles = "VENDOR")
    void shouldAllowCreateProductForVendor() throws Exception {
        CreateProductRestRequest request = new CreateProductRestRequest("Laptop", "Gaming Laptop", new BigDecimal("1500.00"), 10, List.of("img1"));

        String response = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andReturn().getResponse().getContentAsString();

        var responseNode = objectMapper.readTree(response);
        sharedProductId = responseNode.get("id").get("value").asText();
    }

    @Test
    @Order(3)
    @WithMockUser(roles = "CUSTOMER")
    void shouldAllowGetProductForCustomer() throws Exception {
        mockMvc.perform(get("/api/products/" + sharedProductId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.stock").value(10));
    }

    @Test
    @Order(4)
    @WithMockUser(roles = "CUSTOMER")
    void shouldDenyUpdateProductForCustomer() throws Exception {
        UpdateProductRestRequest request = new UpdateProductRestRequest(sharedProductId, "Laptop Pro", "Desc", new BigDecimal("2000.00"), 5, List.of());
        mockMvc.perform(put("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(5)
    @WithMockUser(roles = "ADMIN")
    void shouldAllowUpdateProductForAdmin() throws Exception {
        UpdateProductRestRequest request = new UpdateProductRestRequest(sharedProductId, "Laptop Pro", "Gaming Laptop Pro", new BigDecimal("2000.00"), 5, List.of("img1", "img2"));
        mockMvc.perform(put("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop Pro"))
                .andExpect(jsonPath("$.stock").value(5));
    }

    @Test
    @Order(6)
    @WithMockUser(roles = "CUSTOMER")
    void shouldAllowListProductsForCustomer() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    @Order(7)
    @WithMockUser(roles = "CUSTOMER")
    void shouldDenyDeleteProductForCustomer() throws Exception {
        mockMvc.perform(delete("/api/products/" + sharedProductId))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(8)
    @WithMockUser(roles = "VENDOR")
    void shouldAllowDeleteProductForVendor() throws Exception {
        mockMvc.perform(delete("/api/products/" + sharedProductId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/products/" + sharedProductId))
                .andExpect(status().isNotFound());
    }
}
