package util;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by admin on 2017/9/28.
 */
public class CollectionUtil<E> extends CollectionUtils {

    /**
     * 求两集合的交集
     * 同时清除原集合中的交集部分数据
     * @param a
     * @param b
     * @param <E>
     * @return
     */
    public static <E> List<E> intersection(List<E> a, List<E> b) {
        List<E> intersection = new ArrayList<>();
        for (Iterator<E> it = b.iterator(); it.hasNext();) {
            E e = it.next();
            boolean removed = a.remove(e);
            if (removed) {
                intersection.add(e);
                it.remove();
            }
        }
        return intersection;
    }

}
