package com.itheima.service;

import com.itheima.entity.Result;

import java.util.Map;

public interface OrderService {
    Result order(Map map);

    Map findById(Integer id) throws Exception;
}
