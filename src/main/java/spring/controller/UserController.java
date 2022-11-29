package spring.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.domain.ResultInfo;
import spring.domain.User;
//import spring.pojo.Users;
//import spring.pojo.Users;
import spring.mapper.UserMapper;
import spring.pojo.Users;
import spring.service.UserService;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("user")

public class UserController extends BaseController{

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper mapper;
    @GetMapping("{id}")
    @ResponseBody
    public Users queryUserById(@PathVariable("id")int id){

        return this.userService.queryUserById(id);
    }

    @GetMapping("test")
    public String test(){
        return "index";
    }


    @GetMapping("log")
    public String log(Model model){
//        List<Users> users =  this.userService.queryAll();
//
//        System.out.println(users);
//        model.addAttribute("users",users);
        return "login";
    }
    @GetMapping("reg")
    public String rig(Model model){
//        List<Users> users =  this.userService.queryAll();
//
//        System.out.println(users);
//        model.addAttribute("users",users);
        return "register";
    }




    @GetMapping("myfavorite")
    public String myfavorite(Model model){
//        List<Users> users =  this.userService.queryAll();
//
//        System.out.println(users);
//        model.addAttribute("users",users);
        return "myfavorite";
    }
    @GetMapping("list")
    public String routList(Model model){
//        List<Users> users =  this.userService.queryAll();
//
//        System.out.println(users);
//        model.addAttribute("users",users);
        return "myfavorite";
    }


    @GetMapping("favoriterank")
    public String favoriterank(){
        return "favoriterank";
    }
    /********************************************************/

    @RequestMapping(value = "login" , method = RequestMethod.POST)
    @ResponseBody
    public String login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String[]> map = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        User u = this.userService.login(user);
        request.getSession().setAttribute("user",u);
        ResultInfo info = new ResultInfo();
        info.setFlag(true);
        if(u == null){
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
        }
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),info);

        return "login";
    }
    @RequestMapping(value = "findOne" , method = RequestMethod.GET)
    @ResponseBody
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object user = request.getSession().getAttribute("user");

//        写回客户端
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),user);
        //writeValue(user,response);
    }
    @RequestMapping(value = "exit" , method = RequestMethod.GET)
    @ResponseBody
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().invalidate();
        response.sendRedirect("/user/log");
    }


    @RequestMapping(value = "register" , method = RequestMethod.POST)
    @ResponseBody
    public void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //验证 验证码
        String check = request.getParameter("check");
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        //防止复用
        session.removeAttribute("CHECKCODE_SERVER");
        if(checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)){
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
        }
        Map<String, String[]> map = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //UserService service = new UserServiceImpl();
        boolean flag = this.userService.regist(user);
        ResultInfo info = new ResultInfo();

        if(flag){
            info.setFlag(true);
        }else {
            info.setFlag(false);
            info.setErrorMsg("注册失败");
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }




    @RequestMapping("/header")
    public String head(Map<String,Object> map){

        List<User> users = (List<User>) mapper.selectList(null);
        return "header";

    }
    @RequestMapping("/footer")
    public String footer(Map<String,Object> map){

        List<User> users = (List<User>) mapper.selectList(null);
        return "footer";
    }
}