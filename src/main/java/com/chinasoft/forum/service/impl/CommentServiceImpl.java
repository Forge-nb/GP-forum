package com.chinasoft.forum.service.impl;

import com.chinasoft.forum.dal.CommentRepository;
import com.chinasoft.forum.dal.entity.Comment;
import com.chinasoft.forum.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> findAllParentComment(Integer postId) {
        return commentRepository.findAllParentComment(postId);
    }

    @Override
    public List<Comment> findAllChildComment(Integer parentCommentId, Integer postId) {
        return commentRepository.findByParentCommentIdAndPostIdOrderByCommentTime(parentCommentId,postId);
    }
}
