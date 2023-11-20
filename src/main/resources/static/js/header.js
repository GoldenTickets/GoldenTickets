


    $(function(){
      $('#header').load("header.html");

      $('.hiddensearch').hide();//초기화면에서 검색창 hide

      $('#svciconsearch').on("click",function(){
        $('.hiddensearch').fadeToggle();
      })
    })
    
    
//1.로그인버튼
const offCanvasloginButton = document.getElementById('offCanvasloginButton');
//content-type을 설정하니까 오히려 오류생김.Content-Type 헤더에 안넣었더니 정상작동. fetch가 알아서 해줌.
if (offCanvasloginButton) {
    offCanvasloginButton.addEventListener('click', event => {
		var emaildata=document.getElementById('offcanvas-id').value;
		var passworddata=document.getElementById('offcanvas-password').value;
		
		var formData=new FormData();
		formData.append('email',emaildata)
		formData.append('password',passworddata)
		
        fetch('/login', {
            method:'POST',
            body:formData 
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
    });
}

//2.로그아웃버튼
const offCanvaslogoutButton = document.getElementById('offCanvaslogoutButton');
//content-type을 설정하니까 오히려 오류생김.Content-Type 헤더에 안넣었더니 정상작동. fetch가 알아서 해줌.
if (offCanvaslogoutButton) {
    offCanvaslogoutButton.addEventListener('click', event => {
		
        fetch('/logout', {
            method:'GET' 
        })
            .then(response => {
				if(response.ok){
	                alert('로그아웃이 완료되었습니다.');
	                location.reload();
	             }else{
					alert('로그아웃이 실패했습니다.'); 
					location.reload();
				 }
            });
    });
}