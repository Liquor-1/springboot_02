package spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import spring.domain.*;
import spring.mapper.UserMapper;
import spring.service.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("category")
public class CategoryController extends BaseController{
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserMapper mapper;

    @RequestMapping(value = "findAll" , method = RequestMethod.GET)
    @ResponseBody
    public void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> cs = this.categoryService.findAll();
        writeValue(cs,response);
    }

    @RequestMapping(value = "pageQuery" , method = RequestMethod.GET)
    @ResponseBody
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接受参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");
        String rname = request.getParameter("rname");
        rname = new String(rname.getBytes("iso-8859-1"),"utf-8");
        int cid = 0;
        if(cidStr != null && cidStr .length() > 0 && !"null".equals(cidStr)){
            cid = Integer.parseInt(cidStr);
        }
        int currentPage  = 0;
        if(currentPageStr != null && currentPageStr .length() > 0){
            currentPage = Integer.parseInt(currentPageStr);
        }else {
            currentPage = 1;
        }
        int pageSize  = 0;
        if(pageSizeStr != null && pageSizeStr .length() > 0){
            pageSize = Integer.parseInt(pageSizeStr);
        }else {
            pageSize = 5;
        }

        //调用service查询PageBean对象
        PageBean<Route> pb = this.categoryService.pageQuery(cid,currentPage,pageSize,rname);

        //序列化为json
        writeValue(pb,response);
    }
    @GetMapping("detail")
    public String test(){
        return "route_detail";
    }

    @GetMapping("findOne")
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //接受id
        String rid = request.getParameter("rid");
        //调用service
        Route route = this.categoryService.findOne(rid);
        //转为json
        writeValue(route,response);
    }

    @GetMapping("myFavorite")
    public void myFavorite(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int currentPage;

        User user = (User) request.getSession().getAttribute("user");
        if(user == null || "".equals(user)){
            return;
        }
        String currentPageStr = request.getParameter("currentPage");
        if(currentPageStr != null && currentPageStr .length() > 0){
            currentPage = Integer.parseInt(currentPageStr);
        }else {
            currentPage = 1;
        }
        PageBean<Favorite> pb = this.categoryService.myFavorite(user,currentPage);
        writeValue(pb,response);
    }

    @GetMapping("isFavorite")
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String rid = request.getParameter("rid");
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if(user == null){
            uid = 0;
        }else {
            uid = user.getUid();
        }
        boolean flag = this.categoryService.isFavorite(rid, uid);
        writeValue(flag,response);
    }

    @GetMapping("addFavorite")
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String rid = request.getParameter("rid");
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if(user == null){
            return;
        }else {
            uid = user.getUid();
        }
        this.categoryService.add(rid,uid);
    }
}
