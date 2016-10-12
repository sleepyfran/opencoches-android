$(window).load(function(){
    $(".jumbotronimg").animate({'opacity': 1, 'bottom': 0}, 1000);
});

$(window).load(function(){
    $('.byn').BlackAndWhite({
        hoverEffect:true,
        webworkerPath: 'js/',
        intensity:1,
        speed: {
            fadeIn: 100,
            fadeOut: 800
        },
    });
});

$('.carousel').carousel({
    interval: 5000
})
$('.tooltip-social').tooltip({
    selector: "a[data-toggle=tooltip]"
})

location.hash && $(location.hash + '.collapse').collapse('show');