package com.example.testlogin.project_java2.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String user_bank_code;
    @CreationTimestamp
    private LocalDateTime start_time;
    private double deposit_amount;
    private String payment_content;

    @CreationTimestamp
    private LocalDateTime createdOn;
    @UpdateTimestamp
    private LocalDateTime updatedOn;

    @ManyToOne
    @JoinColumn(name = "bank_account_id", nullable = true)
    //annotion này giúp gỡ lỗi lặp vô hạn khi mapper qua tất cả các BankAccounts
    @JsonBackReference("bank-account-payments")
    private BankAccount bankAccount;

    @ManyToOne
    @JoinColumn(name = "upload_id", nullable = true)
    //annotion này giúp gỡ lỗi lặp vô hạn khi mapper qua tất cả các BankAccounts
    @JsonBackReference("upload-payments")
    private Upload upload;

}
