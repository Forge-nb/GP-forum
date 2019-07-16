package com.chinasoft.forum.service;

import com.chinasoft.forum.dal.entity.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findAllParentComment(Integer postId);

    List<Comment> findAllChildComment(Integer parentCommentId,Integer postId);
}
