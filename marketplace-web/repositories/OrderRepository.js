import Axios from "axios";
import { marketplaceUrl } from "./Repository";
import { getToken, logOut, userIsLogin } from "~/store/auth/action";


export const getOrderProductDetails = async (orderProductId) => {
    
    if (userIsLogin()) {

        const data = await Axios.post(`${marketplaceUrl}/getOrderProductDetailsInJson/${orderProductId}`, {
            headers: {
                Authorization: "Bearer " + getToken(),
            }
        }).then(
            (response) => {
                return response.data;
            },
            (error) => {
             
                logOut();
                //dis(logOut());
                //window.location.assign("/account/login")
                return error;
            }
        ).catch(err => {
            return (err)
        });
        return data;
    } else {
        return "{'msg':'No login user'}";
    }
}