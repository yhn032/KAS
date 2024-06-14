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