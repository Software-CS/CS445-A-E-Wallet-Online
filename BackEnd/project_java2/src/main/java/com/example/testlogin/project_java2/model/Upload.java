package com.example.testlogin.project_java2.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "uploads")
@Data
@NoArgsConstructor
@Setter
@Getter
public class Upload {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String file_name;
    private String file_type;
    @Lob
    private byte[] data;
    @CreationTimestamp
    private LocalDateTime createdOn;
    @UpdateTimestamp
    private LocalDateTime updatedOn;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    //annotion này giúp gỡ lỗi lặp vô hạn khi mapper qua tất cả các BankAccounts
    @JsonBackReference("user-account-uploads")
    private UserAccount userAccount;

    @OneToMany(mappedBy = "upload", cascade = CascadeType.REMOVE)
    @JsonManagedReference("upload-payments")
    private List<Payment> payments = new ArrayList<>();


    public Upload(String file_name, String file_type, byte[] data) {
        this.file_name = file_name;
        this.file_type = file_type;
        this.data = data;
    }
}
