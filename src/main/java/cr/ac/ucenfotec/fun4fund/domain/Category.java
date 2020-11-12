package cr.ac.ucenfotec.fun4fund.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import cr.ac.ucenfotec.fun4fund.domain.enumeration.CategoryStatus;

/**
 * A Category.
 */
@Entity
@Table(name = "category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Category implements Serializable {

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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CategoryStatus status;

    @OneToOne
    @JoinColumn(unique = true)
    private Resource image;

    @OneToMany(mappedBy = "category")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Proyect> proyects = new HashSet<>();

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

    public Category name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Category description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryStatus getStatus() {
        return status;
    }

    public Category status(CategoryStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(CategoryStatus status) {
        this.status = status;
    }

    public Resource getImage() {
        return image;
    }

    public Category image(Resource resource) {
        this.image = resource;
        return this;
    }

    public void setImage(Resource resource) {
        this.image = resource;
    }

    public Set<Proyect> getProyects() {
        return proyects;
    }

    public Category proyects(Set<Proyect> proyects) {
        this.proyects = proyects;
        return this;
    }

    public Category addProyect(Proyect proyect) {
        this.proyects.add(proyect);
        proyect.setCategory(this);
        return this;
    }

    public Category removeProyect(Proyect proyect) {
        this.proyects.remove(proyect);
        proyect.setCategory(null);
        return this;
    }

    public void setProyects(Set<Proyect> proyects) {
        this.proyects = proyects;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return id != null && id.equals(((Category) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
