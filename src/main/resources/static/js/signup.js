//로그인페이지
function loginIdCheck(){
	var inputid = document.getElementById('loginidinput');
	var inputpw = document.getElementById('loginpwinput');
	if(inputid.value.length<1){
		alert('이메일을 다시 확인해주세요');
	}else if(inputpw.value.length<4){
		alert('비밀번호를 다시 확인해주세요');
	}else{
		var loginformdata=new FormData();
		loginformdata.append('email',inputid.value);
		loginformdata.append('password',inputpw.value);
		fetch('/login', {
            method:'POST',
            body:loginformdata 
        })
            .then(response => {
				if(response.ok){
	                alert('로그인이 완료되었습니다.');
	                location.reload();
	             }else{
					alert('로그인이 실패했습니다.'); 
					location.reload();
				 }
            });
		 }//else
		}
	


//회원가입페이지 
function namecheck(){
	if($('input[name=name]').val().length<2){
		$('input[name=name]').removeClass("inputSuccess");
		$('input[name=name]').addClass("inputfail");
		$('input[name=name]').next().css('display','block');
		$('input[name=name]').css('border','1px solid red');
	}else{
		$('input[name=name]').removeClass("inputfail");
		$('input[name=name]').addClass("inputSuccess");
		$('input[name=name]').next().css('display','none');
		$('input[name=name]').css('border','1px solid green');
	}
}

function emailcheck(){
	if($('input[name=email]').val().length<1){
		$('input[name=email]').removeClass("inputSuccess");
		$('input[name=email]').addClass("inputfail");
		$('input[name=email]').next().css('display','block');
		$('input[name=email]').css('border','1px solid red')
	}else{
		$('input[name=email]').removeClass("inputfail");
		$('input[name=email]').addClass("inputSuccess");
		$('input[name=email]').next().css('display','none');
		$('input[name=email]').css('border','1px solid green');
	}
	
}
function passwordcheck(){
		if($('input[name=password]').val().length<4){
		$('input[name=password]').removeClass("inputSuccess");
		$('input[name=password]').addClass("inputfail");
		$('input[name=password]').next().css('display','block');
		$('input[name=password]').css('border','1px solid red');
	}else{
		$('input[name=password]').removeClass("inputfail");
		$('input[name=password]').addClass("inputSuccess");
		$('input[name=password]').next().css('display','none');
		$('input[name=password]').css('border','1px solid green');
	}
	
}
function repasswordcheck(){
		if($('input[name=repassword]').val()!=$('input[name=password]').val()){
		$('input[name=repassword]').removeClass("inputSuccess");
		$('input[name=repassword]').addClass("inputfail");
		$('input[name=repassword]').next().css('display','block');
		$('input[name=repassword]').css('border','1px solid red')
	}else{
		$('input[name=repassword]').removeClass("inputfail");
		$('input[name=repassword]').addClass("inputSuccess");
		$('input[name=repassword]').next().css('display','none');
		$('input[name=repassword]').css('border','1px solid green');
	}
	
}
function nicknamecheck(){
		if($('input[name=nickname]').val().length<2){
		$('input[name=nickname]').removeClass("inputSuccess");
		$('input[name=nickname]').addClass("inputfail");
		$('input[name=nickname]').next().css('display','block');
		$('input[name=nickname]').css('border','1px solid red')
	}else{
		$('input[name=nickname]').removeClass("inputfail");
		$('input[name=nickname]').addClass("inputSuccess");
		$('input[name=nickname]').next().css('display','none');
		$('input[name=nickname]').css('border','1px solid green');
	}
	
}

// 회원가입
const signupButton = document.getElementById('signup-btn');

if (signupButton) {
    signupButton.addEventListener('click', event => {
		
		var nameValue=$('input[name=name]');
		var emailValue=$('input[name=email]');
		var passwordValue=$('input[name=password]');
		var repasswordValue=$('input[name=repassword]');
		var nicknameValue=$('input[name=nickname]');
		var mem_genreList=[];
		for (genre of $('input[name=member_genre]:checked')){
			mem_genreList.push(genre.value);
		}
		
		if(nameValue.val().length<1){
		    
			alert('이름을 확인해주세요');
		}else if(emailValue.val().length<1){
			
			alert('이메일을 확인해주세요');
		}else if(passwordValue.val().length<4){
		
			alert('비밀번호를 확인해주세요');
		}else if(repasswordValue.val()!=passwordValue.val()){
		
			alert('비밀번호가 일치하는지 확인해주세요');
		}else if(nicknameValue.val().length<2){
			alert('닉네임을 확인해주세요');
		}else if(mem_genreList.length<1){
			alert('선호하는 장르를 1개 이상 선택해주세요');
		}else{
		
        fetch('/signup', {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                name:nameValue.val(),
                email:emailValue.val(),
                password:passwordValue.val(),
                nickname:nicknameValue.val(),
                member_genre:mem_genreList
            })
        })
            .then(() => {
                alert('회원가입이 완료되었습니다.');
                location.replace('login');
            });
        }
    });
    
}

