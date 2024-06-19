import { Form, Input, Modal } from 'antd';
import Axios from 'axios';
import React, { useState } from 'react';
import ReCAPTCHA from 'react-google-recaptcha';
import { marketplaceUrl } from '~/repositories/Repository';

const Report_abuse = () => {
    const [cFlag, setCflag] = useState(null);
    const [file, setFile] = useState(null);
    var initialForm={
        'name': '',
        'email': '',
        'subject': '',
        'natureOfAbuse': '',
        'incidentDtTime': '',
        'desc': '',
        'actionRequested': ''
    }
    const [state, setState] = useState(initialForm);


    const onChangeCatcha = (value) => {
        setCflag(value);
    }

    const handleFileChange = (event) => {
        setFile(event.target.files[0]);
    };


    const handleReportAbuseForm = async (event) => {
        event.preventDefault();
        const obj = state;
        const urlRegex = /(https?:\/\/[^\s]+)/gi;

        if (urlRegex.test(obj.name) || obj?.name?.length == 0) {
            Modal.error({
                centered: true,
                title: 'Invalid input!',
                content: `Please enter valid name.`,
            });
        } else if (urlRegex.test(obj.email) || obj?.email?.length == 0) {
            Modal.error({
                centered: true,
                title: 'Invalid input!',
                content: `Please enter valid email.`,
            });
        } else if (urlRegex.test(obj.subject) || obj?.subject?.length == 0) {
            Modal.error({
                centered: true,
                title: 'Invalid input!',
                content: `Please enter subject.`,
            });
        } else if (urlRegex.test(obj.natureOfAbuse) || obj?.natureOfAbuse?.length == 0) {
            Modal.error({
                centered: true,
                title: 'Invalid input!',
                content: `Please enter nature of abuse.`,
            });
        } else if (urlRegex.test(obj.desc) || obj?.desc?.length == 0) {
            Modal.error({
                centered: true,
                title: 'Invalid input!',
                content: `Please enter related description...`,
            });
        } else if (urlRegex.test(obj.actionRequested) || obj?.actionRequested?.length == 0) {
            Modal.error({
                centered: true,
                title: 'Invalid input!',
                content: `Please enter requested action`,
            });
        } else if (cFlag == null) {
            Modal.error({
                centered: true,
                title: 'Captcha not check !!',
                content: `Check the captha first`,
            });
        } else {
            const formData = new FormData();
            formData.append('file', file);
            formData.append("reportData",JSON.stringify(obj));

            try {
                const response = await Axios.post(marketplaceUrl + "/saveReportAbuseData", formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data',
                    },
                });
                setState(initialForm);
                if (response.data.status == 0) {
                    Modal.success({
                        centered: true,
                        title: 'Report Save.',
                        content: "your report is save successfully.",

                    });
                    setFile(null); 
                }
            } catch (error) {
                console.error('Error report abuse', error);
            }
        }
    }


    return (
        <div className="ps-section--custom">
            <div className="container">
                <div className="ps-section__header text-center">
                    <h2>Please use the form below to report any abuse or suspicious activity on our platform. Your report helps us maintain a safe and secure environment for all users.</h2>
                </div>
                <div className="ps-section__content row">

                    <div className="col-sm-12 col-md-6 offset-md-3 ps-form__content card p-5">
                        <h4 className='text-muted'>Report Abuse</h4>
                        <form
                            className="ps-form__billing-info"
                            onSubmit={(event) => { handleReportAbuseForm(event) }}>

                            <div className="form-group">
                                <Input
                                    required
                                    value={state.name}
                                    className="form-control"
                                    type="text"
                                    placeholder="Name *"
                                    name="name"
                                    onChange={(e) => { setState({...state, name: e.target.value }) }}
                                />
                            </div>

                            <div className="form-group">
                                <Input
                                    required
                                    value={state.email}
                                    className="form-control"
                                    type="email"
                                    placeholder="Email *"
                                    name='email'
                                    onChange={(e) => { setState({ ...state,email: e.target.value }) }}
                                />
                            </div>

                            <div className="form-group">
                                <Input
                                    required
                                    value={state.subject}
                                    className="form-control"
                                    type="text"
                                    placeholder="Subject *"
                                    name="subject"
                                    onChange={(e) => { setState({...state,subject: e.target.value }) }}
                                />
                            </div>

                            <div className="form-group">
                                <Input
                                    required
                                    value={state.natureOfAbuse}
                                    className="form-control"
                                    type="text"
                                    placeholder="Nature of abuse *"
                                    name="natureOfAbuse"
                                    onChange={(e) => { setState({...state, natureOfAbuse: e.target.value }) }}
                                />
                            </div>

                            <div className="form-group">
                                <Input
                                    required
                                    value={state.incidentDtTime}
                                    className="form-control"
                                    type='datetime-local'
                                    placeholder="Date and Time of Incident *"
                                    name="incidentDtTime"
                                    onChange={(e) => { setState({ ...state,incidentDtTime: e.target.value }) }}
                                />
                            </div>

                            <div className="form-group">
                                <input
                                    className="form-control"
                                    type="file"
                                    onChange={handleFileChange}
                                    placeholder="Upload Evidence"
                                    name="evidenceFile"
                                />
                            </div>

                            <div className="form-group">
                                <Input
                                    value={state.actionRequested}
                                    required
                                    className="form-control"
                                    type="text"
                                    placeholder="Action Requested *"
                                    name="actionRequested"
                                    onChange={(e) => { setState({ ...state,actionRequested: e.target.value }) }}
                                />
                            </div>

                            <div className="form-group">
                                <textarea
                                    className='form-control'
                                    value={state.desc}
                                    required rows={3} placeholder="Description of Incident *"
                                    name="desc"
                                    onChange={(e) => { setState({...state, desc: e.target.value }) }}
                                ></textarea>
                            </div>

                            <ReCAPTCHA
                                sitekey={process.env.NEXT_PUBLIC_GOOGLE_CAPTCHA_SITEKEY}
                                onChange={(e) => { onChangeCatcha(e) }}
                                size="normal"
                            />
                            <div className="form-group submit">
                                <button
                                    type="submit"
                                    className="ps-btn ps-btn--fullwidth">
                                    submit
                                </button>
                            </div>
                        </form>

                    </div>
                </div>
            </div>
        </div>
    )
};

export default Report_abuse;
