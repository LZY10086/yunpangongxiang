package controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pojo.User;
import service.UserServiceImpl;

import javax.servlet.http.HttpSession;
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    @RequestMapping("/tologin")//转到登陆页面
    public String toLogin(){
        return "login";
    }
    @RequestMapping("/toregist")//转到注册页面
    public String toregist(){
        return "regist";
    }
    @RequestMapping("/regist.action")//进行注册
    public String regist(User user){
        if (userService.save(user)){
            return "login";
        }
        return "regist";
    }
    @RequestMapping("/login.action")//进行登陆
    public String login(User user, HttpSession session) {
        User user1 = userService.check(user);
        if(user1!=null){
            session.setAttribute("user",user1);
            return "index";
        }else {
            return "login";
        }
    }
    @RequestMapping("/logout.action")
    public String logout(HttpSession session){
        session.invalidate();
        return "login";
    }

}
