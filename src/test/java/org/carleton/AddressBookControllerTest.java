package org.carleton;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Integration tests: the whole Spring context starts, and MockMvc sends fake HTTP
 * requests through the real controllers, repositories and H2 database.
 */
@SpringBootTest
@AutoConfigureMockMvc
class AddressBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private long createBook() throws Exception {
        String json = mockMvc.perform(post("/addressbooks"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(json).get("id").asLong();
    }

    @Test
    void createBuddyInsideAddressBook() throws Exception {
        long bookId = createBook();
        mockMvc.perform(post("/addressbooks/" + bookId + "/buddies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Jane\",\"email\":\"Jane@gmail.com\",\"phone\":\"987-654-3210\",\"address\":\"456 Elm St\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.buddyList.length()").value(1))
                .andExpect(jsonPath("$.buddyList[0].name").value("Jane"));
    }

    @Test
    void addExistingBuddyToAddressBook() throws Exception {
        long bookId = createBook();
        String buddyJson = mockMvc.perform(post("/buddies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Bob\",\"email\":\"Bob@gmail.com\",\"phone\":\"555-555-5555\",\"address\":\"789 Oak St\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        JsonNode buddy = objectMapper.readTree(buddyJson);

        mockMvc.perform(put("/addressbooks/" + bookId + "/buddies/" + buddy.get("id").asLong()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.buddyList[0].name").value("Bob"));
    }

    @Test
    void unknownAddressBookReturns404() throws Exception {
        mockMvc.perform(get("/addressbooks/999999")).andExpect(status().isNotFound());
    }

    @Test
    void thymeleafViewListsBuddies() throws Exception {
        long bookId = createBook();
        mockMvc.perform(post("/addressbooks/" + bookId + "/buddies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Rabby\",\"email\":\"Rabby@gmail.com\",\"phone\":\"123-456-7890\",\"address\":\"123 Main St\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/view/addressbooks/" + bookId))
                .andExpect(status().isOk())
                .andExpect(view().name("addressbook"))
                .andExpect(content().string(containsString("Rabby@gmail.com")));
    }
}
