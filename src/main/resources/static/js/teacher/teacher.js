$(function (){
    let initial_path = $("#profileImg").attr('src');
    let upload_path = '';

    $("#profileImgFile").change(function(){
        upload_path = this.files[0];
        if(!upload_path){
            $("#profileImg").attr('src', initial_path);
            return;
        }
        if(upload_path.size > 1024*1024) {
            alert(Math.round(upload_path.size / 1024 / 1024) + 'MB(1MB 까지만 업로드 가능합니다!');
            $("#profileImg").attr('src', initial_path);
            $(this.val(''));
            return;
        }
        //이미지 미리보기 처리
        let reader = new FileReader();
        reader.readAsDataURL(upload_path);

        reader.onload = function(){
            $('#profileImg').attr('src', reader.result);
        };
    });
});
function modifyInfo(){
    const formData = new FormData(document.getElementById("modifyForm"));
    const fileInput = document.getElementById("profileImgFile");
    if(fileInput.files.length > 0) {
        formData.append("profileImgFile", fileInput.files[0]);
    }

    const data = {
        info: {
            teacherLogInID : $("#id").val(),
            teacherName : $("#name").val(),
            teacherNickname : $("#nickname").val(),
            teacherPhoneNumber : $("#number").val(),
            teacherEmailAddress : $("#email").val(),
            teacherChristianName : $("#chName").val(),
            teacherSaintsDay : $("#saints").val(),
        }
    };

    formData.append("teacherDto", new Blob([JSON.stringify(data.info)], {type : "application/json"}));
    console.log(formData);

    $.ajax({
        type : 'POST',
        url : '/teacher/modifyTeacherInfo',
        data : formData,
        processData: false,
        contentType: false,
        success: function (data) {
            console.log('파일 업로드 성공');
        }
    });

    alert("수정 되었습니다.");
    location.href = "/teacher/myPage";
}