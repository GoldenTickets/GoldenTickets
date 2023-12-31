
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
// 검색기능
const searchButton = document.getElementById('search-btn');

if (searchButton) {
    searchButton.addEventListener('click', event => {
			if(document.getElementsByName('keyword')[1].value<1){
				event.preventDefault();
				alert('검색어를 입력해주세요.');
				this.focus();
			}else{
		        body = JSON.stringify({
					subject: document.getElementById('subject').value,
		            keyword: document.getElementById('keyword').value
		        })
		
		        function success() {
		            location.replace(`/articles/search`);
		        }
		
		        function fail() {
		            location.replace(`/articles/${id}`);
		        }
		
		        httpRequest('PUT',`/articles/${id}`, body, success, fail);
		    }
		   });
}

//페이지 갯수 변경
function selectpageSizeChange(){
	selectedValue=document.getElementById('pageSizeChange').value;
	pageCategoryFromParam=document.getElementById('pageCategoryValue').value;
	//pageSizeFromParam=document.getElementById('pageSizeValue').value;
	location.href="/articles?category="+pageCategoryFromParam+"&pagesize="+selectedValue;
}


//게시판 글쓰기 검증 
function writeValidate(){
	
	var categoryValue = $('input[name=category]:checked').val();
	var titleValue = $('#article_title').val();
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
			fetch('/articles',{
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
	          		location.href="/articles";
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
	var titleValue = $('#article_title').val();
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
			fetch('/articles/'+$('#article_id').val(),{
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
	          		location.href="/articles/"+$('#article_id').val();
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
			fetch('/articles/'+$('#article_id').val(),{
			    method:'DELETE'
			}).then(response => response.text())
	  		.then(data => {
	     		 if(data=="success"){
	          		alert('삭제되었습니다.');
	          		location.href="/articles"
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
		fetch('/articles/reply',{
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

//게시판 댓글삭제
function delete_reply(){
		let askbeforedelete = confirm("삭제 하시겠습니까?");
		if(askbeforedelete){
			fetch('/articles/reply/'+$('#reply_id').val(),{
			    method:'DELETE'
			}).then(response => response.text())
	  		.then(data => {
	     		 if(data=="success"){
	          		alert('삭제되었습니다.');
	          		location.reload();
	      		 }else if(data=="needLogin"){
	          		alert('로그인이 필요합니다.');
	          		document.getElementById('offCanvasToggleButton').click();
	      		 }
	  		})
  		}
}

