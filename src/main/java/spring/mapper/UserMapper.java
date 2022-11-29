package spring.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import spring.domain.User;
import spring.pojo.Users;

import java.util.List;

@Mapper
public interface UserMapper  {
    @Select("select * from account")
    public List<Users> findAll();
    @Select("select * from account where id = #{id}")
    Users selectByPrimaryKey(int id);

    /***************************************/

    @Select("select * from tab_user where username = #{username} and password = #{password}")
    User findByUsernameAndPassword(String username, String password);

    @Select("select * from tab_user where username = #{username} ")
    User selectList(String name);

    @Select("select * from tab_user where username = #{username}")
    User findByUsername(String username);

    @Insert("insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) values(#{username},#{password},#{name},#{birthday},#{sex },#{telephone},#{email},#{status},#{code})")
    void save(User user);
}
