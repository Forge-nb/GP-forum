package com.chinasoft.forum.controller;

import com.chinasoft.forum.dal.entity.Comment;
import com.chinasoft.forum.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @RequestMapping("postDetail")
    public String toPostDetail(ModelMap modelMap){
//        Integer postId=1;
//        List<Comment> parentComments=commentService.findAllParentComment(postId);
//        if(parentComments.size()!=0) {
//            modelMap.put("parentComments", parentComments);
//            modelMap.put("parentCommentsNumber", parentComments.size());
//        }
        return "postDetail";
    }
}
