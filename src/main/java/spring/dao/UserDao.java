package spring.dao;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import spring.pojo.Users;

import java.util.List;

@Repository
public interface UserDao {

    @Select("select * from account")
    public List<Users> findAll();
}
