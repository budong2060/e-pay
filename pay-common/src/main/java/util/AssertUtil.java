package util;

import com.pay.enums.PayResultEnum;
import com.pay.exception.PayException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Created by admin on 2017/7/20.
 */
public class AssertUtil extends Assert {

    /**
     * 参数校验
     * @param text
     * @param message
     */
    public static void hasText(String text, String message) {
        if (!StringUtils.hasText(text)) {
            throw new PayException(PayResultEnum.ILLEGAL_ARGUMENTS, message);
        }
    }

}
