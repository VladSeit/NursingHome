package model;

import java.time.LocalDate;

public class Pfleger extends Person{
    private long pfid;
    private LocalDate dateOfBirth;
    private String login;
    private String password;
    private String isLocked;
    private LocalDate dateOfLocking;
    public Pfleger(long pfid, String firstName, String surname, LocalDate dateOfBirth, String login, String password
            , String isLocked, LocalDate dateOfLocking){
        super(firstName, surname);
        this.pfid=pfid;
        this.dateOfBirth=dateOfBirth;
        this.login=login;
        this.password=password;
        this.isLocked=isLocked;
        this.dateOfLocking=dateOfLocking;
    }
    public long getPfid() {
        return pfid;
    }

    public void setPfid(long pfid) {
        this.pfid = pfid;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
    }

    public LocalDate getDateOfLocking() {
        return dateOfLocking;
    }

    public void setDateOfLocking(LocalDate dateOfLocking) {
        this.dateOfLocking = dateOfLocking;
    }
}
