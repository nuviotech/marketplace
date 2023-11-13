import { useRouter } from 'next/router'
import React, { useEffect, useState } from 'react'
import ProductRepository from '~/repositories/ProductRepository';
import ProductDefaultPage from './[pid]';


export default function productDetails() {

  const router=useRouter();
  const {pid} = router.query;
  const [responseData,setResponseData]=useState(null);

  useEffect(async()=>{
    if(pid!=null && pid!='' && pid!=='undefined'){
      //alert(pid);
      const responseData = await ProductRepository.getProductsById(pid);
      if(responseData){
        //alert(JSON.stringify(responseData))
        setResponseData(responseData);
      }else{
        alert("product not found!!")
      }
    }
  },[pid])

  return (
    <div>
      <ProductDefaultPage responseData={responseData} pid={pid}/>
    </div>
  )
}


export async function getStaticProps({params}) {
  
 // const responseData = await ProductRepository.getProductsById(params.pid);
 //const pid=params.pid;
 //console.log("!@#$ ::::: "+pid[1])
 console.log(":::: "+params);
 const responseData =null// await ProductRepository.getProductsById(pid[1]+"");
  return {
      props: {
          responseData
      }
  }
}
