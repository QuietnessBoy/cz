package com.zhjl.tech.tasks.beetl;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MVCExceptionHandler implements HandlerExceptionResolver {
	
	public MVCExceptionHandler(){
		int a = 11111 ;
        System.out.println("just a test 。。");
	}

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                         Exception ex) {

        ex.printStackTrace();
        ModelAndView view = new ModelAndView("bettl`s MVC");
        view.addObject("ex", ex);
        return view;
    }  
}  