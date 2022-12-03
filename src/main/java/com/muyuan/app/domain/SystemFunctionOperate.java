package com.muyuan.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A SystemFunctionOperate.
 */
@Entity
@Table(name = "system_function_operate")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SystemFunctionOperate implements Serializable {

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

    @ManyToOne
    @JsonIgnoreProperties(value = { "category", "operates" }, allowSetters = true)
    private SystemFunction function;

    @ManyToMany(mappedBy = "operates")
    @JsonIgnoreProperties(value = { "operates", "applicationUsers" }, allowSetters = true)
    private Set<Role> roles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SystemFunctionOperate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public SystemFunctionOperate name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public SystemFunctionOperate code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getOrderBy() {
        return this.orderBy;
    }

    public SystemFunctionOperate orderBy(Integer orderBy) {
        this.setOrderBy(orderBy);
        return this;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    public String getNote() {
        return this.note;
    }

    public SystemFunctionOperate note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public SystemFunctionOperate isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public SystemFunction getFunction() {
        return this.function;
    }

    public void setFunction(SystemFunction systemFunction) {
        this.function = systemFunction;
    }

    public SystemFunctionOperate function(SystemFunction systemFunction) {
        this.setFunction(systemFunction);
        return this;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles) {
        if (this.roles != null) {
            this.roles.forEach(i -> i.removeOperates(this));
        }
        if (roles != null) {
            roles.forEach(i -> i.addOperates(this));
        }
        this.roles = roles;
    }

    public SystemFunctionOperate roles(Set<Role> roles) {
        this.setRoles(roles);
        return this;
    }

    public SystemFunctionOperate addRoles(Role role) {
        this.roles.add(role);
        role.getOperates().add(this);
        return this;
    }

    public SystemFunctionOperate removeRoles(Role role) {
        this.roles.remove(role);
        role.getOperates().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemFunctionOperate)) {
            return false;
        }
        return id != null && id.equals(((SystemFunctionOperate) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemFunctionOperate{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", orderBy=" + getOrderBy() +
            ", note='" + getNote() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
