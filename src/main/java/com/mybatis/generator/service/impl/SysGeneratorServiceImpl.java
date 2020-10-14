package com.mybatis.generator.service.impl;

import com.mybatis.generator.domian.column.Column;
import com.mybatis.generator.domian.table.Table;
import com.mybatis.generator.mapper.SysGeneratorMapper;
import com.mybatis.generator.service.SysGeneratorService;
import com.mybatis.generator.utils.GenUtils;
import com.mybatis.generator.vo.result.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * SysGeneratorServiceImpl
 *
 * @author LYJ
 * @date 2020/7/16 9:04
 * @since 1.0
 */
@Service
public class SysGeneratorServiceImpl implements SysGeneratorService {
    @Autowired
    private SysGeneratorMapper sysGeneratorMapper;

    /**
     * findAll
     *
     * @param map map
     * @return ResultVO
     */
    @Override
    public ResultVO findAll(Map<String, Object> map) {
        return null;
    }

    /**
     * findTable
     *
     * @param tableName tableName
     * @return Table
     */
    @Override
    public Table findTable(String tableName) {
        return sysGeneratorMapper.findByTableName(tableName);
    }

    /**
     * List<Map<String, String>>
     *
     * @param tableName tableName
     * @return List<Column>
     */
    @Override
    public List<Column> findColumns(String tableName) {
        return sysGeneratorMapper.findByColumns(tableName);
    }

    /**
     * generatorCode
     *
     * @param tableNames tableNames
     * @return byte[]
     */
    @Override
    public byte[] generatorCode(String[] tableNames) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zip = new ZipOutputStream(outputStream)) {
            for (String tableName : tableNames) {
                //查询表信息
                Table table = findTable(tableName);
                //查询列信息
                List<Column> columns = findColumns(tableName);
                //生成代码
                GenUtils.generatorCode(table, columns, zip);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }
}
