package kz.adem.postservice.util;

import jakarta.servlet.http.HttpServletRequest;
import kz.adem.postservice.exception.UnauthorizedAccessException;
import org.springframework.util.StringUtils;

public class ControllerUtils {
    public static String getUsernameFromRequest(HttpServletRequest request){
        String username =  request.getHeader("user");
        if (!StringUtils.hasText(username)){
            throw new UnauthorizedAccessException("Unauthorized access");
        }
        return username;
    }
    public static Long getUserIdFromRequest(HttpServletRequest request){
        Long userId = Long.parseLong(request.getHeader("user_id"));
        if (userId == null){
            throw new UnauthorizedAccessException("Unauthorized access");
        }
        return userId;
    }
}
