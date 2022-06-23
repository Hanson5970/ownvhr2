package cn.gdut.vhr.service.impl;

import cn.gdut.entity.RespBean;
import cn.gdut.vhr.entity.Hr;
import cn.gdut.vhr.entity.LoginHr;
import cn.gdut.vhr.service.LoginServcie;
import cn.gdut.vhr.utils.JwtUtil;
import cn.gdut.vhr.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

/**
 * 登录service，AuthenticationManger
 */
@Service
public class LoginServiceImpl implements LoginServcie {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public RespBean login(Hr hr) {
        //调用authenticationManager的authenticate()方法进行认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(hr.getUsername(), hr.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if(Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }

        //使用hrid生成token
        LoginHr loginHr = (LoginHr) authenticate.getPrincipal();
        String hrid = loginHr.getHr().getId().toString();
        String jwt = JwtUtil.createJWT(hrid);
        //loginHr存入redis
        redisCache.setCacheObject("login:" + hrid, loginHr);
        //把token响应给前端
        HashMap<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return RespBean.ok("登录成功",map);
    }

    @Override
    public RespBean logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginHr loginHr = (LoginHr) authentication.getPrincipal();
        Integer hrid = loginHr.getHr().getId();
        redisCache.deleteObject("login:" + hrid);
        return RespBean.ok("退出成功");
    }
}
