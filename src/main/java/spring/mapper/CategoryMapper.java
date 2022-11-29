package spring.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import spring.controller.CategoryController;
import spring.domain.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface CategoryMapper {

    @Select("select * from tab_category")
    List<Category> findAll();

    @Select("select count(*) from tab_route where cid = #{cid} and rname like concat('%',#{rname},'%') ")
    int findTotalCount(int cid, String rname);

    @Select("select count(*) from tab_route where cid = #{cid}")
    int findTotalCount_1(int cid);

    @Select("select * from tab_route where cid = #{cid} and rname like concat('%',#{rname},'%') limit #{start} , #{pageSize}")
    List<Route> findByPage(int cid, int start, int pageSize, String rname);

    @Select("select * from tab_route where cid = #{cid} limit #{start} , #{pageSize}")
    List<Route> findByPage_1(int cid, int start, int pageSize);

    @Select("select * from tab_route where rid = #{rid}")
    Route findOne(int rid);

    @Select("select * from tab_route_img where rid = #{rid}")
    List<RouteImg> findById(int rid);

    @Select("select * from tab_seller where sid = #{sid}")
    Seller findSellById(int sid);

    @Select("select count(*) from tab_favorite where rid = #{rid}")
    int findFavoriteCountByRid(int rid);

    @Select("select * from tab_favorite where rid = #{rid} and uid = #{uid}")
    Favorite findByRidAndUid(int rid, int uid);

    @Select("insert into tab_favorite value(#{rid},#{date},#{uid})")
    void add(int rid,String date ,int uid);

    @Select("select count(*) from tab_favorite where uid = #{uid}")
    int findTotalCountByUid(int uid);

    @Select("select * from tab_favorite where uid = #{uid} limit #{start} , #{pageSize} ")
    List<TabFavorite> findByUid(int uid, int start, int pageSize);

    @Select("select count(*) from tab_favorite where rid = #{rid}")
    int findCountByRid(int rid);
}
