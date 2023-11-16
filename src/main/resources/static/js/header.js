    $(function(){
      $('#header').load("header.html");

      $('.hiddensearch').hide();//초기화면에서 검색창 hide

      $('#svciconsearch').on("click",function(){
        $('.hiddensearch').fadeToggle();
      })
    })