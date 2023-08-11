package kz.adem.postservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class PostDto {
    private long id;
    @NotEmpty
    @Size(min = 2,message = "Post title should have at least 2 characters.")
    private String title;
    @NotEmpty
    @Size(min = 10,message = "Post description should have at least 10 characters.")
    private String description;
    @NotEmpty
    private String content;
    @NotEmpty
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private Long userId;
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @NotEmpty
    private String username;


}