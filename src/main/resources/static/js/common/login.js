window.onload = function (){
    //엔터키 입력 감지
    document.getElementById("loginForm").addEventListener("keypress", function(event){
        if(event.key === "Enter"){
            loginValidation();
        }
    });
}
function loginValidation() {
    const id = document.getElementById("id");
    const pw = document.getElementById("pw");

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

    document.getElementById("loginForm").submit();
}
function signupValidation() {
    const id = document.getElementById("id");
    const pw = document.getElementById("pw");
    const emailAddress = document.getElementById("emailAddress");
    const name = document.getElementById("name");
    const christianName = document.getElementById("christianName");

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
    if(emailAddress.value === ''){
        alert("이메일 주소를 입력하세요.");
        emailAddress.focus();
        return;
    }
    if(name.value === ''){
        alert("이름을 입력하세요.");
        name.focus();
        return;
    }
    if(christianName.value === ''){
        alert("세례명을 입력하세요.");
        christianName.focus();
        return;
    }

    document.getElementById("signupForm").submit();
}