package com.mycompany.mikedev.service.dto;

import com.mycompany.mikedev.domain.enumeration.Sexe;

public class TokenPayloadDTO {

    private Long employeeId;
    private String firstName;
    private String LastName;
    private Sexe gender;
    private String login;

    private Long jobId;
    private String jobName;

    private Long idAttendance;
    private Boolean Attendance;





    /**
     * @return Long return the employeeId
     */
    public Long getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return String return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return String return the LastName
     */
    public String getLastName() {
        return LastName;
    }

    /**
     * @param LastName the LastName to set
     */
    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    /**
     * @return String return the gender
     */
    public Sexe getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(Sexe gender) {
        this.gender = gender;
    }

    /**
     * @return Long return the jobId
     */
    public Long getJobId() {
        return jobId;
    }

    /**
     * @param jobId the jobId to set
     */
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    /**
     * @return String return the jobName
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * @param jobName the jobName to set
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * @return Long return the idAttendancel
     */
    public Long getIdAttendance() {
        return idAttendance;
    }

    /**
     * @param idAttendancel the idAttendancel to set
     */
    public void setIdAttendance(Long idAttendancel) {
        this.idAttendance = idAttendancel;
    }

    /**
     * @return Boolean return the Attendance
     */
    public Boolean isAttendance() {
        return Attendance;
    }

    /**
     * @param Attendance the Attendance to set
     */
    public void setAttendance(Boolean Attendance) {
        this.Attendance = Attendance;
    }


    /**
     * @return String return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

}