window.onload = function (){
    //엔터키 입력 감지
    document.getElementById("loginForm").addEventListener("keypress", function(event){
        if(event.key === "Enter"){
            validation();
        }
    });
}
function validation(){
    const id = document.getElementById("id");
    const pw = document.getElementById("pw");
    const loginForm = document.getElementById("loginForm");

    if(id.value === ''){
        alert("아이디를 입력하세요.");
        id.focus();
        return;
    }
    if(pw.value === ''){
        alert("비밀번호를 입력하세요.");
        pw.focus();
        return;
    }

    loginForm.submit();
}