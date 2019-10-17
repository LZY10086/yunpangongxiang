package dao;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import pojo.User;
@Mapper
@Repository
public interface UserDao {
    public User select(User user);//判断用户名和密码是否一致
    public User selectUser(User user);//判断用户名是否存在
    public void insert(User user);
    public void update(User user);
    public void delete(int id);
    public void updateSize(User user);
}
