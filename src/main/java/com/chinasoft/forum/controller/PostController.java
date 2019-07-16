package com.chinasoft.forum.controller;

import com.alibaba.fastjson.JSON;
import com.chinasoft.forum.dal.entity.Comment;
import com.chinasoft.forum.dal.entity.Post;
import com.chinasoft.forum.dal.entity.User;
import com.chinasoft.forum.service.CommentService;
import com.chinasoft.forum.service.PostService;
import com.chinasoft.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
public class PostController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @RequestMapping("postDetail")
    public String toPostDetail(ModelMap modelMap){
        Integer postId=1;
        Post postDetail=postService.getDetail(postId);
        modelMap.put("postDetail",postDetail);
        List<Comment> parentComments=commentService.findAllParentComment(postId);
        User user;
        for(int i=0;i<parentComments.size();i++){
            user = userService.userCoookie(parentComments.get(i).getUserEmail());
            parentComments.get(i).setUserNickName(user.getNickName());
        }
        if(parentComments.size()!=0) {
            modelMap.put("parentComments", parentComments);
            modelMap.put("parentCommentsNumber", parentComments.size());
        }
        return "postDetail";
    }

    @RequestMapping(value = "writeComment",method = RequestMethod.POST)
    @ResponseBody
    public boolean writeComment(HttpServletRequest request){
        User user=(User)request.getSession().getAttribute("User");
        String userEmail=user.getUserEmail();
        String nickName=user.getNickName();
        String content=request.getParameter("commentContent");
        Comment comment=new Comment();
        comment.setUserEmail(userEmail);
        comment.setCommentTime(new Date());
        comment.setContent(content);
        comment.setPostId(1);
        comment.setUserNickName(nickName);
        commentService.insert(comment);
        return true;
    }
    @RequestMapping("commentDetail")
    public String commentDetail(ModelMap modelMap){
        Integer parentId=1;
        Integer postId=1;
        List<Comment> childrenComments=commentService.findAllChildComment(parentId,postId);
        User user;
        for(int i=0;i<childrenComments.size();i++){
            user = userService.userCoookie(childrenComments.get(i).getUserEmail());
            childrenComments.get(i).setUserNickName(user.getNickName());
            user=userService.userCoookie(childrenComments.get(i).getRespondentUserEmail());
            childrenComments.get(i).setRespondentUserNickName(user.getNickName());
        }
        if(childrenComments.size()!=0) {
            modelMap.put("childrenComments", childrenComments);
            modelMap.put("childrenCommentsNumber", childrenComments.size());
        }
        return "postDetail";
    }

    @ResponseBody
    @RequestMapping(value = "writeAction", method = RequestMethod.POST)
    public String write(HttpServletRequest request){
        try {
            Integer section_name = Integer.parseInt(request.getParameter("section_name"));       //板块
            String title = request.getParameter("title");                                        //标题
            String content = request.getParameter("content");                                    //内容
            Boolean commentable = Boolean.parseBoolean(request.getParameter("commentable"));        //是否可评论
            String summary = content.replaceAll("<([^>]*)>", "");                      //摘要需要先把内容正则化，然后再次判断其长度;                                                                //取摘要
            if(summary.length()>=20)
                summary=summary.substring(0,20);
            User user= (User)request.getSession().getAttribute("User");
            String author_email=user.getUserEmail();
            postService.writeContent(author_email, section_name, title, summary, content,  commentable, "正常", new Date());
            return JSON.toJSONString("发表成功！");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return JSON.toJSONString("发表失败！");
        }
    }

}
