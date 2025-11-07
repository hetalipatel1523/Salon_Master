package org.salonmaster.salonmaster.GETSET;

public class Adminclient {
    private String c_name;
    private String s_name;
     private String email;
    private String phone_no;
    private String gst;
    private String password;
    private String address;

    public Adminclient(String c_name, String s_name, String email, String phone_no,  String gst, String password, String address) {
        this.c_name = c_name;
        this.s_name = s_name;
        this.email = email;
        this.phone_no = phone_no;
        this.password = password;
        this.gst = gst;
        this.address = address;
        
      
    }

    // Getter and setter methods for all fields
    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }
    
    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
