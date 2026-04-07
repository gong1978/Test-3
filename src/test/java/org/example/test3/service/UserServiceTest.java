package org.example.test3.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.test3.dao.UserDao;
import org.example.test3.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @Test
    public void testLoginVerify_Success() {
        // 设置Mock对象的行为
        when(userDao.verifyUsername("testuser")).thenReturn(true);
        when(userDao.verifyPassword("testuser", "123456")).thenReturn(true);

        // 调用被测试的方法
        boolean result = userService.loginVerify("testuser", "123456");

        // 验证结果
        assertTrue(result);
        
        // 验证Mock对象的方法是否被正确调用
        verify(userDao).verifyUsername("testuser");
        verify(userDao).verifyPassword("testuser", "123456");
    }

    @Test
    public void testLoginVerify_UserNotFound() {
        when(userDao.verifyUsername("testuser")).thenReturn(false);

        boolean result = userService.loginVerify("testuser", "123456");

        assertFalse(result);
        verify(userDao).verifyUsername("testuser");
        verify(userDao, never()).verifyPassword(anyString(), anyString());
    }

    @Test
    public void testLoginVerify_WrongPassword() {
        when(userDao.verifyUsername("testuser")).thenReturn(true);
        when(userDao.verifyPassword("testuser", "wrong")).thenReturn(false);

        boolean result = userService.loginVerify("testuser", "wrong");

        assertFalse(result);
        verify(userDao).verifyUsername("testuser");
        verify(userDao).verifyPassword("testuser", "wrong");
    }

    @Test
    public void testUserRegister_Success() {
        when(userDao.addUser(any(User.class))).thenReturn(true);

        boolean result = userService.userRegister("newuser", "123456");

        assertTrue(result);
        verify(userDao).addUser(any(User.class));
    }
    
    @Test
    public void testUserRegister_Fail() {
        when(userDao.addUser(any(User.class))).thenReturn(false);

        boolean result = userService.userRegister("existinguser", "123456");

        assertFalse(result);
        verify(userDao).addUser(any(User.class));
    }
}