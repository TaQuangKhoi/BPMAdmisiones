angular.module('scrollTop', []).directive('scrollOnClick', function() {
  return {
    restrict: 'EA',
    template:'<a title="Click aquí para subir" class="scrollup"><i class="glyphicon glyphicon-circle-arrow-up"></i></a>',
    link: function(scope, $elm) {
        $(window).scroll(function () {
            if ($(this).scrollTop() > 300) {
                $('.scrollup').fadeIn();
            } else {
                $('.scrollup').fadeOut();
            }
    });   
      $elm.on('click', function() {
        //alert('hello');
        $("html,body").animate({scrollTop: '0px'}, "slow");
      });
    }
  }
});
