package com.example.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.dto.UserQuery;
import com.example.demo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {
    // 分页查询用户
    IPage<User> getUserPage(int pageNum, int pageSize, Integer status);
    // 分页查询用户(使用查询对象)
    IPage<User> getUserPage(UserQuery query);
    // 更新用户状态
    boolean updateUserStatus(Long id, Integer status);
    // 保存/更新用户
    boolean saveOrUpdateUser(User user);

    // 添加用户
    boolean saveUser(User user);

    // 根据ID获取用户
    User getUserById(Long id);
    // 删除用户
    boolean deleteUser(Long id);
}