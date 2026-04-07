package org.example.test3.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.test3.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
public class RegisterActionTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private RegisterAction registerAction;

    @Test
    public void testExecute_EmptyUsername_ReturnsInput() {
        String result = registerAction.execute("", "123456", model);

        assertEquals("input", result);
        verify(model).addAttribute("error", "用户名不能为空！");
        verify(userService, never()).userRegister(anyString(), anyString());
    }

    @Test
    public void testExecute_EmptyPassword_ReturnsInput() {
        String result = registerAction.execute("testuser", "", model);

        assertEquals("input", result);
        verify(model).addAttribute("error", "密码不能为空！");
        verify(userService, never()).userRegister(anyString(), anyString());
    }

    @Test
    public void testExecute_RegisterSuccess_ReturnsSuccess() {
        when(userService.userRegister("newuser", "pwd123")).thenReturn(true);

        String result = registerAction.execute("newuser", "pwd123", model);

        assertEquals("success", result);
        verify(userService).userRegister("newuser", "pwd123");
        verify(model).addAttribute("message", "注册成功！");
    }

    @Test
    public void testExecute_RegisterFailure_ReturnsInput() {
        when(userService.userRegister("existinguser", "pwd123")).thenReturn(false);

        String result = registerAction.execute("existinguser", "pwd123", model);

        assertEquals("input", result);
        verify(userService).userRegister("existinguser", "pwd123");
        verify(model).addAttribute("error", "注册失败,该用户名已存在！");
    }
}