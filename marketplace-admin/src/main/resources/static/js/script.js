
let captchaValue="";
		(function(){
			
			const fonts=["cursive","sans-serif","serif","monospace"];
			
			function generateCaptcha(){
				let value=btoa(Math.random()*1000000000);
				value=value.substr(0,5+Math.random()*1);
				captchaValue=value;
			}
			
			function setCaptcha(){
				let html=captchaValue.split("").map((char)=>{
					const rotate= -20 +Math.trunc(Math.random()*30);
					const font = Math.trunc(Math.random()*fonts.length);
					return `<span
						style="transform:rotate(${rotate}deg);
							font-family:${fonts[font]}
						">${char}</span>
					`;
				}).join("");
				document.getElementById("captchaPreview").innerHTML = html;
				document.getElementById("captchaGeneratedValue").value = captchaValue;
			//	document.querySelector("#captchaPreview").innerHTML = html;
			}
			
			function initCaptcha(){
				document.querySelector("#captchaPreviewRefresh").addEventListener("click",function(){
				generateCaptcha();
				setCaptcha();
				});
				generateCaptcha();
				setCaptcha();
				
			}
			
			initCaptcha();
			
		})();
	

function addUsername(){
	var email=document.getElementById("email").value;
	var type=document.getElementById("type").value;
	var userEnterCaptch= document.getElementById("enteredCaptcha").value;
	if(captchaValue != userEnterCaptch){
		swal("Invalid Captcha !!", "Wrong captcha entered, please try with correct one.", "error");
		return false;
	}
	document.getElementById("username").value = email+"&"+type;
	return true;
}