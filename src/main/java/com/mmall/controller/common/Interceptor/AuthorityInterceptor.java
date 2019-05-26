package com.mmall.controller.common.Interceptor;

import com.google.common.collect.Maps;
import com.mmall.common.ConstValue;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * @program: mmall
 * @description: 权限拦截器
 * @author: ypwang
 * @create: 2019-05-26 15:34
 **/
@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // controller 处理之前
        log.info("preHandle");
        // 解析参数，方便打印日志
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 获取类名，方法名
        String className = handlerMethod.getBean().getClass().getSimpleName();
        String methodName = handlerMethod.getMethod().getName();

        // 获取参数
        StringBuffer requestParamBuffer = new StringBuffer();
        Map paramMap = request.getParameterMap();
        Iterator it = paramMap.entrySet().iterator();
        if (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String paramName = (String) entry.getKey();

            // 参数值是一个字符串数组
            String paramValue = StringUtils.EMPTY;
            if (entry.getValue() instanceof String[]) {
                String[] strs = (String[]) entry.getValue();
                paramValue = Arrays.toString(strs);
                requestParamBuffer.append(paramName).append("=").append(paramValue);
            }
        }

        /*处理请求白名单，还有一种方式在springmvc配置文件中dispatcher-servlet.xml 添加 mvc:exclude-mapping path="/manage/user/login.do" */
        if (StringUtils.equals(className, "UserManageController") && StringUtils.equals(methodName, "login")) {
            log.info("拦截器拦截到请求:className: {} methodName：{} ", className, methodName);
            // 登录参数信息不能打印，防止日志泄露造成安全问题
            return true;
        }
        log.info("拦截器拦截到请求:className: {} methodName：{} paramValue：{}", className, methodName, requestParamBuffer.toString());
        User user = null;
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)) {
            String userJson = RedisShardedPoolUtil.get(loginToken);
            user = JsonUtil.string2Obj(userJson, User.class);
        }

        if (user == null || user.getRole().intValue() != ConstValue.Role.ROLE_ADMIN) {
            // 由于项目中统一返回值是ServerResponse对象，但是这里的preHandle返回值是bool类型，这里需要对response进行重置
            // 这里必须进行重置，否则会有异常 getWriter() has already been called for this response
            response.reset();
            // 防止乱码
            response.setCharacterEncoding("UTF-8");
            // 设置返回值类型，全部是json接口
            response.setContentType("application/json;charset=UTF-8");

            PrintWriter out = response.getWriter();
            if (user == null) {
                // 特殊处理富文本上传的逻辑
                if (StringUtils.equals(className, "ProductManageController") && StringUtils.equals(methodName, "richTextImgUpload")) {
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "请登录管理员");
                    out.print(JsonUtil.obj2String(resultMap));
                } else {
                    out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截，用户未登录")));
                }
            } else {
                if (StringUtils.equals(className, "ProductManageController") && StringUtils.equals(methodName, "richTextImgUpload")) {
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "用户无权限操作");
                    out.print(JsonUtil.obj2String(resultMap));
                } else {
                    out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截，用户无权限操作")));
                }
            }
            out.flush();
            // 记得关闭流
            out.close();
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // controller 处理之后
        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 所有处理完成之后
        log.info("afterCompletion");
    }
}
