package com.digiwes.wso2API.apiManager.httpClient;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by zhaoyp on 2015/9/9.
 */
public class StoreAPITest {



    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testStoreLoginAPI()throws UnsupportedEncodingException{
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
       url="http://54.223.136.29:9763/store/site/blocks/user/login/ajax/login.jag";
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
        assertEquals("user not exsit", "Login failed.Please recheck the username and password and try again.", String.valueOf(jsonObject.get("message")));

        //case5.password is wrong
        parameter.put("username", "admin");
        parameter.put("password", "aaa");
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("password is wrong","true",String.valueOf(jsonObject.get("error")));
        assertEquals("password is wrong","Login failed.Please recheck the username and password and try again.",String.valueOf(jsonObject.get("message")));

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
    public void testSubscribeAPI()throws UnsupportedEncodingException{
       String url="http://localhost:9763/store/site/blocks/subscription/subscription-add/ajax/subscription-add.jag";
        Map<String ,String> parameter=new HashMap<String, String>();
        //action=addAPISubscription&name=xxx&version=xxx&provider=xxx&tier=xxx&applicationName=xxx
        parameter.put("action","addAPISubscription");
        parameter.put("name","qqOnlineWebService ");
        parameter.put("version","1.0.0");
        parameter.put("provider", "admin");
        parameter.put("tier","Gold");
        parameter.put("applicationName", "DefaultApplication");
        HttpProtocolHandler protocolHandler = HttpProtocolHandler.getInstance();
         //case1. ip is wrong
        HttpResponse responseMessage= protocolHandler.executePostMethod(url,parameter,null,null);
        assertEquals("ip is wrong", null, responseMessage);

        //case2. no login
        url="http://54.223.136.29:9763/store/site/blocks/subscription/subscription-add/ajax/subscription-add.jag";
        responseMessage= protocolHandler.executePostMethod(url,parameter,null,null);
        //JSONObject jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("no login", "", responseMessage.getStringResult());

        //case3.no parameter
        Map<String,String > loginPar=new HashMap<String, String>();
        loginPar.put("username", "admin");
        loginPar.put("password", "admin");
        loginPar.put("action", "login");
        String loginUrl="http://54.223.136.29:9763/store/site/blocks/user/login/ajax/login.jag";
        responseMessage = protocolHandler.executePostMethod(loginUrl, loginPar, null, null);
        responseMessage= protocolHandler.executePostMethod(url, null, null, null);
        JSONObject jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
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
        parameter.put("action", "addAPISubscription");
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        System.out.print(responseMessage.getStringResult());
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("no provided the required parameter", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("no provided the required parameter", "org.wso2.carbon.apimgt.api.APIManagementException: Invalid input parameters for AddAPISubscription method", String.valueOf(jsonObject1.get("message")));

        //7.version not provide
        parameter.put("name", "qqOnlineWebService");
        parameter.remove("version");
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("no provided the required parameter", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("no provided the required parameter", "org.wso2.carbon.apimgt.api.APIManagementException: Invalid input parameters for AddAPISubscription method", String.valueOf(jsonObject1.get("message")).trim());


        //8.version is not exsited
        parameter.put("version", "1.0.1");
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("no provided the required parameter", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("version is not exsited", "org.wso2.carbon.apimgt.api.APIManagementException: Error while adding the subscription for user: admin", String.valueOf(jsonObject1.get("message")));

        //8.provider is not provide
        parameter.put("version", "1.0.0");
        parameter.remove("provider");
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("no provided the required parameter", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("no provided the required parameter", "org.wso2.carbon.apimgt.api.APIManagementException: Invalid input parameters for AddAPISubscription method", String.valueOf(jsonObject1.get("message")));


        //8.provider is wrong
        parameter.put("provider", "abc");
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("no provided the required parameter", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("no provided the required parameter", "org.wso2.carbon.apimgt.api.APIManagementException: Error while adding the subscription for user: admin", String.valueOf(jsonObject1.get("message")));

        //9.the tier not provide
        parameter.put("provider", "admin");
        parameter.remove("tier");
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("provide is wrong", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("provide is wrong", "org.wso2.carbon.apimgt.api.APIManagementException: Invalid input parameters for AddAPISubscription method", String.valueOf(jsonObject1.get("message")));

        //9.tier is wrong
        parameter.put("tier", "abc");
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("provide is wrong", "false", String.valueOf(jsonObject1.get("error")));

        //10 not provide applicationName
        parameter.put("tier", "Gold");
        parameter.remove("applicationName");
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("not provide applicationName", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("not provide applicationName", "org.wso2.carbon.apimgt.api.APIManagementException: Invalid input parameters for AddAPISubscription method", String.valueOf(jsonObject1.get("message")));

        //10.subcribe a duplication api
        parameter.put("applicationName", "DefaultApplication");
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("subcribe a duplication api", "false", String.valueOf(jsonObject1.get("error")));

        //11.subcribe a duplication api of another application
        parameter.put("applicationName","baas");
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("subcribe a duplication api of another application", "false", String.valueOf(jsonObject1.get("error")));


        //12. application  is not exsited
        parameter.put("applicationName","baas1");
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("application  is not exsited", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("application  is not exsited", "org.wso2.carbon.apimgt.api.APIManagementException: Error while adding the subscription for user: admin", String.valueOf(jsonObject1.get("message")));

        //12. application  is not exsited
        parameter.put("applicationName", "baas1");
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("application  is not exsited", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("application  is not exsited", "org.wso2.carbon.apimgt.api.APIManagementException: Error while adding the subscription for user: admin", String.valueOf(jsonObject1.get("message")));

        //13. applicationId  is not exsited
        parameter.remove("applicationName");
        parameter.put("applicationId","6");
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("application  is not exsited", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("application  is not exsited", "org.wso2.carbon.apimgt.api.APIManagementException: Invalid input parameters for AddAPISubscription method", String.valueOf(jsonObject1.get("message")));

        //14. applicationId  is   exsited
        parameter.put("applicationId","5");
        responseMessage = protocolHandler.executePostMethod(url, parameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("application  is not exsited", "false", String.valueOf(jsonObject1.get("error")));

    }
    @Test
    public void testGetPublishedAPI()throws UnsupportedEncodingException{
        String queryParameter = "action=getSubscriptionByApplication&app=App1";
        String url = "http://localhost:9763/store/site/blocks/subscription/subscription-list/ajax/subscription-list.jag";
        HttpProtocolHandler protocolHandler = HttpProtocolHandler.getInstance();
        //case 1 ip is wrong
        HttpResponse responseMessage = protocolHandler.executeGetMethod(url, queryParameter, null, null);
        assertEquals("ip is wrong", null, responseMessage);
        //case 2.no login  54.223.136.29
        url = "http://54.223.136.29:9763/store/site/blocks/subscription/subscription-list/ajax/subscription-list.jag";
        responseMessage = protocolHandler.executeGetMethod(url, queryParameter, null, null);
        assertEquals("no login", "", responseMessage.getStringResult());

        //case 3 parameter is null
        Map<String,String > loginPar=new HashMap<String, String>();
        loginPar.put("username", "admin");
        loginPar.put("password", "admin");
        loginPar.put("action", "login");
        String loginUrl="http://54.223.136.29:9763/store/site/blocks/user/login/ajax/login.jag";
        responseMessage = protocolHandler.executePostMethod(loginUrl, loginPar, null, null);
        responseMessage = protocolHandler.executeGetMethod(url, null, null, null);
        JSONObject jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("no parameter", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("no parameter", "null is not supported", String.valueOf(jsonObject1.get("message")));

        //case4. action is empty
        queryParameter = "app=App1";
        responseMessage = protocolHandler.executeGetMethod(url, queryParameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("no parameter", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("no parameter", "null is not supported", String.valueOf(jsonObject1.get("message")));

        //case5.action is wrong
        queryParameter = "action=getSubscriptionByApplication1&app=App1";
        responseMessage = protocolHandler.executeGetMethod(url, queryParameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("action is wrong", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("action is wrong", "getSubscriptionByApplication1 is not supported", String.valueOf(jsonObject1.get("message")));

        //case6.action is existed
        queryParameter = "action=addAPISubscription&app=App1";
        responseMessage = protocolHandler.executeGetMethod(url, queryParameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("action is existed", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("action is existed", "addAPISubscription is not supported", String.valueOf(jsonObject1.get("message")));

        //case7. app is not existed
        queryParameter = "action=getSubscriptionByApplication&app=App1";
        responseMessage = protocolHandler.executeGetMethod(url, queryParameter, null, null);
        System.out.print(responseMessage.getStringResult());
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        JSONObject jsonObject =JSONObject.fromObject(jsonObject1.get("subscriptionCallbackResponse"));
        assertEquals("action is existed", "true", String.valueOf(jsonObject.get("error")));
        assertEquals("action is existed", " Application App1 does not exist for user admin", String.valueOf(jsonObject.get("message")));

        //case8. app is  existed
        queryParameter = "action=getSubscriptionByApplication&app=DefaultApplication";
        responseMessage = protocolHandler.executeGetMethod(url, queryParameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("action is existed", "false", String.valueOf(jsonObject1.get("error")));

    }

    @Test
    public void testGetAllPanitPublishedAPI()throws UnsupportedEncodingException{
       String queryParameter = "action=getAllPaginatedPublishedAPIs&tenant=carbon.super&start=1&end=100";
       String url = "http://localhost:9763/store/site/blocks/api/listing/ajax/list.jag";
        HttpProtocolHandler protocolHandler = HttpProtocolHandler.getInstance();
        //case 1 ip is wrong
        HttpResponse responseMessage = protocolHandler.executeGetMethod(url, queryParameter, null, null);
        assertEquals("ip is wrong", null, responseMessage);
        //case 2.no login  54.223.136.29
        url = "http://54.223.136.29:9763/store/site/blocks/api/listing/ajax/list.jag";
        responseMessage = protocolHandler.executeGetMethod(url, queryParameter, null, null);
        assertEquals("no login", "", responseMessage.getStringResult());

        //case 3 parameter is null
        Map<String,String > loginPar=new HashMap<String, String>();
        loginPar.put("username", "admin");
        loginPar.put("password", "admin");
        loginPar.put("action", "login");
        String loginUrl="http://54.223.136.29:9763/store/site/blocks/user/login/ajax/login.jag";
        responseMessage = protocolHandler.executePostMethod(loginUrl, loginPar, null, null);
        responseMessage = protocolHandler.executeGetMethod(url, null, null, null);
        JSONObject jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("no parameter", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("no parameter", "null is not supported", String.valueOf(jsonObject1.get("message")));

        //case4. action is empty
        queryParameter = "tenant=carbon.super&start=1&end=100";
        responseMessage = protocolHandler.executeGetMethod(url, queryParameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("no parameter", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("no parameter", "null is not supported", String.valueOf(jsonObject1.get("message")));

        //case5.action is wrong
        queryParameter = "action=getSubscriptionByApplication1&tenant=carbon.super&start=1&end=100";
        responseMessage = protocolHandler.executeGetMethod(url, queryParameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("action is wrong", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("action is wrong", "getSubscriptionByApplication1 is not supported", String.valueOf(jsonObject1.get("message")));

        //case6.action is existed
        queryParameter = "action=addAPISubscription&tenant=carbon.super&start=1&end=100";
        responseMessage = protocolHandler.executeGetMethod(url, queryParameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("action is existed", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("action is existed", "addAPISubscription is not supported", String.valueOf(jsonObject1.get("message")));

        //case 7 tenant is empty
        queryParameter = "action=getAllPaginatedPublishedAPIs&start=1&end=100";
        responseMessage = protocolHandler.executeGetMethod(url, queryParameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("tenant is empty", "false", String.valueOf(jsonObject1.get("error")));
        assertEquals("tenant is empty", 2, JSONArray.fromObject(jsonObject1.get("apis")).size());

        //case 7 tenant is wrong
        queryParameter = "action=getAllPaginatedPublishedAPIs&tenant=carbon.super11&start=1&end=100";
        responseMessage = protocolHandler.executeGetMethod(url, queryParameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("tenant is wrong", "false", String.valueOf(jsonObject1.get("error")));
        assertEquals("tenant is wrong", "null", String.valueOf(jsonObject1.get("apis")));

        //case 8  no start and end parameter
        queryParameter = "action=getAllPaginatedPublishedAPIs&tenant=carbon.super";
        responseMessage = protocolHandler.executeGetMethod(url, queryParameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertNotEquals(" no start and end parameter", "false", String.valueOf(jsonObject1.get("error")));
        assertNotEquals(" no start and end parameter", "true", String.valueOf(jsonObject1.get("error")));


        //case 7  no start   parameter
        queryParameter = "action=getAllPaginatedPublishedAPIs&tenant=carbon.super&end=100";
        responseMessage = protocolHandler.executeGetMethod(url, queryParameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertNotEquals(" no start   parameter", "false", String.valueOf(jsonObject1.get("error")));
        assertNotEquals(" no start   parameter", "true", String.valueOf(jsonObject1.get("error")));

        //case 7  no end   parameter
        queryParameter = "action=getAllPaginatedPublishedAPIs&tenant=carbon.super&start=1";
        responseMessage = protocolHandler.executeGetMethod(url, queryParameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertNotEquals(" no end   parameter", "false", String.valueOf(jsonObject1.get("error")));
        assertNotEquals(" no end   parameter", "true", String.valueOf(jsonObject1.get("error")));

        //case 7  end <start
        queryParameter = "action=getAllPaginatedPublishedAPIs&tenant=carbon.super&start=4&end=1";
        responseMessage = protocolHandler.executeGetMethod(url, queryParameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("end <start", "false", String.valueOf(jsonObject1.get("error")));
        assertEquals("tenant is wrong", "null", String.valueOf(jsonObject1.get("apis")));

        //case 7  no start=-1 parameter
        queryParameter = "action=getAllPaginatedPublishedAPIs&tenant=carbon.super&start=-1&end=100";
        responseMessage = protocolHandler.executeGetMethod(url, queryParameter, null, null);
        jsonObject1 = JSONObject.fromObject(responseMessage.getStringResult());
        assertEquals("tenant is wrong", "true", String.valueOf(jsonObject1.get("error")));
        assertEquals("tenant is wrong", "Invalid inputs [\"start\"]", String.valueOf(jsonObject1.get("message")));

    }

  }