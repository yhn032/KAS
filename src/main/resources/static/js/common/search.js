$(document).ready(function(){
   $("#searchTerm").keydown(function (event){
       if (event.key === 'Enter') {
           event.preventDefault();
           search();
       }
   }) ;
});

function search(){

    const formData = new FormData(document.getElementById("searchForm"));
    location.href='/asset/allList?searchTerm=' + formData.get("searchTerm") + '&page=1&pageUnit=10';
    // $.ajax({
    //     type : 'GET',
    //     url : '/asset/allList',
    //     data : formData,
    //     processData:false,
    //     contentType:false,
    //     success:function (data){
    //         console.log(data);
    //         console.log((data.status));
    //         if(data.totalSize > 0) {
    //             renderingSearchResult(data.resultArray);
    //         }else {
    //             $('.asset-container').empty();
    //             const asseterror = $("<div>", {class:'asset-error', text:'검색결과가 없습니다.'});
    //             $('.asset-container').append(asseterror);
    //             console.log("검색 결과가 없습니다.");
    //         }
    //     }
    // });
}

function renderingSearchResult(resultArray){
    const assetContainer = $('.asset-container');
    assetContainer.empty();
    for(let i=0; i<resultArray.length; i++) {
        const assetItem = $('<div>', {class:'asset-item'});
        //ID태그 생성
        const div = $('<div>', {style:'display:none', text:resultArray[i].assetResult.assetId});
        assetItem.append(div);

        //카테고리 태그 생성
        const assetCtg = $('<div>', {class:'asset-category'});
        const ctgData = $('<span>', {text : '🎀 ' + resultArray[i].assetResult.assetCtg});
        assetCtg.append(ctgData);
        assetItem.append(assetCtg);

        //이미지 태그 생성
        const assetThumbImg = $('<div>', {class : 'asset-thumb-img'});
        //let imgSrc = resultArray[i].assetImgSrc;
        // if (resultArray[0].asset)
        const span = $('<span>')
        let imgSrc = resultArray[i].assetImgSrc;
        if(imgSrc != '') {
            imgSrc = '/img/uploads/asset/' + resultArray[i].assetImgSrc;
        }
        const img = $('<img>', {src:imgSrc, width:'250px', height:'250px'});
        span.append(img);
        assetThumbImg.append(span);
        assetItem.append(assetThumbImg);

        //제목 태그 생성
        const assetTitle = $('<div>', {class : 'asset-title'});
        const span2 = $('<span>', {text:'📑 ' + resultArray[i].assetResult.assetName});
        assetTitle.append(span2);
        assetItem.append(assetTitle);

        //메타 데이터 태그 생성
        const assetMetaData = $('<div>', {class : 'asset-meta-data'});
        const span3 = $('<span>', {text:'🔗 ' + resultArray[i].assetResult.assetRemainCnt});
        const span4 = $('<span>', {text:'👉' + resultArray[i].assetResult.assetPos});
        const span5 = $('<span>', {text:'🙌 ' + resultArray[i].assetResult.regTeacherName});
        const span6 = $('<span>', {text:'📆 ' + resultArray[i].assetResult.assetUpdDate.substring(0, 10)});
        const br = $('<br>');
        assetMetaData.append(span3);
        assetMetaData.append(span4);
        assetMetaData.append(span5);
        assetMetaData.append(br);
        assetMetaData.append(span6);
        assetItem.append(assetMetaData);

        assetContainer.append(assetItem);
    }
};

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


/**
 * 상품 상세보기 팝업
 *
 */

$(document).ready(function(){
    // .asset-container 요소에 클릭 이벤트 리스너 추가, 모든 .asset-item의 하위 요소로 이벤트 위임
    $('.asset-container').on('click', '.asset-item', function() {
        // event.currentTarget을 통해 클릭된 .asset-item 요소를 참조
        let assetId = $(this).find('div').first().text();

        $.ajax({
            type : 'GET',
            url : '/asset/'+assetId+'/show',
            processData: false,
            contentType: false,
            success: function (data) {

                console.log(data);
                $("#assetId").text(data.assetId);
                $("#assetCtg").text(data.assetCtg);
                $("#assetThumbImg img").attr("src", "/img/uploads/asset/"+data.assetImgs[0].saveName);
                $("#assetTitle > span").text(data.assetName);
                $("#assetRemainCnt").text(data.assetRemainCnt);
                $("#assetPos").text(data.assetPos);
                $("#regTeacherName").text(data.regTeacherName);
                $("#assetUpdDate").text(data.assetUpdDate);

                if(data.boards.length > 0) {
                    $("#share-list-now").text(data.boards[0].boardAssetReturnYn + 'when ' + data.boards[0].boardCarryInName);
                }
                $("#assetDtlForm").fadeIn(200);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log('Request failed:');
                console.log('jqXHR:', jqXHR);
                console.log('textStatus:', textStatus);
                console.log('errorThrown:', errorThrown);
            }
        })
    });
})

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