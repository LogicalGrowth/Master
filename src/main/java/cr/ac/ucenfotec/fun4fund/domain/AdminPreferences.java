package cr.ac.ucenfotec.fun4fund.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A AdminPreferences.
 */
@Entity
@Table(name = "admin_preferences")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AdminPreferences implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "inactivity_time", nullable = false)
    private Integer inactivityTime;

    @NotNull
    @Column(name = "notification_recurrence", nullable = false)
    private Integer notificationRecurrence;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getInactivityTime() {
        return inactivityTime;
    }

    public AdminPreferences inactivityTime(Integer inactivityTime) {
        this.inactivityTime = inactivityTime;
        return this;
    }

    public void setInactivityTime(Integer inactivityTime) {
        this.inactivityTime = inactivityTime;
    }

    public Integer getNotificationRecurrence() {
        return notificationRecurrence;
    }

    public AdminPreferences notificationRecurrence(Integer notificationRecurrence) {
        this.notificationRecurrence = notificationRecurrence;
        return this;
    }

    public void setNotificationRecurrence(Integer notificationRecurrence) {
        this.notificationRecurrence = notificationRecurrence;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdminPreferences)) {
            return false;
        }
        return id != null && id.equals(((AdminPreferences) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdminPreferences{" +
            "id=" + getId() +
            ", inactivityTime=" + getInactivityTime() +
            ", notificationRecurrence=" + getNotificationRecurrence() +
            "}";
    }
}
