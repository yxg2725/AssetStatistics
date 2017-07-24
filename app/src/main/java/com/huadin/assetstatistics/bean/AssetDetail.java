package com.huadin.assetstatistics.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by admin on 2017/7/19.
 */

@Entity
public class AssetDetail {
    @Id
    private Long id;
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

    @Generated(hash = 1577015206)
    public AssetDetail(Long id, String assetName, String enterTime, String people,
            String deviceId, String usedCompany, String manufacturer,
            String dateOfProduction, String inspectionNumber,
            String archivesNumber, String checkDate, String nextCheckDate,
            String checkPeople) {
        this.id = id;
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
    }

    @Generated(hash = 1757241519)
    public AssetDetail() {
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(String enterTime) {
        this.enterTime = enterTime;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUsedCompany() {
        return usedCompany;
    }

    public void setUsedCompany(String usedCompany) {
        this.usedCompany = usedCompany;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDateOfProduction() {
        return dateOfProduction;
    }

    public void setDateOfProduction(String dateOfProduction) {
        this.dateOfProduction = dateOfProduction;
    }

    public String getInspectionNumber() {
        return inspectionNumber;
    }

    public void setInspectionNumber(String inspectionNumber) {
        this.inspectionNumber = inspectionNumber;
    }

    public String getArchivesNumber() {
        return archivesNumber;
    }

    public void setArchivesNumber(String archivesNumber) {
        this.archivesNumber = archivesNumber;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public String getNextCheckDate() {
        return nextCheckDate;
    }

    public void setNextCheckDate(String nextCheckDate) {
        this.nextCheckDate = nextCheckDate;
    }

    public String getCheckPeople() {
        return checkPeople;
    }

    public void setCheckPeople(String checkPeople) {
        this.checkPeople = checkPeople;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
