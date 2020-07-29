package com.exalt.ipc.user;

import com.exalt.ipc.controllers.UserController;
import com.exalt.ipc.localization.LocaleService;
import com.exalt.ipc.repositories.UserRepository;
import com.exalt.ipc.security.UserDetailsServiceImpl;
import com.exalt.ipc.services.IPCService;
import com.exalt.ipc.services.JobService;
import com.exalt.ipc.services.JwtService;
import com.exalt.ipc.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
//@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
@RunWith(SpringRunner.class)
class UserControllerTest {
	@MockBean
	IPCService ipcService;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@MockBean
	private LocaleService localeService;

	@MockBean
	private JobService jobService;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private JwtService jwtService;

	@Test
	public void shouldReturnRegisteredUser() throws Exception {
		this.mockMvc.perform(post("/v1/users/signup").content("{\"email\":\"tareq2@gmail.com\",\"firstName\":\"Tareq\"," +
				"\"lastName\":\"Hasan\",\"password\":\"12345678\"}").contentType(MediaType.APPLICATION_JSON_VALUE))
								.andDo(print()).andExpect(status().isOk());
	}
}