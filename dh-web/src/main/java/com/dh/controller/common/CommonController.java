/**
 *  
 */
package com.dh.controller.common;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.dh.common.oss.OSSImgUtils;
import com.dh.common.utils.SpringContextUtil;
import com.dh.common.utils.oss.UploadResultInfo;
import com.dh.interceptor.annotation.CleanUserAgent;
import com.dh.vo.RespVO;

/**
 * @author jiajie_liang 2015年11月21日 上午9:35:04
 *
 */
@Controller
@RequestMapping("/common")
public class CommonController {

	private static Logger LOG = Logger.getLogger(CommonController.class);

	// @Resource(name = "systemPropertyService")
	// private SystemPropertyService systemPropertyService;

	/**
	 * @param user_id
	 *            用户ID
	 * @param type
	 *            上传类型1是水印图片
	 * @param file
	 * @return
	 */
	@CleanUserAgent
	@RequestMapping("/uploadPicUrl.do")
	@ResponseBody
	public Object uploadPicUrl(String user_id,
			@RequestParam(value = "type", required = false, defaultValue = "0") int type, MultipartFile file) {
		InputStream in = null;
		OutputStream out = null;

		try {
			// String imgType =
			// StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
			String imgType = "jpg";// 写死jpg
			String realPath = SpringContextUtil.getProjectPath();
			String tempname = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())
					+ UUID.randomUUID().toString().substring(0, 8) + "." + imgType;
			String filename = realPath + File.separator + tempname;
			new File(filename).createNewFile();
			file.transferTo(new File(filename));

			LOG.info(user_id + "保存本地成功，目录=[" + realPath + "]");
			UploadResultInfo info = null;
			if (type == 1) {
				String markFilename = realPath + File.separator + "images" + File.separator + "mark" + File.separator
						+ "mark.png";// 水印图片的地址
				info = OSSImgUtils.uploadPressImage(markFilename, filename, tempname, user_id);// 水印图片上传
			} else {
				info = OSSImgUtils.uploadImage(filename, tempname, user_id);// 其他图片上传
			}
			LOG.info(user_id + "上传阿里云成功...文件名=[" + tempname + "]");
			/* 删除临时文件 */
			File tempFile = new File(filename);
			tempFile.delete();
			LOG.info(user_id + "删除本地临时文件成功...文件名=[" + tempname + "]");

			String url = info.getFileUrl();
			RespVO resp = new RespVO(0, URLEncoder.encode("上传成功。", "UTF-8"), url);
			return JSON.toJSONString(resp);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(user_id + "上传图片出现异常...", e);
			RespVO resp = null;
			try {
				resp = new RespVO(-1, URLEncoder.encode("上传失败。", "UTF-8"), "");
			} catch (Exception e1) {
				LOG.error(user_id + "上传图片出现异常... ", e);
			}
			return JSON.toJSONString(resp);
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}

	// /**
	// * 是否在审核(IOS调用)
	// *
	// * @return 0非审核状态1审核状态
	// */
	// @CleanUserAgent
	// @RequestMapping("/getApproval.do")
	// @ResponseBody
	// public Object getApproval() {
	// int result = 0;
	// if ("true".equals(systemPropertyService.get("is_approval").getValue())) {
	// result = 1;
	// }
	// RespVO resp = new RespVO.Builder(0).object(result).build();
	// return JSON.toJSONString(resp);
	//
	// }
	//
	// /**
	// * APP分享页面
	// *
	// */
	// @CleanUserAgent
	// @RequestMapping("/shareApp.do")
	// public Object shareApp(HttpServletRequest request) {
	// String url = "";
	// try {
	// String userAgent = request.getHeader("User-Agent");
	// url = systemPropertyService.get("android_app_share_url").getValue();
	//
	// if (userAgent.contains("iPhone") || userAgent.contains("Mac OS")) {
	// url = systemPropertyService.get("ios_app_share_url").getValue();
	// }
	// } catch (Exception e) {
	// LOG.error("APP分享页面异常", e);
	// }
	//
	// ModelMap model = new ModelMap();
	// model.put("app_url", url);
	// return new ModelAndView("/bao_share", model);
	// }
	//
	// /**
	// * 检查更新
	// *
	// */
	// @CleanUserAgent
	// @RequestMapping("/checkVersion.do")
	// @ResponseBody
	// public String checkVersion(HttpServletRequest request) {
	// String userAgent = request.getHeader("User-Agent");
	// com.youpo.bao.vo.resp.RespVO resp = new com.youpo.bao.vo.resp.RespVO();
	// String version =
	// systemPropertyService.get("android_app_version").getValue();
	// String url =
	// systemPropertyService.get("android_app_share_url").getValue();
	//
	// if(userAgent.toUpperCase().contains("IOS-YOUPO-BAO") ||
	// userAgent.contains("iPhone") || userAgent.contains("Mac OS")){
	// version = systemPropertyService.get("ios_app_version").getValue();
	// url = systemPropertyService.get("ios_app_share_url").getValue();
	// }
	//
	// resp.setCode(0);
	// resp.setMsg("success");
	// Map<String, Object> obj = new HashMap<>();
	// obj.put("version", version);
	// obj.put("download_url", url);
	// resp.setObject(obj);
	// return JSON.toJSONString(resp);
	// }
	//
	// @CleanUserAgent
	// @RequestMapping("/urlTest.do")
	// public ModelAndView checkVersion(String uuid) {
	// ModelMap model = new ModelMap();
	// model.put("player_id", uuid);
	// return new ModelAndView("bao_test", model);
	// }
}
