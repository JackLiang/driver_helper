/**
 *  
 */
package com.dh.common.utils.oss;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.dh.common.utils.ParameterUtils;

/**
 * 阿里云资源服务器上传工具类
 * 
 * @author ying_liu 2015年5月13日
 *
 */
public class OSSUtils {

	/**
	 * 
	 * @param path
	 *            上传到资源服务器中文件目录，不包括文件名
	 * @param localFileName
	 *            本地文件的完整路径，包括文件名
	 * @param destFileName
	 *            上传到资源服务器的文件名，单纯的文件名，不包含文件目录，不传则默认为使用上传的本地文件名
	 * @return
	 * @throws IOException
	 */
	public static UploadResultInfo upload(String path, String localFileName, String destFileName, OSSParam ossParam) throws IOException {
		// 校验参数
		ParameterUtils.assertNotBlank(path, "上传到资源服务器中文件目录不能为空");
		ParameterUtils.assertNotBlank(localFileName, "本地文件的完整路径不能为空");
		
		File file = new File(localFileName);
		InputStream input = null;
		try {
			OSSClient client = new OSSClient(ossParam.getEndPoint(), ossParam.getAccessId(), ossParam.getAccessKey());

			ObjectMetadata objectMeta = new ObjectMetadata();
			objectMeta.setContentLength(file.length());

			UploadResultInfo result = new UploadResultInfo();

			result.setSuccessfull(false);
			result.setFileName(file.getName());
			if (StringUtils.isNotBlank(destFileName)) {
				result.setFileName(destFileName.replaceAll("\\s", ""));
			}
			String key = path + result.getFileName();

			result.setFileUrl(ossParam.getUrlBase() + key);

			input = new FileInputStream(file);
			client.putObject(ossParam.getBucketName(), key, input, objectMeta);
			result.setSuccessfull(true);
			return result;
		} catch (Exception e) {
			throw new IOException("上传文件到资源服务器出错，localFileName=" + localFileName, e);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	/**
	 * 将文件从资源服务器中删除
	 * 
	 * @param fileName
	 *            资源服务器中文件的完整路径，包括文件名
	 */
	public static void deleteFile(String fileName, OSSParam ossParam) throws IOException {
		// 校验参数
		ParameterUtils.assertNotBlank(fileName, "删除的文件名不能为空");
		// 初始化OSSClient
		OSSClient client = new OSSClient(ossParam.getUrlBase(), ossParam.getAccessId(), ossParam.getAccessKey());

		try {
			client.deleteObject(ossParam.getBucketName(), fileName.replace(ossParam.getUrlBase(), ""));
		} catch (OSSException | ClientException e) {
			throw new IOException("从资源服务器中删除文件出错，fileName=" + fileName, e);
		}
	}

	/**
	 * 获取资源服务器文件夹中的所有完整路径文件名，不包含子目录
	 * 
	 * @return 所有文件路径列表
	 */
	public static List<String> listFile(String folder, OSSParam ossParam) throws IOException {
		// 校验参数
		ParameterUtils.assertNotBlank(folder, "文件夹名不能为空");

		List<String> files = new ArrayList<String>();
		// 初始化OSSClient
		OSSClient client = new OSSClient(ossParam.getUrlBase(), ossParam.getAccessId(), ossParam.getAccessKey());
		try {
			// 构造ListObjectsRequest请求
			ListObjectsRequest listObjectsRequest = new ListObjectsRequest(ossParam.getBucketName());

			// 递归列出目录下的所有文件
			listObjectsRequest.setDelimiter("/");
			listObjectsRequest.setPrefix(folder);

			// 获取指定bucket下的所有Object信息
			ObjectListing listing = client.listObjects(listObjectsRequest);
			for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
				files.add(ossParam.getUrlBase() + objectSummary.getKey());
			}
		} catch (Exception e) {
			throw new IOException("获取资源服务器中的文件列表出错，folder=" + folder, e);
		}

		return files;
	}
}
