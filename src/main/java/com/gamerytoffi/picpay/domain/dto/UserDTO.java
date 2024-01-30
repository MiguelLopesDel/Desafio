package com.gamerytoffi.picpay.domain.dto;

import com.gamerytoffi.picpay.domain.UserType;

import java.math.BigDecimal;

public record UserDTO(String firstName, String lastName, String document, String email, String password, BigDecimal balance, UserType userType) {
}
