package com.unsolved.hgu.email;

import com.unsolved.hgu.exception.DataNotFoundException;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/emails/verification-request")
    public Map<String, String> mailSend(@RequestBody @Valid EmailRequestForm emailRequestForm,
                                        BindingResult bindingResult) {
        Map<String, String> response = new HashMap<>();
        if (bindingResult.hasErrors()) {
            response.put("error", "error: ");
            return response;
        }
        String code = emailService.joinEmail(emailRequestForm.getEmail());
        response.put("code", code);

        return response;
    }

    @PostMapping("/emails/verification")
    public String mailVerify(@RequestBody @Valid EmailVerifyForm emailVerifyForm) {
        Boolean valid = emailService.isValidAuthString(emailVerifyForm.getEmail(), emailVerifyForm.getAuthString());
        if (valid) {
            return "[Success] 이메일 인증에 성공했습니다.";
        }
        throw new DataNotFoundException("[Fail] 이메일 인증 번호가 다릅니다!");
    }
}
