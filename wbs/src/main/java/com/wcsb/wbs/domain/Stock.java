package com.wcsb.wbs.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String supId;

    private Integer stockNum;

    private Integer lowestNum;

    private Integer highestNum;

    private String creator;

    private String updator;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
