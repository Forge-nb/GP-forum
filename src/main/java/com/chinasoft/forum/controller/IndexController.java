package com.chinasoft.forum.controller;


import com.alibaba.fastjson.JSONObject;
import com.chinasoft.forum.configure.OnlineUserList;
import com.chinasoft.forum.dal.entity.User;
import com.chinasoft.forum.service.RedisService;
import com.chinasoft.forum.service.UserService;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
public class IndexController {
    private JSONObject json = new JSONObject();

    @Autowired
    private RedisService redisUtils;

    @Autowired
    private UserService userService;

//    private List<String> codeList=new ArrayList<>();

    @RequestMapping( "/")
    public String Start(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (LoginController.COOKIE_NAME.equals(cookie.getName())) {
                    String email=cookie.getValue();
                    User user=userService.userCoookie(email);
//                    String password=redisUtils.get(email+LoginController.COOKIE_NAME);
                    if(user!=null){
//                        if (user.getUserPassword().equals(password)) {
                            if (OnlineUserList.containsKey(email)) {
                                HttpSession session = OnlineUserList.get(email);
                                session.invalidate();
                                OnlineUserList.remove(email);
                                HttpSession newSession = request.getSession();
                                newSession.setAttribute("User", user);
                                OnlineUserList.put(email, newSession);
                                return "index";
                            } else {
                                request.getSession().setAttribute("User", user);
                                OnlineUserList.put(email, request.getSession());
                                return "index";
                            }
//                        } else {
//                            return "login";
//                        }
                    }else {
                        return "login";
                    }
                }
            }
        }
        return "login";

    }

    @RequestMapping("/index")
    public String toIndex() {
        return "index";
    }

//    @RequestMapping("star")
//    public String Star(){
//        redisUtils.set("1","点赞");
//        User person1 = new User();
//        User person2 = new User();
//        User person3 = new User();
//        person1.setNickName("1");
//        person2.setNickName("2");
//        person3.setNickName("3");
//        List<User> list = new ArrayList<>();
//        list.add(person1);
//        list.add(person2);
//        list.add(person3);
//        redisUtils.set("redis_list_test", json.toJSONString(list));
//        return "index";
//    }

//    @RequestMapping("send")
//    public String Send(String userEmail){
//        HtmlEmail email=new HtmlEmail();
//        int n=userEmail.indexOf('@');
//        String hostName=userEmail.substring(n+1);
//        email.setHostName("smtp."+hostName);
//        email.setCharset("utf-8");
//        try {
//            email.addTo(userEmail);
//            email.setFrom("1194688236@qq.com","聚集地论坛");
//            email.setAuthentication("1194688236@qq.com","nlqqmczegpalbagd");
//            email.setSubject("邮箱验证");//设置发送主题
//            String code=RandomCode();
//            email.setMsg("您正在注册聚集地论坛，您的验证码为： "+code+"。 如非本人操作，请忽略本邮件。");//设置发送内容
//            email.send();//进行发送
////            codeList.add(code);
////            redisUtils.set("codeList",json.toJSONString(codeList));
//            redisUtils.set(userEmail,code);
//            redisUtils.expire(userEmail,300);
//        } catch (EmailException e) {
//            e.printStackTrace();
//        }
//        return "index";
//    }
//
//    public String RandomCode(){
//        StringBuffer code=new StringBuffer();
//        Random random=new Random();
//        for(int i=0;i<6;i++){
//            code.append(random.nextInt(10)+"");
//        }
//        return code.toString();
//    }
}
