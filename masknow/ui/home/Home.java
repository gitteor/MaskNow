package com.booknbooks.maskinfo.ui.home;


import java.io.Serializable;

public class Home implements Serializable {
    private String name;
    private String address;
    private String remain;
    private String stock;
    private String stock_at;

    private Double latitude;
    private Double longitude;

    private String type;

    public String remain_inKorean;


    public  Home(){}
    public Home(String name, String address, String remain, String stock, String type){
        this.name = name;
        this.address = address;
        this.stock = stock ;
        this.remain = remain;
        this.type = type;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getaddress() {
        return address;
    }
    public void setaddress(String address) {
        this.address = address;
    }

    public String getremain() {
        return remain;
    }
    public void setremain(String remain) {
        this.remain = remain;
    }

    public String getstock() {
        return stock;
    }
    public void setstock(String stock) { this.stock = stock; }

    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }



    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }


    public String getRemain_inKorean() {
        switch (remain){

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
                remain_inKorean ="알 수 없음";
                break;
        }
        return remain_inKorean;
    }



    public String getstock_at() {

        if(stock.equals("null")){
            stock_at = new String("알 수 없음");
        }else {
            String[] str = stock.substring(5,16).split(" ");
            stock_at = str[0].replace("/","월 ") + "일 "
                    + str[1];

        }
        return stock_at;
    }

    public void setstock_at(String stock_at) { this.stock_at = stock_at; }


    @Override
    public String toString() {

        switch (remain){

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
                remain_inKorean ="알 수 없음";
                break;
        }

        if(stock.equals("null")){
            stock_at = new String("알 수 없음");
        }else {

            stock_at = stock.substring(5,16);

        }

        return  name + "\n" +
                address + "\n" +
                //   " 위도 : " +String.valueOf(lat) + "\n" +
                //   " 경도 : " +String.valueOf(lng) + "\n" +
                " 입고 시간 :  " + stock_at+"\n" +
                " 재고 상태 :  " + remain_inKorean ;
    }



}