package com.king.redis.dao;

import com.king.base.BaseMapper;
import com.king.redis.pojo.Country;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryMapper extends BaseMapper<Country> {
    List<String> selectAllIds();
}