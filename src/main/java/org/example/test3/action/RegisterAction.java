package org.example.test3.action;

import org.example.test3.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/register")
public class RegisterAction {

    private final UserService userService;

    public RegisterAction(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public String execute(@RequestParam(required = false) String username, 
                          @RequestParam(required = false) String password, 
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
        if (userService.userRegister(username, password)) {
            model.addAttribute("message", "注册成功！");
            return "success";
        } else {
            model.addAttribute("error", "注册失败,该用户名已存在！");
            return "input";
        }
    }
}