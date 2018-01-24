package com.zhjl.tech.inter.beetl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MVCExceptionHandler implements HandlerExceptionResolver {

    @Resource(name="jdbcTemplate2")
    JdbcTemplate jdbcTemplate;


	public MVCExceptionHandler(){
	    int a = 11111 ;
        System.out.println("just Test Test 。。");
	}

    @Override
    @Transactional("transactionManager2")
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                         Exception ex) {

	    d();
        ex.printStackTrace();
        ModelAndView view = new ModelAndView("bettl`s MVC");
        view.addObject("ex", ex);
        return view;
    }

    @Transactional("transactionManager")
    public void d(){
	    System.out.println("");
    }
}