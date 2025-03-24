package org.example.forumservice.dto.issue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.forumservice.util.user.UserExists;

import java.io.Serializable;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetIssuesDto implements Serializable {
    @UserExists
    private final String username;
}
