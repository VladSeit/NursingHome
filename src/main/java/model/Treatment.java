package model;

import utils.DateConverter;
import java.time.LocalDate;
import java.time.LocalTime;

public class Treatment {
    private long tid;
    private long pid;
    private LocalDate date;
    private LocalTime begin;
    private LocalTime end;
    private String description;
    private String remarks;
    private long pfid;
    private String pflegerSurname;
    private String isLocked;
    private LocalDate dateOfLocking;

    public Treatment(long pid, LocalDate date, LocalTime begin,
                     LocalTime end, String description, String remarks) {
        this.pid = pid;
        this.date = date;
        this.begin = begin;
        this.end = end;
        this.description = description;
        this.remarks = remarks;
    }

    public Treatment(long tid, long pid, LocalDate date, LocalTime begin,
                     LocalTime end, String description, String remarks, long pfid,
                     String pflegerSurname,String isLocked, LocalDate dateOfLocking) {
        this.tid = tid;
        this.pid = pid;
        this.date = date;
        this.begin = begin;
        this.end = end;
        this.description = description;
        this.remarks = remarks;
        this.pfid=pfid;
        this.pflegerSurname=pflegerSurname;
        this.isLocked=isLocked;
        this.dateOfLocking=dateOfLocking;
    }

    public long getTid() {
        return tid;
    }

    public long getPid() {
        return this.pid;
    }

    public String getDate() {
        return date.toString();
    }

    public String getBegin() {
        return begin.toString();
    }

    public String getEnd() {
        return end.toString();
    }

    public void setDate(String s_date) {
        LocalDate date = DateConverter.convertStringToLocalDate(s_date);
        this.date = date;
    }

    public void setBegin(String begin) {
        LocalTime time = DateConverter.convertStringToLocalTime(begin);
        this.begin = time;
    }

    public void setEnd(String end) {
        LocalTime time = DateConverter.convertStringToLocalTime(end);
        this.end = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setBegin(LocalTime begin) {
        this.begin = begin;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public long getPfid() {
        return pfid;
    }

    public void setPfid(long pfid) {
        this.pfid = pfid;
    }

    public String getPflegerSurname() {
        return pflegerSurname;
    }

    public void setPflegerSurname(String pflegerSurname) {
        this.pflegerSurname = pflegerSurname;
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
    public String toString() {
        return "\nBehandlung" + "\nTID: " + this.tid +
                "\nPID: " + this.pid +
                "\nDate: " + this.date +
                "\nBegin: " + this.begin +
                "\nEnd: " + this.end +
                "\nDescription: " + this.description +
                "\nRemarks: " + this.remarks + "\n"+
                "\nPfID: " + this.pid + "\n"+
                "\nPleger Surname" + this.pflegerSurname+ "\n";
    }
}