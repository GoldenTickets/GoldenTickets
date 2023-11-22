const reviewSubmitButton = document.getElementById('reviewwrite-btn');
const reviewLikeButton = document.getElementById('reviewlike');
const reviewDislikeButton = document.getElementById('reviewdislike');

//리뷰 등록기능
if (reviewSubmitButton) {
	reviewSubmitButton.addEventListener('click',event => {
		var reviewmovieid=document.getElementById('movie_id').value;
		var reviewmemberid=document.getElementById('member_id').value;
		if($('input[name=starpoint]:checked').val()==null){
			alert('별점을 선택해주세요.');
		}else if($('#reviewcontent').val().length<1){
			alert('리뷰 내용을 입력해주세요.');
		}else{
			fetch('/movie/submitreview/'+reviewmovieid,{
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
	})
	
	
	
//리뷰 삭제기능
if (reviewDeleteButton) {
	reviewDeleteButton.addEventListener('click',event => {
			var reviewDeleteButton = document.getElementById('reviewDeleteButton');
			var movie_id_fordelete=document.getElementById('movie_id').value;
			fetch('/movie/deleteReview/'+movie_id_fordelete,{
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
				  
	
	   })
	 }
	 
//movieinfo_all 조회수 또는 업데이트
function selectboxchange(){
	var movieinfogenre=document.getElementById('movieinfoallgenre').value;
	var selectboxvalue=document.getElementById('select-order').value;
	if(selectboxvalue=='update'){
		location.href="/movie?page=1&genre="+movieinfogenre+"&order="+selectboxvalue;
	}else if(selectboxvalue=='hit'){
		location.href="/movie?page=1&genre="+movieinfogenre+"&order="+selectboxvalue;
	}
}

}
