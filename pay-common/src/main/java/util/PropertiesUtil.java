package util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by heyinbo on 2017/7/14.
 * properties工具类
 */
public class PropertiesUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);

    private static Properties properties;

    static {
        try {
            String profile = System.getenv("spring.active.profile");
            String fileName = "pay-dev.properties";
            if (StringUtils.isNotEmpty(profile)) {
                fileName = "pay-" + profile + ".properties";
            }
            InputStream stream = PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);
            properties = new Properties();
            if (stream != null) {
                properties.load(stream);
            }
        } catch (Exception ex) {
            LOGGER.warn("Read pay.properties error", ex);
        }
    }

    public static Properties getProperties(){
        return properties;
    }

    public static String getProperty(String key){
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue){
        return properties.getProperty(key, defaultValue);
    }

    public static void setProperty(String key, String value){
        properties.setProperty(key, value);
    }

    public static int getPropertyForInteger(String key) {
        String value = getProperty(key);
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            throw new IllegalArgumentException("转换 \"" + value + "\" 为 int 过程发生错误，引发的 properties 属性为 " + key);
        }
    }

    public static int getPropertyForInteger(String key, String defaultValue) {
        String value = getProperty(key, defaultValue);
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            throw new IllegalArgumentException("转换 \"" + value + "\" 为 int 过程发生错误，引发的 properties 属性为 " + key);
        }
    }
}
