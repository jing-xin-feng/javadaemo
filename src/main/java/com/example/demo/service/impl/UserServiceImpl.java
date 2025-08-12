package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dto.UserQuery; // 移动到dto包
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    // 添加这个方法来修复返回类型冲突
    @Override
    public UserMapper getBaseMapper() {
        return baseMapper;
    }

    @Override
    public IPage<User> getUserPage(int pageNum, int pageSize, Integer status) {
        Page<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (status != null) {
            wrapper.eq("status", status);
        }
        wrapper.eq("deleted", 0);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public boolean updateUserStatus(Long id, Integer status) {
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        return updateById(user);
    }

    @Override
    public boolean saveUser(User user) {
        return save(user);
    }

    @Override
    public User getUserById(Long id) {
        return getById(id);
    }

    @Override
    public boolean saveOrUpdateUser(User user) {
        return saveOrUpdate(user);
    }

    @Override
    public boolean deleteUser(Long id) {
        return removeById(id);
    }

    @Override
    public IPage<User> getUserPage(UserQuery query) {
        Page<User> page = new Page<>(query.getPageNum(), query.getPageSize());
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        if (StringUtils.hasText(query.getUsername())) {
            wrapper.like("username", query.getUsername());
        }
        if (StringUtils.hasText(query.getEmail())) {
            wrapper.like("email", query.getEmail());
        }
        if (query.getStatus() != null) {
            wrapper.eq("status", query.getStatus());
        }
        wrapper.eq("deleted", 0);
        wrapper.orderByDesc("create_time");
        return baseMapper.selectPage(page, wrapper);
    }
}