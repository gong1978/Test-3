package org.example.test3.action;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.example.test3.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.BeforeEach;

@SpringBootTest
public class RegisterActionStandaloneTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @MockitoBean
    private UserService userService;

    @ParameterizedTest(name = "注册场景: username={0}, password={1}")
    @CsvFileSource(resources = "/register_test_data.csv", numLinesToSkip = 1)
    @DisplayName("通过读取CSV文件的多维数据测试注册Action全场景")
    public void testRegisterActionDataDriven(
            String username, String password, boolean mockResult, 
            String expectedView, String expectedMessage, String expectedError) throws Exception {

        if (username == null) username = "";
        if (password == null) password = "";

        when(userService.userRegister(username, password)).thenReturn(mockResult);

        var actions = mockMvc.perform(post("/register")
                .param("username", username)
                .param("password", password))
               .andExpect(status().isOk())
               .andExpect(view().name(expectedView));

        if (expectedError != null && !expectedError.isEmpty()) {
            actions.andExpect(model().attribute("error", expectedError));
        }
        
        if (expectedMessage != null && !expectedMessage.isEmpty()) {
            actions.andExpect(model().attribute("message", expectedMessage));
        }
    }
}
