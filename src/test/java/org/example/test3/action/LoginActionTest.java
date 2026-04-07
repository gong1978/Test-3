package org.example.test3.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.test3.entity.User;
import org.example.test3.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
public class LoginActionTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @InjectMocks
    private LoginAction loginAction;

    @Test
    public void testExecute_EmptyUsername_ReturnsInput() {
        String result = loginAction.execute("", "123456", session, model);

        assertEquals("input", result);
        verify(model).addAttribute("error", "用户名不能为空！");
        verify(userService, never()).loginVerify(anyString(), anyString());
    }

    @Test
    public void testExecute_EmptyPassword_ReturnsInput() {
        String result = loginAction.execute("testuser", null, session, model);

        assertEquals("input", result);
        verify(model).addAttribute("error", "密码不能为空！");
        verify(userService, never()).loginVerify(anyString(), anyString());
    }

    @Test
    public void testExecute_LoginSuccess_ReturnsSuccess() {
        when(userService.loginVerify("testuser", "123456")).thenReturn(true);

        String result = loginAction.execute("testuser", "123456", session, model);

        assertEquals("success", result);
        verify(userService).loginVerify("testuser", "123456");
        verify(session).setAttribute(eq("user"), any(User.class));
    }

    @Test
    public void testExecute_LoginFailure_ReturnsInput() {
        when(userService.loginVerify("testuser", "wrongpwd")).thenReturn(false);

        String result = loginAction.execute("testuser", "wrongpwd", session, model);

        assertEquals("input", result);
        verify(userService).loginVerify("testuser", "wrongpwd");
        verify(model).addAttribute("error", "登录失败！");
    }
}