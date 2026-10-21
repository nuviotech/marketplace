import React, { Component } from 'react';
import Link from 'next/link';
import Router from 'next/router';
import axios from 'axios';
import { login, saveToken } from '../../../store/auth/action';
import { marketplaceUrl } from '~/repositories/Repository';

import { Checkbox, Form, Input, Modal, Button } from 'antd';
import { connect } from 'react-redux';
import { saveUserDetails } from '~/repositories/UserDeatils';
import { MathCaptcha } from '../commons/MathCaptcha';

class Register extends Component {


    constructor(props) {
        super(props);
        this.state = {
            firstName: null,
            lastName: null,
            email: null,
            password: null,
            password2: null,
            phone: null,
            reportingAccountId: this?.props?.affiliate_account_id || 0,
            isCheckTermAndConditions: false,
            cflag: null,
            googleLoading: false
        };
    }

    onChangeCatcha = value => {
        this.setState({ cflag: value });
    }

    handleCaptchaSuccess = ()=>{
        this.setState({ cflag: true });
    }

    // Handle Google Sign-Up response
    handleGoogleSignUp = (response) => {
        if (response.credential) {
            this.setState({ googleLoading: true });
            
            // Preserve affiliate_account_id from URL
            const affiliateId = this?.props?.affiliate_account_id || 0;
            
            axios.post(`${marketplaceUrl}/googleLogin`, {
                credential: response.credential,
                affiliate_account_id: affiliateId
            }).then(
                (response) => {
                    var token = response.data.Token;
                    var status = response.data.status;
                    if (status == 0) {
                        saveToken(token, "google_account");
                        // Redirect to home or checkout
                        var action;
                        if (typeof window !== 'undefined') {
                            action = sessionStorage.getItem("action")
                        }
                        if (action == "checkout") {
                            Router.push('/account/checkout')
                            sessionStorage.removeItem("action");
                        } else {
                            window.location.assign('/');
                        }
                    } else {
                        const modal = Modal.error({
                            centered: true,
                            title: 'Google Sign-Up Failed',
                            content: response.data.message || 'Unable to sign up with Google',
                        });
                    }
                    this.setState({ googleLoading: false });
                },
                (error) => {
                    const modal = Modal.error({
                        centered: true,
                        title: 'Google Sign-Up Error',
                        content: 'An error occurred during Google sign-up',
                    });
                    console.error("Google signup error: " + error);
                    this.setState({ googleLoading: false });
                }
            );
        }
    }

    componentDidMount() {
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
        const btnContainer = document.getElementById('google_signup_btn');
        if (window.google && btnContainer) {
            window.google.accounts.id.initialize({
                client_id: process.env.NEXT_PUBLIC_GOOGLE_CLIENT_ID,
                callback: this.handleGoogleSignUp
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
                    text: 'signup_with',
                    logo_alignment: 'left'
                }
            );
        }
    }

    onCheckBoxChange = (e) => {
        this.setState({ isCheckTermAndConditions: e.target.checked })
    };

    handleSubmit = async (e) => {
        // e.preventDefault();
        //  console.log(JSON.stringify(this.state))
        const urlRegex = /(https?:\/\/[^\s]+)/gi;

        if (this.state.firstName == '' || this.state.lastName == '' || urlRegex.test(this.state.firstName)) {
            const modal = Modal.error({
                centered: true,
                title: 'Invalid input!',
                content: `Please enter first name or last name.`,
            });
            modal.update;
        } else if (this.state.phone.length < 10 || this.state.phone.length > 10 || urlRegex.test(this.state.phone)) {
            const modal = Modal.error({
                centered: true,
                title: 'Invalid input!',
                content: `Please enter correct phone number.`,
            });
        } else if (this.state.password.length < 4 || this.state.password.length > 20 || urlRegex.test(this.state.password)) {
            const modal = Modal.error({
                centered: true,
                title: 'Invalid password!',
                content: `password must be greater than 4 character or less than 20 character.`,
            });
            modal.update;
        } else if (this.state.password != this.state.password2) {
            const modal = Modal.error({
                centered: true,
                title: 'Wrong confirm password!',
                content: `enter the same password in password field or confirm password field.`,
            });
            modal.update;
        } else if (!this.state.isCheckTermAndConditions) {
            const modal = Modal.error({
                centered: true,
                title: 'Accept terms and conditions',
                content: `Accept the terms and condtions .`,
            });
            modal.update;
        }else if(this.state.cflag==null){
            const modal = Modal.error({
                centered: true,
                title: 'Solve the math captcha !!',
                content: `Please solve the puzzle before submitting.`,
            });
            modal.update;
        } else {
            // Ensure reportingAccountId is set from URL if present
            const affiliateId = this?.props?.affiliate_account_id || 0;
            const stateToSave = { ...this.state, reportingAccountId: affiliateId };
            saveUserDetails(stateToSave, "", Router);
        }

        this.props.form?.validateFields((err, values) => {
            if (!err) {
                this.props.dispatch(login());
                Router.push('/account/login');
            } else {
            }
        });
    };

