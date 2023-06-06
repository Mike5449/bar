package com.mycompany.mikedev.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.mikedev.domain.enumeration.RequestStatus;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LeaveRequest.
 */
@Entity
@Table(name = "leave_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LeaveRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @NotNull
    @Column(name = "en_date", nullable = false)
    private Instant enDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status")
    private RequestStatus requestStatus;

    @ManyToOne
    @JsonIgnoreProperties(value = { "job" }, allowSetters = true)
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LeaveRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public LeaveRequest startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEnDate() {
        return this.enDate;
    }

    public LeaveRequest enDate(Instant enDate) {
        this.setEnDate(enDate);
        return this;
    }

    public void setEnDate(Instant enDate) {
        this.enDate = enDate;
    }

    public RequestStatus getRequestStatus() {
        return this.requestStatus;
    }

    public LeaveRequest requestStatus(RequestStatus requestStatus) {
        this.setRequestStatus(requestStatus);
        return this;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LeaveRequest employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaveRequest)) {
            return false;
        }
        return id != null && id.equals(((LeaveRequest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveRequest{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", enDate='" + getEnDate() + "'" +
            ", requestStatus='" + getRequestStatus() + "'" +
            "}";
    }
}
