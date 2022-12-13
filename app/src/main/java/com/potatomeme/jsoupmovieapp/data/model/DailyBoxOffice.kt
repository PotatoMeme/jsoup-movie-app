package com.potatomeme.jsoupmovieapp.data.model


import com.google.gson.annotations.SerializedName

data class DailyBoxOffice(
    @SerializedName("audiAcc")
    val audiAcc: String,//누적관객수를 출력합니다.
    @SerializedName("audiChange")
    val audiChange: String,//전일 대비 관객수 증감 비율을 출력합니다.
    @SerializedName("audiCnt")
    val audiCnt: String,//해당일의 관객수를 출력합니다.
    @SerializedName("audiInten")
    val audiInten: String,//전일 대비 관객수 증감분을 출력합니다.
    @SerializedName("movieCd")
    val movieCd: String,//영화의 대표코드를 출력합니다.
    @SerializedName("movieNm")
    val movieNm: String,//영화명(국문)을 출력합니다.
    @SerializedName("openDt")
    val openDt: String,//영화의 개봉일을 출력합니다.
    @SerializedName("rank")
    val rank: String,//해당일자의 박스오피스 순위를 출력합니다.
    @SerializedName("rankInten")
    val rankInten: String,//전일대비 순위의 증감분을 출력합니다.
    @SerializedName("rankOldAndNew")
    val rankOldAndNew: String,//랭킹에 신규진입여부를 출력합니다.“OLD” : 기존 , “NEW” : 신규
    @SerializedName("rnum")
    val rnum: String,//순번을 출력합니다.
    @SerializedName("salesAcc")
    val salesAcc: String,//누적매출액을 출력합니다.
    @SerializedName("salesAmt")
    val salesAmt: String,//해당일의 매출액을 출력합니다.
    @SerializedName("salesChange")
    val salesChange: String,//전일 대비 매출액 증감 비율을 출력합니다.
    @SerializedName("salesInten")
    val salesInten: String,//전일 대비 매출액 증감분을 출력합니다.
    @SerializedName("salesShare")
    val salesShare: String,//해당일자 상영작의 매출총액 대비 해당 영화의 매출비율을 출력합니다.
    @SerializedName("scrnCnt")
    val scrnCnt: String,//해당일자에 상영한 스크린수를 출력합니다.
    @SerializedName("showCnt")
    val showCnt: String//해당일자에 상영된 횟수를 출력합니다.
)