package com.muyuan.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A UserOrganization.
 */
@Entity
@Table(name = "user_organization")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserOrganization implements Serializable {

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
    @JsonIgnoreProperties(value = { "parent" }, allowSetters = true)
    private Organization organization;

    @ManyToOne
    @JsonIgnoreProperties(value = { "userOrganizations", "userRoles" }, allowSetters = true)
    private UserProxy userProxy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserOrganization id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNote() {
        return this.note;
    }

    public UserOrganization note(String note) {
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

    public UserOrganization user(UserProxy userProxy) {
        this.setUser(userProxy);
        return this;
    }

    public Organization getOrganization() {
        return this.organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public UserOrganization organization(Organization organization) {
        this.setOrganization(organization);
        return this;
    }

    public UserProxy getUserProxy() {
        return this.userProxy;
    }

    public void setUserProxy(UserProxy userProxy) {
        this.userProxy = userProxy;
    }

    public UserOrganization userProxy(UserProxy userProxy) {
        this.setUserProxy(userProxy);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserOrganization)) {
            return false;
        }
        return id != null && id.equals(((UserOrganization) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserOrganization{" +
            "id=" + getId() +
            ", note='" + getNote() + "'" +
            "}";
    }
}
