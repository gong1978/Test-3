package org.example.test3.dao;

import org.example.test3.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    // 添加用户
    public boolean addUser(User user) {
        if (!verifyUsername(user.getUsername())) {
            // 模拟保存逻辑，由于实际测试中会被Mockito等工具Mock，所以直接返回true
            return true;
        }
        return false;
    }

    // 验证用户名是否存在
    public boolean verifyUsername(String username) {
        // 模拟验证逻辑，实际测试会被Mock
        return false;
    }

    // 验证密码是否正确
    public boolean verifyPassword(String username, String password) {
        // 模拟验证逻辑，实际测试会被Mock
        return false;
    }
}
