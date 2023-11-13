import { Form, Input, Modal } from 'antd';
import axios from 'axios';
import { useRouter } from 'next/router';
import React, { useState } from 'react'
import { marketplaceUrl } from '~/repositories/Repository';

export default function ResetPasswordUi() {
    const Router = useRouter();
    const { verifivationId } = Router.query;
    const [password1, setPassword1] = useState(null);
    const [password2, setPassword2] = useState(null);
    const [action, setAction] = useState(false);

    if (verifivationId == null) {
        //Router.push("/");
        //window.location.assign("/")
    }
    const resetThePassword = () => {

        if (password1 === password2) {
            const data = {
                "verificationId": verifivationId,
                "password": password1
            };

            axios.post(`${marketplaceUrl}/resetPassword`, data).then(
                async (response) => {
                    if (response.status == 200) {
                        const modal = Modal.success({
                            centered: true,
                            title: 'Success',
                            content: `Your password has been set successfully!`,
                        });
                        modal.update;
                        setPassword1(null);
                        setPassword2(null);
                        setAction(true);

                    }
                },
                (error) => {
                    //order details is not save to database
                    const modal = Modal.error({
                        centered: true,
                        title: 'Failed',
                        content: `Password not changed, ${JSON.stringify(error.message)}`,
                    });
                    modal.update;
                    console.log("error : " + JSON.stringify(error));
                }
            )
        } else {
            alert("password not match");
        }
    }

    return (
        <div>
            <div className='container text-center'>
                <div className='col-12 col-md-6 offset-md-3 my-5'>
                    <div className="card p-4 mb-5">
                        <label className='text-left'>Reset Password </label>
                        <Form.Item
                            name="Email id"
                            rules={[
                                {
                                    required: true,
                                    message: 'Enter the strong password.',
                                },
                            ]}>
                            <Input
                                className="form-control"
                                type="password"
                                placeholder="enter new password"
                                name="email"
                                onChange={(event) => { setPassword1(event.target.value) }}
                            />

                        </Form.Item>
                        <label className='text-left'>Confirm password </label>
                        <Form.Item
                            name="Email id"
                            rules={[
                                {
                                    required: true,
                                    message: 'Confirm the password.',
                                },
                            ]}>
                            <Input
                                className="form-control"
                                type="password"
                                placeholder="confirm your password"
                                name="email"
                                onChange={(event) => { setPassword2(event.target.value) }}
                            />

                        </Form.Item>
                        <div className="text-right">
                            {action ?
                                (
                                    <button onClick={() => { Router.push("/account/login") }} className='btn btn-lg btn-warning'>Back to login</button>

                                )
                                :
                                (
                                    <button onClick={() => { resetThePassword() }} className='btn btn-lg btn-warning'>Save New Password</button>
                                )
                            }
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}
