package com.qf.qfseckill.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (TbUser)实体类
 *
 * @author makejava
 * @since 2023-07-25 17:45:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_user")
public class TbUser implements Serializable {
    private static final long serialVersionUID = -82358409192539859L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    
    private String email;
    
    private String password;
    
    private String type;




}

