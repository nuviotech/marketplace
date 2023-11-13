
import { createContext, useEffect, useState } from "react";
import { userData } from "~/repositories/UserDeatils";
import { userIsLogin } from "~/store/auth/action";


export const AuthContext=createContext()

export const AuthContextProvider =({children})=>{
    const [currentUser,setCurrentUser]=useState({});
    const unsub=useEffect(async()=>{
        if(userIsLogin()){
            setCurrentUser(await userData())
        }
        return()=>{
            unsub();
        }
    },[]);

    return(
        <AuthContext.Provider value={{currentUser}}>
            {children}
        </AuthContext.Provider>
    );
};