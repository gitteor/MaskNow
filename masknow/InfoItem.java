package com.booknbooks.maskinfo;


public class InfoItem {


    public String name;
    public String addr;

    public Double lat;
    public Double lng;

    public String stock_at;
    public String stock;
    public String remain_stat;

    public String type;


    public String remain_inKorean;







    @Override
    public String toString() {

        switch (remain_stat){

            case "plenty":
                remain_inKorean = "100개 이상";
                break;
            case "some":
                remain_inKorean = "30개 이상 100개미만";
                break;
            case "few":
                remain_inKorean = "2개 이상 30개 미만";
                break;
            case "empty":
                remain_inKorean = "없음";
                break;
            case "break":
                remain_inKorean = "판매중지";
                break;
            default:
                remain_inKorean = "알 수 없음";
                break;
        }


        if(stock_at=="null") {
            stock_at = "알 수 없음";
        } else {
            stock = stock_at.substring(5,16);
        }

        return  name + "\n" +
                addr + "\n" +
                //   " 위도 : " +String.valueOf(lat) + "\n" +
                //   " 경도 : " +String.valueOf(lng) + "\n" +
                "입고 시간 : " + stock +"\n" +
                "재고 상태 : " + remain_inKorean ;
    }


}




