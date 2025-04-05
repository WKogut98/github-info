package wkogut.github_info;

import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WebAppConfiguration
class GithubInfoApplicationTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@BeforeEach
	public void setup() throws Exception
	{
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	public void testIfControllerIsPresentInServletContext()
	{
		ServletContext servletContext = webApplicationContext.getServletContext();
		assertNotNull(servletContext);
        assertInstanceOf(MockServletContext.class, servletContext);
		assertNotNull(webApplicationContext.getBean("mainController"));
	}

	@Test
	public void testIfRepositoriesAreReturnedForValidUser() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.post("/repos").param("username", "torvalds"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("index"))
				.andExpect(content().string(containsString("linux")))
				.andExpect(content().string(containsString("master")));
	}

	@Test
	public void testIfErrorIsShownForInvalidUser() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.post("/repos").param("username", "user_that_is_not_valid"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(view().name("index"))
				.andExpect(content().string(containsString("User not found")));
	}
}
