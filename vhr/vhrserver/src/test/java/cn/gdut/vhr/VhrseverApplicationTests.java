package cn.gdut.vhr;

import cn.gdut.vhr.entity.Hr;
import cn.gdut.vhr.mapper.HrMapper;
import cn.gdut.vhr.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class VhrseverApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private HrMapper hrMapper;

    @Test
    public void testHrMapper() {
        Hr hr = hrMapper.selectById(3L);
        System.out.println(hr);
    }

    @Test
    public void TestBCryptPasswordEnconder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("123");
        String encode1 = bCryptPasswordEncoder.encode("123");
        System.out.println(encode);
        System.out.println(encode1);

        System.out.println(bCryptPasswordEncoder.
                matches("123",
                        "$2a$10$wx1DWUSrxaoftk3QGWcJVu4wUOtATTQrtI23Im6LqKAXabqBg88si"));
    }
}
