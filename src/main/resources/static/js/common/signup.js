window.onload = function (){
    document.getElementById("signupForm").addEventListener("keypress", function(event){
        if(event.key === "Enter"){
            signupValidation();
        }
    });

    //파일이 선택되거나 변경되는 경우 실행될 이벤트 리스너
    //테스트용으로 정적으로 만들었지만 실제로는 동적으로 적용되도록 동적 태그가 생성될 때 리스너 등록 필수
    //회원가입 전에 등록하면 해당 이미지가 누구의 프로필인지 알 수가 없음 해서, 회원가입 후 마이페이지에서 이미지 수정하도록
/*    $("#profileImg").change(function (){
        const fileName = $(this).val().split("\\").pop();
        console.log($(this).val());
        console.log(fileName);

        const formData = new FormData(document.getElementById("signupForm"));
        const fileInput = document.getElementById("profileImg");
        if(fileInput.files.length > 0) {
            formData.append("profileImgFile", fileInput.files[0]);
        }

        $.ajax({
            type : 'POST',
            url : '/teacher/addProfileImg',
            data : formData,
            processData: false,
            contentType: false,
            success: function (data) {
                if(data.result === 'success') {
                    console.log('파일 업로드 성공');

                    console.log(data.orgName);
                    console.log(data.savedName);
                }
            }
        })
    })*/

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

                // 비동기적으로 실행하여 브라우저가 다음 탭 이동을 처리하도록 함
                setTimeout(function() {
                    // 새로 추가한 input 태그에 포커스를 줌
                    dynamic.focus();
                }, 0);
            }
        }
    });
}

function addFormTag(formId){
    const signupForm = document.getElementById("signupForm");

    const addBox = document.createElement("div");
    addBox.id = formId + 'Box';
    addBox.classList.add("formElementBox");

    if(formId === 'profileImg') {

        //회원 가입 버튼 태그
        const signUPBtn = document.createElement("button");
        signUPBtn.className = 'form_btn';
        signUPBtn.type = 'button';
        signUPBtn.textContent = 'Sign Up';
        signUPBtn.onclick = signupValidation;

        addBox.appendChild(signUPBtn);
        signupForm.appendChild(addBox);
        return;
    }

    const pTag = document.createElement("p");

    if(formId == 'pw') pTag.textContent = "비밀번호를 입력하세요. (영문자 + 숫자 + 특수 문자 8자 이상)";
    else if(formId == 'pwCheck') pTag.textContent = "비밀번호를 다시 입력하세요.";
    else if(formId === 'phoneNum') pTag.textContent = "전화번호를 입력하세요. (숫자만 입력)";
    else if(formId === 'nickName') pTag.textContent = "사용할 닉네임을 입력하세요.";
    else if(formId === 'emailAddress') pTag.textContent = "이메일 주소를 입력하세요.";
    else if(formId === 'name') pTag.textContent = "이름을 입력하세요.";
    else if(formId === 'christianName') pTag.textContent = "세례명을 입력하세요.";
    else if(formId === 'saintsDay') pTag.textContent = "축일을 선택하세요.";
    addBox.appendChild(pTag);


    const inputTag = document.createElement("input");

    if(formId.includes('pw')) inputTag.type = "password";
    else inputTag.type = "text";

    if(formId === 'phoneNum') inputTag.maxLength = 13;
    if(formId === 'saintsDay') inputTag.readOnly = true;

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
        document.getElementById(formId).addEventListener("keyup", phoneNumValidation);
        document.getElementById(formId).addEventListener("input", formatting);
    }else if (formId === 'emailAddress'){
        document.getElementById(formId).addEventListener("keyup", emailValidation);
    }else if (formId === 'name'){
        document.getElementById(formId).addEventListener("keyup", nameValidation);
    }else if (formId === 'christianName'){
        document.getElementById(formId).addEventListener("keyup", christianNameValidation);
    }else if (formId === 'nickName'){
        document.getElementById(formId).addEventListener("keyup", nickNameValidation);
    }else if (formId === 'saintsDay'){
        document.getElementById(formId).addEventListener("click", datePick);
    }


    //pw는 초반에 id태그가 있기 때문에 document.onload로 해당 태그를 찾을 수 있지만,
    //그 이후 동적으로 만들어진 태그는 찾을 수 없기때문에 태그를 만들면서 리스너를 등록해놔야 한다.
    if(formId === 'pw') addBlurListener("pw", "pwBox", "pwCheck", "굿 비밀번호 확인 태그를 추가합니다.");
    else if(formId === 'pwCheck') addBlurListener("pwCheck", "pwCheckBox", "name", "굿 이름 태그를 추가합니다.");
    else if(formId === 'name') addBlurListener("name", "nameBox", "emailAddress", "굿 이메일 태그를 추가합니다.");
    else if(formId === 'emailAddress') addBlurListener("emailAddress", "emailAddressBox", "phoneNum", "굿 전화번호 태그를 추가합니다.");
    else if(formId === 'phoneNum') addBlurListener("phoneNum", "phoneNumBox", "nickName", "굿 닉네임 태그를 추가합니다.");
    else if(formId === 'nickName') addBlurListener("nickName", "nickNameBox", "christianName", "굿 세례명 태그를 추가합니다.");
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

