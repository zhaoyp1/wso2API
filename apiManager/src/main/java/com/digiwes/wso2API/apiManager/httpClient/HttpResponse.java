package com.digiwes.wso2API.apiManager.httpClient;

import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.Header;


/**
 * @className: HttpResponse 
 * @description: Http Response Data
 * @author songhn
 * @company HongRi Software Co.,Ltd.
 * @date 2015年8月26日 下午6:09:03
 */
public class HttpResponse {

    /**
     * the header message of response body
     */
    private Header[] responseHeaders;

    /**
     * the result of string Type
     */
    private String   stringResult;

    /**
     * the result of byte Type
     */
    private byte[]   byteResult;

    public Header[] getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(Header[] responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public byte[] getByteResult() {
        if (byteResult != null) {
            return byteResult;
        }
        if (stringResult != null) {
            return stringResult.getBytes();
        }
        return null;
    }

    public void setByteResult(byte[] byteResult) {
        this.byteResult = byteResult;
    }

    public String getStringResult() throws UnsupportedEncodingException {
        if (stringResult != null) {
            return stringResult;
        }
        if (byteResult != null) {
            return new String(byteResult,"UTF-8");
        }
        return null;
    }

    public void setStringResult(String stringResult) {
        this.stringResult = stringResult;
    }

}
