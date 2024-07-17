function loadAssetData(){
    $.ajax({
        type : 'GET',
        url : '/asset/findCtg',
        processData: false,
        contentType: false,
        success: function (data) {

            const categories = $('.share-asset-categories');
            const contents = $('.share-asset-content');
            categories.empty();
            contents.empty();

            for(let i=0; i<data.length; i++) {
                let button = $('<button>', {type:'button',text:data[i], onclick:'loadDtlData(this.textContent)', class: 'share-asset-category'});
                categories.append(button);
            }

        }
    })
}

function loadDtlData(type) {

    $.ajax({
        type : 'GET',
        url : '/asset/dtlData?ctg='+encodeURIComponent(type),
        processData: false,
        contentType: false,
        success: function (data) {

            if(data.length > 0) {
                const contents = $('.share-asset-contents');
                contents.empty();
                for(let i=0; i<data.length; i++) {
                    const div = $('<div>', {id:data[i].assetNo,class :'share-asset-content', onclick: 'settingId(this.id)'});
                    const span1 = $("<span>", {class:'span1',text:data[i].assetNo});
                    const span2 = $("<span>", {class:'span2',text:data[i].assetName});
                    const span3 = $("<span>", {class:'span3',text:data[i].assetRemainCnt});
                    const span4 = $("<span>", {class:'span4',text:data[i].assetId}).css('display', 'none');

                    div.append(span1);
                    div.append(span2);
                    div.append(span3);
                    div.append(span4);
                    contents.append(div);
                }
            }

        }
    })
    console.log(type)
}

function settingId(id) {
    const selected = $("#"+id);

    let assetId = $("#"+id+' > .span4').text();
    let assetName = $("#"+id+' > .span2').text();

    $("#boardAssetAssetId").val(assetId);
    $("#boardAssetAssetName").val(assetName);
}

function addShareBoard() {
    const formData = new FormData(document.getElementById("shareAddForm"));
    $.ajax({
        type : 'POST',
        url : '/board/addShare',
        data : formData,
        processData: false,
        contentType: false,
        success: function (data) {

            if(data.status === 'success') {

                alert('게시판 등록 성공');
                location.reload();

            }else if (data.status === 'fail') {
                alert(data.errorMessage);
            }

        }
    })
}

function layerPopOpen(id) {
    if(id === 'shareForm') {
        const categories = $('.share-asset-categories');
        const contents = $('.share-asset-content');
        categories.empty();
        contents.empty();
        $("#boardAssetAssetId").val('');
        $("#boardAssetAssetName").val('');
        $("#boardShareCount").val('');
        $("#boardCarryInName").val('');
        $("#teacher-list option:first").select();
        if($("#shareEditForm").is(":visible")) $("#shareEditForm").fadeOut(200);
    }else if (id == 'shareEditForm') {
        if($("#shareForm").is(":visible")) $("#shareForm").fadeOut(200);
    }
    $('#' + id).fadeIn(200);
}

function layerPopHide(e) {
    $(e).parents('.pop-layer').fadeOut(200);
    $(e).parents('.pop-edit-layer').fadeOut(200);
    $(e).parents('.pop-asset-layer').fadeOut(200);
}

//레이어 바깥을 클릭한 경우 닫기
$(document).click(function (event) {
    let clickedElement = $(event.target);
    if($("#shareForm").is(":visible")){
        if (clickedElement.hasClass('add-share-btn')) return false;

        if (!clickedElement.hasClass('pop-layer') && !clickedElement.closest('.pop-layer').length) {
            // 모든 레이어 숨김
            $('.pop-layer').fadeOut(200);
            // $('.btn-close').click();
        }
    }
    if($("#shareEditForm").is(":visible")){
        if (clickedElement.closest('.share-lists').length) return false;
        if (!clickedElement.hasClass('pop-edit-layer') && !clickedElement.closest('.pop-edit-layer').length) {
            // 모든 레이어 숨김
            $('.pop-edit-layer').fadeOut(200);
        }
    }
    if($("#assetDtlForm").is(":visible")){
        // if (clickedElement.closest('.asset-container').length) return false;
        if (!clickedElement.hasClass('pop-asset-layer') && !clickedElement.closest('.pop-asset-layer').length) {
            // 모든 레이어 숨김
            $('.pop-asset-layer').fadeOut(200);
        }
    }
});

//페이지 이동
function movePageList(newPage) {

    let url = new URL(window.location.href);
    url.searchParams.set('page', newPage);
    location.href = url.href;
};

function openDtlShare(tr) {
    let boardId = tr.cells.item(0).textContent
    $.ajax({
        type : 'GET',
        url : '/board/'+boardId+'/show',
        processData: false,
        contentType: false,
        success: function (data) {

            $("#board-asset-asset-id").val(data.boardAssetAssetId);
            $("#board-id").val(data.boardId);
            $("#board-asset-name").val(data.boardAsset.assetName);
            $("#board-teacher-teacher-name").val(data.boardTeacher.teacherName);
            $("#board-carry-in-name").val(data.boardCarryInName);
            $("#board-share-count").val(data.boardShareCount);
            console.log(data.boardAssetReturnYn);
            if(data.boardAssetReturnYn === 'N') {
                $("#pop-edit-content #return-btn").remove();
                $("#pop-edit-content").append($("<button>", {class:'btn', id: 'return-btn', text:'반납', type:'button', onclick:'returnBtn()'}));
            }
        },
        error:function (jqXHR, textStatus, errorThrown) {
            console.log("Request Failed");
            console.log('jqXHR : ', jqXHR);
            console.log('textStatus : ', textStatus);
            console.log('errorThrown : ', errorThrown);
        }
    })
    layerPopOpen('shareEditForm');
}