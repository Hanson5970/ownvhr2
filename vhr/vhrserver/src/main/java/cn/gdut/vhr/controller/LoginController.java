package cn.gdut.vhr.controller;

import cn.gdut.entity.RespBean;
import cn.gdut.vhr.entity.Hr;
import cn.gdut.vhr.service.LoginServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录接口
 */

@RestController
public class LoginController {

    @Autowired
    private LoginServcie loginService;

    @PostMapping("/user/login")
    public RespBean login(@RequestBody Hr hr) {
        return loginService.login(hr);
    }

    @GetMapping("/user/logout")
    public RespBean logout() {
        return loginService.logout();
    }

}
