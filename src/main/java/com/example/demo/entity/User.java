package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@TableName("sys_user")  //// 映射数据库表
public class User {
    @TableId(type = IdType.AUTO)   // 自增主键
    private Long id;
    @NotBlank(message = "用户名不能为空")   // 校验规则
    @Size(min = 2, max = 20, message = "用户名长度必须在2-20之间")
    private String username;
    
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
    
    private Integer status; //  // 逻辑删除标记（0-未删除，1-已删除）
    
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic // 逻辑删除
    private Integer deleted;
}
