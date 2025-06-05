package ikw.school.busreservation.service;

import ikw.school.busreservation.entity.Member;
import ikw.school.busreservation.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    private final MemberRepository memberRepository;

    public LoginService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // ✅ 로그인 검증
    public boolean login(String userId, String password) {
        return memberRepository.findByUserId(userId)
                .map(member -> member.getPassword().equals(password))
                .orElse(false);
    }

    // ✅ 사용자 전체 정보 가져오기
    public Optional<Member> getMember(String userId) {
        return memberRepository.findByUserId(userId);
    }

    // ✅ 사용자 이름만 가져오기
    public String getNameByUserId(String userId) {
        return memberRepository.findByUserId(userId)
                .map(Member::getName)
                .orElse("이름없음");
    }
}
