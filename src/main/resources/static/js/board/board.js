/**
 * 게시판 반납 기능
 */
function returnBtn() {

    let boardId = $("#board-id").val();
    console.log(boardId);
    if(confirm("반납 수량을 확인 후 선택하세요. 반납 완료 처리하시겠습니까?")) {
        $.ajax({
            type : 'GET',
            url : '/board/'+boardId+'/return',
            processData: false,
            contentType: false,
            success: function (data) {
                if(data === 'success') {
                    //페이지 새로 고침
                    location.reload();
                }
            },
            error:function (jqXHR, textStatus, errorThrown) {
                console.log("Request Failed");
                console.log('jqXHR : ', jqXHR);
                console.log('textStatus : ', textStatus);
                console.log('errorThrown : ', errorThrown);
            }
        });
    }else {
        alert("반납이 취소되었습니다.");
    }
}