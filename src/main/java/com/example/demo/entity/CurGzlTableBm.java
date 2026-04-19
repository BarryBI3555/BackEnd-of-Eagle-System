package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("acd_dangri_gzl_bm")
public class CurGzlTableBm {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("comcode")
    private String comCode; // 部门编码

    @TableField("comname")
    private String comName; // 部门

    @TableField("ck_jsl")
    private Integer ckJsl; // 查勘接收量

    @TableField("ck_jsl_wcl")
    private Integer ckJslWcl; // 查勘接收完成数量

    @TableField("ck_wcl")
    private Integer ckWcl; // 查勘完成量

    @TableField("ds_tjl")
    private Integer dsTjl; // 定损提交量

    @TableField("ds_wcl")
    private Integer dsWcl; // 定损完成量

    @TableField("ds_zfl")
    private Integer dsZfl; // 定损支付量

    @TableField("shougen")
    private Integer shouGen; // 首跟数量

    @TableField("hougen")
    private Integer houGen; // 后跟数量

    @TableField("tiaojie")
    private Integer tiaoJie; // 调解数量

    @TableField("ja")
    private Integer ja; // 结案数量

    @TableField("zl")
    private Integer zl; // 总量

    @TableField("tjdate")
    private String tjDate; // 统计日期

    @TableField("tjtime")
    private String maxTjTime; // 统计时间
}