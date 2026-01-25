package de.imnotpan.orderinventory.common.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import de.imnotpan.orderinventory.domain.inventory.exception.InsufficentStockException;
import de.imnotpan.orderinventory.domain.inventory.exception.InsufficientReservedStockException;
import de.imnotpan.orderinventory.domain.inventory.exception.InvalidInventoryOperationException;
import de.imnotpan.orderinventory.domain.inventory.exception.InventoryNotFoundException;
import de.imnotpan.orderinventory.domain.product.exception.ProductAlreadyExistsException;
import de.imnotpan.orderinventory.domain.product.exception.ProductNotFoundException;

import java.time.Instant;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleProductAlreadyExists(
        ProductAlreadyExistsException ex,
        HttpServletRequest req
    ){
        return build(HttpStatus.CONFLICT, "PRODUCT_ALREADY_EXISTS", ex.getMessage(), req);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleProductNotFound(
        ProductNotFoundException ex,
        HttpServletRequest req
    ){
        return build(HttpStatus.NOT_FOUND, "PRODUCT_NOT_FOUND", ex.getMessage(), req);
    }

    @ExceptionHandler(InventoryNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleInventoryNotFound(
        InventoryNotFoundException ex,
        HttpServletRequest req
    ){
        return build(HttpStatus.NOT_FOUND, "INVENTORY_NOT_FOUND", ex.getMessage(), req);
    }


    @ExceptionHandler(InsufficentStockException.class)
    public ResponseEntity<ApiErrorResponse> handleInsufficentStock(
        InsufficentStockException ex,
        HttpServletRequest req
    ){
        return build(HttpStatus.CONFLICT, "INSUFFICIENT_STOCK", ex.getMessage(), req);
    }

    @ExceptionHandler(InsufficientReservedStockException.class)
    public ResponseEntity<ApiErrorResponse> handleInsufficientReservedStock(
        InsufficientReservedStockException ex,
        HttpServletRequest req
    ){
        return build(HttpStatus.CONFLICT,"INSUFFICIENT_RESERVED_STOCK", ex.getMessage(), req );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
        MethodArgumentNotValidException ex,
        HttpServletRequest req
    ){
        String msg = ex.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(err -> err.getField() + "" + err.getDefaultMessage())
            .orElse("Validation error");

        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", msg, req);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(
        IllegalArgumentException ex,
        HttpServletRequest req
    ){
        return build(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage(), req);
    }

    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(
        Exception ex,
        HttpServletRequest req
    ){
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "UNEXPECTED_ERROR", "Unexpected server error", req);
    }

    @ExceptionHandler(InvalidInventoryOperationException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidInventoryOperation(
        InvalidInventoryOperationException ex,
        HttpServletRequest req
    ){
        return build(
            HttpStatus.CONFLICT,
            "INVALID_INVENTORY_OPERATION",
            ex.getMessage(),
            req
        );
    }


    private ResponseEntity<ApiErrorResponse> build(
        HttpStatus status,
        String code,
        String message,
        HttpServletRequest req
    ){
        return ResponseEntity.status(status).body(new ApiErrorResponse(
            code, 
            message,
            Instant.now(), 
            req.getRequestURI()));
    }
}
