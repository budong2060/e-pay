package util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by admin on 2017/7/18.
 */
public abstract class BeanUtil extends BeanUtils {

    /**
     *
     * @param source
     * @param target
     * @param ignoreNullProperties 是否忽略null值
     * @throws BeansException
     */
    public static void copyProperties(Object source, Object target, boolean ignoreNullProperties) throws BeansException {
        copyProperties(source, target, null, ignoreNullProperties, null);
    }

    /**
     *
     * @param source
     * @param target
     * @param ignoreNullProperties
     * @param ignoreProperties
     * @throws BeansException
     */
    public static void copyProperties(Object source, Object target, boolean ignoreNullProperties, String... ignoreProperties) throws BeansException {
        copyProperties(source, target, null, ignoreNullProperties, ignoreProperties);
    }

    /**
     *
     * @param source
     * @param target
     * @param editable
     * @param ignoreNullProperties
     * @param ignoreProperties
     * @throws BeansException
     */
    private static void copyProperties(Object source, Object target, Class<?> editable, boolean ignoreNullProperties, String... ignoreProperties)
            throws BeansException {

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
                        "] not assignable to Editable class [" + editable.getName() + "]");
            }
            actualEditable = editable;
        }
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null &&
                            ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source);
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            //判断source值是否为null
                            if (null == value && ignoreNullProperties) {
                                continue;
                            }
                            writeMethod.invoke(target, value);
                        }
                        catch (Throwable ex) {
                            throw new FatalBeanException(
                                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }
    }

    /**
     * 将bean转换为map
     * @param t
     * @param <T>
     * @throws BeansException
     */
    public static <T> Map<String, String> bean2Map(T t, String... ignoreProperties) throws BeansException {
//        BeanInfo beanInfo = Introspector.getBeanInfo(t.getClass());
        PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(t.getClass());
        Map<String, String> map = new TreeMap<>();
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);
        for (PropertyDescriptor souce : propertyDescriptors) {
            if (ignoreList == null || !ignoreList.contains(souce.getName())) {
                Method readMethod = souce.getReadMethod();
                try {
                    if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                        readMethod.setAccessible(true);
                    }
                    Object value = readMethod.invoke(t);
                    //TODO
                    map.put(souce.getName(), String.valueOf(value));
                } catch (Throwable ex) {
                    throw new FatalBeanException(
                            "Could not copy property '" + souce.getName() + "' from source to target", ex);
                }
            }
        }
        return map;
    }

}
