window.onload = function (){
    document.getElementById("signupForm").addEventListener("keypress", function(event){
        if(event.key === "Enter"){
            signupValidation();
        }
    });

    //id
    document.getElementById("id").addEventListener("blur", function(event){
        event.preventDefault();
        //여러번 추가 되더라. 조건 확인해서 이미 추가되어 있다면 추가 되지 않게 막아야 할 듯
        if(document.getElementById("idBox").classList.contains("success")){
            const formTag = document.getElementById("pwBox");
            if(formTag == null){
                // alert("굿 비밀번호 태그를 추가합니다.");
                addFormTag("pw");
                const dynamic = document.getElementById("pw");
                dynamic.setAttribute("tabindex", "0");
                // dynamic.focus();

                // 비동기적으로 실행하여 브라우저가 다음 탭 이동을 처리하도록 함
                setTimeout(function() {
                    // 새로 추가한 input 태그에 포커스를 줌
                    dynamic.focus();
                }, 0);
            }
        }
    });
}

//ajax의 경우 비동기 방식으로 진행되기 때문에
//return으로 함수가 제대로 종료되지 않을 수 있다.
//ajax의 결과로 함수를 탈출하기 위해서는 promise 혹은 콜백함수를 사용해야 한다.
function validateInsertId(insertId){
    return new Promise((resolve, reject) => {
        const feedback = document.getElementById("idFeedback");
        const id = document.getElementById("id");

        //id 중복 체크 ajax
        $.ajax({
            url : '/teacher/duplicateId',
            method : 'POST',
            data: { id: insertId },
            success : function(result){
                if(result === 'error'){
                    feedback.textContent = "이미 사용중인 아이디입니다.";
                    id.style.boxShadow = "inset 1px 1px 1px #fc6c9c, inset -1px -1px 7px #ff0000";
                    feedback.style.color = "red";
                    resolve('');
                }else if (result === 'success'){
                    feedback.textContent = "";
                    id.style.boxShadow = "inset 1px 1px 1px #6ac3e1, inset -1px -1px 7px #2641ff";
                    feedback.style.color = "white";
                    resolve('perfect');
                }else {
                    console.log(result);
                    reject(new Error('Unexpected result')); // Promise 실패 시 에러 반환
                }
            },
            error: function(error) {
                console.error(error);
                reject(error); // Promise 실패 시 에러 반환
            }
        });
    });
}

