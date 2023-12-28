window.onload = function (){
    document.getElementById("signupForm").addEventListener("keypress", function(event){
        if(event.key === "Enter"){
            signupValidation();
        }
    });

    document.getElementById("id").addEventListener("blur", function(){
        if(document.getElementById("idBox").classList.contains("success")){
            alert("굿 비밀번호 태그를 추가합니다.");
            addFormTag("pw");
        }
    });
}

function addFormTag(formId){
    const signupForm = document.getElementById("signupForm");

    const addBox = document.createElement("div");
    addBox.id = formId + 'Box';
    addBox.classList.add("formElementBox");

    const pTag = document.createElement("p");
    pTag.textContent = "비밀번호를 입력하세요. (영문자 + 숫자 + 특수 문자 8자 이상)";
    addBox.appendChild(pTag);

    const inputTag = document.createElement("input");
    inputTag.type = "password";
    inputTag.id = formId;
    inputTag.placeholder = formId;
    inputTag.name = 'teacherLogIn' + formId.toUpperCase();
    inputTag.autocomplete = "off"
    addBox.appendChild(inputTag);

    const pTag2 = document.createElement("p");
    pTag2.id = formId + 'Feedback';
    pTag2.style.fontSize = "10px";
    addBox.appendChild(pTag2);

    signupForm.appendChild(addBox);

    document.getElementById(inputTag.id).addEventListener("keyup", pwValidation);
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

function idValidation(){
    const id = document.getElementById("id");
    const feedback = document.getElementById("idFeedback");
    const koreanRegex = /[ㄱ-ㅎㅏ-ㅣ가-힣]/;
    const specialCharacterRegex = /[^\w\s]|\s/;
    const alphanumericRegex = /^(?=.*[0-9])(?=.*[a-zA-Z])[a-zA-Z0-9]{6,}$/;

    if(koreanRegex.test(id.value)){
        feedback.textContent = "한글은 입력할 수 없습니다.";
        id.style.boxShadow = "inset 1px 1px 1px #fc6c9c, inset -1px -1px 7px #ff0000";
        feedback.style.color = "red";
    }else {
        feedback.textContent = "";
        id.style.boxShadow = "inset 1px 1px 1px #6ac3e1, inset -1px -1px 7px #2641ff";
        feedback.style.color = "white";
    }

    if(specialCharacterRegex.test(id.value)){
        feedback.textContent = "공백을 포함한 특수문자 입력은 불가능합니다.";
        id.style.boxShadow = "inset 1px 1px 1px #fc6c9c, inset -1px -1px 7px #ff0000";
        feedback.style.color = "red";
    }else {
        feedback.textContent = "";
        id.style.boxShadow = "inset 1px 1px 1px #6ac3e1, inset -1px -1px 7px #2641ff";
        feedback.style.color = "white";
    }

    if(alphanumericRegex.test(id.value)){
        feedback.textContent = "올바른 아이디 조합입니다.";
        id.style.boxShadow = "inset 1px 1px 1px #c0ffc9, inset -1px -1px 7px #17ff00";
        feedback.style.color = "#17ff00";
        document.getElementById("idBox").classList.add("success");
    }
}

function pwValidation(){

}