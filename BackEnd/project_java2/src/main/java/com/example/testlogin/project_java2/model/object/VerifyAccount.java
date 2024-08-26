package com.example.testlogin.project_java2.model.object;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "verify_account_temporaries")
@Getter
@Setter
public class VerifyAccount {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String verify_token;
    @CreationTimestamp
    private LocalDateTime createdAt;

    public VerifyAccount(String email, String verify_token) {
        this.email = email;
        this.verify_token = verify_token;
    }
}
