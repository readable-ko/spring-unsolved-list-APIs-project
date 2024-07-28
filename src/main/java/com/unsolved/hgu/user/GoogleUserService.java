package com.unsolved.hgu.user;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
class GoogleUserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}", oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = null;

        //Google
        if (provider.equals("google")) {
            log.info("구글 로그인");
            oAuth2UserInfo = new GoogleUserDetails(oAuth2User.getAttributes());
        }

        assert oAuth2UserInfo != null;
        Optional<SiteUser> findUser = userRepository.findByLoginId(provider, oAuth2UserInfo.getProviderId());

        if (findUser.isPresent()) {
            return new OAuth2UserDetails(findUser.get(), oAuth2User.getAttributes());
        }

        SiteUser siteUser = SiteUser.builder()
                .username(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .provider(provider)
                .providerId(oAuth2UserInfo.getProviderId())
                .role(UserRole.USER)
                .build();

        userRepository.save(siteUser);

        return new OAuth2UserDetails(siteUser, oAuth2User.getAttributes());
    }
}