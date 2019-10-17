package service;
import dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pojo.User;
@Service
public class UserServiceImpl  implements UserService{
    @Autowired
    private UserDao userDao;
    @Override
    public boolean save(User user){
        if(userDao.selectUser(user)==null){
            userDao.insert(user);
            return true;
        }else {
            return false;
        }
    }
    @Override
    public User check(User user){
        User user1=null;
        user1=userDao.select(user);
        if(user1!=null){
            return user1;
        }
        return null;
    }
}
