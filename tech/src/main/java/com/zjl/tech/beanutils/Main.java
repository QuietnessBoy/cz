package com.zjl.tech.beanutils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by wind on 2016/12/30.
 */
@Slf4j
public class Main {

    public static void main(String[] args) throws IOException {
        Employee employee = new Employee();
        employee.setName("nnn");
        employee.setAge(11);

        Emp a = new Emp();
        BeanUtils.copyProperties(employee, a);
        log.info(JSON.toJSONString(a));

        HashMap<String,Object> map = new HashMap<>(3);
        map.put("name", "13880808080");
        map.put("age", 22);
        log.info(JSON.toJSONString(a));

    }
}
