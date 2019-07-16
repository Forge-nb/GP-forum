package com.chinasoft.forum.dal.entity;


import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="comment")
@ToString
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    private String userEmail;

    private String userNickName;

    private String respondentUserNickName;

    private String respondentUserEmail;

    private Integer postId;

    private String content;
    
    private Integer targetCommentId;

    private Integer parentCommentId;

    @CreatedDate
    private Date commentTime;

    public Integer getTargetCommentId() {
        return targetCommentId;
    }

    public void setTargetCommentId(Integer targetCommentId) {
        this.targetCommentId = targetCommentId;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    public Integer getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Integer parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getRespondentUserNickName() {
        return respondentUserNickName;
    }

    public void setRespondentUserNickName(String respondentUserNickName) {
        this.respondentUserNickName = respondentUserNickName;
    }

    public String getRespondentUserEmail() {
        return respondentUserEmail;
    }

    public void setRespondentUserEmail(String respondentUserEmail) {
        this.respondentUserEmail = respondentUserEmail;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", userEmail='" + userEmail + '\'' +
                ", userNickName='" + userNickName + '\'' +
                ", respondentUserNickName='" + respondentUserNickName + '\'' +
                ", respondentUserEmail='" + respondentUserEmail + '\'' +
                ", postId=" + postId +
                ", content='" + content + '\'' +
                ", targetCommentId=" + targetCommentId +
                ", parentCommentId=" + parentCommentId +
                ", commentTime=" + commentTime +
                '}';
    }
}
