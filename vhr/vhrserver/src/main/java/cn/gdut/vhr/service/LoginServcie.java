package cn.gdut.vhr.service;

import cn.gdut.entity.RespBean;
import cn.gdut.vhr.entity.Hr;

public interface LoginServcie {
    RespBean login(Hr hr);

    RespBean logout();
}
