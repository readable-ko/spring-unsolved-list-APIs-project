package com.unsolved.hgu.user;

import com.unsolved.hgu.problem.ProblemDto;
import com.unsolved.hgu.problem.ProblemMapper;
import java.util.List;

public class SiteUserMapper {
    public static SiteUserDto toDto(SiteUser siteUser) {
        List<ProblemDto> problemDto = siteUser.getSavedProblem()
                .stream()
                .map(ProblemMapper::toDto)
                .toList();
        
        return new SiteUserDto(siteUser.getId(),
                siteUser.getUsername(),
                siteUser.getPassword(),
                siteUser.getEmail(),
                siteUser.getRole(),
                siteUser.getProvider(),
                siteUser.getProviderId(),
                problemDto
        );
    }
}
