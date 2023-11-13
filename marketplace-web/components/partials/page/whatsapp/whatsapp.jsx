import React from 'react';


 const whatsapp = () =>{
    return(
        <a
        href={`https://wa.me/+91${process.env.NEXT_PUBLIC_WHATSAPP_NUMBER}`}
        class="whatsapp_float"
        target="_blank"
        rel="noopener noreferrer"
      >
        <i class="fa fa-whatsapp whatsapp-icon"></i>
      </a>

    )
}
export default whatsapp;
