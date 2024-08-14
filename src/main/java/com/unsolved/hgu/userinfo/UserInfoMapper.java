package com.unsolved.hgu.userinfo;

public class UserInfoMapper {
    public static UserInfoDto ContributeDtoToDto(UserInfoContributeDto userInfoContributeDto) {
        return new UserInfoDto(
                userInfoContributeDto.getUsername(),
                userInfoContributeDto.getTier(),
                Math.toIntExact(userInfoContributeDto.getSolvedCount()),
                userInfoContributeDto.getSubClass(),
                userInfoContributeDto.getModifyDate()
        );
    }
}
