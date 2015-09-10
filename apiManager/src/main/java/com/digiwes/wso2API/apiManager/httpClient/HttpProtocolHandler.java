package com.digiwes.wso2API.apiManager.httpClient;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.digiwes.wso2API.apiManager.httpClient.util.HttpClientUtil;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.FilePartSource;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.IdleConnectionTimeoutThread;

/**
 * @className: HttpProtocolHandler 
 * @description: get remote Http Data
 * @author zhaoyp
 * @date 2015年9月8日 下午6:09:21
 */
public class HttpProtocolHandler {

    private static String      DEFAULT_CHARSET                     = "UTF-8";

    /** Connection timeout，be setted by bean factory   ，Default value is 8 seconds. */
    private int               defaultConnectionTimeout            = 8000;

    /** Response timeout, be setted by bean factory   ，default value is 30 seconds.*/
    private int               defaultSoTimeout                    = 30000;

    /** Idle connection timeout,be setted by bean factory Parameter，default value is 60 seconds*/
    private int               defaultIdleConnTimeout              = 60000;

    private int               defaultMaxConnPerHost               = 30;

    private int               defaultMaxTotalConn                 = 80;

    /** Default wait for HttpConnectionManager to return the connection timeout (only when the maximum number of connections is reached)：1seconds*/
    private static final long defaultHttpConnectionManagerTimeout = 3 * 1000;

    /**
     * HTTP connection manager, the connection manager must be thread safe.
     */
    private HttpConnectionManager      connectionManager;

    private static HttpProtocolHandler httpProtocolHandler   = new HttpProtocolHandler();

    private HttpClient  httpclient = null;

    /**
     * Factory method
     * 
     * @return
     */
    public static HttpProtocolHandler getInstance() {
        return httpProtocolHandler;
    }

    /**
     *
     */
    private HttpProtocolHandler() {
        // Create a thread safe HTTP connection pool
        connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(defaultMaxConnPerHost);  //Set the maximum number of active links in the thread pool
        connectionManager.getParams().setMaxTotalConnections(defaultMaxTotalConn);           //Set total number of connections
        IdleConnectionTimeoutThread ict = new IdleConnectionTimeoutThread();
        ict.addConnectionManager(connectionManager);
        ict.setConnectionTimeout(defaultIdleConnTimeout);
        ict.start();    //Handle timeout link, if the connection is more than 60 seconds, disconnect the connection
        httpclient = new HttpClient(connectionManager);
    }

    private HttpClient getHttpClient(){
        // Set connection timeout
        httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(defaultConnectionTimeout);

        // Set response timeout
        httpclient.getHttpConnectionManager().getParams().setSoTimeout(defaultSoTimeout);

        // Set the time waiting for ConnectionManager to release connection
        httpclient.getParams().setConnectionManagerTimeout(defaultHttpConnectionManagerTimeout);

        return httpclient;
    }

    /**
     * execut Http Request
     * 
     * @param method QuestMethod
     * @param header header
     * @param resultType resultType String byte
     * @return
     * @throws HttpException, IOException 
     */
    private HttpResponse execute(HttpMethod method,Map<String,Object> header ,String resultType){
        HttpClient hClient = getHttpClient();
        // set RequestHeader
        if(null!=header){
        	Set<String> mapKeys = header.keySet();
        	for(Iterator<String> it=mapKeys.iterator();it.hasNext();){
        		String key = it.next();
        		method.addRequestHeader(key,String.valueOf(header.get(key)));
        	}
        }
        HttpResponse response = new HttpResponse();
        try {
            httpclient.executeMethod(method);
            if (HttpResultType.BYTES.equals(resultType)) {
                response.setByteResult(method.getResponseBody());
            } else {
                response.setStringResult(method.getResponseBodyAsString());
            }
            response.setResponseHeaders(method.getResponseHeaders());
        } catch (UnknownHostException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        } catch (Exception ex) {
            return null;
        } finally {
            method.releaseConnection();
        }
        return response;
    }

    public HttpResponse executeGetMethod(String url ,String parameter,Map<String,Object> header ,String resultType){
        HttpMethod method = null;
        //get Model and not with upload File
        method = new GetMethod(url);
        method.getParams().setCredentialCharset(DEFAULT_CHARSET);
        // ParseNotifyConfig will ensure that the use of GET method, request must use QueryString
        method.setQueryString(parameter);
        return  this.execute(method,header,resultType);
    }

    public HttpResponse executePostMethod(String url,Map<String,String> parameter,Map<String,Object> header,String resultType){
        //post Model and not with upload File
        HttpMethod method = new PostMethod(url);
        if( null != parameter ) {
            NameValuePair[] nameValuePairs= HttpClientUtil.generatNameValuePair(parameter);
            ((PostMethod) method).addParameters(nameValuePairs);
        }
        return  this.execute(method,header,resultType);
    }

    public HttpResponse executePostMethodWithUpLoadFile(String url,Map<String,String> parameter,Map<String,Object> header,String resultType,String strParaFileName,String strFilePath  )throws IOException{
        //post  Model and  with upload File
        HttpMethod method = new PostMethod(url);
        List<Part> parts = new ArrayList<Part>();

        if( null != parameter ) {
            NameValuePair[] nameValuePairs= HttpClientUtil.generatNameValuePair(parameter);
            for (int i = 0; i < nameValuePairs.length; i++) {
                parts.add(new StringPart(nameValuePairs[i].getName(), nameValuePairs[i].getValue(), DEFAULT_CHARSET));
            }
        }
        //add File parameter，strParaFileName是parameterName，use local File
        parts.add(new FilePart(strParaFileName, new FilePartSource(new File(strFilePath))));
        // set requestBody
        ((PostMethod) method).setRequestEntity(new MultipartRequestEntity(parts.toArray(new Part[0]), new HttpMethodParams()));

        return  this.execute(method,header,resultType);
    }
}