    render() {

        return (

            <div className="ps-my-account">
                <div className="container">
                    <Form
                        className="ps-form--account"
                        onFinish={this.handleSubmit.bind(this)}>
                        <ul className="ps-tab-list">
                            <li>
                                <Link href="/account/login">
                                    <a>Login</a>
                                </Link>
                            </li>
                            <li className="active">
                                <Link href="/account/register">
                                    <a>Register</a>
                                </Link>
                            </li>
                        </ul>
                        <div className="ps-tab active" id="register">
                            <div className="ps-form__content">
                                <h5>Register An Account</h5>

                                <div className="row">
                                    <div className="col-sm-6">
                                        <div className="form-group">
                                            <Form.Item
                                                name="firstName"
                                                rules={[
                                                    {
                                                        required: true,
                                                        message: 'first name is not valid !!',
                                                        pattern: new RegExp(/^[a-zA-Z '.-]*$/)
                                                    },
                                                ]}>
                                                <Input
                                                    className="form-control"
                                                    type="text"
                                                    placeholder="First Name"
                                                    name="fname"
                                                    onChange={(event) => { this.setState({ firstName: event.target.value }) }}

                                                />
                                            </Form.Item>
                                        </div>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="form-group">
                                            <Form.Item
                                                name="lastName"
                                                rules={[
                                                    {
                                                        required: true,
                                                        message: 'last name is not valid !!',
                                                        pattern: new RegExp(/^[a-zA-Z '.-]*$/)
                                                    },
                                                ]}>
                                                <Input
                                                    className="form-control"
                                                    type="text"
                                                    placeholder="Last Name"
                                                    name="last_name"
                                                    onChange={(event) => { this.setState({ lastName: event.target.value }) }}

                                                />
                                            </Form.Item>
                                        </div>
                                    </div>
                                </div>


                                <div className="form-group">
                                    <Form.Item
                                        name="email"
                                        rules={[
                                            {
                                                required: true,
                                                message: 'email address is not valid !!',
                                                pattern: new RegExp("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
                                            },
                                        ]}>
                                        <Input
                                            className="form-control"
                                            type="email"
                                            placeholder="Email id"
                                            onChange={(event) => { this.setState({ email: event.target.value }) }}

                                        />
                                    </Form.Item>
                                </div>
                                <div className="form-group">
                                    <Form.Item
                                        name="phone"
                                        rules={[
                                            {
                                                required: true,
                                                message:
                                                    'invalid contact number !!',
                                                pattern: new RegExp("^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}$")

                                            },
                                        ]}>
                                        <Input
                                            className="form-control"
                                            type="text"
                                            placeholder="Enter contact"
                                            onChange={(event) => { this.setState({ phone: event.target.value }) }}

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
                                <div className="form-group form-forgot">
                                    <Form.Item
                                        name="password2"
                                        rules={[
                                            {
                                                required: true,
                                                message:
                                                    'Please repeat your password!',
                                            },
                                        ]}>
                                        <Input
                                            className="form-control"
                                            type="password"
                                            placeholder="repeat password.."
                                            id='password2'
                                            onChange={(event) => { this.setState({ password2: event.target.value }) }}

                                        />
                                    </Form.Item>
                                </div>


                                {/* <ReCAPTCHA
                                    sitekey={process.env.NEXT_PUBLIC_GOOGLE_CAPTCHA_SITEKEY}
                                    onChange={this.onChangeCatcha}
                                    size="normal"
                                /> */}

                                <MathCaptcha onInvalid={()=>{this.setState({ cflag: null });}} onSuccess={this.handleCaptchaSuccess} />

                                <div className="form-group submit">
                                    <button
                                        type="submit"
                                        className="ps-btn ps-btn--fullwidth">
                                        Register
                                    </button>
                                </div>

                                <Checkbox className='my-2' onChange={this.onCheckBoxChange}>
                                    Accept Terms And Conditions
                                    <Link href="/page/terms_and_conditions">
                                        <span style={{ color: "blue" }}> Read</span>
                                    </Link>
                                </Checkbox>

                                <div className='text-center mt-3 mb-3'>
                                    <p>OR</p>
                                </div>

                                <div className="form-group mb-3" style={{ display: 'flex', justifyContent: 'center', width: '100%' }}>
                                    <div id="google_signup_btn" style={{ width: '100%', display: 'flex', justifyContent: 'center' }}></div>
                                </div>
                            </div>
                            {/*<div className="ps-form__footer">
                                <p>Connect with:</p>
                                <ul className="ps-list--social">
                                    <li>
                                        <a className="facebook" href="#">
                                            <i className="fa fa-facebook"></i>
                                        </a>
                                    </li>
                                    <li>
                                        <a className="google" href="#">
                                            <i className="fa fa-google-plus"></i>
                                        </a>
                                    </li>
                                    <li>
                                        <a className="twitter" href="#">
                                            <i className="fa fa-twitter"></i>
                                        </a>
                                    </li>
                                    <li>
                                        <a className="instagram" href="#">
                                            <i className="fa fa-instagram"></i>
                                        </a>
                                    </li>
                                </ul>
                             </div>*/}
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
export default connect(mapStateToProps)(Register);
