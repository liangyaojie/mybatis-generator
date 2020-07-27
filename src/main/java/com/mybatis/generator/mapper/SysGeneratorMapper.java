package com.mybatis.generator.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mybatis.generator.domian.column.Column;
import com.mybatis.generator.domian.table.Table;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * SysGeneratorMapper
 *
 * @author chentudong
 * @date 2020/5/9 11:25 上午
 * @since 1.0
 */
@Repository
public interface SysGeneratorMapper {
    /**
     * findAll
     *
     * @param page page
     * @param map  map
     * @return List<Map < String, String>>
     */
    List<Map<String, Object>> findAll(Page<Map<String, Object>> page, @Param("p") Map<String, Object> map);


    /**
     * findByTableName
     *
     * @param tableName tableName
     * @return Map<String, String>
     */
    Table findByTableName(String tableName);

    /**
     * findByColumns
     *
     * @param tableName tableName
     * @return List<Column>
     */
    List<Column> findByColumns(String tableName);
}
