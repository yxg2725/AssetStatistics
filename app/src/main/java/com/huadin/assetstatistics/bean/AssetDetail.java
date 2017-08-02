package com.huadin.assetstatistics.bean;

import android.support.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by admin on 2017/7/19.
 */

@Entity
public class AssetDetail  implements Comparable<AssetDetail>{
    @Id
    private Long id;
    private String barcode;//条码号
    private String assetName;//设备名称
    private String enterTime;//入库时间
    private String people;//检验人
    private String deviceId;//规格型号
    private String usedCompany;//规格型号
    private String manufacturer;//生产厂家
    private String dateOfProduction;//生产日期
    private String inspectionNumber;//检测编号
    private String archivesNumber;//档案编号
    private String checkDate;//检测日期
    private String nextCheckDate;//下次检测日期
    private String checkPeople;//检验员
    private String exist;//存在   出入库标志
    private String imgPath;//图片路径


    @Generated(hash = 357989332)
    public AssetDetail(Long id, String barcode, String assetName, String enterTime,
            String people, String deviceId, String usedCompany,
            String manufacturer, String dateOfProduction, String inspectionNumber,
            String archivesNumber, String checkDate, String nextCheckDate,
            String checkPeople, String exist, String imgPath) {
        this.id = id;
        this.barcode = barcode;
        this.assetName = assetName;
        this.enterTime = enterTime;
        this.people = people;
        this.deviceId = deviceId;
        this.usedCompany = usedCompany;
        this.manufacturer = manufacturer;
        this.dateOfProduction = dateOfProduction;
        this.inspectionNumber = inspectionNumber;
        this.archivesNumber = archivesNumber;
        this.checkDate = checkDate;
        this.nextCheckDate = nextCheckDate;
        this.checkPeople = checkPeople;
        this.exist = exist;
        this.imgPath = imgPath;
    }


    @Generated(hash = 1757241519)
    public AssetDetail() {
    }


    @Override
    public int compareTo(@NonNull AssetDetail o) {
        if (this.getAssetName().compareTo(o.getAssetName()) > 0)
            return 1;
        else if (this.getAssetName().compareTo(o.getAssetName()) == 0)
            return 0;
        else
            return -1;
    }


    public String getImgPath() {
        return this.imgPath;
    }


    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }


    public String getExist() {
        return this.exist;
    }


    public void setExist(String exist) {
        this.exist = exist;
    }


    public String getCheckPeople() {
        return this.checkPeople;
    }


    public void setCheckPeople(String checkPeople) {
        this.checkPeople = checkPeople;
    }


    public String getNextCheckDate() {
        return this.nextCheckDate;
    }


    public void setNextCheckDate(String nextCheckDate) {
        this.nextCheckDate = nextCheckDate;
    }


    public String getCheckDate() {
        return this.checkDate;
    }


    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }


    public String getArchivesNumber() {
        return this.archivesNumber;
    }


    public void setArchivesNumber(String archivesNumber) {
        this.archivesNumber = archivesNumber;
    }


    public String getInspectionNumber() {
        return this.inspectionNumber;
    }


    public void setInspectionNumber(String inspectionNumber) {
        this.inspectionNumber = inspectionNumber;
    }


    public String getDateOfProduction() {
        return this.dateOfProduction;
    }


    public void setDateOfProduction(String dateOfProduction) {
        this.dateOfProduction = dateOfProduction;
    }


    public String getManufacturer() {
        return this.manufacturer;
    }


    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }


    public String getUsedCompany() {
        return this.usedCompany;
    }


    public void setUsedCompany(String usedCompany) {
        this.usedCompany = usedCompany;
    }


    public String getDeviceId() {
        return this.deviceId;
    }


    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


    public String getPeople() {
        return this.people;
    }


    public void setPeople(String people) {
        this.people = people;
    }


    public String getEnterTime() {
        return this.enterTime;
    }


    public void setEnterTime(String enterTime) {
        this.enterTime = enterTime;
    }


    public String getAssetName() {
        return this.assetName;
    }


    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }


    public String getBarcode() {
        return this.barcode;
    }


    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }



}
