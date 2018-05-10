package vn.com.la.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "ignore_name")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class IgnoreName extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(name = "name", length = 1000)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IgnoreName ignoreName = (IgnoreName) o;
        if (ignoreName.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ignoreName.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
