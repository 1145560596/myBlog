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
@TableName("tb_config")
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置项的名称
     */
    @TableId(value = "config_name", type = IdType.ID_WORKER_STR)
    private String configName;

    /**
     * 配置项的值
     */
    private String configValue;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Config{" +
        "configName=" + configName +
        ", configValue=" + configValue +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
