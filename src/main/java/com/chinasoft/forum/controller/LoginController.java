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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

@Controller
public class LoginController {

    public final static String COOKIE_NAME = "sd53a32ds33daa5151f3d3aa1s3";

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    private JSONObject json = new JSONObject();

    @RequestMapping("/login")
    public String toLogin() {
        return "login";
    }

    @ResponseBody
    @RequestMapping(value = "signinAction", method = RequestMethod.POST)
    public String SignIn(HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
//        System.out.println(email+"\t"+password);
        User user = userService.signIn(email, password);
        if (user != null) { //用户名密码正确
            HttpSession newSession = request.getSession();
            if (OnlineUserList.containsKey(email)) {  //判断该账户是否已登录
                HttpSession session = OnlineUserList.get(email);
                if (session.getId().equals(newSession.getId())) { //判断是否为同一台机器登录
                    return "您已在该设备登录,请勿重复操作";
                } else {  //两台机器登录同一账号，后一个把前一个挤掉
                    session.invalidate();
                    OnlineUserList.remove(email);
                    if(user.getThisLogTime()!=null){
                        user.setLastLogTime(user.getThisLogTime());
                    }
                    user.setThisLogTime(new Date());
                    newSession.setAttribute("User", user);
                    OnlineUserList.put(email, newSession);
                    addCookie(response,user);
                    return "登录成功";
                }
            } else {  //该账号未登陆，正常进行登录
                if(user.getThisLogTime()!=null){
                    user.setLastLogTime(user.getThisLogTime());
                }
                user.setThisLogTime(new Date());
                newSession.setAttribute("User", user);
                OnlineUserList.put(email, newSession);
                modelMap.put("user", user);
                addCookie(response,user);
                return "登录成功";
            }
        } else {  //用户名或密码错误
            return "用户名或密码错误";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/signupAction", method = RequestMethod.POST)
    public String SignUp(HttpServletRequest request, ModelMap modelMap,HttpServletResponse response) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String veriCode = request.getParameter("veriCode");
        String code = new String();
        String msg = new String();
        code = redisService.get(email);
        if(!userService.checkRepeat(email)){
            msg="该邮箱已被注册";
            return msg;
        }
        if (code == null || code.equals("")) {
            msg = "验证码失效";
            return msg;
        }
        if (!password.equals(confirmPassword)) {
            msg = "两次密码不一致";
            return msg;
        } else if (!code.equals(veriCode)) {
            msg = "验证码错误";
            return msg;
        }
//        if(password.equals(confirmPassword)){
        User user = new User();
        user.setUserEmail(email);
        user.setUserPassword(password);
        user.setNickName(email);
        user.setAvatar("/img/no_avatar.png");
        user.setRegTime(new Date());
        user.setThisLogTime(new Date());
        userService.signUp(user);
        request.getSession().setAttribute("User", user);
        modelMap.put("user", user);
        redisService.remove(email);
        msg = "注册成功";
        addCookie(response,user);
        return msg;
//        }else
//            return "redirect:login";
    }

    @ResponseBody
    @RequestMapping(value = "sendCode", method = RequestMethod.POST)
    public String Send(HttpServletRequest request) {
        String userEmail = request.getParameter("email");
        if (!userService.checkRepeat(userEmail)) {
            return "该邮箱已被注册";
        } else {
            HtmlEmail email = new HtmlEmail();
            int n = userEmail.indexOf('@');
            String hostName = userEmail.substring(n + 1);
            email.setHostName("smtp." + hostName);
            email.setCharset("utf-8");
            try {
                email.addTo(userEmail);
                email.setFrom("1194688236@qq.com", "聚集地论坛");
                email.setAuthentication("1194688236@qq.com", "rkjhrvjftpizigbg");
                email.setSubject("邮箱验证");//设置发送主题
                String code = RandomCode();
                email.setMsg("您正在注册聚集地论坛，您的验证码为： " + code + ",有效时间三分钟。 如非本人操作，请忽略本邮件。");//设置发送内容
                email.send();//进行发送
//            codeList.add(code);
//            redisUtils.set("codeList",json.toJSONString(codeList));
                redisService.set(userEmail, code);
                redisService.expire(userEmail, 300);
            } catch (EmailException e) {
                e.printStackTrace();
                return "发送失败，请重试";
            }
            return "验证码已发送";
        }
    }

    public String RandomCode() {
        StringBuffer code = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10) + "");
        }
        return code.toString();
    }

    public void addCookie(HttpServletResponse response, User user){
        String email= user.getUserEmail();
        Cookie cookie = new Cookie(LoginController.COOKIE_NAME,email);
//        redisService.set(email+LoginController.COOKIE_NAME,user.getUserPassword());
        cookie.setMaxAge(604800);//保存一周
//        redisService.expire(email+LoginController.COOKIE_NAME,604800);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    @RequestMapping(value = "signout",method = RequestMethod.POST)
    public String signOut(HttpServletRequest request,HttpServletResponse response){
        Cookie cookie = new Cookie(LoginController.COOKIE_NAME, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        request.getSession().removeAttribute("User");
        return "index";
    }
}
