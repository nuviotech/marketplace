import { message, Modal, notification } from "antd";

export const actionTypes = {
    LOGIN_REQUEST: 'LOGIN_REQUEST',
    LOGIN_SUCCESS: 'LOGIN_SUCCESS',
    LOGOUT: 'LOGOUT',
    LOGOUT_SUCCESS: 'LOGOUT_SUCCESS',
    CHECK_AUTHORIZATION: 'CHECK_AUTHORIZATION',
};

export function login() {
    return { type: actionTypes.LOGIN_REQUEST };
}

export function loginSuccess() {
    return { type: actionTypes.LOGIN_SUCCESS };
}

export function removeCouponDetails(){
    localStorage.removeItem("c_code");
    localStorage.removeItem("c_amt");
}

export function logOut() {
   // localStorage.clear();
    localStorage.removeItem("token");
    var lt=localStorage.getItem("_loginType_");
    localStorage.removeItem("_loginType_");
    localStorage.removeItem("name");
    localStorage.removeItem("action");
    localStorage.removeItem("c_code");
    localStorage.removeItem("c_amt");
    if(lt=="affiliate_account")
        window.location.href="/page/add_affiliate_accnt?tab=2";
    else
        window.location.href="/account/login";
  //  return { type: actionTypes.LOGOUT};
}

export function logOutSuccess() {
    return { type: actionTypes.LOGOUT_SUCCESS };
}

export function saveToken(token,loginType){
    modalSuccess2("success");
    localStorage.setItem("token",token); 
    localStorage.setItem("_loginType_",loginType);
    return true;
}

export function getLoginType(){
    var lt=null
    if (typeof window !== 'undefined') 
    lt= localStorage.getItem("_loginType_");
    return lt;
}

export function getToken(){
    var token=null;
   // token= localStorage.getItem("token");
   if (typeof window !== 'undefined') {
    token=localStorage.getItem("token");
    }
    //console.log("Token "+token);
    return token;
}

export function userIsLogin(){
    var token=getToken();
    //console.log("token  : "+token)
    if(token===null || token==='' || token==null)
        return false;
    else 
        return true;    
}


const modalSuccess2 = type => {
    notification[type]({
        message: 'Wellcome back',
        description: 'You are login successful!',
    });
};

const modalWarning2 = type => {
    notification[type]({
        message: 'Good bye!',
        description: 'Your account has been logged out!',
    });
};

