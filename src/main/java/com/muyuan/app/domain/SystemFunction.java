package com.muyuan.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A SystemFunction.
 */
@Entity
@Table(name = "system_function")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SystemFunction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "order_by")
    private Integer orderBy;

    @Column(name = "icon")
    private String icon;

    @Column(name = "note")
    private String note;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parent", "systemFunctions" }, allowSetters = true)
    private SystemCategory systemCategory;

    @OneToMany(mappedBy = "systemFunctions")
    @JsonIgnoreProperties(value = { "systemFunctions", "role" }, allowSetters = true)
    private Set<SystemFunctionOperate> systemFunctionOperates = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SystemFunction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public SystemFunction name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrderBy() {
        return this.orderBy;
    }

    public SystemFunction orderBy(Integer orderBy) {
        this.setOrderBy(orderBy);
        return this;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    public String getIcon() {
        return this.icon;
    }

    public SystemFunction icon(String icon) {
        this.setIcon(icon);
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNote() {
        return this.note;
    }

    public SystemFunction note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public SystemFunction isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public SystemCategory getSystemCategory() {
        return this.systemCategory;
    }

    public void setSystemCategory(SystemCategory systemCategory) {
        this.systemCategory = systemCategory;
    }

    public SystemFunction systemCategory(SystemCategory systemCategory) {
        this.setSystemCategory(systemCategory);
        return this;
    }

    public Set<SystemFunctionOperate> getSystemFunctionOperates() {
        return this.systemFunctionOperates;
    }

    public void setSystemFunctionOperates(Set<SystemFunctionOperate> systemFunctionOperates) {
        if (this.systemFunctionOperates != null) {
            this.systemFunctionOperates.forEach(i -> i.setSystemFunctions(null));
        }
        if (systemFunctionOperates != null) {
            systemFunctionOperates.forEach(i -> i.setSystemFunctions(this));
        }
        this.systemFunctionOperates = systemFunctionOperates;
    }

    public SystemFunction systemFunctionOperates(Set<SystemFunctionOperate> systemFunctionOperates) {
        this.setSystemFunctionOperates(systemFunctionOperates);
        return this;
    }

    public SystemFunction addSystemFunctionOperates(SystemFunctionOperate systemFunctionOperate) {
        this.systemFunctionOperates.add(systemFunctionOperate);
        systemFunctionOperate.setSystemFunctions(this);
        return this;
    }

    public SystemFunction removeSystemFunctionOperates(SystemFunctionOperate systemFunctionOperate) {
        this.systemFunctionOperates.remove(systemFunctionOperate);
        systemFunctionOperate.setSystemFunctions(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemFunction)) {
            return false;
        }
        return id != null && id.equals(((SystemFunction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemFunction{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", orderBy=" + getOrderBy() +
            ", icon='" + getIcon() + "'" +
            ", note='" + getNote() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