function pwValidation(){
    let inputPw = $("#pw").val();
    const lengthCheck = /^.{8,}$/;
    const regex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*])[a-zA-Z\d!@#$%^&*]+$/;

    //8자 이상인지 체크
    if(!lengthCheck.test(inputPw)) {
        $("#pwFeedback").text("8글자 이상의 비밀번호를 설정하세요.");
        $("#pw").css("box-shadow", "inset 1px 1px 1px #fc6c9c, inset -1px -1px 7px #ff0000");
        $("#pwFeedback").css("color", "red");
        document.getElementById("pwBox").classList.remove("success");
        return;
    }

    if(!regex.test(inputPw)) {
        $("#pwFeedback").text("주어진 조합에 맞게 비밀번호를 설정하세요.");
        $("#pw").css("box-shadow", "inset 1px 1px 1px #fc6c9c, inset -1px -1px 7px #ff0000");
        $("#pwFeedback").css("color", "red");
        document.getElementById("pwBox").classList.remove("success");
        return;
    }

    if(lengthCheck.test(inputPw) && regex.test(inputPw)) {
        $("#pwFeedback").text("올바른 비밀번호 조합입니다.");
        $("#pw").css("box-shadow", "inset 1px 1px 1px #c0ffc9, inset -1px -1px 7px #17ff00");
        $("#pwFeedback").css("color", "#17ff00");
        document.getElementById("pwBox").classList.add("success");
        return;
    }
}

function pwCheckValidation(){
    let pw = $("#pw").val();
    let inputPwCheck = $("#pwCheck").val();

    if(pw === inputPwCheck) {
        $("#pwCheckFeedback").text("비밀번호 확인이 완료되었습니다.");
        $("#pwCheck").css("box-shadow", "inset 1px 1px 1px #c0ffc9, inset -1px -1px 7px #17ff00");
        $("#pwCheckFeedback").css("color", "#17ff00");
        document.getElementById("pwCheckBox").classList.add("success");
        return;
    }else {
        $("#pwCheckFeedback").text("입력한 비밀번호가 서로 다릅니다.");
        $("#pwCheck").css("box-shadow", "inset 1px 1px 1px #fc6c9c, inset -1px -1px 7px #ff0000");
        $("#pwCheckFeedback").css("color", "red");
        document.getElementById("pwCheckBox").classList.remove("success");
        return;
    }
}

function nameValidation(){
    if($("#name").val() != '') {
        $("#nameFeedback").text("본인 이름은 알아서 잘 쓰겠지...");
        $("#name").css("box-shadow", "inset 1px 1px 1px #c0ffc9, inset -1px -1px 7px #17ff00");
        $("#nameFeedback").css("color", "#17ff00");
        document.getElementById("nameBox").classList.add("success");
    }else {
        $("#nameFeedback").text("이름을 입력하세요.");
        $("#name").css("box-shadow", "inset 1px 1px 1px #fc6c9c, inset -1px -1px 7px #ff0000");
        $("#nameFeedback").css("color", "red");
        document.getElementById("nameBox").classList.remove("success");
    }
}

function emailValidation(){
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    let inputEmail = $("#emailAddress").val();

    if(emailRegex.test(inputEmail)) {
        $("#emailAddressFeedback").text("올바른 이메일 형식입니다.");
        $("#emailAddress").css("box-shadow", "inset 1px 1px 1px #c0ffc9, inset -1px -1px 7px #17ff00");
        $("#emailAddressFeedback").css("color", "#17ff00");
        document.getElementById("emailAddressBox").classList.add("success");
    }else {
        $("#emailAddressFeedback").text("이메일 형식에 맞게 입력하세요.");
        $("#emailAddress").css("box-shadow", "inset 1px 1px 1px #fc6c9c, inset -1px -1px 7px #ff0000");
        $("#emailAddressFeedback").css("color", "red");
        document.getElementById("emailAddressBox").classList.remove("success");
    }
}
function phoneNumValidation(){

    if($("#phoneNum").val().length >= 13) {
        $("#phoneNumFeedback").text("올바른 전화번호");
        $("#phoneNum").css("box-shadow", "inset 1px 1px 1px #c0ffc9, inset -1px -1px 7px #17ff00");
        $("#phoneNumFeedback").css("color", "#17ff00");
        document.getElementById("phoneNumBox").classList.add("success");
    }else {
        $("#phoneNumFeedback").text("");
        $("#phoneNum").css("box-shadow", "inset 1px 1px 1px #6ac3e1, inset -1px -1px 7px #2641ff");
        $("#phoneNumFeedback").css("color", "white");
        document.getElementById("phoneNumBox").classList.remove("success");
    }
}
function formatting(){
    let num = $("#phoneNum").val();
    let numericValue = num.replace(/\D/g, '');
    let formattedValue = formatPhoneNumber(numericValue);

    $("#phoneNum").val(formattedValue);
}
function formatPhoneNumber(value) {
    // value를 전화번호 형식으로 포맷
    if (value.length < 4) {
        return value;
    } else if (value.length < 8) {
        return `${value.slice(0, 3)}-${value.slice(3)}`;
    } else {
        return `${value.slice(0, 3)}-${value.slice(3, 7)}-${value.slice(7, 11)}`;
    }
}

