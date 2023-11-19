


    $(function(){
      $('#header').load("header.html");

      $('.hiddensearch').hide();//초기화면에서 검색창 hide

      $('#svciconsearch').on("click",function(){
        $('.hiddensearch').fadeToggle();
      })
    })
    
    
const offCanvasloginButton = document.getElementById('offCanvasloginButton');
//content-type을 json이 아니라 x-www 로 설정. form에서 제출과 같은방식. Controller 에서 @RequestParam로 매개변수 전달받음
if (offCanvasloginButton) {
    offCanvasloginButton.addEventListener('click', event => {
		var emaildata=document.getElementById('offcanvas-id').value;
		var passworddata=document.getElementById('offcanvas-password').value;
		
		formData.append('email',emaildata)
		formData.append('password',passworddata	)
		
        fetch('/login', {
            method: 'POST',
            headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
            },
            body:formData 
        })
            .then(() => {
                alert('로그인이 완료되었습니다.');
                location.replace('/main');
            });
    });
}