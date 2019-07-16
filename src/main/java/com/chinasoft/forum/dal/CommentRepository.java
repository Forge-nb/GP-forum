package com.chinasoft.forum.dal;

import com.chinasoft.forum.dal.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CommentRepository  extends JpaRepository<Comment,Integer> {

    @Query(value = "select * from gpf_dev.comment where post_id=?1 and parent_comment_id is null order by comment_time",nativeQuery = true)
    List<Comment> findAllParentComment(Integer postId);

    List<Comment> findByParentCommentIdAndPostIdOrderByCommentTime(Integer parentCommentId,Integer postId);

}
