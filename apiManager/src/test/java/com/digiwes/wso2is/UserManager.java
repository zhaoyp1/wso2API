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

    /**
     * @description add user
     * @throws RemoteException
     * @throws LoginAuthenticationExceptionException
     * @throws UserStoreException
     */
    @Test
    public void testAddUser() throws RemoteException, LoginAuthenticationExceptionException ,UserStoreException{
        username = "admin";
        password = "admin";
        // login
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
            //add a user and appoint the user's role and fill in default user's profile
            if (!userStoreManager.isExistingUser("liujava3")) {// judge the user is existing or not
                //if it's not existing ,add it
                Map<String,String> claims = new HashMap<String,String>();
                claims.put("http://wso2.org/claims/givenname", "liu");
                claims.put("http://wso2.org/claims/emailaddress", "liurl@asiainfo");
                claims.put("http://wso2.org/claims/lastname", "test");
                claims.put("http://wso2.org/claims/businessphone", "0112842302");
                userStoreManager.addUser("liujava3", "123456", new String[]{"login"}, claims, null);
            }

            //add a user and appoint the user's role and fill in testPhone user's profile
            if (!userStoreManager.isExistingUser("liujava4")) {// judge the user is existing or not
                //if it's not existing ,add it
                Map<String,String> claims = new HashMap<String,String>();
                claims.put("http://wso2.org/claims/givenname", "liu");
                claims.put("http://wso2.org/claims/emailaddress", "liurl@asiainfo");
                claims.put("http://wso2.org/claims/lastname", "test");
                claims.put("http://wso2.org/claims/businessphone", "0112842302");
                userStoreManager.addUser("liujava4", "123456", new String[]{"login"}, claims, "testPhone");
            }

            //add a user and appoint the user's role and fill in testPhone2 user's profile
            if (!userStoreManager.isExistingUser("liujava5")) {// judge the user is existing or not
                //if it's not existing ,add it
                Map<String,String> claims = new HashMap<String,String>();
                claims.put("http://wso2.org/claims/givenname", "liu");
                claims.put("http://wso2.org/claims/emailaddress", "liurl@asiainfo");
                claims.put("http://wso2.org/claims/lastname", "test");
                claims.put("http://wso2.org/claims/businessphone", "0112842302");
                userStoreManager.addUser("liujava5", "123456", new String[]{"login"}, claims, "testPhone2",true);
            }

            //add a user and appoint the user's role and fill in testPhone2 user's profile
            if (!userStoreManager.isExistingUser("liujava6")) {// judge the user is existing or not
                //if it's not existing ,add it
                Map<String,String> claims = new HashMap<String,String>();
                claims.put("http://wso2.org/claims/givenname", "liu");
                claims.put("http://wso2.org/claims/emailaddress", "liurl@asiainfo");
                claims.put("http://wso2.org/claims/lastname", "test");
                claims.put("http://wso2.org/claims/businessphone", "0112842302");
                userStoreManager.addUser("liujava6", "123456", new String[]{"login"}, claims, "testPhone2",false);
            }
        }
    }

    /**
     * @description delete user by userName
     * @throws RemoteException
     * @throws LoginAuthenticationExceptionException
     * @throws UserStoreException
     */
    @Test
    public void testDeleteUser() throws RemoteException, LoginAuthenticationExceptionException, UserStoreException {
        username = "admin";
        password = "admin";
        // login
        loginFlag = authstub.login(username, password, "54.223.170.22");
        if(loginFlag) {
            authCookie = (String) authstub._getServiceClient().getServiceContext().getProperty(HTTPConstants.COOKIE_STRING);
            careatUserStoreManager();
            if(userStoreManager.isExistingUser("liujava6")){
                userStoreManager.deleteUser("liujava6");
            }
        }
    }

    /**
     * @description get userName by user's profile
     * @throws RemoteException
     * @throws LoginAuthenticationExceptionException
     * @throws UserStoreException
     */
    @Test
    public void testGetUserList() throws RemoteException, LoginAuthenticationExceptionException, UserStoreException {
        username = "admin";
        password = "admin";
        // login
        loginFlag = authstub.login(username, password, "54.223.170.22");
        if(loginFlag) {
            authCookie = (String) authstub._getServiceClient().getServiceContext().getProperty(HTTPConstants.COOKIE_STRING);
            careatUserStoreManager();
            //find users by default user's profile
            String[] users = userStoreManager.getUserList("http://wso2.org/claims/businessphone","0112842302",null);
            if(null != users){
                for(int i=0 ; i<users.length ; i++){
                    System.out.println(users[i]);
                }
            }
            //find users by specified user's profile
            String[] users2 = userStoreManager.getUserList("http://wso2.org/claims/businessphone","0112842302","testPhone");
            if(null != users2){
                for(int i=0 ; i<users2.length ; i++){
                    System.out.println(users2[i]);
                }
            }
        }
    }

    /**
     * @description get userId by userName
     * @throws RemoteException
     * @throws LoginAuthenticationExceptionException
     * @throws UserStoreException
     */
    @Test
    public void testGetUserId() throws RemoteException, LoginAuthenticationExceptionException, UserStoreException {
        username = "admin";
        password = "admin";
        // login
        loginFlag = authstub.login(username, password, "54.223.170.22");
        if(loginFlag) {
            authCookie = (String) authstub._getServiceClient().getServiceContext().getProperty(HTTPConstants.COOKIE_STRING);
            careatUserStoreManager();
            int userId = userStoreManager.getUserId("liujava");
            System.out.println(userId);
        }
    }

    /**
     * @description modify user's profile
     * @throws UserStoreException
     * @throws RemoteException
     * @throws LoginAuthenticationExceptionException
     */
    @Test
    public void testSetUserClaimValue() throws UserStoreException, RemoteException, LoginAuthenticationExceptionException {
        username = "admin";
        password = "admin";
        // login
        loginFlag = authstub.login(username, password, "54.223.170.22");
        if(loginFlag) {
            authCookie = (String) authstub._getServiceClient().getServiceContext().getProperty(HTTPConstants.COOKIE_STRING);
            careatUserStoreManager();
            if(userStoreManager.isExistingUser("liujava4")){
                //modify default user's profile
                userStoreManager.setUserClaimValue("liujava4","http://wso2.org/claims/givenname", "rui",null);

                //modify testPhone user's profile
                userStoreManager.setUserClaimValue("liujava4","http://wso2.org/claims/givenname", "rui","testPhone");
            }

        }
    }

    /**
     * @description modify user's profile
     * @throws UserStoreException
     * @throws RemoteException
     * @throws LoginAuthenticationExceptionException
     */
    @Test
    public void testSetUserClaimValues() throws UserStoreException, RemoteException, LoginAuthenticationExceptionException {
        username = "admin";
        password = "admin";
        // login
        loginFlag = authstub.login(username, password, "54.223.170.22");
        if(loginFlag) {
            authCookie = (String) authstub._getServiceClient().getServiceContext().getProperty(HTTPConstants.COOKIE_STRING);
            careatUserStoreManager();
            if(userStoreManager.isExistingUser("liujava4")){
                Map<String,String> claims = new HashMap<String,String>();
                claims.put("http://wso2.org/claims/givenname", "liu");
                claims.put("http://wso2.org/claims/emailaddress", "liurl@asiainfo");
                claims.put("http://wso2.org/claims/lastname", "rui");
                claims.put("http://wso2.org/claims/businessphone", "0112842302");
                //modify default user's profile
                userStoreManager.setUserClaimValues("liujava4", claims, null);

                Map<String,String> claims2 = new HashMap<String,String>();
                claims2.put("http://wso2.org/claims/lastname", "ling");
                claims2.put("http://wso2.org/claims/businessphone", "777788888777");
                //modify testPhone user's profile
                userStoreManager.setUserClaimValues("liujava4", claims2, "testPhone");
            }
        }
    }

    /**
     * @description  modify user's password
     * @throws RemoteException
     * @throws LoginAuthenticationExceptionException
     * @throws UserStoreException
     */
    @Test
    public void testUpdateCredential() throws RemoteException, LoginAuthenticationExceptionException, UserStoreException {
        username = "admin";
        password = "admin";
        // login
        loginFlag = authstub.login(username, password, "54.223.170.22");
        if(loginFlag) {
            authCookie = (String) authstub._getServiceClient().getServiceContext().getProperty(HTTPConstants.COOKIE_STRING);
            careatUserStoreManager();
            if(userStoreManager.isExistingUser("liujava4")){
                //modify user's password.newPassword=abc123,oldPassword=123456
                userStoreManager.updateCredential("liujava4", "abc123", "123456");
            }
        }
    }

    /**
     * @description modify user's password by admin
     * @throws RemoteException
     * @throws LoginAuthenticationExceptionException
     * @throws UserStoreException
     */
    @Test
    public void testUpdateCredentialByAdmin() throws RemoteException, LoginAuthenticationExceptionException, UserStoreException {
        username = "admin";
        password = "admin";
        // login
        loginFlag = authstub.login(username, password, "54.223.170.22");
        if(loginFlag) {
            authCookie = (String) authstub._getServiceClient().getServiceContext().getProperty(HTTPConstants.COOKIE_STRING);
            careatUserStoreManager();
            if(userStoreManager.isExistingUser("liujava4")){
                //modify user's password.newPassword=123456
                userStoreManager.updateCredentialByAdmin("liujava4", "123456");
            }
        }
    }

    /**
     * @description add role
     * @throws RemoteException
     * @throws LoginAuthenticationExceptionException
     * @throws org.wso2.carbon.user.api.UserStoreException
     */
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

            //add a role and appoint Permission and users
            if (!userStoreManager.isExistingRole("liuRole3")) {// judge the role is existing or not
                //if it's not existing ,add it
                Permission per = new Permission("/permission/admin/manage/api/subscribe","ui.execute");
                Permission per2 = new Permission("/permission/admin/login","ui.execute");
                Permission [] pers = new Permission[]{per,per2};
                userStoreManager.addRole("liuRole3", new String[]{"liujava", "liujava3"}, pers);
            }
        }
    }

    /**
     * @description get all roleName
     * @throws RemoteException
     * @throws LoginAuthenticationExceptionException
     * @throws UserStoreException
     */
    @Test
    public void testGetRoleNames() throws RemoteException, LoginAuthenticationExceptionException, UserStoreException {
        username = "admin";
        password = "admin";
        // login
        loginFlag = authstub.login(username, password, "54.223.170.22");
        if(loginFlag) {
            authCookie = (String) authstub._getServiceClient().getServiceContext().getProperty(HTTPConstants.COOKIE_STRING);
            careatUserStoreManager();
            String[] roleNames = userStoreManager.getRoleNames();
            if(null != roleNames){
                for(int i=0 ; i<roleNames.length ; i++){
                    System.out.println(roleNames[i]);
                }
            }
        }
    }

    /**
     * @description delete role by name
     * @throws RemoteException
     * @throws LoginAuthenticationExceptionException
     * @throws UserStoreException
     */
    @Test
    public void testDeleteRole() throws RemoteException, LoginAuthenticationExceptionException, UserStoreException {
        username = "admin";
        password = "admin";
        // login
        loginFlag = authstub.login(username, password, "54.223.170.22");
        if(loginFlag) {
            authCookie = (String) authstub._getServiceClient().getServiceContext().getProperty(HTTPConstants.COOKIE_STRING);
            careatUserStoreManager();
            if(userStoreManager.isExistingRole("login")){
                userStoreManager.deleteRole("login");
            }
        }
    }
    /**
     * @description modify user's role by userName
     * @throws RemoteException
     * @throws LoginAuthenticationExceptionException
     * @throws UserStoreException
     */
    @Test
    public void testUpdateRoleListOfUser() throws RemoteException, LoginAuthenticationExceptionException, UserStoreException {
        username = "admin";
        password = "admin";
        // login
        loginFlag = authstub.login(username, password, "54.223.170.22");
        if(loginFlag) {
            authCookie = (String) authstub._getServiceClient().getServiceContext().getProperty(HTTPConstants.COOKIE_STRING);
            careatUserStoreManager();
            if (userStoreManager.isExistingUser("liujava2")) {// judge the user is existing or not
                //modify the user's role
                //add role to the user
                userStoreManager.updateRoleListOfUser("liujava2", null, new String[]{"liuRole2"});
                //remove role to the user
                userStoreManager.updateRoleListOfUser("liujava2", new String[]{"liuRole2"}, null);
                //Back to the user specified role
               userStoreManager.updateRoleListOfUser("liujava2", new String[]{"login"}, new String[]{"liuRole2"});
            }
        }
    }

    /**
     * @description get user's roles by userName
     * @throws RemoteException
     * @throws LoginAuthenticationExceptionException
     * @throws UserStoreException
     */
    @Test
    public void testGetRoleListOfUser() throws RemoteException, LoginAuthenticationExceptionException, UserStoreException {
        username = "admin";
        password = "admin";
        // login
        loginFlag = authstub.login(username, password, "54.223.170.22");
        if(loginFlag) {
            authCookie = (String) authstub._getServiceClient().getServiceContext().getProperty(HTTPConstants.COOKIE_STRING);
            careatUserStoreManager();
            if (userStoreManager.isExistingUser("liujava2")) {// judge the user is existing or not
                //get the user's roles
                String [] roles = userStoreManager.getRoleListOfUser("liujava2");
                if(null != roles) {
                    for (int i = 0; i < roles.length; i++) {
                        System.out.println(roles[i]);
                    }
                }
            }
        }
    }

    /**
     * @description modify role's users by roleName
     * @throws RemoteException
     * @throws LoginAuthenticationExceptionException
     * @throws UserStoreException
     */
    @Test
    public void testUpdateUserListOfRole() throws RemoteException, LoginAuthenticationExceptionException, UserStoreException {
        username = "admin";
        password = "admin";
        // login
        loginFlag = authstub.login(username, password, "54.223.170.22");
        if(loginFlag) {
            authCookie = (String) authstub._getServiceClient().getServiceContext().getProperty(HTTPConstants.COOKIE_STRING);
            careatUserStoreManager();
            if (userStoreManager.isExistingRole("liuRole3")) {// judge the user is existing or not
                //modify the role's users
                //add user to the role
                userStoreManager.updateUserListOfRole("liuRole3",null,new String[]{"liujava2"});
                //remove user to the role
                userStoreManager.updateUserListOfRole("liuRole3", new String[]{"liujava2"}, null);
                //Back to the role specified user
                userStoreManager.updateUserListOfRole("liuRole3", new String[]{"liujava"}, new String[]{"liujava2"});
            }
        }
    }

    /**
     * @description get role's users by roleName
     * @throws RemoteException
     * @throws LoginAuthenticationExceptionException
     * @throws UserStoreException
     */
    @Test
    public void testGetUserListOfRole() throws RemoteException, LoginAuthenticationExceptionException, UserStoreException {
        username = "admin";
        password = "admin";
        // logined add a user
        loginFlag = authstub.login(username, password, "54.223.170.22");
        if(loginFlag) {
            authCookie = (String) authstub._getServiceClient().getServiceContext().getProperty(HTTPConstants.COOKIE_STRING);
            careatUserStoreManager();
            if (userStoreManager.isExistingRole("liuRole3")) {// judge the user is existing or not
                //get the user's roles
                String [] users = userStoreManager.getUserListOfRole("liuRole3");
                for(int i=0 ; i<users.length ; i++){
                    System.out.println(users[i]);
                }
            }
        }
    }

    /**
     * @description add secondary userStore(Method does not implement)
     * @throws RemoteException
     * @throws LoginAuthenticationExceptionException
     * @throws UserStoreException
     */
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
