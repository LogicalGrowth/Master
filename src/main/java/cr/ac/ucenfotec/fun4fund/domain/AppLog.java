package cr.ac.ucenfotec.fun4fund.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

import cr.ac.ucenfotec.fun4fund.domain.enumeration.ActionType;

/**
 * A AppLog.
 */
@Entity
@Table(name = "app_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AppLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "time_stamp", nullable = false)
    private ZonedDateTime timeStamp;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private ActionType action;

    @NotNull
    @Column(name = "user", nullable = false)
    private String user;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getTimeStamp() {
        return timeStamp;
    }

    public AppLog timeStamp(ZonedDateTime timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public void setTimeStamp(ZonedDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ActionType getAction() {
        return action;
    }

    public AppLog action(ActionType action) {
        this.action = action;
        return this;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public String getUser() {
        return user;
    }

    public AppLog user(String user) {
        this.user = user;
        return this;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public AppLog description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppLog)) {
            return false;
        }
        return id != null && id.equals(((AppLog) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppLog{" +
            "id=" + getId() +
            ", timeStamp='" + getTimeStamp() + "'" +
            ", action='" + getAction() + "'" +
            ", user='" + getUser() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
