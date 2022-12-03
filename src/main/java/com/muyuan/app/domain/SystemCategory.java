package com.muyuan.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A SystemCategory.
 */
@Entity
@Table(name = "system_category")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SystemCategory implements Serializable {

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
    @JsonIgnoreProperties(value = { "parent", "functions" }, allowSetters = true)
    private SystemCategory parent;

    @OneToMany(mappedBy = "category")
    @JsonIgnoreProperties(value = { "category", "operates" }, allowSetters = true)
    private Set<SystemFunction> functions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SystemCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public SystemCategory name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrderBy() {
        return this.orderBy;
    }

    public SystemCategory orderBy(Integer orderBy) {
        this.setOrderBy(orderBy);
        return this;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    public String getIcon() {
        return this.icon;
    }

    public SystemCategory icon(String icon) {
        this.setIcon(icon);
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNote() {
        return this.note;
    }

    public SystemCategory note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public SystemCategory isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public SystemCategory getParent() {
        return this.parent;
    }

    public void setParent(SystemCategory systemCategory) {
        this.parent = systemCategory;
    }

    public SystemCategory parent(SystemCategory systemCategory) {
        this.setParent(systemCategory);
        return this;
    }

    public Set<SystemFunction> getFunctions() {
        return this.functions;
    }

    public void setFunctions(Set<SystemFunction> systemFunctions) {
        if (this.functions != null) {
            this.functions.forEach(i -> i.setCategory(null));
        }
        if (systemFunctions != null) {
            systemFunctions.forEach(i -> i.setCategory(this));
        }
        this.functions = systemFunctions;
    }

    public SystemCategory functions(Set<SystemFunction> systemFunctions) {
        this.setFunctions(systemFunctions);
        return this;
    }

    public SystemCategory addFunctions(SystemFunction systemFunction) {
        this.functions.add(systemFunction);
        systemFunction.setCategory(this);
        return this;
    }

    public SystemCategory removeFunctions(SystemFunction systemFunction) {
        this.functions.remove(systemFunction);
        systemFunction.setCategory(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemCategory)) {
            return false;
        }
        return id != null && id.equals(((SystemCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", orderBy=" + getOrderBy() +
            ", icon='" + getIcon() + "'" +
            ", note='" + getNote() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
