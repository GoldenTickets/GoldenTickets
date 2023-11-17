const reviewSubmitButton = document.getElementById('reviewwrite-btn');
const reviewLikeButton = document.getElementById('reviewlike');
const reviewDislikeButton = document.getElementById('reviewdislike');

if (reviewSubmitButton) {
	reviewSubmitButton.addEventListener('click',event => {
		var reviewmovieid=document.getElementById('movie_id').value;
		var reviewmemberid=document.getElementById('member_id').value;
		
		fetch('/movie/submitreview/'+reviewmovieid,{
			method:'POST',
			headers:{"Content-Type":"application/json"},
			body:JSON.stringify({
				movie_id:reviewmovieid,
				mem_id:reviewmemberid,
				rating:$('input[name=starpoint]:checked').val(),
				content:$('#reviewcontent').val()	
			})
		}).then(()=>{
			alert('리뷰 등록이 완료되었습니다.');
			location.reload();
		})
	})
}
