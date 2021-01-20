package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util;

import cn.hutool.extra.template.engine.freemarker.FreemarkerTemplate;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zbk
 * @date 2020-05-14 14:33:55
 */
public class WordGeneratorUtils {
    private static Configuration configuration = null;
    private static Map<String, Template> allTemplates = null;
    public static final String DATA_CENTER_REPORT = "dataCenterReport";

    static {
        configuration = new Configuration(Configuration.VERSION_2_3_28);
        configuration.setDefaultEncoding("utf-8");
        configuration.setClassForTemplateLoading(WordGeneratorUtils.class, "/freemarker/template");
        allTemplates = new HashMap();
        try {
            allTemplates.put(DATA_CENTER_REPORT, configuration.getTemplate(DATA_CENTER_REPORT + ".ftl"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private WordGeneratorUtils() {
        throw new AssertionError();
    }

    public static File createDoc(String templateName,String filePath,Map<String,Object> dataMap) {
        try {
            File f = new File(filePath);
            Template t = allTemplates.get(templateName);
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
            Writer w = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
            t.process(dataMap, w);
            w.close();
            return f;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("生成word文档失败");
        }
    }
}
