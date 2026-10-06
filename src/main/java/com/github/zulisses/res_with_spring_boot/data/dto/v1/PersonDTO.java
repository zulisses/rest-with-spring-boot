package com.github.zulisses.res_with_spring_boot.data.dto.v1;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class PersonDTO extends RepresentationModel<PersonDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
    private boolean enabled;

    public PersonDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public @NonNull String toString() {
        return "Person [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", address=" + address
                + ", gender=" + gender + ", enabled=" + enabled + "]";
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PersonDTO other = (PersonDTO) obj;
        return Objects.equals(id, other.id) && Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName) && Objects.equals(address, other.address) && Objects.equals(gender, other.gender) && Objects.equals(enabled, other.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, address, gender, enabled);
    }
}
