package com.base444.android.headsuptaichung.model;

import java.util.Date;

import io.realm.RealmObject;

public class ApplicationCase extends RealmObject {

    private String 申請書編號;

    private String 廠商名稱;

    private String 許可證編號;

    private String 地點;

    private String locatoin_x;

    private String location_y;

    private String 工程名稱;

    private String 承辦人電話;

    private String 核准迄日;

    private String 申請單位;

    private String 區域名稱;

    private String 廠商電話;

    private String 管線工程類別;

    private String 是否開工;

    private String 承辦人;

    private String 案件類別;

    private String area_location;

    private String 核准起日;

    private Date startDate;

    private Date dueDate;

    public String get申請書編號 ()
    {
        return 申請書編號;
    }

    public void set申請書編號 (String 申請書編號)
    {
        this.申請書編號 = 申請書編號;
    }

    public String get廠商名稱 ()
    {
        return 廠商名稱;
    }

    public void set廠商名稱 (String 廠商名稱)
    {
        this.廠商名稱 = 廠商名稱;
    }

    public String get許可證編號 ()
    {
        return 許可證編號;
    }

    public void set許可證編號 (String 許可證編號)
    {
        this.許可證編號 = 許可證編號;
    }

    public String get地點 ()
    {
        return 地點;
    }

    public void set地點 (String 地點)
    {
        this.地點 = 地點;
    }

    public String getLocatoin_x()
    {
        return locatoin_x;
    }

    public void setLocatoin_x(String locatoin_x)
    {
        this.locatoin_x = locatoin_x;
    }

    public String getLocation_y()
    {
        return location_y;
    }

    public void setLocation_y(String location_y)
    {
        this.location_y = location_y;
    }

    public String get工程名稱 ()
    {
        return 工程名稱;
    }

    public void set工程名稱 (String 工程名稱)
    {
        this.工程名稱 = 工程名稱;
    }

    public String get承辦人電話 ()
    {
        return 承辦人電話;
    }

    public void set承辦人電話 (String 承辦人電話)
    {
        this.承辦人電話 = 承辦人電話;
    }

    public String get核准迄日 ()
    {
        return 核准迄日;
    }

    public void setupDate(){
        String day = 核准迄日.substring(核准迄日.length() - 2);
        String month = 核准迄日.substring(核准迄日.length()-4, 核准迄日.length()-2);
        String year = 核准迄日.substring(0, 核准迄日.length()-4);
        dueDate = new Date((11 + Integer.parseInt(year)), Integer.parseInt(month) - 1, Integer.parseInt(day));

        day = 核准起日.substring(核准起日.length() - 2);
        month = 核准起日.substring(核准起日.length()-4, 核准起日.length()-2);
        year = 核准起日.substring(0, 核准起日.length()-4);

        startDate = new Date((11 + Integer.parseInt(year)), Integer.parseInt(month) - 1, Integer.parseInt(day));
    }

    public void set核准迄日 (String 核准迄日)
    {
        this.核准迄日 = 核准迄日;
    }

    public String get申請單位 ()
    {
        return 申請單位;
    }

    public void set申請單位 (String 申請單位)
    {
        this.申請單位 = 申請單位;
    }

    public String get區域名稱 ()
    {
        return 區域名稱;
    }

    public void set區域名稱 (String 區域名稱)
    {
        this.區域名稱 = 區域名稱;
    }

    public String get廠商電話 ()
    {
        return 廠商電話;
    }

    public void set廠商電話 (String 廠商電話)
    {
        this.廠商電話 = 廠商電話;
    }

    public String get管線工程類別 ()
    {
        return 管線工程類別;
    }

    public void set管線工程類別 (String 管線工程類別)
    {
        this.管線工程類別 = 管線工程類別;
    }

    public String get是否開工 ()
    {
        return 是否開工;
    }

    public void set是否開工 (String 是否開工)
    {
        this.是否開工 = 是否開工;
    }

    public String get承辦人 ()
    {
        return 承辦人;
    }

    public void set承辦人 (String 承辦人)
    {
        this.承辦人 = 承辦人;
    }

    public String get案件類別 ()
    {
        return 案件類別;
    }

    public void set案件類別 (String 案件類別)
    {
        this.案件類別 = 案件類別;
    }

    public String getArea_location()
    {
        return area_location;
    }

    public void setArea_location(String area_location)
    {
        this.area_location = area_location;
    }

    public String get核准起日 ()
    {
        return 核准起日;
    }

    public void set核准起日 (String 核准起日)
    {
        this.核准起日 = 核准起日;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [申請書編號 = "+申請書編號+", 廠商名稱 = "+廠商名稱+", 許可證編號 = "+許可證編號+", 地點 = "+地點+", locatoin_x = "+ locatoin_x +", location_y = "+ location_y +", 工程名稱 = "+工程名稱+", 承辦人電話 = "+承辦人電話+", 核准迄日 = "+核准迄日+", 申請單位 = "+申請單位+", 區域名稱 = "+區域名稱+", 廠商電話 = "+廠商電話+", 管線工程類別 = "+管線工程類別+", 是否開工 = "+是否開工+", 承辦人 = "+承辦人+", 案件類別 = "+案件類別+", area_location = "+ area_location +", 核准起日 = "+核准起日+"]";
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
