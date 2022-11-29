package spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.domain.User;
import spring.mapper.UserMapper;
import spring.pojo.Users;
import spring.utils.UuidUtil;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper mapper;

    public Users queryUserById(int id){
        return this.mapper.selectByPrimaryKey(id);
    }
//
//    @Transactional
//    public void deleteUserById(int id){
//        this.userMapper.deleteByPrimaryKey(id);
//    }

    public List<Users> queryAll() {
        return mapper.findAll();
    }

    public User login(User user) {
        return mapper.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }

    public boolean regist(User user) {
        User u = mapper.findByUsername(user.getUsername());
        if(u != null){
            return  false;
        }
        user.setCode(UuidUtil.getUuid());
        user.setStatus("N");
        mapper.save(user);
        //String content = "<a href='http://localhost/travel/user/active?code="+user.getCode()+"'>点击</a>";
//        cn.itcast.travel.util.MailUtils.sendMail(user.getEmail(),content,"激活邮件");
        return true;
    }
}
