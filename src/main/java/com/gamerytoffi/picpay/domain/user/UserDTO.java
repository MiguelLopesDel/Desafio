package com.gamerytoffi.picpay.domain.user;

import java.math.BigDecimal;

public record UserDTO(String firstName, String lastName, String document, String email, String password, BigDecimal balance, UserType userType) {
}
