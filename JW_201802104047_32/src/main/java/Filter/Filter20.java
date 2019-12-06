package Filter;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "Filter 20",urlPatterns = "/*")
public class Filter20 implements Filter {
    @Override
    public void destroy() {
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        //打印提示信息
        System.out.println("access authorization verification starts");
        //强制类型转换成HttpServletRequest类型
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        //强制类型转换成HttpServletRequest类型
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获得请求的url
        String path = request.getRequestURI();
        JSONObject message = new JSONObject();
        if (!path.contains("/login")) {
            //访问权限验证
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("currentUser")==null){
                message.put("message","请登录或重新登陆");
                //响应message到前端
                response.getWriter().println(message);
            }
        }
        //执行其他过滤器，若过滤器已经执行完毕，则执行原请求
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("access authorization verification ends");
    }
    @Override
    public void init(FilterConfig config) throws ServletException {

    }

}
