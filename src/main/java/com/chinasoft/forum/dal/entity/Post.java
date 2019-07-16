package com.chinasoft.forum.dal.entity;

import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.data.annotation.CreatedDate;

@Entity
@ToString
@Table(name="post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;

    private String authorEmail;

    private Integer sectionName;

    @Column(length=10000)
    private String content;

    @CreatedDate
    private Date lastEditTime;

    private String postStatus;

    private String title;

    private String summary;

    private boolean commentable;

    @Column(length = 10000)
    private String firstImg;

    public boolean isCommentable() {
        return commentable;
    }

    public void setCommentable(boolean commentable) {
        this.commentable = commentable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstImg() {
        return firstImg;
    }

    public void setFirstImg(String content) {
        Document document= Jsoup.parse(content);
        Element element=document.select("img[src]").first();
        if(element!=null){
            this.firstImg=element.attr("src");
        }else{
            this.firstImg="";
        }
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public Integer getSectionName() {
        return sectionName;
    }

    public void setSectionName(Integer sectionName) {
        this.sectionName = sectionName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public String getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", authorEmail='" + authorEmail + '\'' +
                ", sectionName=" + sectionName +
                ", content='" + content + '\'' +
                ", lastEditTime=" + lastEditTime +
                ", postStatus='" + postStatus + '\'' +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", commentable=" + commentable +
                ", firstImg='" + firstImg + '\'' +
                '}';
    }
}
