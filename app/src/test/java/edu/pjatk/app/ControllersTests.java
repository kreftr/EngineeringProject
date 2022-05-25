package edu.pjatk.app;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
//@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)  // cleans application context before each test
@TestPropertySource(locations="classpath:test-application.properties")
public class ControllersTests {

    @Autowired
    private MockMvc mockMvc;

//    @Autowired
//    private WebApplicationContext webApplicationContext;

    @Test
    public void test_if_tests_are_working() {
        assertThat(1).isEqualTo(1);
    }

    // TODO methods returns 404 instead of expected 200, it has something to do with class configuration annotations

    @Test
    public void test_homepage_status() throws Exception {
        mockMvc
                .perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    public void test_login_controller_status() throws Exception {
        mockMvc
                .perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    public void test_search_controller_status() throws Exception {
        mockMvc
                .perform(get("/search"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void test_conversation_creation() throws Exception {
        mockMvc
                .perform(post("/conversation/createConversation/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void test_conversation_deletion() throws Exception {
        mockMvc
                .perform(delete("/conversation/deleteConversation/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
