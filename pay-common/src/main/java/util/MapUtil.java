package util;

import java.util.Map;

/**
 * Created by admin on 2017/7/14.
 */
public class MapUtil {

    private MapUtil() {}

    /**
     * 将map转换为url参数
     * @param params
     * @return
     */
    public static String map2UrlParams(Map<String, String> params) {
        if (null == params) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry entry : params.entrySet()) {
            sb.append(entry.getKey());
            sb.append('=');
            sb.append(entry.getValue());
            sb.append('&');
        }

        if (sb.length() > 0) {
            return sb.substring(0, sb.length() - 1);
        }
        return  null;
    }

    /**
     * map转换为xml
     * @param params
     * @return
     */
    public static String map2Xml(Map<String, String> params) {
        if (null == params) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (Map.Entry entry : params.entrySet()) {
            sb.append("<").append(entry.getKey()).append(">")
                    .append(entry.getValue())
                    .append("</").append(entry.getKey()).append(">");
        }
        sb.append("</xml>");

        return sb.toString();
    }

}
