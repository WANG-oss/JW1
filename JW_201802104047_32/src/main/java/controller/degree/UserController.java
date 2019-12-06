package controller.degree;

import domain.User;
import service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/user.ctl")
public class UserController extends HttpServlet {
    /**
     * 方法-功能
     * put 修改
     * post 添加
     * delete 删除
     * get 查找
     */

    /**
     * GET, http://localhost:8080/degree.ctl?id=1, 查询id=1的学位
     * GET, http://localhost:8080/degree.ctl, 查询所有的学位
     * 响应一个或所有学位对象
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        String username = request.getParameter("username");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id值不为空，调用根据id值响应一个用户对象方法，否则，调用根据用户名响应一个用户对象的方法
            if (id_str != null){
                //强制类型转换成int型
                int id = Integer.parseInt(id_str);
                this.responseUser(id,response);
            }else if (username != null){
                this.responseUserByUserName(username,response);
            }else{
                responseUsers(response);
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        response.getWriter().println(message);
    }
    //根据id响应一个用户对象
    private void responseUser(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找院系
        User user = UserService.getInstance().find(id);
        String user_json = JSON.toJSONString(user);
        //响应Department_json到前端
        response.getWriter().println(user_json);
    }
    //根据用户名响应一个用户对象
    private void responseUserByUserName(String username, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找院系
        User user = UserService.getInstance().findByUsername(username);
        String user_json = JSON.toJSONString(user);
        //响应Department_json到前端
        response.getWriter().println(user_json);
    }
    //响应所有学位对象
    private void responseUsers(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有学院
        Collection<User> users = UserService.getInstance().findAll();
        String users_json = JSON.toJSONString(users, SerializerFeature.DisableCircularReferenceDetect);
        //响应message到前端
        response.getWriter().println(users_json);

    }

    /**
     * PUT, http://localhost:8080/user.ctl, 修改学位
     * 修改一个学位对象：将来自前端请求的JSON对象，更新数据库表中相同id的记录
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        String user_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        User userToUpdate = JSON.parseObject(user_json, User.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //修改User对象
            boolean puted = UserService.getInstance().changePassword(userToUpdate,id);
            if (puted) {
                message.put("message", "修改成功");
            }else {
                message.put("message", "已被修改");
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
        }

        //响应message到前端
        response.getWriter().println(message);
    }
}
