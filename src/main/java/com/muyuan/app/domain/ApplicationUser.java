package com.muyuan.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ApplicationUser.
 */
@Entity
@Table(name = "application_user")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApplicationUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "real_name", nullable = false)
    private String realName;

    @OneToOne
    @JoinColumn(unique = true)
    private User internalUser;

    @ManyToMany
    @JoinTable(
        name = "rel_application_user__organizaitons",
        joinColumns = @JoinColumn(name = "application_user_id"),
        inverseJoinColumns = @JoinColumn(name = "organizaitons_id")
    )
    @JsonIgnoreProperties(value = { "parent", "applicationUsers" }, allowSetters = true)
    private Set<Organization> organizaitons = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_application_user__roles",
        joinColumns = @JoinColumn(name = "application_user_id"),
        inverseJoinColumns = @JoinColumn(name = "roles_id")
    )
    @JsonIgnoreProperties(value = { "operates", "applicationUsers" }, allowSetters = true)
    private Set<Role> roles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApplicationUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRealName() {
        return this.realName;
    }

    public ApplicationUser realName(String realName) {
        this.setRealName(realName);
        return this;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public ApplicationUser internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    public Set<Organization> getOrganizaitons() {
        return this.organizaitons;
    }

    public void setOrganizaitons(Set<Organization> organizations) {
        this.organizaitons = organizations;
    }

    public ApplicationUser organizaitons(Set<Organization> organizations) {
        this.setOrganizaitons(organizations);
        return this;
    }

    public ApplicationUser addOrganizaitons(Organization organization) {
        this.organizaitons.add(organization);
        organization.getApplicationUsers().add(this);
        return this;
    }

    public ApplicationUser removeOrganizaitons(Organization organization) {
        this.organizaitons.remove(organization);
        organization.getApplicationUsers().remove(this);
        return this;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public ApplicationUser roles(Set<Role> roles) {
        this.setRoles(roles);
        return this;
    }

    public ApplicationUser addRoles(Role role) {
        this.roles.add(role);
        role.getApplicationUsers().add(this);
        return this;
    }

    public ApplicationUser removeRoles(Role role) {
        this.roles.remove(role);
        role.getApplicationUsers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationUser)) {
            return false;
        }
        return id != null && id.equals(((ApplicationUser) o).id);
    }

    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUser{" +
                "id=" + getId() +
                ", realName='" + getRealName() + "'" +
                "}";
    }
}
