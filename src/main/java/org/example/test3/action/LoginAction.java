package org.example.test3.action;

import org.example.test3.entity.User;
import org.example.test3.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginAction {

    private final UserService userService;

    public LoginAction(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public String execute(@RequestParam(required = false) String username, 
                          @RequestParam(required = false) String password, 
                          HttpSession session, 
                          Model model) {
        // validate逻辑
        if (username == null || "".equals(username)) {
            model.addAttribute("error", "用户名不能为空！");
            return "input";
        }
        if (password == null || "".equals(password)) {
            model.addAttribute("error", "密码不能为空！");
            return "input";
        }

        // execute逻辑
        if (userService.loginVerify(username, password)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            session.setAttribute("user", user);
            return "success";
        } else {
            model.addAttribute("error", "登录失败！");
            return "input";
        }
    }
}