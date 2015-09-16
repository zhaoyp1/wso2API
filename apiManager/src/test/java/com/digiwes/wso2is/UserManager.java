package com.digiwes.wso2is;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.transport.http.HTTPConstants;
import org.junit.Before;
import org.junit.Test;
import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.um.ws.api.WSRealmBuilder;
import org.wso2.carbon.um.ws.api.WSUserStoreManager;
import org.wso2.carbon.user.core.Permission;
import org.wso2.carbon.user.core.UserRealm;
import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.UserStoreManager;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by liurl3 on 2015/9/14.
 */
public class UserManager {
    private final static String SERVER_URL = "https://54.223.170.22:9445/services/";
    public static final String TRUST_STORE_PATH = "F:/cer/wso2carbon.jks";
    public static final String TRUST_STORE_PASSWORD = "wso2carbon";
    private static String username;
    private static String password;
    private ConfigurationContext ctx ;
    private AuthenticationAdminStub authstub;
    private String authCookie;
    private boolean loginFlag = false;
    private UserStoreManager userStoreManager = null;
    private  String[] userRoles = null;

    @Before
    public void setUp() throws RemoteException, LoginAuthenticationExceptionException {
        ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
        String authEPR = SERVER_URL + "AuthenticationAdmin";
        //create a AuthenticationAdmin
        authstub = new AuthenticationAdminStub(ctx, authEPR);
        //set trust store properties required in SSL communication.
        System.setProperty("javax.net.ssl.trustStore", TRUST_STORE_PATH);
        System.setProperty("javax.net.ssl.trustStorePassword", TRUST_STORE_PASSWORD);
        //log in as admin user and obtain the cookie
       // loginFlag = authstub.login(username,password,"54.223.170.22");
    }

    @Test
    public void testLogin(){
        username = "admin";
        password = "admin";

        //case1 login IP is null
        try {
            loginFlag = authstub.login(username,password,null);
          //  assertEquals("login IP is null",false,loginFlag);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (LoginAuthenticationExceptionException e) {
            e.printStackTrace();
        }

        //case2 login IP is localhost
        try {
            loginFlag = authstub.login(username,password,"localhost");
           // assertEquals("login IP is localhost",false,loginFlag);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (LoginAuthenticationExceptionException e) {
            e.printStackTrace();
        }

        //case3 login IP is remote server address
        try {
            loginFlag = authstub.login(username,password,"54.223.170.22");
            assertEquals("login IP is remote server address",true,loginFlag);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (LoginAuthenticationExceptionException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testAddUser() throws RemoteException, LoginAuthenticationExceptionException ,UserStoreException{
        username = "admin";
        password = "admin";
        // logined add a user
        loginFlag = authstub.login(username, password, "54.223.170.22");
        if(loginFlag) {
            authCookie = (String) authstub._getServiceClient().getServiceContext().getProperty(HTTPConstants.COOKIE_STRING);
            careatUserStoreManager();
            //just add a user
            if (!userStoreManager.isExistingUser("liujava")) {// judge the user is existing or not
                //if it's not existing ,add it
                userStoreManager.addUser("liujava", "123456", null, null, null);
            }
            //add a user and appoint the user's role
            if (!userStoreManager.isExistingUser("liujava2")) {// judge the user is existing or not
                //if it's not existing ,add it
                userStoreManager.addUser("liujava2", "123456", new String[]{"login"}, null, null);
            }
            //add a user and appoint the user's role and fill in user's profile
            if (!userStoreManager.isExistingUser("liujava3")) {// judge the user is existing or not
                //if it's not existing ,add it
                Map<String,String> claims = new HashMap<String,String>();
                claims.put("http://wso2.org/claims/givenname", "liu");
                claims.put("http://wso2.org/claims/emailaddress", "liurl@asiainfo");
                claims.put("http://wso2.org/claims/lastname", "test");
                claims.put("http://wso2.org/claims/businessphone", "0112842302");
                userStoreManager.addUser("liujava3", "123456", new String[]{"login"}, claims, null);
            }
        }
    }

    @Test
    public void testAddRole() throws RemoteException, LoginAuthenticationExceptionException, org.wso2.carbon.user.api.UserStoreException {
        username = "admin";
        password = "admin";
        // logined add a role
        loginFlag = authstub.login(username, password, "54.223.170.22");
        if(loginFlag) {
            authCookie = (String) authstub._getServiceClient().getServiceContext().getProperty(HTTPConstants.COOKIE_STRING);
            careatUserStoreManager();
            //just add a role
            if (!userStoreManager.isExistingRole("liuRole")) {// judge the role is existing or not
                //if it's not existing ,add it
                userStoreManager.addRole("liuRole", null, null);
            }

            //add a role and appoint Permission
            if (!userStoreManager.isExistingRole("liuRole2")) {// judge the role is existing or not
                //if it's not existing ,add it
                Permission per = new Permission("/permission/admin/manage/api/subscribe","ui.execute");
                Permission per2 = new Permission("/permission/admin/login","ui.execute");
                Permission [] pers = new Permission[]{per,per2};
                userStoreManager.addRole("liuRole2", null, pers);
            }

        }
    }
    @Test
    public void testAddSecondaryUserStoreManager() throws RemoteException, LoginAuthenticationExceptionException, UserStoreException {
        username = "admin";
        password = "admin";
        //case2 logined add a SecondaryUserStore
        loginFlag = authstub.login(username, password, "54.223.170.22");
        if(loginFlag) {
            authCookie = (String) authstub._getServiceClient().getServiceContext().getProperty(HTTPConstants.COOKIE_STRING);
            careatUserStoreManager();
            UserStoreManager userStore = new WSUserStoreManager(SERVER_URL, authCookie, ctx);
            userStoreManager.addSecondaryUserStoreManager("liuUserStore", userStore);
        }
    }
    private void careatUserStoreManager() throws UserStoreException {
        UserRealm realm = WSRealmBuilder.createWSRealm(SERVER_URL, authCookie, ctx);
        userStoreManager = realm.getUserStoreManager();
    }
}
