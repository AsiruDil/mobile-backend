package com.project.elephant.dto.request;

import lombok.Data;

@Data
public class ProfileUpdateRequest {
    private String imageUrl;
    private String idCardNumber;
    private String birthday;
    private int age;
    private String gender;
    private String mobileNumber;
    private boolean pushAlertsEnabled;
}