package com.mybatis.generator.utils;

import cn.hutool.core.date.DateUtil;
import com.mybatis.generator.domian.column.Column;
import com.mybatis.generator.domian.table.Table;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * GenUtils
 *
 * @author chentudong
 * @date 2020/5/9 11:30 上午
 * @since 1.0
 */
public final class GenUtils {
    private GenUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final static String FILE_NAME_MODEL = "Model.java.vm";
    private final static String FILE_NAME_MAPPER = "Mapper.java.vm";
    private final static String FILE_NAME_MAPPERXML = "Mapper.xml.vm";
    private final static String FILE_NAME_SERVICE = "Service.java.vm";
    private final static String FILE_NAME_SERVICEIMPL = "ServiceImpl.java.vm";
    private final static String FILE_NAME_CONTROLLER = "Controller.java.vm";
    private final static String FILE_NAME_PAGE = "index.html.vm";
    private final static String TEMPLATE_PATH = "template/";
    private final static String PACKAGE = "package";
    private final static String MODULE_NAME = "moduleName";

    public static List<String> getTemplates() {
        List<String> templates = new ArrayList<>();
        templates.add(TEMPLATE_PATH + FILE_NAME_MODEL);
        templates.add(TEMPLATE_PATH + FILE_NAME_MAPPER);
        templates.add(TEMPLATE_PATH + FILE_NAME_MAPPERXML);
        templates.add(TEMPLATE_PATH + FILE_NAME_SERVICE);
        templates.add(TEMPLATE_PATH + FILE_NAME_SERVICEIMPL);
        templates.add(TEMPLATE_PATH + FILE_NAME_CONTROLLER);
        templates.add(TEMPLATE_PATH + FILE_NAME_PAGE);
        return templates;
    }

    /**
     * 生成代码
     *
     * @param table   table
     * @param columns columns
     * @param zip     zip
     */
    public static void generatorCode(Table table, List<Column> columns, ZipOutputStream zip) {
        //配置信息
        Configuration config = getConfig();
        boolean hasBigDecimal = false;
        //表名转换成Java类名
        String className = tableToJava(table.getTableName(), config.getString("tablePrefix"));
        table.setClassName(className);
        table.setClassname(StringUtils.uncapitalize(className));

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        String mainPath = config.getString("mainPath");
        List<String> ignoreColumns = getIgnoreColumns(config);
        //列信息
        List<Column> ignoreColumnsInfo = new ArrayList<>();
        List<Column> columnsInfo = new ArrayList<>();
        for (Column column : columns) {
            String columnName = column.getColumnName();
            columnsInfo.add(column);
            if (!ignoreColumns.contains(columnName)) {
                ignoreColumnsInfo.add(column);
            }
            column.setColumnNameUpperCase(columnName.toUpperCase());
            //列名转换成Java属性名
            String attrName = columnToJava(columnName);
            column.setAttrName(attrName);
            column.setAttrname(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            String attrType = config.getString(column.getDataType(), "unknowType");
            column.setAttrType(attrType);
            if (!hasBigDecimal && "BigDecimal".equals(attrType)) {
                hasBigDecimal = true;
            }
            //是否主键
            if ("PRI".equalsIgnoreCase(column.getColumnKey()) && Objects.isNull(table.getPk())) {
                table.setPk(column);
            }
        }
        table.setColumns(columnsInfo);
        //没主键，则第一个字段为主键
        if (Objects.isNull(table.getPk())) {
            table.setPk(table.getColumns().get(0));
        }
        mainPath = StringUtils.isBlank(mainPath) ? "io.renren" : mainPath;
        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", table.getTableName());
        map.put("comments", table.getComments());
        map.put("pk", table.getPk());
        map.put("className", table.getClassName());
        map.put("classname", table.getClassname());
        map.put("pathName", table.getClassname().toLowerCase());
        map.put("columns", setAs(table.getColumns()));
        map.put("ignoreColumns", ignoreColumnsInfo);
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("mainPath", mainPath);
        map.put(PACKAGE, config.getString(PACKAGE));
        map.put(MODULE_NAME, config.getString(MODULE_NAME));
        map.put("author", config.getString("author"));
        map.put("since", config.getString("since"));
        map.put("email", config.getString("email"));
        map.put("datetime", DateUtil.format(new Date(), DATETIME_FORMAT));
        VelocityContext context = new VelocityContext(map);
        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            //渲染模板
            try (StringWriter sw = new StringWriter()) {
                Template tpl = Velocity.getTemplate(template, "UTF-8");
                tpl.merge(context, sw);
                //添加到zip
                zip.putNextEntry(new ZipEntry(getFileName(template, table.getClassName(), config.getString(PACKAGE), config.getString(MODULE_NAME))));
                IOUtils.write(sw.toString(), zip, StandardCharsets.UTF_8);
                zip.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取忽略字段
     *
     * @param config config
     * @return List<String>
     */
    private static List<String> getIgnoreColumns(Configuration config) {
        if (Objects.isNull(config)) {
            return new ArrayList<>(0);
        }
        return new ArrayList<>(Arrays.asList(config.getStringArray("ignoreColumns")));
    }

    private static List<Column> setAs(List<Column> columns) {
        if (Objects.isNull(columns)) {
            return new ArrayList<>(0);
        }
        List<Column> result = new ArrayList<>(columns.size());
        int size = columns.size();
        int lastSize = size - 1;
        for (int i = 0; i < size; i++) {
            Column column = columns.get(i);
            String columnName = column.getColumnName();
            String attrName = column.getAttrname();
            if (i < lastSize) {
                column.setCommaColumnName(columnName + ",");
                column.setAsColumnName(columnName + " as " + attrName + ",");
            } else {
                column.setCommaColumnName(columnName);
                column.setAsColumnName(columnName + " as " + attrName);
            }
            result.add(column);
        }
        return result;
    }

    /**
     * 表名转换成Java类名
     *
     * @param tableName   tableName
     * @param tablePrefix tablePrefix
     * @return String
     */
    public static String tableToJava(String tableName, String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.substring(tablePrefix.length());
        }
        return columnToJava(tableName);
    }

    /**
     * 列名转换成Java属性名
     *
     * @param columnName columnName
     * @return String
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 获取文件名
     *
     * @param template    template
     * @param className   className
     * @param packageName packageName
     * @param moduleName  moduleName
     * @return String
     */
    public static String getFileName(String template, String className, String packageName, String moduleName) {
        String packagePath = "main" + File.separator + "java" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator + moduleName + File.separator;
        }

        if (template.contains(FILE_NAME_MODEL)) {
            return packagePath + "model" + File.separator + className + ".java";
        }

        if (template.contains(FILE_NAME_MAPPER)) {
            return packagePath + "mapper" + File.separator + className + "Mapper.java";
        }

        if (template.contains(FILE_NAME_SERVICE)) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }

        if (template.contains(FILE_NAME_SERVICEIMPL)) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if (template.contains(FILE_NAME_CONTROLLER)) {
            return packagePath + "controller" + File.separator + className + "Controller.java";
        }

        if (template.contains(FILE_NAME_MAPPERXML)) {
            return "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + className + "Mapper.xml";
        }

        if (template.contains(FILE_NAME_PAGE)) {
            return "main" + File.separator + "view" + File.separator + "pages" +
                    File.separator + moduleName + File.separator + "index.html";
        }

        return null;
    }

    /**
     * 获取配置信息
     */
    public static Configuration getConfig() {
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            throw new RuntimeException("获取配置文件失败，", e);
        }
    }
}
