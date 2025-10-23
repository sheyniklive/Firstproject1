package org.example.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;
@Entity
@Table(name = "persons")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonEntity {
@Id
    private UUID id;
@Column
    private String name;
@Column
    private String surname;
@Column
    private Integer age;
@OneToMany(mappedBy = "person_id", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PetEntity> pets;

}
