import React from 'react';

import ReactWhatsapp from 'react-whatsapp';
import { WhatsappIcon } from 'react-share';

 const whatsapp = () =>{
    return(
        <a
        href={`https://wa.me/+91${process.env.NEXT_PUBLIC_WHATSAPP_NUMBER}`}
        class="whatsapp_float"
        target="_blank"
        rel="noopener noreferrer"
      >
        <ReactWhatsapp number={`91${process?.env?.NEXT_PUBLIC_WHATSAPP_NUMBER}`} message="Hi" element={"div"} >
            <WhatsappIcon size={35} round="true"></WhatsappIcon>
        </ReactWhatsapp>
                                            
        <i class="fa fa-whatsapp whatsapp-icon"></i>
      </a>

    )
}
export default whatsapp;
