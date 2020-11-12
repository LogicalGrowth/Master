package cr.ac.ucenfotec.fun4fund.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Prize.
 */
@Entity
@Table(name = "prize")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Prize implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "prize")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Resource> images = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Prize name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Prize description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Resource> getImages() {
        return images;
    }

    public Prize images(Set<Resource> resources) {
        this.images = resources;
        return this;
    }

    public Prize addImage(Resource resource) {
        this.images.add(resource);
        resource.setPrize(this);
        return this;
    }

    public Prize removeImage(Resource resource) {
        this.images.remove(resource);
        resource.setPrize(null);
        return this;
    }

    public void setImages(Set<Resource> resources) {
        this.images = resources;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Prize)) {
            return false;
        }
        return id != null && id.equals(((Prize) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Prize{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
