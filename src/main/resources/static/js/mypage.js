$(function(){
	 var genre_list = document.getElementsByName('member_genre');
	
	for(let genre of genre_list){
	if(init_mem_genre_id_list.includes(parseInt(genre.value))){
        genre.checked="checked";
	  }
	}
	
	
})



function mypageUpdate(){
	var input_name = document.getElementById('input_name');
	var input_email = document.getElementById('input_email');
	var input_pw = document.getElementById('input_pw');
	var input_re_pw = document.getElementById('input_re_pw');
	var input_nickname = document.getElementById('input_nickname');
	var member_genre_List=[];
		for(var mem_genre of $('input[name=member_genre]:checked')){
			member_genre_List.push(mem_genre.value)
		}
	
	
	
	
	if(input_name.value.length<2){
		alert('이름을 2자 이상 입력해주세요');
	}else if(input_email.value.length<2){
		alert('입력 하신 이메일을 다시 확인해주세요.');
	}else if(input_pw.value.length<4){
		alert('비밀번호는 4자 이상입니다.');
	}else if(input_re_pw.value!=input_pw.value){
		alert('비밀번호가 일치하는지 확인해주세요.');
	}else if(input_nickname.value.length<2){
		alert('닉네임은 2자 이상입니다.');
	}else if(member_genre_List.length<2){
		alert('선호하는 장르를 1개 이상 선택해야됩니다.');
	}else{
		fetch('/mypage/update',{
				method:'PUT',
				headers:{"Content-Type":"application/json"},
				body:JSON.stringify({
					name:input_name.value,
					email:input_email.value,
					password:input_pw.value,
					nickname:input_nickname.value,
					member_genre:member_genre_List
				})
			}).then(response => response.text())
			  .then(data => {
				  if(data=="success"){
	                alert('회원정보수정이 완료되었습니다.');
	                location.reload();
	             }else if(data=="fail"){
					alert('회원정보수정에 실패했습니다. 다시 시도해주세요');
				 }
			  }).catch(error=>{
				  alert('오류가 발생했습니다.');
			  })
	}
}
	
	



