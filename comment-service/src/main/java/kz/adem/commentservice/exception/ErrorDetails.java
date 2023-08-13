package kz.adem.commentservice.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Builder
public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String details;
}