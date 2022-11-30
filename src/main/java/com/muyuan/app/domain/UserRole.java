package com.muyuan.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A UserRole.
 */
@Entity
@Table(name = "user_role")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JsonIgnoreProperties(value = { "userOrganizations", "userRoles" }, allowSetters = true)
    private UserProxy user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "systemFunctionOperates" }, allowSetters = true)
    private Role role;

    @ManyToOne
    @JsonIgnoreProperties(value = { "userOrganizations", "userRoles" }, allowSetters = true)
    private UserProxy userProxy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserRole id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNote() {
        return this.note;
    }

    public UserRole note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public UserProxy getUser() {
        return this.user;
    }

    public void setUser(UserProxy userProxy) {
        this.user = userProxy;
    }

    public UserRole user(UserProxy userProxy) {
        this.setUser(userProxy);
        return this;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserRole role(Role role) {
        this.setRole(role);
        return this;
    }

    public UserProxy getUserProxy() {
        return this.userProxy;
    }

    public void setUserProxy(UserProxy userProxy) {
        this.userProxy = userProxy;
    }

    public UserRole userProxy(UserProxy userProxy) {
        this.setUserProxy(userProxy);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserRole)) {
            return false;
        }
        return id != null && id.equals(((UserRole) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserRole{" +
            "id=" + getId() +
            ", note='" + getNote() + "'" +
            "}";
    }
}
