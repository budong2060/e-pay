package util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * ID生成
 */
public class IdUtils {

	/**
	 * 根据时间戳16位交易流水号。
	 * 日期(yyyyMMddhhmmss) + 5位随机数
	 *
	 * @return 交易流水号
	 */
	public static String genId() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		String seqNo = format.format(Calendar.getInstance().getTime());
		DateFormatUtils.format(new Date(), "yyMMddhhmmss");
		Random random = new Random();
		return seqNo + random.nextInt(10000);
	}


}
