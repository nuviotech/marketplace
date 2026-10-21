import React, { Component } from 'react';
import Link from 'next/link';
import Router from 'next/router';
import axios from 'axios';
import { login, loginSuccess, saveToken } from '../../../store/auth/action';
import { marketplaceUrl } from '~/repositories/Repository';
import { Form, Input, notification, Modal, Button, Spin } from 'antd';
import { connect } from 'react-redux';
import { AuthContextProvider } from '~/context/loginContext';
import ReCAPTCHA from "react-google-recaptcha";
//import FacebookLogin from 'react-facebook-login';
import { MathCaptcha } from '../commons/MathCaptcha';


class Login extends Component {
    constructor(props) {
        super(props);
        this.state = {
            email: null,
            password: null,
            login: false,
            store: null,
            cflag: null,
            googleLoading: false
        };
    }

    static getDerivedStateFromProps(props) {
        if (props.isLoggedIn === true) {
            Router.push('/');
        }

        return false;
    }

    handleFeatureWillUpdate(e) {
        e.preventDefault();
        notification.open({
            message: 'Opps! Something went wrong.',
            description: 'This feature has been updated later!',
            duration: 500,
        });
    }

    // onChange = value => {
    //     this.setState({ cflag: value });
    // }

    handleCaptchaSuccess = () => {
        this.setState({ cflag: true });
    }

    // responseFacebook = (response) => {
    //     //alert(JSON.stringify(response.email));
    //     // loginAxiosAction("facebook");
    // }

    // Handle Google Sign-In response
    handleGoogleSignIn = (response) => {
        if (response.credential) {
            this.setState({ googleLoading: true });

            axios.post(`${marketplaceUrl}/googleLogin`, {
                credential: response.credential
            }).then(
                (response) => {
                    var token = response.data.Token;
                    var status = response.data.status;
                    if (status == 0) {
                        saveToken(token, "google_account");
                        var action;
                        if (typeof window !== 'undefined') {
                            action = sessionStorage.getItem("action")
                        }
                        if (action == "checkout") {
                            Router.push('/account/checkout')
                            sessionStorage.removeItem("action");
                        } else if (action != null && action?.length > 0) {
                            Router.push(action);
                            sessionStorage.removeItem("action");
                        } else
                            window.location.assign('/');
                    } else {
                        const modal = Modal.error({
                            centered: true,
                            title: 'Google Login Failed',
                            content: response.data.message || 'Unable to login with Google',
                        });
                    }
                    this.setState({ googleLoading: false });
                },
                (error) => {
                    const modal = Modal.error({
                        centered: true,
                        title: 'Google Login Error',
                        content: 'An error occurred during Google login',
                    });
                    console.error("Google login error: " + error);
                    this.setState({ googleLoading: false });
                }
            );
        }
    }

    loginAxiosAction = (action) => {
        const loginCredentials = {
            email: this.state.email,
            loginGateway: "normal_account",
            password: this.state.password,
        }

        const urlRegex = /(https?:\/\/[^\s]+)/gi;
        if (urlRegex.test(this.state.email)) {
            alert("link not allow!!");
        } else if (urlRegex.test(this.state.password)) {
            alert("link not allow!!");
        } else {
            axios.post(`${marketplaceUrl}/login`, loginCredentials).then(
                async (response) => {
                    // console.log(JSON.stringify(response));
                    var token = response.data.Token;
                    var status = response.data.status;
                    if (status == 0) {
                        saveToken(token, "normal_account");
                        //this.props.dispatch(login());
                        //this.props.dispatch(loginSuccess());
                        // Router.push('/');
                        var action;
                        if (typeof window !== 'undefined') {
                            action = sessionStorage.getItem("action")
                        }
                        if (action == "checkout") {
                            Router.push('/account/checkout')
                            sessionStorage.removeItem("action");
                        } else if (action != null && action?.length > 0) {
                            Router.push(action);
                            sessionStorage.removeItem("action");
                        } else
                            window.location.assign('/');
                    } else if (status == 1) {
                        const modal = Modal.error({
                            centered: true,
                            title: 'Opps, something went wrong!!',
                            content: `` + response.data.message,
                        });
                        modal.update;
                    }
                },
                (error) => {
                    if (action == "google") {
                        const modal = Modal.error({
                            centered: true,
                            title: 'Wrong Email !!',
                            content: `This email is not register, please try with another.`,
                        });
                        modal.update;
                        console.error("error : " + error);
                    } else {
                        const modal = Modal.error({
                            centered: true,
                            title: 'Wrong credentials !!',
                            content: `Email or password is wrong, please enter correct one.. `,
                        });
                        modal.update;
                        console.error("error : " + error);
                    }

                }
            )
        }
    }


