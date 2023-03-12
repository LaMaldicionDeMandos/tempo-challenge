package com.tempo.challenge.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="HISTORY")
public class EndpointCall {

    public EndpointCall(final String path, final String result) {
        this.id = UUID.randomUUID().toString();
        this.path = path;
        this.result = result;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "PATH")
    private String path;

    @Column(name = "RESULT")
    private String result;
}
