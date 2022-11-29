package spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import spring.controller.CategoryController;
import spring.domain.*;
import spring.mapper.CategoryMapper;
import spring.mapper.RouteImgeMapper;
import spring.utils.JedisUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper mapper;

    public List<Category> findAll() {
//1.从redis中查询
        //1.1获取jedis
        //Jedis jedis = JedisUtil.getJedis();
        //1.2使用sortedset排序查询
        // Set<String> categorys = jedis.zrange("category", 0, -1);
        //Set<Tuple> categorys = jedis.zrangeWithScores("category", 0, -1);


        List<Category> cs = null;
        //2.判断是否为空
        //if(categorys == null || categorys.size() == 0){
            //3.1数据库查询
            cs = mapper.findAll();
            //3.2将集合数据存储到redis中的category的key里
           // for (int i = 0; i < cs.size(); i++) {
                //System.out.println(111);
             //   jedis.zadd("category",cs.get(i).getCid(),cs.get(i).getCname());
            //}
//        }else {
//            //System.out.println(222);
//            cs = new ArrayList<Category>();
//            for (Tuple tuple : categorys) {
//                Category category = new Category();
//                category.setCname(tuple.getElement());
//                category.setCid((int)tuple.getScore());
//                cs.add(category);
//            }
//        }
        //3.空 从数据库中查询 存入数据库

        //4.不为空 直接返回
        return cs;
    }

    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname) {
        PageBean<Route> pb = new PageBean<Route>();
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);
        int totalCount = 0;
        if(!"".equals(rname)){
             totalCount = mapper.findTotalCount(cid,rname);
            System.out.println("有name");
        }else {
             totalCount = mapper.findTotalCount_1(cid);
            System.out.println("没有name");
        }
        System.out.println("count"+totalCount);
        pb.setTotalCount(totalCount);

        List<Route> list;
        int start = (currentPage - 1) * pageSize;
        if(!"".equals(rname)){
             list = mapper.findByPage(cid,start,pageSize,rname);
        }else{
             list = mapper.findByPage_1(cid,start,pageSize);
        }

        pb.setList(list);
        int totalPage = totalCount % pageSize == 0 ? (totalCount / pageSize) : ((totalCount / pageSize) + 1);
        pb.setTotalPage(totalPage);
        return pb;
    }

    public Route findOne(String rid) {
//1.根据rid去route表中查询对象
        Route route = mapper.findOne(Integer.parseInt(rid));
        //2.根据route的id查询图片的集合信息
        List<RouteImg> routeImgList = mapper.findById(route.getRid());
        route.setRouteImgList(routeImgList);
        Seller seller = mapper.findSellById(route.getSid());
        route.setSeller(seller);
        int count =  mapper.findFavoriteCountByRid(route.getRid());
        route.setCount(count);
        return route;
    }

    public boolean isFavorite(String rid, int uid) {
        Favorite favorite = mapper.findByRidAndUid(Integer.parseInt(rid), uid);

        return favorite != null;
    }

    public void add(String rid, int uid) {
        SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd");
        String date = tempDate.format(new java.util.Date());
        mapper.add(Integer.parseInt(rid),date, uid);
    }

    public PageBean<Favorite> myFavorite(User user, int currentPage) {

        List<Favorite> myFavorite = new ArrayList<>();
        PageBean<Favorite> pb = new PageBean<Favorite>();
        int pageSize = 5;
        int totalCount = mapper.findTotalCountByUid(user.getUid());
        int totalPage = (totalCount % pageSize == 0)?(totalCount / pageSize):(totalCount / pageSize + 1);
        int start = (currentPage - 1) * pageSize;
        List<TabFavorite> list = mapper.findByUid(user.getUid(),start,pageSize);
        Route route;
        for (TabFavorite tabTavorite : list) {
            //根据路线的rid去查tab_route得到真正的路线信息
            route = mapper.findOne(tabTavorite.getRid());
            //根据route的id 查询图片集合信息
            List<RouteImg> routeImgList = mapper.findById(route.getRid());
            //将集合设置到route对象
            route.setRouteImgList(routeImgList);
            //根据route的sid（商家id）查询商家对象
            Seller seller = mapper.findSellById(route.getSid());
            route.setSeller(seller);
            //通过rid查询tab_favorite表中该路线的收藏次数
            route.setCount(mapper.findCountByRid(tabTavorite.getRid()));
            Favorite favorite = new Favorite(route,tabTavorite.getDate(),user);
            myFavorite.add(favorite);
        }
        pb.setTotalCount(totalCount);
        pb.setTotalPage(totalPage);
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);
        pb.setList(myFavorite);
        return pb;
    }
}
