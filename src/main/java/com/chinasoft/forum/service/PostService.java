package com.chinasoft.forum.service;

import com.chinasoft.forum.dal.entity.Post;

import java.util.Date;
import java.util.List;

public interface PostService {
    void writeContent(String author_email, Integer section_name, String title, String summary, String content, boolean commentable, String post_status, Date lastEditTime);

    List<Post> getByEdiTime(String sectionName);

    List<Post> getByStarNumber(String sectionName);

    Post getDetail(Integer postId);
}
