package cn.gdut.vhr.service.impl;

import cn.gdut.vhr.entity.Hr;
import cn.gdut.vhr.entity.LoginHr;
import cn.gdut.vhr.mapper.HrMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 创建一个类实现UserDetailsService接口，重写其中的方法。
 * 用户名从数据库中查询用户信息
 */
@Service
public class HrDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private HrMapper hrMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名Hr查询用户信息
        LambdaQueryWrapper<Hr> wrapper = new LambdaQueryWrapper<>();
        System.out.println(username);
        wrapper.eq(Hr::getUsername, username);
        Hr hr = hrMapper.selectOne(wrapper);
        //如果查询不到数据就通过抛出异常来给出提示
        if(Objects.isNull(hr)) {
            throw new RuntimeException("用户名或密码错误");
        }

        //TODO 根据用户查询权限信息，添加到LoginUser中
        //封装成HrDetails对象返回
        return new LoginHr(hr);
    }
}
