package cr.ac.ucenfotec.fun4fund.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A UserPreferences.
 */
@Entity
@Table(name = "user_preferences")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserPreferences implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "notifications", nullable = false)
    private Boolean notifications;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isNotifications() {
        return notifications;
    }

    public UserPreferences notifications(Boolean notifications) {
        this.notifications = notifications;
        return this;
    }

    public void setNotifications(Boolean notifications) {
        this.notifications = notifications;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserPreferences)) {
            return false;
        }
        return id != null && id.equals(((UserPreferences) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserPreferences{" +
            "id=" + getId() +
            ", notifications='" + isNotifications() + "'" +
            "}";
    }
}
