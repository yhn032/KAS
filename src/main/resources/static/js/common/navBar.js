$(document).ready(function(){

    const prevScrollPos = $(window).scrollTop();
    console.log("prevScrollPos :: " + prevScrollPos);

    window.addEventListener('scroll', function(event){
        const currentScrollPos = $(window).scrollTop();
        const navBar = $(".content-navBar");

        console.log("currentScrollPos :: " + currentScrollPos);
        console.log("prevScrollPos :: " + prevScrollPos);

        if(prevScrollPos > currentScrollPos) {
            alert("hide");
        }else {
            alert("show");
        }
    });

    $('.has-asset-sub-menu > a').click(function (e){
        e.preventDefault();
        $('.arrow-up').toggle();
        $('.asset-sub-menu').toggle();
    });

    $('.sub-list').click(function (){
        $('.sub-list > ul').toggleClass('none');
    });
});
