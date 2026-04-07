package org.example.test3.action;

import static org.mockito.ArgumentMatchers.anyString;
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
public class LoginActionStandaloneTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @MockitoBean
    private UserService userService;

    @ParameterizedTest(name = "登录场景: username={0}, password={1}")
    @CsvFileSource(resources = "/login_test_data.csv", numLinesToSkip = 1)
    @DisplayName("通过读取CSV文件的多维数据测试登录Action框架层")
    public void testLoginActionDataDriven(
            String username, String password, boolean mockResult, 
            String expectedView, String expectedError) throws Exception {

        // 处理CSV读取null值的情形
        if (username == null) username = "";
        if (password == null) password = "";

        // 根据场景设置Mockito规则
        // 为了防范任意字符串调用引发问题，我们设定不论什么账号密码，只要与当前行的要求匹配，就返回预设的mockResult
        when(userService.loginVerify(username, password)).thenReturn(mockResult);

        // 如果存在预期的Error提示信息，则不仅验证视图名称，而且还要验证model里传递的error变量
        if (expectedError != null && !expectedError.isEmpty()) {
            mockMvc.perform(post("/login")
                    .param("username", username)
                    .param("password", password))
                   .andExpect(status().isOk())
                   .andExpect(view().name(expectedView))
                   .andExpect(model().attribute("error", expectedError));
        } else {
            mockMvc.perform(post("/login")
                    .param("username", username)
                    .param("password", password))
                   .andExpect(status().isOk())
                   .andExpect(view().name(expectedView));
        }
    }
}
