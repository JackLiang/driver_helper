/**
 *  
 */
package com.dh.common.utils.oss;

/**
 * 阿里OSS服务的请求参数，具体参数存在system_property表，key见下述
 * 
 * @author haibin_chen 2015年7月23日
 *
 */
public class OSSParam {
	/**
	 * 对应表key：oss_access_id
	 */
	private String accessId;

	/**
	 * 对应表key：oss_access_key
	 */
	private String accessKey;

	/**
	 * 对应表key：oss_url_base
	 */
	private String urlBase;

	/**
	 * 对应key：oss_bucket_name
	 */
	private String bucketName;
	
	/**
	 *  对应key：oss_end_point
	 */
	private String endPoint;

	public OSSParam(String accessId, String accessKey, String urlBase, String bucketName) {
		this.accessId = accessId;
		this.accessKey = accessKey;
		this.urlBase = urlBase;
		this.bucketName = bucketName;
	}
	
	public OSSParam(String accessId, String accessKey, String urlBase, String bucketName,String endPoint) {
		this.accessId = accessId;
		this.accessKey = accessKey;
		this.urlBase = urlBase;
		this.bucketName = bucketName;
		this.endPoint = endPoint;
	}

	public String getAccessId() {
		return accessId;
	}

	public void setAccessId(String accessId) {
		this.accessId = accessId;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getUrlBase() {
		return urlBase;
	}

	public void setUrlBase(String urlBase) {
		this.urlBase = urlBase;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}
	
}
