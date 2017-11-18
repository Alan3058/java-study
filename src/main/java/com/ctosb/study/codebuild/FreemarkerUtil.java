package com.ctosb.study.codebuild;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Map;

public class FreemarkerUtil {

    private static Configuration config;

    public static String compile(String templatePath, String templateName, Map<String, Object> map, String outPath) {
        PrintWriter out = null;
        try {
            init(templatePath);
            Template temp = config.getTemplate(templateName);

            File file = new File(outPath);
            File path = file.getParentFile();
            if (path != null && !path.exists()) {
                path.mkdirs();
            }
            // 定义模板输出
            out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            // StringWriter out = new StringWriter();
            // 模板解释
            temp.process(map, out);
            return file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return "";
    }

    public static String compile(String templatePath, String templateName, Map<String, Object> map, String outPath, boolean isChange) {
        String fileName = compile(templatePath, templateName, map, outPath);
        File file = new File(fileName);
        //替换字符（防止freemarker和el重复）
        if (isChange) {
            String data;
            try {
                data = FileUtils.readFileToString(file).replace("<<change>>", "");
                FileUtils.writeStringToFile(file, data);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return fileName;
    }


    private static void init(String templatePath) throws IOException {
        // 加载模板
        // String dir = this.getClass().getResource("").getPath();
        config = new Configuration();
        // config.setClassForTemplateLoading(FreemarkerUtil.class,
        // templatePath);
        config.setDirectoryForTemplateLoading(new File(templatePath));
        // 设置对象包装器
        config.setObjectWrapper(new DefaultObjectWrapper());
        // 设置异常处理器
        config.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
    }
}
