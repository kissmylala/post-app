package kz.adem.gatewayservice.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
/**
 * Represents detailed information about an error that occurred.
 * Contains a timestamp of when the error happened, a message describing the error,
 * and additional details about the context or cause of the error.
 */
@AllArgsConstructor
@Getter
@Builder
public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String details;
}
