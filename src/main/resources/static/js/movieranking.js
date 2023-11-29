//북마크 추가
function bookmark_append(movie_id){
	fetch('/movies/bookmark/'+movie_id,{
		method:'POST'
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
		method:'DELETE'
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


