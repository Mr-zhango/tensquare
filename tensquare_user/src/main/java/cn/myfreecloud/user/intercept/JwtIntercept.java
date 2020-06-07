package cn.myfreecloud.user.intercept;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtIntercept implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    //进入Controller方法之前拦截
    //在进入Controller之前就需要进行鉴权
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //System.out.println("执行鉴权操作");

        //获取token,从请求头获取
        String header = request.getHeader("Authorization");

        //判断token是否为空
        if (header != null && header.startsWith("Bearer ")) {
            //如果不为空,而且是Bearer开头,表示token数据正确,可以进行解析

            try {
                //获取token的值,Bearer 需要去掉
                String token = header.substring(7);
                //根据token,获取claims,可以验证token的正确性,同时可以获取到权限是什么
                Claims claims = jwtUtil.parseJWT(token);
                if (claims != null) {
                    if ("admin".equals(claims.get("roles"))) {
                        //把信息放到request中
                        request.setAttribute("roles_admin", claims);
                    }
                    if ("user".equals(claims.get("roles"))) {
                        //把信息放到request中
                        request.setAttribute("roles_user", claims);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        //返回false   表示拦截,就不会执行Controller方法
        //返回true,   表示放行,后续的Controller就会执行
        return true;
    }

    //进入Controller方法,并执行,在返回ModelAndView结果之前拦截
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView
            modelAndView) throws Exception {

    }

    //Controller所有该执行的都执行完成,并且返回了结果,最后在执行的方法
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception
            ex) throws Exception {

    }
}
