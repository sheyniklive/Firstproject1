package org.example.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "persons")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "pets")
public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "surname", nullable = false, length = 100)
    private String surname;
    @Column(name = "age", nullable = false)
    private Integer age;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<PetEntity> pets = new HashSet<>();

    public void addPet(PetEntity petEntity) {
        pets.add(petEntity);
        petEntity.setOwner(this);
    }
}
