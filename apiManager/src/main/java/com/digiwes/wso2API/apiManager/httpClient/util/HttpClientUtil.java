/**   
 * @title: HttpClientUtil.java 
 * @package com.boboqi.pay.alipay.util.httpclient 
 * @description: TODO(用一句话描述该文件做什么) 
 * @author songhn
 * @company HongRi Software Co.,Ltd.
 * @date 2015年8月26日 下午6:49:54 
 * @version v1.0
 */
package com.digiwes.wso2API.apiManager.httpClient.util;

import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;

/**
 * @className: HttpClientUtil
 * @description: HttpClient tools ClASS
 * @author zhaoyp
 * @date 2015-9-9 6:49:54PM
 */
public class HttpClientUtil {

	/**
	 * @title: generatNameValuePair 
	 * @description: MAP type array is converted into NameValuePair type
	 * @author ZHAOYP
	 * @param properties
	 * @return
	 * @date 2015年8月26日 下午6:51:28
	 */
	public static NameValuePair[] generatNameValuePair(
			Map<String, String> properties) {
		NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
		int i = 0;
		for (Map.Entry<String, String> entry : properties.entrySet()) {
			nameValuePair[i++] = new NameValuePair(entry.getKey(),
					entry.getValue());
		}
		return nameValuePair;
	}

	/**
	 * @title: toString 
	 * @description: NameValuePairs array into a string
	 * @author songhn 
	 * @param nameValues
	 * @return
	 * @date 2015-8-26- 6:51:35PM
	 */
	protected String toString(NameValuePair[] nameValues) {
		if (nameValues == null || nameValues.length == 0) {
			return "null";
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < nameValues.length; i++) {
			NameValuePair nameValue = nameValues[i];
			if (i == 0) {
				buffer.append(nameValue.getName() + "=" + nameValue.getValue());
			} else {
				buffer.append("&" + nameValue.getName() + "="
						+ nameValue.getValue());
			}
		}
		return buffer.toString();
	}

}
