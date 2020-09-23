package dearfriend.abhirams.example.com.dearfriend.Model;

import java.io.Serializable;
import java.util.Date;

public class UserDO implements Serializable {

    Integer Id;
    private String userId;
    private String password;
    private String area;
    private String email;
    private String auditDateTime;


    public UserDO(){}

    public UserDO(Integer id, String userId, String password, String area, String email, String auditDateTime) {
        Id = id;
        this.userId = userId;
        this.password = password;
        this.area = area;
        this.email = email;
        this.auditDateTime = auditDateTime;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuditDateTime() {
        return auditDateTime;
    }

    public void setAuditDateTime(String auditDateTime) {
        this.auditDateTime = auditDateTime;
    }
}
