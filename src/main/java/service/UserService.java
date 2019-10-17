package service;
import pojo.User;
public interface UserService {
    public boolean save(User user);
    public User check(User user);
}
