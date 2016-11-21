package com.example.nmarcantonio.flysys2;

/**
 * Created by Nicolas on 11/21/2016.
 */

public class OfferInfo {

    private String id;
    private String number;
    private String srcAir;
    private String dstAir;
    private String price;
    private String depDate;
    private String arrDate;

    public String getDepDate() {
        return depDate;
    }

    public String getArrDate() {
        return arrDate;
    }

    public OfferInfo(String id, String number, String srcAir, String dstAir, String price, String depDate, String arrDate) {
        this.id = id;
        this.number = number;
        this.srcAir = srcAir;
        this.dstAir = dstAir;
        this.price = price;
        this.depDate = depDate;
        this.arrDate = arrDate;
    }


    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDstAir() {
        return dstAir;
    }

    public void setDstAir(String dstAir) {
        this.dstAir = dstAir;
    }

    public String getSrcAir() {
        return srcAir;
    }

    public void setSrcAir(String srcAir) {
        this.srcAir = srcAir;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

