

// 수정 기능
const modifyButton = document.getElementById('modifycomplete-btn');

if (modifyButton) {
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        body = JSON.stringify({
			category_id: $('input[name=category]:checked').val(),
            title: $('#'),
            content: document.getElementById('acontent').value
        })

        function success() {
            alert('수정 완료되었습니다.');
            location.replace(`/articles/${id}`);
        }

        function fail() {
            alert('수정 실패했습니다.');
            location.replace(`/articles/${id}`);
        }

        httpRequest('PUT',`/api/articles/${id}`, body, success, fail);
    });
}


// 쿠키를 가져오는 함수
function getCookie(key) {
    var result = null;
    var cookie = document.cookie.split(';');
    cookie.some(function (item) {
        item = item.replace(' ', '');

        var dic = item.split('=');

        if (key === dic[0]) {
            result = dic[1];
            return true;
        }
    });

    return result;
}

// HTTP 요청을 보내는 함수
function httpRequest(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: { // 로컬 스토리지에서 액세스 토큰 값을 가져와 헤더에 추가
            Authorization: 'Bearer ' + localStorage.getItem('access_token'),
            'Content-Type': 'application/json',
        },
        body: body,
    }).then(response => {
        if (response.status === 200 || response.status === 201) {
            return success();
        }
        const refresh_token = getCookie('refresh_token');
        if (response.status === 401 && refresh_token) {
            fetch('/api/token', {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    refreshToken: getCookie('refresh_token'),
                }),
            })
                .then(res => {
                    if (res.ok) {
                        return res.json();
                    }
                })
                .then(result => { // 재발급이 성공하면 로컬 스토리지값을 새로운 액세스 토큰으로 교체
                    localStorage.setItem('access_token', result.accessToken);
                    httpRequest(method, url, body, success, fail);
                })
                .catch(error => fail());
        } else {
            return fail();
        }
    });

}


//페이지 갯수 변경
function selectpageSizeChange(){
	selectedValue=document.getElementById('pageSizeChange').value;
	pageCategoryFromParam=document.getElementById('pageCategoryValue').value;
	//pageSizeFromParam=document.getElementById('pageSizeValue').value;
	location.href="/board?category="+pageCategoryFromParam+"&pagesize="+selectedValue;
}


//게시판 글쓰기 검증 
function writeValidate(){
	
	var categoryValue = $('input[name=category]:checked').val();
	var titleValue = $('#title').val();
	var contentValue = $('#content').val();
	
	if(categoryValue==null){
		alert('카테고리를 선택해주세요.');
	}else if(titleValue.length<1){
		alert('제목을 입력해주세요.');
	}else if(contentValue.length<1){
		alert('내용을 입력해주세요.');
	}else{
		let askbeforewrite = confirm("등록 하시겠습니까?");
		if(askbeforewrite){
			fetch('/board/write',{
			    method:'POST',
			    headers:{"Content-Type":"application/json"}
			    ,body:JSON.stringify({
			        category_id:categoryValue,
			        title:titleValue,
			        content:contentValue
			    })
			}).then(response => response.text())
	  		.then(data => {
	     		 if(data=="success"){
	          		alert('등록되었습니다.');
	          		location.href="/board";
	      		 }else if(data=="needLogin"){
	          		alert('로그인이 필요합니다.');
	          		document.getElementById('offCanvasToggleButton').click();
	      		 }
	  		})
		}
	}
}

//게시판 검증 후 수정 
function updateArticle(){
	
	var categoryValue = $('input[name=category]:checked').val();
	var titleValue = $('#title').val();
	var contentValue = $('#content').val();
	
	if(categoryValue==null){
		alert('카테고리를 선택해주세요.');
	}else if(titleValue.length<1){
		alert('제목을 입력해주세요.');
	}else if(contentValue.length<1){
		alert('내용을 입력해주세요.');
	}else{
		let askbeforewrite = confirm("수정 하시겠습니까?");
		if(askbeforewrite){
			fetch('/board/update/'+$('#article_id').val(),{
			    method:'PUT',
			    headers:{"Content-Type":"application/json"}
			    ,body:JSON.stringify({
			        category_id:categoryValue,
			        title:titleValue,
			        content:contentValue
			    })
			}).then(response => response.text())
	  		.then(data => {
	     		 if(data=="success"){
	          		alert('수정되었습니다.');
	          		location.href="/board/"+$('#article_id').val();
	      		 }else if(data=="needLogin"){
	          		alert('로그인이 필요합니다.');
	          		document.getElementById('offCanvasToggleButton').click();
	      		 }else{
					alert('수정 실패했습니다.'); 
				 }
	  		})
		}
	}
}

//게시판 글삭제
function delete_article(){
		let askbeforedelete = confirm("삭제 하시겠습니까?");
		if(askbeforedelete){
			fetch('/board/delete/'+$('#article_id').val(),{
			    method:'DELETE'
			}).then(response => response.text())
	  		.then(data => {
	     		 if(data=="success"){
	          		alert('삭제되었습니다.');
	          		location.href="/board"
	      		 }else if(data=="needLogin"){
	          		alert('로그인이 필요합니다.');
	          		document.getElementById('offCanvasToggleButton').click();
	      		 }
	  		})
  		}
}


//게시판 댓글쓰기 검증
function replyValidate(){
	
	let contentValue = $('#replyTextArea').val();
	
	if(contentValue.length<1){
		alert('내용을 입력해주세요.');
	}else{
		fetch('/board/replywrite',{
		    method:'POST',
		    headers:{"Content-Type":"application/json"}
		    ,body:JSON.stringify({
				article_id:$('#article_id').val()
				,content:contentValue
		    })
		}).then(response => response.text())
  		.then(data => {
     		 if(data=="success"){
          		alert('등록되었습니다.');
          		location.reload();
      		 }else if(data=="needLogin"){
          		alert('로그인이 필요합니다.');
          		document.getElementById('offCanvasToggleButton').click();
      		 }
  		})
	}
}

//게시판 댓글글삭제
function delete_reply(){
		let askbeforedelete = confirm("삭제 하시겠습니까?");
		if(askbeforedelete){
			fetch('/board/deleteReply/'+$('#reply_id').val(),{
			    method:'DELETE'
			}).then(response => response.text())
	  		.then(data => {
	     		 if(data=="success"){
	          		alert('삭제되었습니다.');
	          		location.href="/board"
	      		 }else if(data=="needLogin"){
	          		alert('로그인이 필요합니다.');
	          		document.getElementById('offCanvasToggleButton').click();
	      		 }
	  		})
  		}
}

