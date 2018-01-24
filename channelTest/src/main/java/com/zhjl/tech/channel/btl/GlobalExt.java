package com.zhjl.tech.channel.btl;

import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.ext.web.WebRenderExt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Author: wildfist
 * Email: windsp@foxmail.com
 * Date: 2017/2/21
 * Time: 15:38
 * Describe:
 */
public class GlobalExt implements WebRenderExt {

    static long version = System.currentTimeMillis();

    @Override
    public void modify(Template template, GroupTemplate arg1, HttpServletRequest arg2, HttpServletResponse response) {
        response.setBufferSize(1024*24);
        //js,css 的版本编号
        template.binding("version",version);
    }
}