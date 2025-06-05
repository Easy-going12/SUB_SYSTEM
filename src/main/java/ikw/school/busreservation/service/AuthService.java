package ikw.school.busreservation.service;

import ikw.school.busreservation.entity.Member;
import ikw.school.busreservation.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberRepository memberRepository;

    public AuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원가입 처리 로직
    public ResponseEntity<String> register(Member member) {
        String userId = member.getUserId();

        // 아이디 중복 검사
        if (memberRepository.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("이미 존재하는 아이디입니다.");
        }

        // 비밀번호 유효성 검사
        String password = member.getPassword();
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{1,10}$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("비밀번호는 영문과 숫자를 포함하고 10자리 이하로 입력해야 합니다.");
        }

        // 전화번호 유효성 검사
        String phone = member.getPhoneNumber();
        if (!phone.matches("^010-\\d{4}-\\d{4}$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("전화번호는 010-XXXX-XXXX 형식으로 입력해야 합니다.");
        }

        memberRepository.save(member);
        return ResponseEntity.ok("회원가입 성공");
    }

    // ✅ 아이디 중복 여부 확인 메서드
    public boolean isUserIdDuplicate(String userId) {
        return memberRepository.existsById(userId);
    }
}
