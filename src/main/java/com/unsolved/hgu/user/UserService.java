package com.unsolved.hgu.user;

import com.unsolved.hgu.answer.Answer;
import com.unsolved.hgu.answer.AnswerRepository;
import com.unsolved.hgu.exception.DataNotFoundException;
import com.unsolved.hgu.problem.Problem;
import com.unsolved.hgu.question.Question;
import com.unsolved.hgu.question.QuestionRepository;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Builder
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public SiteUser create(String username, String email, String password) {
        SiteUser siteUser = SiteUser.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .provider("site")
                .role(UserRole.USER)
                .build();

        this.userRepository.save(siteUser);
        return siteUser;
    }

    public SiteUser modifyUserName(SiteUser siteUser, String username) {
        SiteUser modifiedUser = siteUser.toBuilder()
                .username(username)
                .build();

        this.userRepository.save(modifiedUser);
        reloadLoginStatus(modifiedUser);

        return modifiedUser;
    }

    private void reloadLoginStatus(SiteUser siteUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = new User(
                siteUser.getUsername(),
                siteUser.getPassword(),
                authentication.getAuthorities()
        );
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                authentication.getCredentials(),
                authentication.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("Site user not found");
        }
    }

    public MyPageUserDto getUserDto(String username) {
        Optional<SiteUser> optionalSiteUser = this.userRepository.findByUsername(username);
        if (optionalSiteUser.isEmpty()) {
            throw new DataNotFoundException("Site user not found");
        }

        SiteUser siteUser = optionalSiteUser.get();
        List<Question> questions = this.questionRepository.findAllByAuthor(siteUser);
        List<Answer> answers = this.answerRepository.findAllByAuthor(siteUser);

        return MyPageUserDto.builder()
                .siteUser(SiteUserMapper.toDto(siteUser))
                .writtenQuestions(questions)
                .writtenAnswers(answers)
                .build();
    }

    public Boolean saveProblem(SiteUser siteuser, Problem problem) {
        boolean add = true;
        if (siteuser.getSavedProblem().contains(problem)) {
            siteuser.getSavedProblem().remove(problem);
            add = false;
        } else {
            siteuser.getSavedProblem().add(problem);
        }

        this.userRepository.save(siteuser);
        return add;
    }

    public Boolean isExistEmail(String email) {
        Optional<SiteUser> siteUser = this.userRepository.findByEmailAndProvider(email, "site");
        return siteUser.isPresent();
    }
}
