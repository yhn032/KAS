const colors = ['#ff0000', '#00ff00', '#0000ff'];
const header = document.querySelector(".content-navBar");
let idx = 0;

//일정 시간마다 배경색 변경 함수
/*function changeBackground(){
    header.style.backgroundColor = colors[idx];
    idx = (idx+1) % colors.length;
}
//3초를 주기로 메서드 실행
setInterval(changeBackground, 3000);*/

function toggleSidebar() {
    if($(".sidebar").css("margin-left") === '-250px'){ //안보이는 경우
        $(".sidebar").css("margin-left", '0');
        $(".content").css("margin-left", '250px');
        $(".asset-main").css("margin-left", '250px');
        $(".content-navBar").css("margin-left", '250px');
        $(".content-main").css("margin-left", '250px');
        $(".content-footer").css("margin-left", '250px');
        $(".content-toggle-btn").text("◀");
        $(".content-toggle-btn").css("left", '261px');
    } else {
        $(".sidebar").css("margin-left", '-250px');
        $(".content").css("margin-left", '0');
        $(".asset-main").css("margin-left", '0');
        $(".content-navBar").css("margin-left", '0');
        $(".content-main").css("margin-left", '0');
        $(".content-footer").css("margin-left", '0');
        $(".content-toggle-btn").text("▶");
        $(".content-toggle-btn").css("left", '11px');
    }
}

let lastScrollTop = 0;
window.addEventListener('scroll', function(event){
    const contentToggleBtn = document.querySelector('.content-toggle-btn');
    const computedStyle = window.getComputedStyle(contentToggleBtn);

    const lastValue = computedStyle.getPropertyValue('bottom').replace('px', '');

    let currentScrollTop = window.pageYOffset || document.documentElement.scrollTop;

    if (currentScrollTop > lastScrollTop) {
        contentToggleBtn.style.bottom = parseInt(lastValue)-(currentScrollTop-lastScrollTop)+'px';
        console.log(currentScrollTop-lastScrollTop);
        /*contentToggleBtn.style.bottom =*/
    } else {
        contentToggleBtn.style.bottom = parseInt(lastValue)+(lastScrollTop-currentScrollTop)+'px';
        console.log(lastScrollTop-currentScrollTop);
    }

    lastScrollTop = currentScrollTop <= 0 ? 0 : currentScrollTop;

});

$(document).ready(function (){
    $.ajax({
        type : 'GET',
        url : '/teacher/getTeacherProfile',
        processData: false,
        contentType: false,
        success: function (data) {
            $('.overlay-image').css("backgroundImage", "url('" + '../img/uploads/profile/' + data + "')");
            console.log(data);
        }
    });

    //모든 파일에 include되어 있는 sideBar의 정보를 가져온다.

})