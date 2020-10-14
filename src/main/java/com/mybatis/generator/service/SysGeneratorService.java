package com.mybatis.generator.service;

import com.mybatis.generator.domian.column.Column;
import com.mybatis.generator.domian.table.Table;
import com.mybatis.generator.vo.result.ResultVO;

import java.util.List;
import java.util.Map;

/**
 * SysGeneratorService
 *
 * @author LYJ
 * @date 2020/7/16 8:55
 * @since 1.0
 */
public interface SysGeneratorService {

    /**
     * findAll
     *
     * @param map map
     * @return ResultVO
     */
    ResultVO findAll(Map<String, Object> map);

    /**
     * findTable
     *
     * @param tableName tableName
     * @return Table
     */
    Table findTable(String tableName);

    /**
     * List<Map<String, String>>
     *
     * @param tableName tableName
     * @return List<Column>
     */
    List<Column> findColumns(String tableName);

    /**
     * generatorCode
     *
     * @param tableNames tableNames
     * @return byte[]
     */
    byte[] generatorCode(String[] tableNames);
}
