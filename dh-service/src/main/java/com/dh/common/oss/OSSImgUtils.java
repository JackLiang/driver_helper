package com.dh.common.oss;

/**
 *  
 */

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dh.common.utils.ImageUtils;
import com.dh.common.utils.ParameterUtils;
import com.dh.common.utils.http.FileUploader;
import com.dh.common.utils.oss.OSSParam;
import com.dh.common.utils.oss.OSSUtils;
import com.dh.common.utils.oss.UploadResultInfo;

/**
 * 云资源服务器图片管理类
 * 
 * @author ying_liu 2015年5月6日
 *
 */
public class OSSImgUtils {
	// private static Logger LOG = LoggerFactory.getLogger(OSSImgUtils.class);

	/**
	 * 公众号推送图片路径
	 */
	public static String IMAGES_FOLDER = "images/dh/";

	/**
	 * logo头像大小不能超过1M
	 */
	private static long MAX_LOGO_SIZE = 1 * 1024 * 1024;

	private static String OSS_ACCESS_ID = "LA5LEsX6WkG0vxkZ";

	private static String OSS_ACCESS_KEY = "bmqOCKDDDiw4EjF99PY1qzFUSGam3h";

	private static String OSS_URL_BASE = "http://driverhelper.oss-cn-shenzhen.aliyuncs.com/";

	private static String OSS_BUCKET_NAME = "driverhelper";
	
	private static String OSS_END_POINT = "http://oss-cn-shenzhen.aliyuncs.com/";



	/**
	 * 将公众号推送图片存放到资源服务器
	 * 
	 * @param localImgPath
	 *            本地图片的完整路径，包括图片名
	 * @param destImgName
	 *            上传到服务器的图片名，不包含图片路径，不传则默认为本地图片名
	 * @param appId
	 *            公众号ID
	 * @return
	 * @throws IOException
	 */
	public static UploadResultInfo uploadImage(String localImgPath, String destImgName, String imgType) throws IOException {
		// 校验参数
		ParameterUtils.assertNotBlank(localImgPath, "上传图片路径不能为空");
		// ParameterUtils.assertNotBlank(appId, "公众号ID不能为空");

		OSSParam param = new OSSParam(OSS_ACCESS_ID, OSS_ACCESS_KEY, OSS_URL_BASE, OSS_BUCKET_NAME, OSS_END_POINT);

		String path = IMAGES_FOLDER;
		path += imgType + "/";
		String originName = String.valueOf(System.currentTimeMillis());
		String suffix = StringUtils.substringAfter(destImgName, ".");
		return OSSUtils.upload(path, localImgPath, originName + "." + suffix, param);
	}


	/**
	 * @param markFilename 水印图片地址
	 * @param localImgPath 本地晒单图片地址
	 * @param destImgName 保存在oss的图片名称
	 * @param imgDir 保存在oss的目录
	 * @return
	 * @throws IOException
	 */
	public static UploadResultInfo uploadPressImage(String markFilename, String localImgPath, String destImgName, String imgDir) throws IOException {
		// 校验参数
		ParameterUtils.assertNotBlank(localImgPath, "上传图片路径不能为空");
		
		ImageUtils.pressImage(markFilename, localImgPath, localImgPath, 0.5f);//加图片水印
		OSSParam param = new OSSParam(OSS_ACCESS_ID, OSS_ACCESS_KEY, OSS_URL_BASE, OSS_BUCKET_NAME, OSS_END_POINT);

		String path = IMAGES_FOLDER;
		path += imgDir + "/";
		String originName = String.valueOf(System.currentTimeMillis());
		String suffix = StringUtils.substringAfter(destImgName, ".");
		return OSSUtils.upload(path, localImgPath, originName + "." + suffix, param);
	}

	/**
	 * 将公众号中的图片从资源服务器中删除
	 * 
	 * @param imageSrc
	 *            资源服务器中图片的完整路径，包括图片名
	 */
	public static void deleteImage(String imageSrc) throws IOException {
		// 校验参数
		ParameterUtils.assertNotBlank(imageSrc, "删除的图片名不能为空");

		OSSParam param = new OSSParam(OSS_ACCESS_ID, OSS_ACCESS_KEY, OSS_URL_BASE, OSS_BUCKET_NAME);

		OSSUtils.deleteFile(imageSrc, param);
	}

	/**
	 * 列出上传图片
	 * 
	 * @param appId
	 * @return
	 * @throws IOException
	 */
	public static List<String> listImage(String appId, String imgType) throws IOException {
		// 校验参数
		ParameterUtils.assertNotBlank(appId, "公众号ID不能为空");
		String folderPath = IMAGES_FOLDER.replace("${APPID}", appId);
		folderPath += imgType + "/";

		OSSParam param = new OSSParam(OSS_ACCESS_ID, OSS_ACCESS_KEY, OSS_URL_BASE, OSS_BUCKET_NAME);

		return OSSUtils.listFile(folderPath, param);
	}

	/**
	 * 上传LOGO到微信服务器
	 * 
	 * @param localImgPath
	 *            本地图片路径
	 * @param reqUrl
	 *            调用微信请求URL
	 * @return
	 * @throws Exception
	 */
	public static String getWxCardLogo(String localImgPath, String reqUrl) throws Exception {
		try {
			File logo = new File(localImgPath);
			if (logo.exists() && logo.length() < MAX_LOGO_SIZE) {
				FileUploader fileUpload = new FileUploader(logo, logo.length());
				HttpURLConnection connection = (HttpURLConnection) new URL(reqUrl).openConnection();
				String result = fileUpload.send(connection);

				JSONObject jsonObject = JSON.parseObject(result);
				String resultUrl = jsonObject.getString("url");
				if (resultUrl == null) {
					throw new Exception("将商户图标上传微信服务器失败,返回码:" + jsonObject.getString("errcode") + ",错误信息：" + jsonObject.getString("errmsg"));
				}

				return resultUrl;
			} else {
				throw new Exception("将商户图标上传微信服务器失败,图片不存在或者大小超过1M，logoPath=" + localImgPath);
			}
		} catch (Exception e) {
			throw new Exception("上传临时文件到微信服务器失败 ", e);
		}
	}

	public static void main(String[] args) {

		// String url = "http://localhost:8080/bao-web/common//uploadPicUrl.do";
		// File file = new
		// File("C:\\Users\\jiajie_liang\\Desktop\\upload_source15_gd.jpg");
		// Map<String, Object> param = new HashMap<String, Object>();
		// param.put("file", file);
		// String resp = HttpUtils.doHttpsPost(url, null, param, "utf-8");
		// System.out.println(resp);

		String localImgPath = "C:\\Users\\jiajie_liang\\Desktop\\123.jpg";
//		String pressImg = "C:\\Users\\jiajie_liang\\Desktop\\mark.png";

		String deImgPath = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + UUID.randomUUID().toString().substring(0, 8) + "."
				+ "jpg";
		
//		String time = DateUtils.dateToString(new Date(), DateUtils.DATA_DATE_TIME_FORMAT);
//		ImageUtils.savePressTextImg("连环夺宝["+time+"]", localImgPath, localImgPath, "微软雅黑", 0, Color.WHITE, 16, 230, 10, 1f);
		
//		ImageUtils.pressImage(pressImg, localImgPath, localImgPath, 0.5f);
//		ImageUtils.scale(localImgPath, localImgPath, 2, false);
			try {
				UploadResultInfo info =	OSSImgUtils.uploadImage(localImgPath, deImgPath, "logo");
				System.out.println(JSON.toJSONString(info));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
