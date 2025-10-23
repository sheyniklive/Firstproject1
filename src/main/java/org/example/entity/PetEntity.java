package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.pet.enums.PetType;

import java.math.BigInteger;
import java.util.UUID;

@Entity
@Table(name = "pets")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PetEntity {

    @Id
    private BigInteger id;
    @Column
    private String name;
    @Column
    @Enumerated(EnumType.STRING)
    private PetType type;
    @Column
    @JoinColumn(name = "pets")
    private UUID person_id;

}
