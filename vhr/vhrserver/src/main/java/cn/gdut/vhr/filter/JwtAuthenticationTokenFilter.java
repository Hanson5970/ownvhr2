package cn.gdut.vhr.filter;

import cn.gdut.vhr.entity.LoginHr;
import cn.gdut.vhr.utils.JwtUtil;
import cn.gdut.vhr.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 认证过滤器，该过滤器会获取请求头中的token，对token进行解析取出其中的hrid
 * 使用hrid在redis中获取对应的loginHr对象
 * 然后封装Authentication对象存入Security
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if(!StringUtils.hasText(token)) {
            //放行
            //此时没有token需要解析，放行，让其进入AuthenticationManager进行处理
            filterChain.doFilter(request,response);
            return;
        }
        //解析token
        String hrid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            hrid = claims.getSubject();
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }

        //从redis中获取用户信息
        String rediskey = "login:" + hrid;
        LoginHr loginHr = redisCache.getCacheObject(rediskey);
        if(Objects.isNull(loginHr)) {
            throw new RuntimeException("用户未登录");
        }
        //存入SecurityContextHolder
        //TODO 获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginHr, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request,response);
    }
}
