const reviewSubmitButton = document.getElementById('reviewwrite-btn');//자꾸 로드 안돼서 onclick으로 변경
const reviewLikeButton = document.getElementById('reviewlike');
const reviewDislikeButton = document.getElementById('reviewdislike');

//리뷰 등록기능
function reviewSubmit() {
		var reviewmovieid=document.getElementById('movie_id').value;
		var reviewmemberid=document.getElementById('member_id').value;
		if($('input[name=starpoint]:checked').val()==null){
			alert('별점을 선택해주세요.');
		}else if($('#reviewcontent').val().length<1){
			alert('리뷰 내용을 입력해주세요.');
		}else{
			fetch('/movies/review/'+reviewmovieid,{
				method:'POST',
				headers:{"Content-Type":"application/json"},
				body:JSON.stringify({
					movie_id:reviewmovieid,
					mem_id:reviewmemberid,
					rating:$('input[name=starpoint]:checked').val(),
					content:$('#reviewcontent').val()	
				})
			}).then(response => response.text())
			  .then(data => {
				  if(data=="success"){
	                alert('리뷰 등록이 완료되었습니다.');
	                location.reload();
	             }else if(data=="needLogin"){
					alert('로그인 후 이용가능합니다.');
					document.getElementById('offCanvasToggleButton').click();
				 }else if(data=="duplicated"){
					alert('리뷰는 하나만 등록이 가능합니다.'); 
				 }
			  }).catch(error=>{
				  alert('리뷰 등록에 실패했습니다.');
			  })
				  
	
	   }
}	
	
	
//리뷰 삭제기능
function reviewDelete() {
			var reviewDeleteButton = document.getElementById('reviewDeleteButton');
			var movie_id_fordelete=document.getElementById('movie_id').value;
			fetch('/movies/'+movie_id_fordelete,{
				method:'GET',
				}).then(response => response.text())
			  .then(data => {
				  if(data=="success"){
	                alert('삭제 완료되었습니다.');
	                location.reload();
	             }else if(data=="needLogin"){
					alert('로그인 후 이용가능합니다.');
					document.getElementById('offCanvasToggleButton').click();
				 }
			  }).catch(error=>{
				  alert('삭제에 실패했습니다.');
			  })

	 }
	 

//북마크 추가
function bookmark_append(movie_id){
	fetch('/movies/bookmark/'+movie_id,{
		method:'GET'
	}).then(response => {
	  if(response.ok){
		  alert('북마크가 추가되었습니다');
		  location.reload();
		  return response.text();
	  }else{
		  alert('로그인 후 이용가능합니다.');
		  document.getElementById('offCanvasToggleButton').click();
		  response.text()
	  }
	  })
	  .catch(error => {
		  alert('북마크 등록에 실패하었습니다');
		  location.reload();
	  })
	}

//북마크 취소
function bookmark_cancel(movie_id){
	fetch('/movies/bookmark/'+movie_id,{
		method:'GET'
	}).then(response => {
	  if(response.ok){
		  alert('북마크가 취소되었습니다');
		  location.reload();
		  return response.text();
	  }else{
		  alert('로그인 후 이용가능합니다.');
		  document.getElementById('offCanvasToggleButton').click();
		  response.text()
	  }
	  })
	  .catch(error => {
		  alert('북마크 취소에 실패하었습니다');
		  location.reload();
	  })
	}


