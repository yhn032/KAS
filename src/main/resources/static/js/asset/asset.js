$(document).ready(function(){
    /**
     * 자산 검색 키보드 이벤트
     */
    $("#searchTerm").keydown(function (event){
        if (event.key === 'Enter') {
            event.preventDefault();
            search();
        }
    }) ;


    /**
     * 상품 상세보기 팝업
     *
     */
    // .asset-container 요소에 클릭 이벤트 리스너 추가, 모든 .asset-item의 하위 요소로 이벤트 위임
    $('.asset-container').on('click', '.asset-item', function() {
        // event.currentTarget을 통해 클릭된 .asset-item 요소를 참조
        let assetId = $(this).find('div').first().text();
        $("#assetThumbImg").empty();

        $.ajax({
            type : 'GET',
            url : '/asset/'+assetId+'/show',
            processData: false,
            contentType: false,
            success: function (data) {

                console.log(data);
                $("#assetId").text(data.assetId);
                $("#assetCtg").text(data.assetCtg);
                for(let i = 1; i<=data.assetImgs.length; i++) {
                    let span = $("<span>");
                    let img = $("<img>", {id:"assetImg"+i, src: "/img/uploads/asset/"+data.assetImgs[i-1].saveName});
                    span.append(img);
                    $("#assetThumbImg").append(span);
                }
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

    /**
     * 선택한 사진 미리보기
     * @type {HTMLElement}
     */
    const fileInput = document.getElementById("assetImgFile");
    $('#assetImgFile').change(function(){
        $("#assetImg1").attr("src", "/img/uploads/profile/default.png");
        $("#assetImg2").attr("src", "/img/uploads/profile/default.png");
        $("#assetImg3").attr("src", "/img/uploads/profile/default.png");

        let files = $(this)[0].files;
        if( files.length > 3) {
            alert("You can upload up to 3 images only");
            $(this).val('');
            return;
        }

        for(let i =0; i<files.length; i++) {
            let upload_photo = this.files[i];

            let reader = new FileReader();
            reader.readAsDataURL(upload_photo);

            reader.onload = function(){
                $("#assetImg"+(i+1)).attr("src", reader.result);
            };
        }

    });

    /**
     * 자산 수정시 등록된 이미지의 개수 파악하여 기본 이미지 세팅하기
     */
    let imgCnt = $(".image-list img").length;
    const imageList = $(".image-list");
    if(imgCnt < 3) {
        for(let i=imgCnt+1; i<=3; i++) {
            let img = $("<img>", {id: 'assetImg' + i,width: '300px', height: '300px', src:'/img/uploads/profile/default.png'});
            imageList.append(img);
        }
    }

});



function search(){
    const formData = new FormData(document.getElementById("searchForm"));
    location.href='/asset/allList?searchTerm=' + formData.get("searchTerm") + '&page=1&pageUnit=10';
}

/**
 * 자산 검색 결과 렌더링 비동기 방식
 * @param resultArray
 */
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

/**
 * 자산 상세보기 수정
 */
function modifyAssetForm(){
    if(confirm("자산 수정 폼으로 가시겠습니까?")) {
        let assetId = $("#assetId").text();
        location.href ="/asset/"+assetId+"/modify";
    }else {
        return;
    }
}

/**
 * 자산 상세보기 삭제
 */
function deleteAsset() {
    if(confirm("자산이 삭제됩니다. 삭제 처리 하시겠습니까?")) {
        let assetId = $("#assetId").text();
        $.ajax({
            type : 'GET',
            url : '/asset/'+assetId+'/delete',
            processData: false,
            contentType: false,
            success: function (response) {
                console.log(response)
                alert(response + "!! 자산이 삭제 되었습니다.");
                location.reload();
            },
            error:function (jqXHR, textStatus, errorThrown) {
                console.log("Request Failed");
                console.log('jqXHR : ', jqXHR);
                console.log('textStatus : ', textStatus);
                console.log('errorThrown : ', errorThrown);
                alert(jqXHR.responseJSON.status + " : " + jqXHR.responseJSON.error +"\n " +jqXHR.responseJSON.message);
            }
        });
    }else {
        alert("삭제가 취소되었습니다.");
    }
}

/**
 * 자산 등록 함수
 */
function addAsset(){
    //FormData 객체를 사용하여 form 데이터 수집
    const formData = new FormData(document.getElementById("assetForm"));
    let files = $("#assetImgFile")[0].files;
    if( files.length > 3) {
        alert("You can upload up to 3 images only");
        $(this).val('');
        return;
    }

    for(let i =0; i<files.length; i++) {
        formData.append('assetImgFile' + (i+1), files[i]);
    }

    const data = {
        info: {
            assetName : $("#assetName").val(),
            assetTotalCnt : $("#assetCnt").val(),
            assetRemainCnt : $("#assetCnt").val(),
            assetPos : $("#assetPos").val(),
            assetCtg : $("#assetCtg").val(),
            regTeacherName : $("#regTeacherName").val()
        }
    };

    formData.append("assetDto", new Blob([JSON.stringify(data.info)], {type : "application/json"}));
    console.log(formData);

    $.ajax({
        type : 'POST',
        url : '/asset/addList',
        data : formData,
        processData: false,
        contentType: false,
        success: function (response) {

            alert("자산이 등록 되었습니다.");
            location.href = '/asset/allList';
        },
        error:function (jqXHR, textStatus, errorThrown) {
            console.log("Request Failed");
            console.log('jqXHR : ', jqXHR);
            console.log('textStatus : ', textStatus);
            console.log('errorThrown : ', errorThrown);
        }
    })
}


/**
 * 자산 수정 함수
 */
function modifyAsset(){
    let assetId = $("#assetId").text();
    //FormData 객체를 사용하여 form 데이터 수집
    const formData = new FormData(document.getElementById("modifyForm"));
    let files = $("#assetImgFile")[0].files;
    if( files.length > 3) {
        alert("You can upload up to 3 images only");
        $(this).val('');
        return;
    }

    for(let i =0; i<files.length; i++) {
        formData.append('assetImgFile' + (i+1), files[i]);
    }

    const data = {
        info: {
            assetId : $("#assetId").val(),
            assetName : $("#assetName").val(),
            assetTotalCnt : $("#assetCnt").val(),
            assetRemainCnt : $("#assetCnt").val(),
            assetPos : $("#assetPos").val(),
            assetCtg : $("#assetCtg").val(),
            regTeacherName : $("#regTeacherName").val(),
            updTeacherName : $("#updTeacherName").val()
        }
    };

    formData.append("assetDto", new Blob([JSON.stringify(data.info)], {type : "application/json"}));
    console.log(formData);

    $.ajax({
        type : 'POST',
        url : '/asset/modify',
        data : formData,
        processData: false,
        contentType: false,
        success: function (response) {

            alert("자산이 수정 되었습니다.");
            location.href = '/asset/allList';
        },
        error:function (jqXHR, textStatus, errorThrown) {
            console.log("Request Failed");
            console.log('jqXHR : ', jqXHR);
            console.log('textStatus : ', textStatus);
            console.log('errorThrown : ', errorThrown);
            alert(jqXHR.responseJSON.status + " : " + jqXHR.responseJSON.error +"\n " +jqXHR.responseJSON.message);
        }
    })
}