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

export function logOut() {
   // localStorage.clear();
    localStorage.removeItem("token");
    modalWarning2("success");
    return { type: actionTypes.LOGOUT};
}

export function logOutSuccess() {
    return { type: actionTypes.LOGOUT_SUCCESS };
}

export function saveToken(token){
    modalSuccess2("success");
    localStorage.setItem("token",token); 
    return true;
}

export function getToken(){
    var token=null;
   // token= localStorage.getItem("token");
   if (typeof window !== 'undefined') {
    token=localStorage.getItem("token");
}
    return token;
}

export function userIsLogin(){
    var token=getToken();
    //console.log("token  : "+token)
    if(token===null || token==='')
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

