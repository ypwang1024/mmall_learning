package com.mmall.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: mmall
 * @description: 全局异常处理类
 * @author: ypwang
 * @create: 2019-05-26 14:47
 **/
@Slf4j
/*声明该类是spring容器的一个bean*/
@Component
public class ExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        /*后台打印日志*/
        log.error("{} Exception", httpServletRequest.getRequestURI(), e);
        // 当使用Jackson 2.x的时候使用MappingJackson2JsonView，课程中使用的是1.9
        ModelAndView modelAndView = new ModelAndView(new MappingJacksonJsonView());
        modelAndView.addObject("status", ResponseCode.ERROR.getCode());
        modelAndView.addObject("msg","接口异常，详情请查看服务端异常信息");
        modelAndView.addObject("data", e.toString());
        return modelAndView;
    }
}
