import { Modal } from "antd";
import Axios from "axios";
import { marketplaceUrl } from "~/repositories/Repository";
import { getToken, logOut } from "~/store/auth/action";

export const getAffiliateAccountDetails = async () => {
   const data= Axios.get(`${marketplaceUrl}/getAffiliateAccountDetails`, {
        headers: {
            Authorization: "Bearer " + getToken(),
        }
    }).then(
        async (response) => {
            if (response.data.status == 0) {
                //console.log(response?.data?.affiliate_account);
               return response?.data?.affiliate_account;
            } else {
                const modal = Modal.error({
                    centered: true,
                    title: 'Error!',
                    content: `invalid access!!`,
                });
            }
            return null;
        },
        (error) => {
           console.error("(7475) Some thing wrong.")
           logOut();
            console.log(error)
            return null;
        }
    )
    return data;
}