    handleLoginSubmit = e => {

        //console.log('test');
        //alert(this.state.email+" | "+this.state.password);


        if (this.state.cflag !== null) {

            this.loginAxiosAction("custom");

        } else {
            alert("check the captcha first...")
        }


        // this.props.dispatch(login());
        //  Router.push('/');
    };


    componentDidMount() {
        try {
            const { email } = Router.query;
            if (email) {
                this.setState({ email: email });
            }
        } catch (error) {
            console.log(error);
        }

        // Load Google Identity Services script
        if (!window.google) {
            const script = document.createElement('script');
            script.src = 'https://accounts.google.com/gsi/client';
            script.async = true;
            script.defer = true;
            script.onload = () => {
                setTimeout(() => {
                    this.initializeAndRenderGoogleButton();
                }, 100);
            };
            document.head.appendChild(script);
        } else {
            setTimeout(() => {
                this.initializeAndRenderGoogleButton();
            }, 100);
        }
    }

    initializeAndRenderGoogleButton = () => {
        const btnContainer = document.getElementById('google_login_btn');
        if (window.google && btnContainer) {
            window.google.accounts.id.initialize({
                client_id: process.env.NEXT_PUBLIC_GOOGLE_CLIENT_ID,
                callback: this.handleGoogleSignIn
            });

            // Measure container width and clamp to Google GSI bounds (200px - 400px)
            const containerWidth = btnContainer.offsetWidth || 370;
            const buttonWidth = Math.min(Math.max(containerWidth, 200), 400);

            // Render Google button with proper width in pixels
            window.google.accounts.id.renderButton(
                btnContainer,
                {
                    theme: 'outline',
                    size: 'large',
                    width: buttonWidth,
                    text: 'signin_with',
                    logo_alignment: 'left'
                }
            );
        }
    }



    render() {

        return (

            <div className="ps-my-account">
                <div className="container">
                    <Form
                        className="ps-form--account"
                        onFinish={this.handleLoginSubmit.bind(this)}>
                        <ul className="ps-tab-list">
                            <li className="active">
                                <Link href="/account/login">
                                    <a>Login</a>
                                </Link>
                            </li>
                            <li>
                                <Link href="/account/register">
                                    <a>Register</a>
                                </Link>
                            </li>
                        </ul>

                        <div className="ps-tab active" id="sign-in">
                            <div className="ps-form__content">
                                <h5>Log In Your Account</h5>

                                <div className="form-group">
                                    <Form.Item>
                                        <Input
                                            name="username"
                                            required
                                            className="form-control"
                                            type="text"
                                            value={this.state.email}
                                            placeholder="enter email address"
                                            onChange={(event) => { this.setState({ email: event.target.value }) }}
                                        />
                                    </Form.Item>
                                </div>
                                <div className="form-group form-forgot">
                                    <Form.Item
                                        name="password"
                                        rules={[
                                            {
                                                required: true,
                                                message:
                                                    'Please input your password!',
                                            },
                                        ]}>
                                        <Input
                                            className="form-control"
                                            type="password"
                                            placeholder="Password..."
                                            onChange={(event) => { this.setState({ password: event.target.value }) }}
                                        />
                                    </Form.Item>
                                </div>

                                {/* <ReCAPTCHA
                                    sitekey={process.env.NEXT_PUBLIC_GOOGLE_CAPTCHA_SITEKEY}
                                    onChange={this.onChange}
                                    size="normal"
                                /> */}

                                <MathCaptcha onInvalid={() => { this.setState({ cflag: null }); }} onSuccess={this.handleCaptchaSuccess} />
                                {/* <AlgebraicCaptcha onSuccess={handleCaptchaSuccess} /> */}

                                <div className="form-group submit mt-3">
                                    <button
                                        type="submit"
                                        className="ps-btn ps-btn--fullwidth">
                                        Login
                                    </button>
                                </div>

                                <div className='text-left mb-3'>
                                    <a href='https://nuvio.in/page/Forgote_password'>forgot password ?</a>
                                </div>

                                <div className='text-center mt-3 mb-3'>
                                    <p>OR</p>
                                </div>

                                <div className="form-group mb-2" style={{ display: 'flex', justifyContent: 'center', width: '100%' }}>
                                    <div id="google_login_btn" style={{ width: '100%', display: 'flex', justifyContent: 'center' }}></div>
                                </div>

                                {/* <div style={{ display: 'flex', justifyContent: 'center', marginTop: '10px', marginBottom: '30px', width: '100%' }}>
                                    <FacebookLogin
                                        appId="1029697068000890"
                                        autoLoad={false}
                                        fields="name,email,picture"
                                        callback={this.responseFacebook}
                                        icon="fa-facebook"
                                        cssClass="my-facebook-button-class"
                                    />
                                </div> */}
                            </div>
                            <div className="ps-form__footer">
                            </div>
                        </div>
                    </Form>
                </div>
            </div>
        );
    }
}
const mapStateToProps = state => {
    return state.auth;
};
export default connect(mapStateToProps)(Login);
