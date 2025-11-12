package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class View {
    @Id
    @Column(name = "id")
    private UUID personEntityId;
    @Column(name = "name")
    private String personEntityName;
}
