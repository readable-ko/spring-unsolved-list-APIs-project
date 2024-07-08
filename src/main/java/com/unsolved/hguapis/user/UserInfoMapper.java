package com.unsolved.hguapis.user;

public class UserInfoMapper {
    public static UserInfoDto ContributeDtoToDto(UserInfoContributeDto userInfoContributeDto) {
        return new UserInfoDto(
                userInfoContributeDto.getUsername(),
                userInfoContributeDto.getSubClass(),
                Math.toIntExact(userInfoContributeDto.getSolvedCount()),
                userInfoContributeDto.getTier(),
                userInfoContributeDto.getModifyDate()
        );
    }
}
