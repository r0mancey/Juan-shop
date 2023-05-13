package com.example.juan.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 用户信息
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //手机号
    private String phone;

    //密码
    private String password;

    //昵称
    private String nickname;

    //邮箱
    private String email;

    //头像
    private String avatar;

    //状态 0:禁用，1:正常
    private Integer status;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
