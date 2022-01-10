package edu.pjatk.app;

import edu.pjatk.app.socials.chat.Conversation;
import edu.pjatk.app.socials.chat.ConversationRepository;
import edu.pjatk.app.socials.chat.Message;
import edu.pjatk.app.user.User;
import edu.pjatk.app.user.UserRole;
import edu.pjatk.app.user.profile.Profile;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class WebAppTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConversationRepository conversationRepository;

    @InjectMocks
    private Conversation conversation;

    @InjectMocks
    private Message message;

    @InjectMocks
    private Profile profile;

    @InjectMocks
    private User user;

    @Test
    void test() {
        assertThat(1).isEqualTo(1);
    }
    @Test
    void test_profile_name() {
        System.out.println(profile.getName());
    }
    @Test
    void test_set_profile() {
        profile.setId(1l);
        profile.setName("John");
        profile.setSurname("Smith");
        profile.setBio("some Bio");

        assertThat(profile.getName()).isNotNull();


        assertThat(profile).isNotNull();
        System.out.println(profile);
    }

    @Test
    void test_conversation() {
        conversation.setId(1l);
        conversation.setFirst_user(user);
        conversation.setSecond_user(user);
        message.setConversation(conversation);

        conversationRepository.createConversation(conversation);
        assertThat(conversationRepository.getConversationById(1l)).isNotNull();
    }

//    @Test
//    void test_adding_user() {
//        user.setId(1l);
//        user.setUsername("janek01");
//        user.setEmail("janek@email.com");
//        user.setPassword("$2a$12$LERaQcqzMkbfIaVROkJyLu6QhHdcfB7fnWq4A7Sjqu/P3TfVLKWNO");
//        user.setCreationDate(LocalDateTime.now());
//        user.setUserRole(UserRole.USER);
//        user.setProfile(profile);
//        user.setLocked(false);
//        user.setEnabled(true);
//    }

    void shouldGetSinglePost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/post/1"))
                .andDo(MockMvcResultHandlers.print());
    }
}
