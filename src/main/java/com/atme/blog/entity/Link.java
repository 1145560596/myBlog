package com.atme.blog.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author testjava
 * @since 2020-10-18
 */
@TableName("tb_link")
public class Link implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 友链表主键id
     */
    @TableId(value = "link_id", type = IdType.AUTO)
    private Integer linkId;

    /**
     * 友链类别 0-友链 1-推荐 2-个人网站
     */
    private Integer linkType;

    /**
     * 网站名称
     */
    private String linkName;

    /**
     * 网站链接
     */
    private String linkUrl;

    /**
     * 网站描述
     */
    private String linkDescription;

    /**
     * 用于列表排序
     */
    private Integer linkRank;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    private Integer isDeleted;

    /**
     * 添加时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


    public Integer getLinkId() {
        return linkId;
    }

    public void setLinkId(Integer linkId) {
        this.linkId = linkId;
    }

    public Integer getLinkType() {
        return linkType;
    }

    public void setLinkType(Integer linkType) {
        this.linkType = linkType;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getLinkDescription() {
        return linkDescription;
    }

    public void setLinkDescription(String linkDescription) {
        this.linkDescription = linkDescription;
    }

    public Integer getLinkRank() {
        return linkRank;
    }

    public void setLinkRank(Integer linkRank) {
        this.linkRank = linkRank;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Link{" +
        "linkId=" + linkId +
        ", linkType=" + linkType +
        ", linkName=" + linkName +
        ", linkUrl=" + linkUrl +
        ", linkDescription=" + linkDescription +
        ", linkRank=" + linkRank +
        ", isDeleted=" + isDeleted +
        ", createTime=" + createTime +
        "}";
    }
}
