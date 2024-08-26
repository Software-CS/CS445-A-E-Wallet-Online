package com.example.testlogin.project_java2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "temporaries")
@Data
@NoArgsConstructor
@Setter
@Getter
public class Temporary {


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String payment_token;
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    //annotion này giúp gỡ lỗi lặp vô hạn khi mapper qua tất cả các BankAccounts
    @JsonBackReference("user-account-temporary")
    private UserAccount userAccount;

}
