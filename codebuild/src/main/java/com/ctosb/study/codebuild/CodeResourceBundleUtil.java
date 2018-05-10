package com.ctosb.study.codebuild;

import java.util.ResourceBundle;


public class CodeResourceBundleUtil {

    private static ResourceBundle resourceBundleJdbc = ResourceBundle.getBundle("config/template_jdbc");
    private static ResourceBundle resourceBundleConfig = ResourceBundle.getBundle("config/template_config");

    /**
     * 数据库驱动
     *
     * @return
     * @author alan
     * @date 2014-8-13 下午2:36:05
     */
    public static String getDriver() {
        return resourceBundleJdbc.getString("jdbc.driver");
    }

    /**
     * 数据库url
     *
     * @return
     * @author alan
     * @date 2014-8-13 下午2:35:54
     */
    public static String getUrl() {
        return resourceBundleJdbc.getString("jdbc.url");
    }

    /**
     * 数据库密码
     *
     * @return
     * @author alan
     * @date 2014-8-13 下午2:35:42
     */
    public static String getPassword() {
        return resourceBundleJdbc.getString("jdbc.password");
    }

    /**
     * 数据库用户名
     *
     * @return
     * @author alan
     * @date 2014-8-13 下午2:35:29
     */
    public static String getUseName() {
        return resourceBundleJdbc.getString("jdbc.username");
    }

    /**
     * @return
     * @author alan
     * @date 2014-8-13 下午2:32:55
     */
    public static String getJavaSourceFolder() {
        return resourceBundleConfig.getString("java.source.folder").replace('.', '\\');
    }

    /**
     * web资源的源文件
     *
     * @return
     * @author alan
     * @date 2014-8-13 下午2:31:45
     */
    public static String getWebSourceFolder() {
        return resourceBundleConfig.getString("web.source.folder").replace('.', '\\');
    }

    /**
     * java文件的包名
     *
     * @return
     * @author alan
     * @date 2014-8-13 下午2:31:08
     */
    public static String getBuildPackage() {
        return resourceBundleConfig.getString("build.package").replace('.', '\\');
    }

    /**
     * 模版路径
     *
     * @return
     * @author alan
     * @date 2014-8-13 下午2:30:57
     */
    public static String getTemplatePath() {
        return resourceBundleConfig.getString("template.path").replace('.', '\\');
    }

    /**
     * 过滤的字段（公用的数据库字段）
     *
     * @return
     * @author alan
     * @date 2014-8-13 下午2:30:10
     */
    public static String getFilterFields() {
        return resourceBundleConfig.getString("filter.fields");
    }
}
