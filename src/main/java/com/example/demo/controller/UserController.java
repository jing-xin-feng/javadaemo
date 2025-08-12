package com.example.demo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.dto.UserQuery;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public String listUsers(UserQuery query, Model model) {
        // 手动设置默认值（确保UserQuery类中有pageNum/pageSize字段）
        if (query.getPageNum() == null) query.setPageNum(1);
        if (query.getPageSize() == null) query.setPageSize(10);

        IPage<User> userPage = userService.getUserPage(query);
        model.addAttribute("page", userPage);
        model.addAttribute("query", query); // 回显查询条件
        return "user/list";
    }

    // 用户详情页（保持不变）
    @GetMapping("/{id}")
    public String getUserDetail(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) {
            log.warn("用户ID: {} 不存在", id);
            return "redirect:/users";
        }
        model.addAttribute("user", user);
        return "user/detail";
    }

    // 新增用户表单（保持不变）
    @GetMapping("/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User()); // Add this line
        return "user/form";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        log.info("编辑用户ID: {}", id);
        User user = userService.getUserById(id);
        if (user == null) {
            log.warn("编辑不存在的用户: {}", id);
            return "redirect:/users";
        }
        model.addAttribute("user", user);
        return "user/form";
    }

    // 保存用户（优化：移除多余的模型属性）
    @PostMapping("/save")
    public String saveUser(
            @Valid @ModelAttribute("user") User user, // 绑定到"user"属性
            BindingResult result // 自动紧跟被验证对象
    ) {
        if (result.hasErrors()) {
            return "user/form"; // 自动使用model中的"user"
        }

        if (!userService.saveOrUpdateUser(user)) {
            // 添加业务错误（非校验错误）
            result.reject("global.error", "保存用户失败");
            return "user/form";
        }
        return "redirect:/users";
    }

    // 删除用户（保持不变）
    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    // 更新用户状态（添加验证失败处理）
    @PostMapping("/status")
    public String updateStatus(
            @RequestParam Long id,
            @RequestParam @Min(0) @Max(1) Integer status
    ) {
        // 参数验证由Spring自动处理，失败会抛出MethodArgumentNotValidException
        userService.updateUserStatus(id, status);
        return "redirect:/users";
    }
    @GetMapping("/form")
    public String redirectToAddForm() {
        return "redirect:/users/add";
    }
}