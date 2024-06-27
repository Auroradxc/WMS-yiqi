package com.wcsb.wbs.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 不知道这个in_out_state是来自于那里的，所以目前不用这个表
 * </p>
 *
 * @author Jessica_Clorize
 * @since 2024-06-14
 */
@Getter
@Setter
@TableName("bar_code")
public class BarCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String supId;

    private String supplier;

    private String barNum;

    private String status;

    private String wrapSize;

    private String numContained;

    private String creator;

    private String updator;

    private String createTime;

    private String updateTime;


}
