package com.kuui.kas.common.utils;

import org.springframework.stereotype.Component;

@Component
public class PagingUtil {
    // 화면에 출력할 페이지 버튼 수
    public static int showBtnCnt = 10;

    // 화면에 출력할 시작 페이지
    public static int getStartPage(int currentPage) {

        return (int)Math.ceil(currentPage / (double)showBtnCnt) * showBtnCnt - (showBtnCnt - 1);
    }

    // 화면에 출력할 마지막 페이지
    public static int getEndPage(int startPage, int totalPage) {

        return Math.min(startPage + showBtnCnt - 1, totalPage);
    }
}
