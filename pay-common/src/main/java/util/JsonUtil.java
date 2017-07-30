package util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/7/5.
 */
public class JsonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    private static ObjectMapper mapper;

    private JsonUtil() {
    }

    private static ObjectMapper getMapperInstance() {
        return getMapperInstance(false);
    }

    private static synchronized ObjectMapper getMapperInstance(boolean createNew) {
        if(createNew) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            return objectMapper;
        } else {
            if(mapper == null) {
                mapper = new ObjectMapper();
            }

            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //添加日期序列化格式
            mapper.getSerializationConfig().with(simpleDateFormat);
            mapper.getDeserializationConfig().with(simpleDateFormat);
            return mapper;
        }
    }

    /**
     *
     * @param obj
     * @return
     */
    public static String toString(Object obj) {
        StringWriter sw = null;
        try {
            ObjectMapper e = getMapperInstance();
            sw = new StringWriter();
            JsonGenerator gen = (new JsonFactory()).createJsonGenerator(sw);
            e.writeValue(gen, obj);
            return sw.toString();
        } catch (IOException e) {
            LOGGER.error("json转换失败，原因:{}", e);
            return null;
        } finally {
            try {
                sw.close();
            } catch (Exception e) {}
        }
    }

    public static <T> T toObject(String string, Class<?> cls) {
        try {
            if(StringUtils.isEmpty(string)) {
                return null;
            } else {
                ObjectMapper e = getMapperInstance();
                e.configure(SerializationFeature.INDENT_OUTPUT, Boolean.TRUE.booleanValue());
                JavaType type = e.getTypeFactory().constructType(cls);
                return e.readValue(string, type);
            }
        } catch (IOException e) {
            LOGGER.error("json转换失败，原因:{}", e);
            return null;
        }
    }

    public static <T> T toMap(String string) {
        try {
            if(StringUtils.isEmpty(string)) {
                return null;
            } else {
                ObjectMapper e = getMapperInstance();
                JavaType type = e.getTypeFactory().constructType(Map.class);
                return e.readValue(string, type);
            }
        } catch (IOException e) {
            LOGGER.error("json转换失败，原因:{}", e);
            return null;
        }
    }

    public static <T> T toList(String string, Class<?> cls) {
        try {
            ObjectMapper e = getMapperInstance();
            JavaType type = e.getTypeFactory().constructParametricType(List.class, new Class[]{ cls });
            return e.readValue(string, type);
        } catch (IOException e) {
            LOGGER.error("json转换失败，原因:{}", e);
            return null;
        }
    }

    public static JsonNode toJsonNode(String string) {
        try {
            ObjectMapper e = getMapperInstance();
            return e.readTree(string);
        } catch (IOException var2) {
            LOGGER.error(var2.getMessage(), var2);
            return null;
        }
    }
}
