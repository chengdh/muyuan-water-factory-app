package com.muyuan.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Role.
 */
@Entity
@Table(name = "role")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "order_by")
    private Integer orderBy;

    @Column(name = "note")
    private String note;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "role")
    @JsonIgnoreProperties(value = { "systemFunctions", "role" }, allowSetters = true)
    private Set<SystemFunctionOperate> systemFunctionOperates = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Role id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Role name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public Role code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getOrderBy() {
        return this.orderBy;
    }

    public Role orderBy(Integer orderBy) {
        this.setOrderBy(orderBy);
        return this;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    public String getNote() {
        return this.note;
    }

    public Role note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Role isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<SystemFunctionOperate> getSystemFunctionOperates() {
        return this.systemFunctionOperates;
    }

    public void setSystemFunctionOperates(Set<SystemFunctionOperate> systemFunctionOperates) {
        if (this.systemFunctionOperates != null) {
            this.systemFunctionOperates.forEach(i -> i.setRole(null));
        }
        if (systemFunctionOperates != null) {
            systemFunctionOperates.forEach(i -> i.setRole(this));
        }
        this.systemFunctionOperates = systemFunctionOperates;
    }

    public Role systemFunctionOperates(Set<SystemFunctionOperate> systemFunctionOperates) {
        this.setSystemFunctionOperates(systemFunctionOperates);
        return this;
    }

    public Role addSystemFunctionOperates(SystemFunctionOperate systemFunctionOperate) {
        this.systemFunctionOperates.add(systemFunctionOperate);
        systemFunctionOperate.setRole(this);
        return this;
    }

    public Role removeSystemFunctionOperates(SystemFunctionOperate systemFunctionOperate) {
        this.systemFunctionOperates.remove(systemFunctionOperate);
        systemFunctionOperate.setRole(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        return id != null && id.equals(((Role) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Role{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", orderBy=" + getOrderBy() +
            ", note='" + getNote() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
