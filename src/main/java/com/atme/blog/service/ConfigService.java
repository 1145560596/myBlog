package com.atme.blog.service;

import com.atme.blog.entity.Config;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2020-10-18
 */
public interface ConfigService extends IService<Config> {

    int updateConfig(String websiteDescription, String websiteDescriptionToUpdate);

    Map<String,String> getAllConfigs();
}
