package cn.myfreecloud.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

@Component
public class ManagerFilter extends ZuulFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String filterType() {
        //设置在路由前执行过滤器
        return "pre";
    }

    @Override
    public int filterOrder() {
        //设置过滤器的优先级最高
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //设置使用该过滤器
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println("使用后台网关的过滤器....");
        //获取请求上下文
        RequestContext requestContext = RequestContext.getCurrentContext();
        //从请求上下文获取 request 请求对象
        HttpServletRequest request = requestContext.getRequest();
        //从request中获取头信息
        String header = request.getHeader("Authorization");

        //判断request的请求方法是否是OPTIONS 是的话直接放行
        if (request.getMethod().equals("OPTIONS")) {
            //如果是OPTIONS就进行路由, 微服务网关zuul,会使用 OPTIONS 请求 进行路由
            return null;
        }

        //判断管理员是否是进行的登录操作
        //登录操作不能拦截
        String url = request.getRequestURL().toString();
        // 包含登录的url
        if (url.indexOf("/admin/login") > 0) {
            System.out.println("登陆页面" + url);
            return null;
        }

        try {
            //判断头信息是否合法
            if (header != null && header.startsWith("Bearer ")) {
                //如果合法就对头信息进行解析,进行token权限的校验
                String token = header.substring(7);
                //解析token
                Claims claims = jwtUtil.parseJWT(token);

                //如果clasim不为空,而且roles是admin,表示用户是管理,可以进行路由
                if (claims != null && "admin".equals(claims.get("roles"))) {
                    //把头信息进行转发,到具体的微服务
                    requestContext.addZuulRequestHeader("Authorization", header);

                    //返回null,就是进行路由操作
                    return null;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //如果不合法,后台网关,不应该进行路由(转发)到微服务
        //过滤器结束后是否进行路由(转发)到微服务
        //如果参数是false,就不再进行路由(转发),不会到微服务
        //如果参数是true,就会进行路由,默认就是true
        requestContext.setSendZuulResponse(false);//不进行路由
        //如果不路由到微服务,这里就需要进行相应
        // 设置响应的状态码
        requestContext.setResponseStatusCode(401);//设置权限不足
        // 设置响应内容,可以任意编写
        requestContext.setResponseBody("用户权限不足");

        //设置包括编码在内的响应头
        requestContext.getResponse().setContentType("text/html;charset=utf-8");

        //如果返回的是null,就会放行进行路由
        return null;
    }
}
