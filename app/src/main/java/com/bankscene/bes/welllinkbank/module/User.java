package com.bankscene.bes.welllinkbank.module;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 6862183887551416267L;

    //memberId userName
    private String memberId;
    private String userId;

    //头像
    private String image = "";
    //身份证
    private String IDCard;
    //真实姓名
    private String name;
    //电话
    private String mobileNum;
    //会员等级
    private String level;
    //银行卡列表

    private String loginCount;
    private String loginDate;

    private String point = "";
    private String grow ="";

    private String deviceID = "";
    private String memId;

    private String bindDeviceCardNo;
    private String bindDevicePhoneNo;
    private String address = "";
    private String english_name="";
    private String image_base64="";
    private boolean isGestureOpen=false;
    private boolean isGestureSetted=false;

    public boolean isGestureOpen() {
        return isGestureOpen;
    }

    public void setGestureOpen(boolean gestureOpen) {
        isGestureOpen = gestureOpen;
    }

    public boolean isGestureSetted() {
        return isGestureSetted;
    }

    public void setGestureSetted(boolean gestureSetted) {
        isGestureSetted = gestureSetted;
    }

    public String getImage_base64() {
        return image_base64;
    }

    public void setImage_base64(String image_base64) {
        this.image_base64 = image_base64;
    }

    public String getEnglish_name() {
        return english_name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    private String image_path;

    public User() {
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setLoginCount(String loginCount) {
        this.loginCount = loginCount;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public void setGrow(String grow) {
        this.grow = grow;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public void setMemId(String memId) {
        this.memId = memId;
    }

    public void setBindDeviceCardNo(String bindDeviceCardNo) {
        this.bindDeviceCardNo = bindDeviceCardNo;
    }

    public void setBindDevicePhoneNo(String bindDevicePhoneNo) {
        this.bindDevicePhoneNo = bindDevicePhoneNo;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getUserId() {
        return userId;
    }

    public String getImage() {
        return image;
    }

    public String getIDCard() {
        return IDCard;
    }

    public String getName() {
        return name;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public String getLevel() {
        return level;
    }

    public String getLoginCount() {
        return loginCount;
    }

    public String getLoginDate() {
        return loginDate;
    }

    public String getPoint() {
        return point;
    }

    public String getGrow() {
        return grow;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public String getMemId() {
        return memId;
    }

    public String getBindDeviceCardNo() {
        return bindDeviceCardNo;
    }

    public String getBindDevicePhoneNo() {
        return bindDevicePhoneNo;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "UserId=="+getUserId()+"-------------isOpen"+isGestureOpen()+"-------------isSetted"+isGestureSetted();
    }
}
