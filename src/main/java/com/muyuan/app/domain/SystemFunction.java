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

    @Column(name = "default_action")
    private String defaultAction;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parent", "functions" }, allowSetters = true)
    private SystemCategory category;

    @OneToMany(mappedBy = "function")
    @JsonIgnoreProperties(value = { "function", "roles" }, allowSetters = true)
    private Set<SystemFunctionOperate> operates = new HashSet<>();

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

    public String getDefaultAction() {
        return this.defaultAction;
    }

    public SystemFunction defaultAction(String defaultAction) {
        this.setDefaultAction(defaultAction);
        return this;
    }

    public void setDefaultAction(String defaultAction) {
        this.defaultAction = defaultAction;
    }

    public SystemCategory getCategory() {
        return this.category;
    }

    public void setCategory(SystemCategory systemCategory) {
        this.category = systemCategory;
    }

    public SystemFunction category(SystemCategory systemCategory) {
        this.setCategory(systemCategory);
        return this;
    }

    public Set<SystemFunctionOperate> getOperates() {
        return this.operates;
    }

    public void setOperates(Set<SystemFunctionOperate> systemFunctionOperates) {
        if (this.operates != null) {
            this.operates.forEach(i -> i.setFunction(null));
        }
        if (systemFunctionOperates != null) {
            systemFunctionOperates.forEach(i -> i.setFunction(this));
        }
        this.operates = systemFunctionOperates;
    }

    public SystemFunction operates(Set<SystemFunctionOperate> systemFunctionOperates) {
        this.setOperates(systemFunctionOperates);
        return this;
    }

    public SystemFunction addOperates(SystemFunctionOperate systemFunctionOperate) {
        this.operates.add(systemFunctionOperate);
        systemFunctionOperate.setFunction(this);
        return this;
    }

    public SystemFunction removeOperates(SystemFunctionOperate systemFunctionOperate) {
        this.operates.remove(systemFunctionOperate);
        systemFunctionOperate.setFunction(null);
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
            ", defaultAction='" + getDefaultAction() + "'" +
            "}";
    }
}