function nickNameValidation(){
    if($("#nickName").val() != '') {
        $("#nickNameFeedback").text("본인 이름은 알아서 잘 쓰겠지...");
        $("#nickName").css("box-shadow", "inset 1px 1px 1px #c0ffc9, inset -1px -1px 7px #17ff00");
        $("#nickNameFeedback").css("color", "#17ff00");
        document.getElementById("nickNameBox").classList.add("success");
    }else {
        $("#nickNameFeedback").text("닉네임을 입력하세요.");
        $("#nickName").css("box-shadow", "inset 1px 1px 1px #fc6c9c, inset -1px -1px 7px #ff0000");
        $("#nickNameFeedback").css("color", "red");
        document.getElementById("nickNameBox").classList.remove("success");
    }
}

function christianNameValidation(){
    if($("#christianName").val() != '') {
        $("#christianNameFeedback").text("본인 세례명은 알아서 잘 쓰겠지...");
        $("#christianName").css("box-shadow", "inset 1px 1px 1px #c0ffc9, inset -1px -1px 7px #17ff00");
        $("#christianNameFeedback").css("color", "#17ff00");
        document.getElementById("christianNameBox").classList.add("success");
    }else {
        $("#christianNameFeedback").text("닉네임을 입력하세요.");
        $("#christianName").css("box-shadow", "inset 1px 1px 1px #fc6c9c, inset -1px -1px 7px #ff0000");
        $("#christianNameFeedback").css("color", "red");
        document.getElementById("christianNameBox").classList.remove("success");
    }
}

function saintsDayValidation(){
    if($("#saintsDay").val() != '') {
        $("#saintsDayFeedback").text("축일 선택 완료!");
        $("#saintsDay").css("box-shadow", "inset 1px 1px 1px #c0ffc9, inset -1px -1px 7px #17ff00");
        $("#saintsDayFeedback").css("color", "#17ff00");
        document.getElementById("saintsDayBox").classList.add("success");

        if (document.getElementById("saintsDayBox").classList.contains("success")) {
            const formTag = document.getElementById("profileImg");
            if (formTag == null) {
                // alert(message);
                addFormTag("profileImg");
                const dynamic = document.getElementById("profileImg");
                dynamic.setAttribute("tabindex", "0");
                // dynamic.focus();

                // 비동기적으로 실행하여 브라우저가 다음 탭 이동을 처리하도록 함
                setTimeout(function() {
                    // 새로 추가한 input 태그에 포커스를 줌
                    dynamic.focus();
                }, 0);
            }
        }
    }else {
        $("#saintsDayFeedback").text("축일을 입력하세요.");
        $("#saintsDay").css("box-shadow", "inset 1px 1px 1px #fc6c9c, inset -1px -1px 7px #ff0000");
        $("#saintsDayFeedback").css("color", "red");
        document.getElementById("saintsDayBox").classList.remove("success");
    }
}

function datePick(){
    $("#saintsDay").datepicker({
        monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
        dayNamesMin: ['일','월','화','수','목','금','토'],
        weekHeader: 'Wk',
        dateFormat: 'mm-dd', //형식(03-03)
        autoSize: false, //오토리사이즈(body등 상위태그의 설정에 따른다)
        changeMonth: true, //월변경가능
        changeYear: true, //년변경가능
        showMonthAfterYear: true, //년 뒤에 월 표시
        buttonImageOnly: true, //이미지표시
        buttonImage: '../../img/btn_calendar.gif', //이미지주소
        showOn: "button", //엘리먼트 사용
        yearRange: '2023:2024', //2023년부터 2024년까지
        onSelect: saintsDayValidation
    });
}