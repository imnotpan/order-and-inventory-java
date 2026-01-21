package de.imnotpan.orderinventory.common.error;

public record ApiErrorResponse(
    String code,
    String message
){  }
