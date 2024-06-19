import axios from "axios";
import { getToken, logOut, saveToken, userIsLogin, } from "~/store/auth/action";
import { marketplaceUrl } from "./Repository";
import { Modal } from "antd";


export const userData = async () => {

    if (userIsLogin()) {
        const data = await axios.get(`${marketplaceUrl}/getUserDetails`, {
            headers: {
                Authorization: "Bearer " + getToken(),
            }
        }).then(
            (response) => {
                return response.data;
            },
            (error) => {
                console.log(error)
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

export const updateUserDetails = async (updatedData) => {
    const data = await axios.post(`${marketplaceUrl}/updateUserDetails`, updatedData, {
        headers: {
            Authorization: "Bearer " + getToken(),
        }
    }).then(
        (response) => {
            return response.data;
        },
        (error) => {
            logOut();
            window.location.assign("/account/login")
            return error;
        }
    ).catch(err => {
        return (err)
    });
    return data;
}

export const returnPolicyByUser = async (info) => {
    const data = await axios.post(`${marketplaceUrl}/returnPolicyByUser`, info, {
        headers: {
            Authorization: "Bearer " + getToken(),
        }
    }).then(
        (responce) => {
            //alert("res :"+responce.data);
            return responce.data;
        },
        (error) => {
            alert("something wrong  " + error);
        }
    ).catch(err => {
        return (err)
    });
    //alert(data);
    return data;
}

export const saveUserDetails = async (state, pathValue, router) => {
    try {
        await axios.post(`${marketplaceUrl}/saveUser`, state).then(
            (response) => {
                var res = response.data;
                if (res.status == -1) {
                    const modal = Modal.error({
                        centered: true,
                        title: 'Invalid input!',
                        content: `Please enter valid first name or last name.`,
                    });
                    modal.update;
                } else if (res.status == -2) {
                    const modal = Modal.error({
                        centered: true,
                        title: 'Invalid input!',
                        content: `Please enter valid first name or last name.`,
                    });
                    modal.update;
                } else if (res.status == -3) {
                    const modal = Modal.error({
                        centered: true,
                        title: 'Invalid input!',
                        content: `Please enter valid first name or last name.`,
                    });
                    modal.update;
                } else if (res.status == -4) {
                    const modal = Modal.error({
                        centered: true,
                        title: 'Invalid input!',
                        content: `Please enter valid first name or last name.`,
                    });
                    modal.update;
                } else if (res.status == -5) {
                    if (pathValue == 'checkout' || pathValue === 'checkout') {
                        if (typeof window !== 'undefined') {
                            sessionStorage.setItem("action", "checkout")
                        }
                    }

                    const modal = Modal.error({
                        centered: true,
                        title: 'Email Already Registered !!',
                        content: state?.email + ` this email is already registered. Please log in instead.`,
                    });
                    router.push('/account/login?email=' + state?.email);
                    modal.update;

                } else if (res.status == -6) {
                    const modal = Modal.error({
                        centered: true,
                        title: 'Invalid state!!',
                        content: `please select the state.`,
                    });
                    modal.update;
                } else if (res.status == 1) {
                    const modal = Modal.error({
                        centered: true,
                        title: 'Server Error!!',
                        content: `Something went wrong on server.`,
                    });
                    modal.update;
                } else if (res.status == '0') {

                    saveToken(res.token, "normal_account");
                    if (pathValue == 'checkout' || pathValue === 'checkout') {
                        window.location.assign('/account/checkout')
                    } else {
                        const modal = Modal.success({
                            centered: true,
                            title: `All Set!`,
                            content: `Your account is ready to go. Dive into Nuvio and discover our exclusive offers and latest arrivals.`,
                        });
                        modal.update;
                        router.push('/shop');
                    }
                }
            },
            (error) => {
                console.error("Register user (error) : " + error);
                const modal = Modal.error({
                    centered: true,
                    title: 'Something went wrong on server!!',
                    content: `Oops! It seems that something unexpected occurred on our end. We apologize for any inconvenience this may have caused. Our team has been notified and is working diligently to fix the issue. Please try again later, or feel free to contact our support team if you need immediate assistance. Thank you for your understanding.`,
                });
                modal.update;
            }
        )
    } catch (error) {
        console.log("Error (20) : " + error);
    }
}

export const identifyCodStatus = async () => {
    if (userIsLogin()) {
        const data = await axios.get(`${marketplaceUrl}/getCodStatus`, {
            headers: {
                Authorization: "Bearer " + getToken(),
            }
        }).then(
            (response) => {
                // alert(JSON.stringify(response?.data));
                return response.data;
            },
            (error) => {
                console.log(error)
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



