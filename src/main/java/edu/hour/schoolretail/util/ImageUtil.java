package edu.hour.schoolretail.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;

/**
 * @author demoy
 * @version 1.0.0
 * @Description 根据不同的系统环境使用不同的路径拼接，并返回资源路径
 * @createTime 2023年03月16日 14:56
 */
public class ImageUtil {

	private static final String IMAGE_BASE_PATH;

	private static final String SEPARATOR = File.separator;

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); // 时间格式化的格式

	private static final Random r = new Random();

	// 初始化系统对应的图片存储路径
	static {
		String os = System.getProperty("os.name");
		if (os.toLowerCase().startsWith("win")) {
			IMAGE_BASE_PATH = "F:/dshop/images/";
		} else {
			IMAGE_BASE_PATH = "/home/demoy/dshop/images/";
		}

		IMAGE_BASE_PATH.replaceAll("/", Matcher.quoteReplacement(SEPARATOR));
	}

	/**
	 * 获取用户头像绝对路径路径
	 * @param extension
	 * @param filePathConstant
	 * @param filename
	 * @return
	 */
	public static String getUserImageAbsolutePath(String extension, String filePathConstant, String filename) {
		String directory = getSaveFileDirectory(filePathConstant);
		filename = filename + extension;
		String path = IMAGE_BASE_PATH + directory;
		while (checkFilePath(path + filename)) {
			filename = getRandomFilename();
		}
		return (path + filename).replaceAll("/", Matcher.quoteReplacement(SEPARATOR));
	}

	/**
	 * 获取图片文件的绝对路径
	 * @param filePathConstant
	 * @param otherInfo
	 * @return
	 */
	public static String getPictureAbsolutePath(String extension, String filePathConstant, String... otherInfo) {
		String directory = getSaveFileDirectory(filePathConstant, otherInfo);
		String filename = getRandomFilename() + extension;
		String path = IMAGE_BASE_PATH + directory;
		while (checkFilePath(path + filename)) {
			filename = getRandomFilename();
		}
		return (path + filename).replaceAll("/", Matcher.quoteReplacement(SEPARATOR));
	}

	/**
	 * 获取存储文件文件夹的路径
	 * @param filePathConstant 存储文件的类型，比如图片
	 * @param otherInfo 需要追加的信息，比如说时间、所属用户用户名
	 * @return
	 */
	private static String getSaveFileDirectory(String filePathConstant, String... otherInfo) {
		StringBuilder res = new StringBuilder(filePathConstant);
		for (String info : otherInfo) {
			res.append(info).append("/");
		}
		return res.toString();
	}

	/**
	 * 返回一个随机的文件名：时间+随机五位数
	 * @return
	 */
	private static String getRandomFilename() {
		String time = sdf.format(new Date());
		String random = String.valueOf(r.nextInt(90000) + 10000);
		return time + random;
	}

	/**
	 * 检测指定路径是否存在，如果存在而且是文件夹则直接返回，否则创建对应的文件夹
	 * @param path
	 */
	private static boolean checkFilePath(String path) {
		File file = new File(path);
		if (file.exists() && file.isDirectory()) {
			return true;
		}
		file.mkdirs();
		return false;
	}
}
