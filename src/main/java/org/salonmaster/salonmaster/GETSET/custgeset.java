package org.salonmaster.salonmaster.GETSET;

import java.sql.Date;

public class custgeset {

    int id;
    String name;
    String phone_no;
    Date c_birthday;
    Date c_anniversary;
    String gender;
    Integer reward;

    public custgeset(int id,String name, String phone_no, Date c_birthday, Date c_anniversary, String gender, Integer reward) {
        this.id = id;
        this.name = name;
        this.phone_no = phone_no;
        this.c_birthday = c_birthday;
        this.c_anniversary = c_anniversary;
        this.gender = gender;
        this.reward = reward;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public Date getC_birthday() {
        return c_birthday;
    }

    public void setC_birthday(Date c_birthday) {
        this.c_birthday = c_birthday;
    }

    public Date getC_anniversary() {
        return c_anniversary;
    }

    public void setC_anniversary(Date c_anniversary) {
        this.c_anniversary = c_anniversary;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getReward() {
        return reward;
    }

    public void setReward(Integer reward) {
        this.reward = reward;
    }

 
    

   
}
