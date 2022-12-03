package com.muyuan.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Organization.
 */
@Entity
@Table(name = "organization")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "leader")
    private String leader;

    @Column(name = "address")
    private String address;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "position")
    private Integer position;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parent", "applicationUsers" }, allowSetters = true)
    private Organization parent;

    @ManyToMany(mappedBy = "organizaitons")
    @JsonIgnoreProperties(value = { "internalUser", "organizaitons", "roles" }, allowSetters = true)
    private Set<ApplicationUser> applicationUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Organization id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Organization name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeader() {
        return this.leader;
    }

    public Organization leader(String leader) {
        this.setLeader(leader);
        return this;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getAddress() {
        return this.address;
    }

    public Organization address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Organization isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getPosition() {
        return this.position;
    }

    public Organization position(Integer position) {
        this.setPosition(position);
        return this;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getNote() {
        return this.note;
    }

    public Organization note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Organization getParent() {
        return this.parent;
    }

    public void setParent(Organization organization) {
        this.parent = organization;
    }

    public Organization parent(Organization organization) {
        this.setParent(organization);
        return this;
    }

    public Set<ApplicationUser> getApplicationUsers() {
        return this.applicationUsers;
    }

    public void setApplicationUsers(Set<ApplicationUser> applicationUsers) {
        if (this.applicationUsers != null) {
            this.applicationUsers.forEach(i -> i.removeOrganizaitons(this));
        }
        if (applicationUsers != null) {
            applicationUsers.forEach(i -> i.addOrganizaitons(this));
        }
        this.applicationUsers = applicationUsers;
    }

    public Organization applicationUsers(Set<ApplicationUser> applicationUsers) {
        this.setApplicationUsers(applicationUsers);
        return this;
    }

    public Organization addApplicationUsers(ApplicationUser applicationUser) {
        this.applicationUsers.add(applicationUser);
        applicationUser.getOrganizaitons().add(this);
        return this;
    }

    public Organization removeApplicationUsers(ApplicationUser applicationUser) {
        this.applicationUsers.remove(applicationUser);
        applicationUser.getOrganizaitons().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Organization)) {
            return false;
        }
        return id != null && id.equals(((Organization) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Organization{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", leader='" + getLeader() + "'" +
            ", address='" + getAddress() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", position=" + getPosition() +
            ", note='" + getNote() + "'" +
            "}";
    }
}
