package com.unsolved.hgu.user;

import com.unsolved.hgu.problem.ProblemDto;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Getter
@Builder(toBuilder = true)
public class SiteUserDto {
    private Long id;

    private String username;

    private String password;

    private String email;

    private UserRole role;

    private String provider;

    private String providerId;

    private List<ProblemDto> problems;
}
