import React from 'react';
import { useState } from 'react';
import Whatsapp from './whatsapp/whatsapp';
const ContactForm = () => {
    const [email,setEmail] = useState('')
    const [name,setName] = useState('')
    const [subject,setSubject] = useState('')
    const [message,setMessage] = useState('')

    function sendEmail() {
        Email.send({
          Host: "smtp.gmail.com",
          Username: "rupesh.nuvio@gmail.com",
          Password: "kqexwurbnbuxkgwb",
          To: 'rnk0775@gmail.com',
          From: "rupesh.nuvio@gmail.com",
          Subject: "Sending Email testing",
          Body: "Well that was easy!!",
        })
          .then(function (message) {
            //alert("mail sent successfully "+message)
          });
      }

    return (
        <div className="ps-contact-form">
            <div className="container">
                
                    <h3>Get In Touch</h3>
                    <div className="row">
                        <div className="col-xl-6 col-lg-6 col-md-6 col-sm-12 col-12 ">
                            <div className="form-group">
                                <input
                                    className="form-control"
                                    type="text"
                                    placeholder="Name *"
                                />
                            </div>
                        </div>
                        <div className="col-xl-6 col-lg-6 col-md-6 col-sm-12 col-12 ">
                            <div className="form-group">
                                <input
                                    className="form-control"
                                    type="text"
                                    placeholder="Email *"
                                />
                            </div>
                        </div>
                        <div className="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12 ">
                            <div className="form-group">
                                <input
                                    className="form-control"
                                    type="text"
                                    placeholder="Subject *"
                                />
                            </div>
                        </div>
                        <div className="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12 ">
                            <div className="form-group">
                                <textarea
                                    className="form-control"
                                    rows="5"
                                    placeholder="Message"></textarea>
                            </div>
                        </div>
                    </div>
                    <div className="form-group submit">
                        <button onClick={()=>{sendEmail()}} className="ps-btn">Send message</button>
                    </div>
        
                <Whatsapp />
            </div>
        </div>
    );
};

export default ContactForm;
