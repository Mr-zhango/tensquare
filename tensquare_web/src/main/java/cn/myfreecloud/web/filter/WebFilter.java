package cn.myfreecloud.web.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class WebFilter extends ZuulFilter {

    // 过滤器类型
    @Override
    public String filterType() {
        //过滤器什么时候执行,pre就是最开始执行
        return "pre";
        //pre ：可以在请求被路由之前调用
        //route ：在路由请求时候被调用
        //post ：在route和error过滤器之后被调用
        //error ：处理请求时发生错误时被调用
    }

    @Override
    public int filterOrder() {
        //设置过滤器执行的优先级,如果是0,优先级最高
        //数字越大,优先级越低
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //是否使用该过滤器,返回true表示使用该过滤器
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //编写过滤器具体的业务逻辑,例如校验权限
        System.out.println("前端网关过滤器执行了....");

        //获取请求的上下文
        RequestContext requestContext = RequestContext.getCurrentContext();

        //从请求的上下文中获取用户的request请求
        HttpServletRequest request = requestContext.getRequest();

        //从request中获取请求头
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            //把获取到的请求头再放到下一次请求的request中,
            //第一个参数是请求头名字
            //第二个参数是请求头的值
            requestContext.addZuulRequestHeader("Authorization", header);
        }

        return null;
    }
}
