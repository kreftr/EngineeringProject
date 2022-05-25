package edu.pjatk.app;

import edu.pjatk.app.socials.chat.ConversationController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.transaction.Transactional;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest

@AutoConfigureMockMvc
@ContextConfiguration(classes = WebApp.class, loader = AnnotationConfigContextLoader.class)
@WebAppConfiguration
class WebAppTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConversationController conversationController;

    private final List<File> dbchangelogFiles = getDBChangelogFiles();

    public WebAppTests() {

    }

//
//    @Test
//    @Transactional
//    @Sql(scripts = dbchangelogFiles,
//            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    void shouldGetSinglePost() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/post/1"))
//                .andDo(MockMvcResultHandlers.print());
//    }



    public List<File> getDBChangelogFiles() {
        final Path resourceDirectory = Paths.get("src", "main", "resources", "db.changelog");
        final String db_changelog_path = resourceDirectory.toFile().getAbsolutePath();
        final File db_changelog_file = new File(db_changelog_path);

        List<File> db_scripts = new ArrayList<>();
        for (File singleFile: Objects.requireNonNull(db_changelog_file.listFiles()))
        {
//            System.out.println(singleFile.getName());
            db_scripts.add(singleFile);
        }

        return db_scripts;
    }


    @Test
    public void contextLoads() throws Exception {



        assertThat(conversationController).isNotNull();
    }

    /*@Test
    public void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, World")));
    }*/

}
