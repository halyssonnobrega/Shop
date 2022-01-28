package com.demo.shop;

import com.demo.shop.business.entity.ItemEO;
import com.demo.shop.business.exception.custom.InvalidPriceException;
import com.demo.shop.business.repository.ItemCustomRepository;
import com.demo.shop.resources.model.ItemRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.ws.rs.core.MediaType;
import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class ShopApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ItemCustomRepository itemCustomRepository;

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void createItemToUserFail() throws Exception {
        ItemRequest item = new ItemRequest();
        item.setName("Item Test");
        item.setPrice(1.0);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/shop/createItem")
                                .content(objectMapper.writeValueAsString(item))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void createItemToAdminSuccess() throws Exception {
        ItemRequest item = new ItemRequest();
        item.setName("Item Test");
        item.setPrice(1.0);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/shop/createItem")
                                .content(objectMapper.writeValueAsString(item))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        List<ItemEO> list = itemCustomRepository.findItensByFilters(null, "Item Test", null);

        Assertions.assertThat(list.stream().filter(itemEO -> itemEO.getName().equals("Item Test")));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void createItemWithInvalidPriceToAdminFail() throws Exception {
        ItemRequest item = new ItemRequest();
        item.setName("Item Test");
        item.setPrice(0.0);

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/shop/createItem")
                                .content(objectMapper.writeValueAsString(item))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andReturn();

        Assertions.assertThat(result.getResolvedException().getClass()).isEqualTo(InvalidPriceException.class);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void getItensToUserSuccess() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/shop/getItens")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "USER")
    void getItensToAdminSuccess() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/shop/getItens")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}