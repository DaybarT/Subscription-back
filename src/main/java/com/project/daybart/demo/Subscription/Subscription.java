package com.project.daybart.demo.Subscription;


import java.time.Instant;

import com.project.daybart.demo.Users.Users;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Subscription {
    @Id
    @GeneratedValue
    private Integer id;

    @Basic
    private Boolean payed = false;
    private String serial;
    private Instant date;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

}
