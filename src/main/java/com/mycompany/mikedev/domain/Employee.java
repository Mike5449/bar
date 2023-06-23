package com.mycompany.mikedev.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.mikedev.domain.enumeration.Sexe;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Employee extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name ="login" , nullable = false)
    private String login;

    @NotNull
    @Column(name ="password" , nullable = false)
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Sexe gender;

    @Column(name = "date_of_birth")
    private Instant dateOfBirth;

    @NotNull
    @Column(name = "contact_number", nullable = false)
    private String contactNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "nif_cin")
    private String nifCin;

    @Column(name = "address")
    private String address;

    @Column(name = "base_salary")
    private Double baseSalary;

    @Column(name = "commission_rate")
    private Double commissionRate;

    @Column(name = "salary")
    private Double salary;

    @Column(name = "hire_date")
    private Instant hireDate;

    @Column(name = "termination_date")
    private Instant terminationDate;

    @ManyToOne
    private Job job;

    @JsonIgnoreProperties(value = { "employee", "authorities" }, allowSetters = true)
    @OneToOne(mappedBy = "employee")
    private User user;

    @JsonIgnoreProperties(value = { "employee", "authorities" }, allowSetters = true)
    @OneToMany(mappedBy = "employee")
    private List< Attendance> attendance;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Employee(){

    }
    
    public Employee(Long id){
        this.id=id;
    }
    public Long getId() {
        return this.id;
    }

    public Employee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Employee firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Employee lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Sexe getGender() {
        return this.gender;
    }

    public Employee gender(Sexe gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Sexe gender) {
        this.gender = gender;
    }

    public Instant getDateOfBirth() {
        return this.dateOfBirth;
    }

    public Employee dateOfBirth(Instant dateOfBirth) {
        this.setDateOfBirth(dateOfBirth);
        return this;
    }

    public void setDateOfBirth(Instant dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getContactNumber() {
        return this.contactNumber;
    }

    public Employee contactNumber(String contactNumber) {
        this.setContactNumber(contactNumber);
        return this;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public Employee email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNifCin() {
        return this.nifCin;
    }

    public Employee nifCin(String nifCin) {
        this.setNifCin(nifCin);
        return this;
    }

    public void setNifCin(String nifCin) {
        this.nifCin = nifCin;
    }

    public String getAddress() {
        return this.address;
    }

    public Employee address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getBaseSalary() {
        return this.baseSalary;
    }

    public Employee baseSalary(Double baseSalary) {
        this.setBaseSalary(baseSalary);
        return this;
    }

    public void setBaseSalary(Double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public Double getCommissionRate() {
        return this.commissionRate;
    }

    public Employee commissionRate(Double commissionRate) {
        this.setCommissionRate(commissionRate);
        return this;
    }

    public void setCommissionRate(Double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public Double getSalary() {
        return this.salary;
    }

    public Employee salary(Double salary) {
        this.setSalary(salary);
        return this;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Instant getHireDate() {
        return this.hireDate;
    }

    public Employee hireDate(Instant hireDate) {
        this.setHireDate(hireDate);
        return this;
    }

    public void setHireDate(Instant hireDate) {
        this.hireDate = hireDate;
    }

    public Instant getTerminationDate() {
        return this.terminationDate;
    }

    public Employee terminationDate(Instant terminationDate) {
        this.setTerminationDate(terminationDate);
        return this;
    }

    public void setTerminationDate(Instant terminationDate) {
        this.terminationDate = terminationDate;
    }

    public Job getJob() {
        return this.job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Employee job(Job job) {
        this.setJob(job);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", gender='" + getGender() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", contactNumber='" + getContactNumber() + "'" +
            ", email='" + getEmail() + "'" +
            ", nifCin='" + getNifCin() + "'" +
            ", address='" + getAddress() + "'" +
            ", baseSalary=" + getBaseSalary() +
            ", commissionRate=" + getCommissionRate() +
            ", salary=" + getSalary() +
            ", hireDate='" + getHireDate() + "'" +
            ", terminationDate='" + getTerminationDate() + "'" +
            "}";
    }

    /**
     * @return User return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
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

    /**
     * @return String return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }



    /**
     * @return List< Attendance> return the attendance
     */
    public List< Attendance> getAttendance() {
        return attendance;
    }

    /**
     * @param attendance the attendance to set
     */
    public void setAttendance(List< Attendance> attendance) {
        this.attendance = attendance;
    }

}