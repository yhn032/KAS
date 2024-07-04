$(document).ready(function(){
   $("#searchTerm").keydown(function (event){
       if (event.key === 'Enter') {
           event.preventDefault();
           search();
       }
   }) ;
});

function search(){
    if($("#searchTerm").val() === '' ) {
        alert("검색어를 입력하세요.");
        return false;
    }
    const formData = new FormData(document.getElementById("searchForm"));
    $.ajax({
        type : 'POST',
        url : '/asset/searchAsset',
        data : formData,
        processData:false,
        contentType:false,
        success:function (data){
            console.log(data);
            console.log((data.status));
            if(data.totalSize > 0) {
                renderingSearchResult(data.resultArray);
            }else {
                $('.asset-container').empty();
                const asseterror = $("<div>", {class:'asset-error', text:'검색결과가 없습니다.'});
                $('.asset-container').append(asseterror);
                console.log("검색 결과가 없습니다.");
            }
        }
    });
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
        const span3 = $('<span>', {text:'🔗 ' + resultArray[i].assetResult.assetCnt});
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


function layerPopOpen(id) {
    $('#' + id).fadeIn(200);
}

function layerPopHide(e) {
    $(e).parents('.pop-layer').fadeOut(200);
}

/*
$(document).on('click', function(event) {
    if (!$(event.target).closest('.pop-layer').length && !$(event.target).is('#shareForm')) {
        $(".btn_x").click();
    }
});*/

function loadAssetData(){

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

            }else if (data.status === 'fail') {
                alert(data.errorMessage);
            }

        }
    })
}
