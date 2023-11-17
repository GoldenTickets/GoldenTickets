
const userid='test1';
const reviewSubmitButton = document.getElementById('reviewwrite-btn');
const reviewLikeButton = document.getElementById('reviewlike');
const reviewDislikeButton = document.getElementById('reviewdislike');

if (reviewSubmitButton) {
	reviewSubmitButton.addEventListener('click',event => {
		fetch('/movieinfo/submitreview',{
			method:'POST',
			headers:{"Content-Type":"application/json"},
			body:JSON.stringify({
				movie_id:'7',
				mem_id:'test2',
				rating:$('input[name=starpoint]:checked').val(),
				content:$('#reviewcontent').val()
				
			})
		}).then(()=>{
			alert('리뷰 등록이 완료되었습니다.');
			location.reload();
		})
	})
}
