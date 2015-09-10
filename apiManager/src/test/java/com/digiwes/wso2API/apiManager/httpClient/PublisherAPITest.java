package com.digiwes.wso2API.apiManager.httpClient;

import net.sf.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.ws.http.HTTPException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by zhaoyp on 2015/9/9.
 */
public class PublisherAPITest {



    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testPublisherLoginAPI()throws UnsupportedEncodingException{
       String password="";
       String username="";
       String actionName="";
       String url="http://localhost:9763/store/site/blocks/user/login/ajax/login.jag";
       String message="<html>\n" +
               "<head>\n" +
               "\n" +
               "</head>\n" +
               "\n" +
               "<body>\n" +
               "<h2>Error 500 : The page cannot be displayed.</h2>\n" +
               "\n" +
               "<br/>\n" +
               "\n" +
               "<p>\n" +
               "\n" +
               "\n" +
               "<h4>The server encountered an internal error or misconfiguration and was unable to complete your request.  </h4>\n" +
               "\n" +
               "\n" +
               "\n" +
               "</body>\n" +
               "</html>"     ;
       //case1. ip is wrong
       HttpProtocolHandler protocolHandler = HttpProtocolHandler.getInstance();
       Map<String,String> parameter =new HashMap<String,String>();
       parameter.put("username", "admin");
       parameter.put("password", "admin");
       parameter.put("action","login");
       HttpResponse responseMessage= protocolHandler.executePostMethod(url,parameter,null,null);
       assertEquals("ip is wrong", null, responseMessage);

       //case2.no parameter
       url="http://54.223.136.29:9763/publisher/site/blocks/user/login/ajax/login.jag";
       responseMessage= protocolHandler.executePostMethod(url,null,null,null);
       JSONObject jsonObject = JSONObject.fromObject(responseMessage.getStringResult());
       assertEquals("no parameter","true",String.valueOf(jsonObject.get("error")));
       assertEquals("no parameter", "null is not supported", String.valueOf(jsonObject.get("message")));

        //case3.actionName is wrong and the specify not exist in APIMANAGER
        parameter.put("action", "loginAPI");
        responseMessage= protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("actionName is wrong and the specify not exist in APIMANAGER", "true", String.valueOf(jsonObject.get("error")));
        assertEquals("actionName is wrong and the specify not exist in APIMANAGER", "loginAPI is not supported", String.valueOf(jsonObject.get("message")));


        //case3.actionName is wrong and the specify  exist in APIMANAGER
        parameter.put("action", "addAPI");
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("actionName is wrong and the specify  exist in APIMANAGER", "true", String.valueOf(jsonObject.get("error")));

        // 5.actionName is wrong and the specify  exist in APIMANAGER
        parameter.put("action", "logout");
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("actionName is wrong and the specify  exist in APIMANAGER", "false", String.valueOf(jsonObject.get("error")));

        //case4.user not exsit
        parameter.put("action","login");
        parameter.put("username", "aaa");
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("user not exsit","true",String.valueOf(jsonObject.get("error")));
        assertEquals("user not exsit", "Login failed.Please recheck the username and password and try again..", String.valueOf(jsonObject.get("message")));

        //case5.password is wrong
        parameter.put("username", "admin");
        parameter.put("password", "aaa");
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("password is wrong","true",String.valueOf(jsonObject.get("error")));
        assertEquals("password is wrong","Login failed.Please recheck the username and password and try again..",String.valueOf(jsonObject.get("message")));

        //case6.  no provided the required parameter
        Map<String,String> parameter1 =new HashMap<String,String>();
        parameter1.put("action","login");
        responseMessage = protocolHandler.executePostMethod(url, parameter1, null, null);
        assertEquals("no provided the required parameter", message, responseMessage.getStringResult().trim());

        // case7. api used Get
        responseMessage = protocolHandler.executeGetMethod(url, "action=login&username=admin&password=admin", null, null);
        jsonObject = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("api used GetMethod", "true", String.valueOf(jsonObject.get("error")));
        assertEquals("api used GetMethod", "login is not supported", String.valueOf(jsonObject.get("message")));
        //case 8
        parameter.put("username", "admin");
        parameter.put("password", "admin");
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("","false",String.valueOf(jsonObject.get("error")));


    }
    @Test
    public void testPublishAPI()throws UnsupportedEncodingException{
       String url="http://localhost:9763/publisher/site/blocks/item-add/ajax/add.jag";
        Map<String ,String> parameter=new HashMap<String, String>();
        parameter.put("action","addAPI");
        parameter.put("name", "PhoneVerification");
        parameter.put("context","/phoneverify");
        parameter.put("thumbUrl","");
        parameter.put("version","1.0.0");
        parameter.put("visibility","public");
        parameter.put("description","Verify a phone number");
        parameter.put("tags","phone,mobile,multimedia");
        parameter.put("endpointType","nonsecured");
        parameter.put("tiersCollection","Gold,Bronze");
        parameter.put("http_checked","http");
        parameter.put("https_checked","https");
        parameter.put("resourceCount","0");
        parameter.put("resourceMethod-0","GET");
        parameter.put("resourceMethodAuthType-0","Application");
        parameter.put("resourceMethodThrottlingTier-0","Unlimited");
        parameter.put("uriTemplate-0","Unlimited");
        parameter.put("tiersCollection","/*");
        parameter.put("tiersCollection","/*");
        parameter.put("default_version_checked","default_version");
        parameter.put("bizOwner","xx");
        parameter.put("bizOwnerMail","xx@ee.com");
        parameter.put("techOwner","xx");
        parameter.put("techOwnerMail","ggg@ww.com");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url","http://ws.cdyne.com/phoneverify/phoneverify.asmx") ;
        jsonObject.put("config",null);
        JSONObject  endpoint_config    =new JSONObject();
        endpoint_config.put("production_endpoints",jsonObject.toString());
        endpoint_config.put("endpoint_type", "http");
        parameter.put("endpoint_config", endpoint_config.toString());
        HttpProtocolHandler protocolHandler = HttpProtocolHandler.getInstance();
         //case1. ip is wrong
        HttpResponse responseMessage= protocolHandler.executePostMethod(url,parameter,null,null);
        assertEquals("ip is wrong", null, responseMessage);

        //case2. no login
        url="http://54.223.136.29:9763/publisher/site/blocks/item-add/ajax/add.jag";
        responseMessage= protocolHandler.executePostMethod(url,parameter,null,null);
        JSONObject jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("no parameter","true",String.valueOf(jsonObject1.get("error")));
        assertEquals("no parameter", "timeout", String.valueOf(jsonObject1.get("message")));


        //case3.no parameter
        Map<String,String > loginPar=new HashMap<String, String>();
        loginPar.put("username", "admin");
        loginPar.put("password", "admin");
        loginPar.put("action", "login");
        String loginUrl="http://54.223.136.29:9763/publisher/site/blocks/user/login/ajax/login.jag";
        responseMessage = protocolHandler.executePostMethod(loginUrl, loginPar, null, null);
        url="http://54.223.136.29:9763/publisher/site/blocks/item-add/ajax/add.jag";
        responseMessage= protocolHandler.executePostMethod(url, null, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("no parameter", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("no parameter", "null is not supported", String.valueOf(jsonObject1.get("message")));

        //case4.actionName is wrong and the specify not exist in APIMANAGER
        parameter.put("action", "loginAPI");
        responseMessage= protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("actionName is wrong and the specify not exist in APIMANAGER", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("actionName is wrong and the specify not exist in APIMANAGER", "loginAPI is not supported", String.valueOf(jsonObject1.get("message")));


        //case5.actionName is wrong and the specify  exist in APIMANAGER
        parameter.put("action", "logout");
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("actionName is wrong and the specify  exist in APIMANAGER", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("actionName is wrong and the specify  exist in APIMANAGER", "logout is not supported", String.valueOf(jsonObject1.get("message")));

        //case6.  name not provide
        parameter.remove("name");
        parameter.put("action", "addAPI");
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("no provided the required parameter", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("no provided the required parameter", " Error while adding the API- null-1.0.0", String.valueOf(jsonObject1.get("message")));
        //7.context not provide
        parameter.put("name", "PhoneVerification");
        parameter.remove("context");
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("no provided the required parameter", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("no provided the required parameter", "null", String.valueOf(jsonObject1.get("message")).trim());


        //8.version not provide
        parameter.put("context", "/phoneverify");
        parameter.remove("version");
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("no provided the required parameter", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("no provided the required parameter", " Error while adding the API- PhoneVerification-null", String.valueOf(jsonObject1.get("message")));

        //9.publish a api
        parameter.put("version", "1.0.0");
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("publish a api", "false", String.valueOf(jsonObject1.get("error")));
        //10.publish a duplication api
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("publish a duplication api", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("publish a duplication api", " Error occurred while adding the API. A duplicate API context already exists for /phoneverify", String.valueOf(jsonObject1.get("message")));

    }

}