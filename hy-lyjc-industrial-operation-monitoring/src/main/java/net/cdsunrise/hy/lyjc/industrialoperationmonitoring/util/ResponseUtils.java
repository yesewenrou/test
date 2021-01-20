/**
 * Project Name file-server
 * File Name ResponseUtils.java
 * Package Name net.cdsunrise.alps.file.utils
 * Date 2018年8月1日上午11:29:43
 * Copyright (c) 2018, izbk@163.com All Rights Reserved.
 *
*/

package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * 下载文件工具类
 * @author zbk
 * @date 2020/4/29 10:18
 */
public class ResponseUtils {
	/**
	 * 构建下载类
	 * @param file
	 * @return
	 */
	public static ResponseEntity<byte[]> buildResponseEntity(File file) {
		byte[] body = null;
		// 获取文件
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			body = new byte[is.available()];
			is.read(body);
			HttpHeaders headers = new HttpHeaders();
			// 设置文件类型
			headers.add(HttpHeaders.CONTENT_DISPOSITION,
					"attchement;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
			headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream; charset=UTF-8");
			headers.setContentLength(body.length);
			// 设置Http状态码
			HttpStatus statusCode = HttpStatus.OK;
			// 封装返回数据
			ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, statusCode);
			return entity;
		} catch (Exception e) {
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
			}
		}
		return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
	}
	
}
