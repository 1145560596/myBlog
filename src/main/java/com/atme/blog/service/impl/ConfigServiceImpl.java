package com.atme.blog.service.impl;

import com.atme.blog.entity.Config;
import com.atme.blog.mapper.ConfigMapper;
import com.atme.blog.service.ConfigService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-10-18
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

    @Override
    public int updateConfig(String websiteDescription, String websiteDescriptionToUpdate) {
        Config config = new Config();
        config.setConfigValue(websiteDescriptionToUpdate);

        UpdateWrapper<Config> configUpdateWrapper = new UpdateWrapper<>();
        configUpdateWrapper.eq("config_name",websiteDescription);

        return baseMapper.update(config,configUpdateWrapper);
    }

    @Override
    public Map<String, String> getAllConfigs() {
        Wrapper<Config> wrapper = new QueryWrapper<>();
        List<Config> configs = baseMapper.selectList(wrapper);

        Map<String, String> configMap = configs.stream().
                collect(Collectors.toMap(Config::getConfigName, Config::getConfigValue));

        return configMap;
    }
}
