package cr.ac.ucenfotec.fun4fund.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import cr.ac.ucenfotec.fun4fund.domain.enumeration.IdType;

/**
 * A ApplicationUser.
 */
@Entity
@Table(name = "application_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApplicationUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 9, max = 13)
    @Column(name = "identification", length = 13, nullable = false)
    private String identification;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "id_type", nullable = false)
    private IdType idType;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private ZonedDateTime birthDate;

    @NotNull
    @Size(min = 8, max = 11)
    @Column(name = "phone_number", length = 11, nullable = false)
    private String phoneNumber;

    @NotNull
    @Column(name = "admin", nullable = false)
    private Boolean admin;

    @OneToOne
    @JoinColumn(unique = true)
    private User internalUser;

    @OneToMany(mappedBy = "owner")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<PaymentMethod> paymentMethods = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Proyect> proyects = new HashSet<>();

    @OneToMany(mappedBy = "applicationUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<DonationHistory> donations = new HashSet<>();

    @OneToMany(mappedBy = "applicationUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Notification> notifications = new HashSet<>();

    @OneToMany(mappedBy = "applicationUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Payment> payments = new HashSet<>();

    @OneToMany(mappedBy = "applicationUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Proyect> favorites = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentification() {
        return identification;
    }

    public ApplicationUser identification(String identification) {
        this.identification = identification;
        return this;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public IdType getIdType() {
        return idType;
    }

    public ApplicationUser idType(IdType idType) {
        this.idType = idType;
        return this;
    }

    public void setIdType(IdType idType) {
        this.idType = idType;
    }

    public ZonedDateTime getBirthDate() {
        return birthDate;
    }

    public ApplicationUser birthDate(ZonedDateTime birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(ZonedDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ApplicationUser phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean isAdmin() {
        return admin;
    }

    public ApplicationUser admin(Boolean admin) {
        this.admin = admin;
        return this;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public User getInternalUser() {
        return internalUser;
    }

    public ApplicationUser internalUser(User user) {
        this.internalUser = user;
        return this;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public Set<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public ApplicationUser paymentMethods(Set<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
        return this;
    }

    public ApplicationUser addPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethods.add(paymentMethod);
        paymentMethod.setOwner(this);
        return this;
    }

    public ApplicationUser removePaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethods.remove(paymentMethod);
        paymentMethod.setOwner(null);
        return this;
    }

    public void setPaymentMethods(Set<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public Set<Proyect> getProyects() {
        return proyects;
    }

    public ApplicationUser proyects(Set<Proyect> proyects) {
        this.proyects = proyects;
        return this;
    }

    public ApplicationUser addProyect(Proyect proyect) {
        this.proyects.add(proyect);
        proyect.setOwner(this);
        return this;
    }

    public ApplicationUser removeProyect(Proyect proyect) {
        this.proyects.remove(proyect);
        proyect.setOwner(null);
        return this;
    }

    public void setProyects(Set<Proyect> proyects) {
        this.proyects = proyects;
    }

    public Set<DonationHistory> getDonations() {
        return donations;
    }

    public ApplicationUser donations(Set<DonationHistory> donationHistories) {
        this.donations = donationHistories;
        return this;
    }

    public ApplicationUser addDonation(DonationHistory donationHistory) {
        this.donations.add(donationHistory);
        donationHistory.setApplicationUser(this);
        return this;
    }

    public ApplicationUser removeDonation(DonationHistory donationHistory) {
        this.donations.remove(donationHistory);
        donationHistory.setApplicationUser(null);
        return this;
    }

    public void setDonations(Set<DonationHistory> donationHistories) {
        this.donations = donationHistories;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public ApplicationUser notifications(Set<Notification> notifications) {
        this.notifications = notifications;
        return this;
    }

    public ApplicationUser addNotification(Notification notification) {
        this.notifications.add(notification);
        notification.setApplicationUser(this);
        return this;
    }

    public ApplicationUser removeNotification(Notification notification) {
        this.notifications.remove(notification);
        notification.setApplicationUser(null);
        return this;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    public ApplicationUser payments(Set<Payment> payments) {
        this.payments = payments;
        return this;
    }

    public ApplicationUser addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setApplicationUser(this);
        return this;
    }

    public ApplicationUser removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.setApplicationUser(null);
        return this;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }

    public Set<Proyect> getFavorites() {
        return favorites;
    }

    public ApplicationUser favorites(Set<Proyect> proyects) {
        this.favorites = proyects;
        return this;
    }

    public ApplicationUser addFavorite(Proyect proyect) {
        this.favorites.add(proyect);
        proyect.setApplicationUser(this);
        return this;
    }

    public ApplicationUser removeFavorite(Proyect proyect) {
        this.favorites.remove(proyect);
        proyect.setApplicationUser(null);
        return this;
    }

    public void setFavorites(Set<Proyect> proyects) {
        this.favorites = proyects;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationUser)) {
            return false;
        }
        return id != null && id.equals(((ApplicationUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUser{" +
            "id=" + getId() +
            ", identification='" + getIdentification() + "'" +
            ", idType='" + getIdType() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", admin='" + isAdmin() + "'" +
            "}";
    }
}
