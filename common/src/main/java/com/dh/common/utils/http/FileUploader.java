package com.dh.common.utils.http;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUploader {
	private static final Logger LOG = LoggerFactory.getLogger(FileUploader.class);
	
	private StringBuffer footer;
	private StringBuffer header;
	private long contextLength;
	private File localFile;
	private long writeCount;

	public FileUploader(File localFile, long size) throws MalformedURLException {
		if (localFile == null) {
			throw new NullPointerException("Local file could not be null");
		}
		this.writeCount = size;
		this.localFile = localFile;

		this.footer = new StringBuffer();
		this.header = new StringBuffer();

		this.header.append("--");
		this.header.append("---------------------------13405632728728586031877927932");

		this.header.append("\r\n");
		this.header.append("Content-Disposition: form-data; name=\"file\"; filename=");
		this.header.append("\"");
		this.header.append(this.localFile.getName());
		this.header.append("\"");
		this.header.append("\r\n");
		this.header.append("Content-Type: text/plain");
		this.header.append("\r\n");
		this.header.append("\r\n");

		this.footer.append("\r\n");
		this.footer.append("--");
		this.footer.append("---------------------------13405632728728586031877927932");
		this.footer.append("--");
		this.footer.append("\r\n");

		this.contextLength = (this.header.length() + this.writeCount + this.footer.length());
	}

	public String send(HttpURLConnection connection) throws Exception {
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=---------------------------13405632728728586031877927932");
		connection.setRequestProperty("Content-Length", String.valueOf(this.contextLength));

		OutputStream outputStream = connection.getOutputStream();
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
		writeFileContent(bufferedOutputStream);
		return response(connection);
	}

	private String response(HttpURLConnection connection) throws Exception {
		InputStream inputStream = null;
		OutputStream outputStream = null;

		boolean errorOccurred = false;
		boolean doOutput = false;
		try {
			doOutput = connection.getDoOutput();
			if (doOutput) {
				outputStream = connection.getOutputStream();
			}
			if (connection.getResponseCode() != 200) {
				errorOccurred = true;
			}
			if (errorOccurred) {
				inputStream = connection.getErrorStream();
			} else {
				inputStream = connection.getInputStream();
			}
			ByteArrayOutputStream byteArrayOutputStream = null;
			byteArrayOutputStream = new ByteArrayOutputStream(1024);
			int readByte;
			while ((readByte = inputStream.read()) != -1) {
				byteArrayOutputStream.write(readByte);
			}
			String message = new String(byteArrayOutputStream.toByteArray());
			byteArrayOutputStream = null;

			return message;
		} catch (Exception e) {
			throw e;
		} finally {
			if ((doOutput) && (outputStream != null)) {
				IOUtils.closeQuietly(outputStream);
			}
			if (inputStream != null) {
				IOUtils.closeQuietly(inputStream);
			}
		}
	}

	private synchronized void writeFileContent(BufferedOutputStream out) {
		InputStream input = null;
		try {
			out.write(this.header.toString().getBytes());
			out.flush();
			input = new FileInputStream(this.localFile);
			// 注意buf大小，上传多少就声明多大
			byte[] buf = new byte[(int) this.localFile.length()];
			input.read(buf);

			out.write(buf);
			out.write(this.footer.toString().getBytes());

			out.flush();
		} catch (Exception e) {
			LOG.error("HTTP发送文件异常 ", e);
		} finally {
			if (input != null) {
				IOUtils.closeQuietly(input);
			}
		}
	}
}
