package com.ecommerce.infra.email;

public record EmailSenderDTO(String to, String subject, String message) {
}
