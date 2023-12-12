package com.example.dreamcase.request;

import com.example.dreamcase.model.Country;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class CreateUserRequest {

    @JsonProperty("username")
    @NotBlank(message = "Username is required")
    private String username;

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

}
