package org.salonmaster.salonmaster.GETSET;

import java.math.BigInteger;

public class Employeelist {
    
    private int id;
    private String ename;
    private BigInteger enumber;
    private String eemail;
    private Integer commission;
    private String eaddress;

    public Employeelist(int id, String ename, BigInteger enumber, String eemail, Integer commission, String eaddress) {
        this.id = id;
        this.ename = ename;
        this.enumber = enumber;
        this.eemail = eemail;
        this.commission = commission;
        this.eaddress = eaddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public BigInteger getEnumber() {
        return enumber;
    }

    public void setEnumber(BigInteger enumber) {
        this.enumber = enumber;
    }

    public String getEemail() {
        return eemail;
    }

    public void setEemail(String eemail) {
        this.eemail = eemail;
    }

    public Integer getCommission() {
        return commission;
    }

    public void setCommission(Integer commission) {
        this.commission = commission;
    }

    public String getEaddress() {
        return eaddress;
    }

    public void setEaddress(String eaddress) {
        this.eaddress = eaddress;
    }

   
   
}
