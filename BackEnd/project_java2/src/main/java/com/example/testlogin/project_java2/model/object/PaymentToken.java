package com.example.testlogin.project_java2.model.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
public class PaymentToken implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;
    private String token;


}
