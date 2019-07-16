package com.chinasoft.forum.service.impl;

import com.chinasoft.forum.dal.PostRepository;
import com.chinasoft.forum.dal.entity.Post;
import com.chinasoft.forum.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    public List<Post> getByEdiTime(String sectionName){
        List<Post> postList=postRepository.findBySectionNameOrderByLastEditTimeDesc(sectionName);
        return postList;
    }

    @Override
    public List<Post> getByStarNumber(String sectionName) {
        List<Post> postList=postRepository.findBySectionNameOrderByStarNumberDesc(sectionName);
        return postList;
    }

    @Override
    public Post getDetail(Integer postId) {
        return postRepository.findByPostId(postId);
    }

    @Override
    public void writeContent(String author_email, Integer section_name, String title, String summary, String content, boolean commentable, String post_status, Date lastEditTime) {
        Post post=new Post();
        post.setAuthorEmail(author_email);
        post.setSectionName(section_name);
        post.setTitle(title);
        post.setSummary(summary);
        post.setContent(content);
        post.setFirstImg(content);
        post.setCommentable(commentable);
        post.setPostStatus(post_status);
        post.setLastEditTime(lastEditTime);
        postRepository.save(post);
    }
}