function addFormTag(formId){
    const signupForm = document.getElementById("signupForm");

    const addBox = document.createElement("div");
    addBox.id = formId + 'Box';
    addBox.classList.add("formElementBox");

    const pTag = document.createElement("p");

    if(formId == 'pw') pTag.textContent = "비밀번호를 입력하세요. (영문자 + 숫자 + 특수 문자 8자 이상)";
    else if(formId == 'pwCheck') pTag.textContent = "비밀번호를 다시 입력하세요.";
    else if(formId === 'phoneNum') pTag.textContent = "전화번호를 입력하세요.";
    else if(formId === 'nickName') pTag.textContent = "닉네임을 입력하세요.";
    else if(formId === 'emailAddress') pTag.textContent = "이메일 주소를 입력하세요.";
    else if(formId === 'name') pTag.textContent = "이름을 입력하세요.";
    else if(formId === 'christianName') pTag.textContent = "세례명을 입력하세요.";
    else if(formId === 'saintsDay') pTag.textContent = "축일을 선택하세요.";
    addBox.appendChild(pTag);

    const inputTag = document.createElement("input");
    if(formId.includes('pw')) inputTag.type = "password";
    else inputTag.type = "text";

    inputTag.id = formId;
    inputTag.placeholder = formId;

    if(formId === 'pw') inputTag.name = 'teacherLogIn' + formId.toUpperCase();
    else if(formId === 'phoneNum') inputTag.name = 'teacherPhoneNumber';
    else if(formId === 'nickName') inputTag.name = 'teacherNickname';
    else if(formId === 'emailAddress') inputTag.name = 'teacherEmailAddress';
    else if(formId === 'name') inputTag.name = 'teacherName';
    else if(formId === 'christianName') inputTag.name = 'teacherChristianName';
    else if(formId === 'saintsDay') inputTag.name = 'teacherSaintsDay';

    inputTag.autocomplete = "off"

    addBox.appendChild(inputTag);

    const pTag2 = document.createElement("p");
    pTag2.id = formId + 'Feedback';
    pTag2.style.fontSize = "10px";

    addBox.appendChild(pTag2);

    signupForm.appendChild(addBox);

    if(formId === 'pw'){
        document.getElementById(formId).addEventListener("keyup", pwValidation);
    }else if (formId === 'pwCheck'){
        document.getElementById(formId).addEventListener("keyup", pwCheckValidation);
    }else if (formId === 'phoneNum'){
        document.getElementById(formId).addEventListener("keyup", pwValidation);
    }else if (formId === 'nickName'){
        document.getElementById(formId).addEventListener("keyup", pwValidation);
    }else if (formId === 'emailAddress'){
        document.getElementById(formId).addEventListener("keyup", pwValidation);
    }else if (formId === 'name'){
        document.getElementById(formId).addEventListener("keyup", pwValidation);
    }else if (formId === 'christianName'){
        document.getElementById(formId).addEventListener("keyup", pwValidation);
    }else if (formId === 'saintsDay'){
        document.getElementById(formId).addEventListener("keyup", pwValidation);
    }


    //pw는 초반에 id태그가 있기 때문에 document.onload로 해당 태그를 찾을 수 있지만,
    //그 이후 동적으로 만들어진 태그는 찾을 수 없기때문에 태그를 만들면서 리스너를 등록해놔야 한다.
    if(formId === 'pw') addBlurListener("pw", "pwBox", "pwCheck", "굿 비밀번호 확인 태그를 추가합니다.");
    else if(formId === 'pwCheck') addBlurListener("pwCheck", "pwCheckBox", "phoneNum", "굿 전화번호 태그를 추가합니다.");
    else if(formId === 'phoneNum') addBlurListener("phoneNum", "phoneNumBox", "nickName", "굿 닉네임 태그를 추가합니다.");
    else if(formId === 'nickName') addBlurListener("nickName", "nickNameBox", "emailAddress", "굿 이메일 태그를 추가합니다.");
    else if(formId === 'emailAddress') addBlurListener("emailAddress", "emailAddressBox", "name", "굿 이름 태그를 추가합니다.");
    else if(formId === 'name') addBlurListener("name", "nameBox", "christianName", "굿 세례명 태그를 추가합니다.");
    else if(formId === 'christianName') addBlurListener("christianName", "christianNameBox", "saintsDay", "굿 축일 태그를 추가합니다.");

}
function addBlurListener(elementId, nextElementId, nextFormTag, message) {
    document.getElementById(elementId).addEventListener("blur", function (event) {
        event.preventDefault();
        if (document.getElementById(nextElementId).classList.contains("success")) {
            const formTag = document.getElementById(nextFormTag);
            if (formTag == null) {
                // alert(message);
                addFormTag(nextFormTag);
                const dynamic = document.getElementById(nextFormTag);
                dynamic.setAttribute("tabindex", "0");
                // dynamic.focus();

                // 비동기적으로 실행하여 브라우저가 다음 탭 이동을 처리하도록 함
                setTimeout(function() {
                    // 새로 추가한 input 태그에 포커스를 줌
                    dynamic.focus();
                }, 0);
            }
        }
    });
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

    //한글 여부 체크
    if(koreanRegex.test(id.value)){
        feedback.textContent = "한글은 입력할 수 없습니다.";
        id.style.boxShadow = "inset 1px 1px 1px #fc6c9c, inset -1px -1px 7px #ff0000";
        feedback.style.color = "red";
        return;
    }else {
        feedback.textContent = "";
        id.style.boxShadow = "inset 1px 1px 1px #6ac3e1, inset -1px -1px 7px #2641ff";
        feedback.style.color = "white";
    }

    //특수문자 체크
    if(specialCharacterRegex.test(id.value)){
        feedback.textContent = "공백을 포함한 특수문자 입력은 불가능합니다.";
        id.style.boxShadow = "inset 1px 1px 1px #fc6c9c, inset -1px -1px 7px #ff0000";
        feedback.style.color = "red";
        return;
    }else {
        feedback.textContent = "";
        id.style.boxShadow = "inset 1px 1px 1px #6ac3e1, inset -1px -1px 7px #2641ff";
        feedback.style.color = "white";
    }

    //6글자 이상입력시 부터 중복 체크
    let encodeUri = encodeURIComponent(id.value);
    if(encodeUri.length >= 6){

        //비동기로 일어나기 때문에 함수 종료가 제때 되지 않음
        validateInsertId(encodeUri)
            .then(answer => {
                console.log('answer : ' + answer);
                if(answer === 'perfect' && alphanumericRegex.test(id.value)){
                    feedback.textContent = "올바른 아이디 조합입니다.";
                    id.style.boxShadow = "inset 1px 1px 1px #c0ffc9, inset -1px -1px 7px #17ff00";
                    feedback.style.color = "#17ff00";
                    document.getElementById("idBox").classList.add("success");
                }else {
                    document.getElementById("idBox").classList.remove("success");
                }
            })
            .catch(error => {
                console.error('Error: ' + error);
            });
    }
}

function pwValidation(){
    document.getElementById("pwBox").classList.add("success");
    if (document.getElementById("pwCheckBox")) document.getElementById("pwCheckBox").classList.add("success");
    if (document.getElementById("phoneNumBox")) document.getElementById("phoneNumBox").classList.add("success");
    if (document.getElementById("nickNameBox")) document.getElementById("nickNameBox").classList.add("success");
    if (document.getElementById("emailAddressBox")) document.getElementById("emailAddressBox").classList.add("success");
    if (document.getElementById("nameBox")) document.getElementById("nameBox").classList.add("success");
    if (document.getElementById("christianNameBox")) document.getElementById("christianNameBox").classList.add("success");
}

function pwCheckValidation(){
    document.getElementById("pwCheckBox").classList.add("success");
}