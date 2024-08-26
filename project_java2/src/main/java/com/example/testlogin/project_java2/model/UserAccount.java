package com.example.testlogin.project_java2.model;


import com.example.testlogin.project_java2.constant.EnumConstant;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
@Getter
@Setter
public class UserAccount {


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String name;
    private String email;
    private String password;


    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private EnumConstant type;

    @Enumerated(EnumType.STRING)
    @Column(name = "active")
    private EnumConstant active;

    @CreationTimestamp
    private LocalDateTime createdOn;
    @UpdateTimestamp
    private LocalDateTime updatedOn;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private List<Role> roles = new ArrayList<>();

    @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL)
    //annotion này giúp gỡ lỗi lặp vô hạn khi mapper qua tất cả các users
    @JsonManagedReference("user-account-bank-account")
    private BankAccount bankAccount;

    @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL)
    //annotion này giúp gỡ lỗi lặp vô hạn khi mapper qua tất cả các users
    @JsonManagedReference("user-account-temporary")
    private Temporary temporary;

    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.REMOVE)
    @JsonManagedReference("user-account-uploads")
    private List<Upload> uploads = new ArrayList<>();

}
