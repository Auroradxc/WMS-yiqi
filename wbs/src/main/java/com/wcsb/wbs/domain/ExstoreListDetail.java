package com.wcsb.wbs.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author Jessica_Clorize
 * @since 2024-06-03
 */
@Getter
@Setter
@TableName("exstore_list_detail")
public class ExstoreListDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String listId;

    private String listNum;

    private String supId;

    private String wrapSize;

    private Integer expInNum;

    private Integer actInNum;

    private String creator;

    private String updator;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
