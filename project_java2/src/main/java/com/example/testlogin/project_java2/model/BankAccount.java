package com.example.testlogin.project_java2.model;


import com.example.testlogin.project_java2.constant.EnumConstant;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bank_account")
@Getter
@Setter
public class BankAccount {


    @Id
    private String id;
    private String code;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EnumConstant status;
    private double amount;

    @OneToOne
    @JoinColumn(name = "user_id")
    //annotion này giúp gỡ lỗi lặp vô hạn khi mapper qua tất cả các BankAccounts
    @JsonBackReference("user-account-bank-account")
    private UserAccount userAccount;

    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.REMOVE)
    @JsonManagedReference("bank-account-payments")
    private List<Payment> payments = new ArrayList<>();


}
