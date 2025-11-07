package org.salonmaster.salonmaster.GETSET;
import java.time.LocalDate;

public class APBLIST {

    int id;
    String name;
    String phone_no;
    LocalDate date;
    String time;

    public APBLIST(int id,String name, String phone_no, LocalDate date, String time) {
        this.id = id;
        this.name = name;
        this.phone_no = phone_no;
        this.date = date;
        this.time = time;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

   
 
}
