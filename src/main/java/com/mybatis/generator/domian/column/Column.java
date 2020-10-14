package com.mybatis.generator.domian.column;

/**
 * Column
 *
 * @author LYJ
 * @date 2020/5/7 2:18 下午
 * @since 1.0
 */
public class Column {
    /**
     * 主键
     */
    private String columnKey;
    /**
     * 列名
     */
    private String columnName;

    /**
     * 加上 AS
     */
    private String asColumnName;

    /**
     * 在末尾加上逗号
     */
    private String commaColumnName;

    /**
     * 列名全部大写
     */
    private String columnNameUpperCase;

    /**
     * 列名类型
     */
    private String dataType;

    /**
     * 列名备注
     */
    private String comments;

    /**
     * 属性名称(第一个字母大写)，如：user_name => UserName
     */
    private String attrName;

    /**
     * 属性名称(第一个字母小写)，如：user_name => userName
     */
    private String attrname;

    /**
     * 属性类型
     */
    private String attrType;

    /**
     * auto_increment
     */
    private String extra;

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnNameUpperCase() {
        return columnNameUpperCase;
    }

    public void setColumnNameUpperCase(String columnNameUpperCase) {
        this.columnNameUpperCase = columnNameUpperCase;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrname() {
        return attrname;
    }

    public void setAttrname(String attrname) {
        this.attrname = attrname;
    }

    public String getAttrType() {
        return attrType;
    }

    public void setAttrType(String attrType) {
        this.attrType = attrType;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getAsColumnName() {
        return asColumnName;
    }

    public void setAsColumnName(String asColumnName) {
        this.asColumnName = asColumnName;
    }

    public String getCommaColumnName() {
        return commaColumnName;
    }

    public void setCommaColumnName(String commaColumnName) {
        this.commaColumnName = commaColumnName;
    }
}
