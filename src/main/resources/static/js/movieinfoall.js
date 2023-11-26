
$(function(){
	
    const genrefromcontroller=document.getElementById('movieinfoallorder').value;
    if(genrefromcontroller=="releasedate"){
		document.getElementById('updateoption').setAttribute("selected","selected");
	}else if(genrefromcontroller=="hit"){
		document.getElementById('hitoption').setAttribute("selected","selected");
	}
})

function selectboxchange(){
	var movieinfogenre=document.getElementById('movieinfoallgenre').value;
	var selectboxvalue=document.getElementById('select-order').value;
	if(selectboxvalue=='releasedate'){
		location.href="/movie?page=1&genre="+movieinfogenre+"&order="+selectboxvalue;
	}else if(selectboxvalue=='hit'){
		location.href="/movie?page=1&genre="+movieinfogenre+"&order="+selectboxvalue;
	}
}