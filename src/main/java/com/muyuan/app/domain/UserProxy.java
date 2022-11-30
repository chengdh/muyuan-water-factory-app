package com.muyuan.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A UserProxy.
 */
@Entity
@Table(name = "user_proxy")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProxy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "userProxy")
    @JsonIgnoreProperties(value = { "user", "organization", "userProxy" }, allowSetters = true)
    private Set<UserOrganization> userOrganizations = new HashSet<>();

    @OneToMany(mappedBy = "userProxy")
    @JsonIgnoreProperties(value = { "user", "role", "userProxy" }, allowSetters = true)
    private Set<UserRole> userRoles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserProxy id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public UserProxy isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getNote() {
        return this.note;
    }

    public UserProxy note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<UserOrganization> getUserOrganizations() {
        return this.userOrganizations;
    }

    public void setUserOrganizations(Set<UserOrganization> userOrganizations) {
        if (this.userOrganizations != null) {
            this.userOrganizations.forEach(i -> i.setUserProxy(null));
        }
        if (userOrganizations != null) {
            userOrganizations.forEach(i -> i.setUserProxy(this));
        }
        this.userOrganizations = userOrganizations;
    }

    public UserProxy userOrganizations(Set<UserOrganization> userOrganizations) {
        this.setUserOrganizations(userOrganizations);
        return this;
    }

    public UserProxy addUserOrganizations(UserOrganization userOrganization) {
        this.userOrganizations.add(userOrganization);
        userOrganization.setUserProxy(this);
        return this;
    }

    public UserProxy removeUserOrganizations(UserOrganization userOrganization) {
        this.userOrganizations.remove(userOrganization);
        userOrganization.setUserProxy(null);
        return this;
    }

    public Set<UserRole> getUserRoles() {
        return this.userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        if (this.userRoles != null) {
            this.userRoles.forEach(i -> i.setUserProxy(null));
        }
        if (userRoles != null) {
            userRoles.forEach(i -> i.setUserProxy(this));
        }
        this.userRoles = userRoles;
    }

    public UserProxy userRoles(Set<UserRole> userRoles) {
        this.setUserRoles(userRoles);
        return this;
    }

    public UserProxy addUserRoles(UserRole userRole) {
        this.userRoles.add(userRole);
        userRole.setUserProxy(this);
        return this;
    }

    public UserProxy removeUserRoles(UserRole userRole) {
        this.userRoles.remove(userRole);
        userRole.setUserProxy(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProxy)) {
            return false;
        }
        return id != null && id.equals(((UserProxy) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProxy{" +
            "id=" + getId() +
            ", isActive='" + getIsActive() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
