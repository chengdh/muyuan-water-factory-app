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

    @ManyToMany
    @JoinTable(
        name = "rel_role__operates",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "operates_id")
    )
    @JsonIgnoreProperties(value = { "function", "roles" }, allowSetters = true)
    private Set<SystemFunctionOperate> operates = new HashSet<>();

    @ManyToMany(mappedBy = "roles")
    @JsonIgnoreProperties(value = { "internalUser", "organizaitons", "roles" }, allowSetters = true)
    private Set<ApplicationUser> applicationUsers = new HashSet<>();

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

    public Set<SystemFunctionOperate> getOperates() {
        return this.operates;
    }

    public void setOperates(Set<SystemFunctionOperate> systemFunctionOperates) {
        this.operates = systemFunctionOperates;
    }

    public Role operates(Set<SystemFunctionOperate> systemFunctionOperates) {
        this.setOperates(systemFunctionOperates);
        return this;
    }

    public Role addOperates(SystemFunctionOperate systemFunctionOperate) {
        this.operates.add(systemFunctionOperate);
        systemFunctionOperate.getRoles().add(this);
        return this;
    }

    public Role removeOperates(SystemFunctionOperate systemFunctionOperate) {
        this.operates.remove(systemFunctionOperate);
        systemFunctionOperate.getRoles().remove(this);
        return this;
    }

    public Set<ApplicationUser> getApplicationUsers() {
        return this.applicationUsers;
    }

    public void setApplicationUsers(Set<ApplicationUser> applicationUsers) {
        if (this.applicationUsers != null) {
            this.applicationUsers.forEach(i -> i.removeRoles(this));
        }
        if (applicationUsers != null) {
            applicationUsers.forEach(i -> i.addRoles(this));
        }
        this.applicationUsers = applicationUsers;
    }

    public Role applicationUsers(Set<ApplicationUser> applicationUsers) {
        this.setApplicationUsers(applicationUsers);
        return this;
    }

    public Role addApplicationUsers(ApplicationUser applicationUser) {
        this.applicationUsers.add(applicationUser);
        applicationUser.getRoles().add(this);
        return this;
    }

    public Role removeApplicationUsers(ApplicationUser applicationUser) {
        this.applicationUsers.remove(applicationUser);
        applicationUser.getRoles().remove(this);
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
