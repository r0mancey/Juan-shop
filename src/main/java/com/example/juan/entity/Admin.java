package com.example.juan.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 管理员
 */
@Data
public class Admin implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    //账号
    private String username;

    //密码
    private String password;

    //公共字段填充——创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //公共字段填充——更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
