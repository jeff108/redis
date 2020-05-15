package com.king.redis.pojo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String countryname;

    /**
     * 代码
     */
    private String countrycode;

    private String name;
}