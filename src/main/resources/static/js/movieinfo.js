const reviewSubmitButton = document.getElementById('reviewwrite-btn');
const reviewLikeButton = document.getElementById('reviewlike');
const reviewDislikeButton = document.getElementById('reviewdislike');

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
}
