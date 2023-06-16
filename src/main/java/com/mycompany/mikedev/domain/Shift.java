package com.mycompany.mikedev.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.mikedev.domain.enumeration.RequestStatus;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Shift.
 */
@Entity
@Table(name = "shift")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Shift extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "shift_date")
    private Instant shiftDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "shift_type")
    private RequestStatus shiftType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "job" }, allowSetters = true)
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Shift id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getShiftDate() {
        return this.shiftDate;
    }

    public Shift shiftDate(Instant shiftDate) {
        this.setShiftDate(shiftDate);
        return this;
    }

    public void setShiftDate(Instant shiftDate) {
        this.shiftDate = shiftDate;
    }

    public RequestStatus getShiftType() {
        return this.shiftType;
    }

    public Shift shiftType(RequestStatus shiftType) {
        this.setShiftType(shiftType);
        return this;
    }

    public void setShiftType(RequestStatus shiftType) {
        this.shiftType = shiftType;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Shift employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Shift)) {
            return false;
        }
        return id != null && id.equals(((Shift) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Shift{" +
            "id=" + getId() +
            ", shiftDate='" + getShiftDate() + "'" +
            ", shiftType='" + getShiftType() + "'" +
            "}";
    }
}
