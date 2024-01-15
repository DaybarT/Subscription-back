package com.project.daybart.demo.Users;
import com.project.daybart.demo.Subscription.Subscription;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Users {
   @Id
   @GeneratedValue
   private Integer id;
   @Basic
   private String role;
   private String username;
   private String name;
   private String lastName;
   private String email;
   private String password;

   @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Subscription subscription;
}