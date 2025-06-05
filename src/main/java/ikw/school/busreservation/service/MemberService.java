package ikw.school.busreservation.service;

import ikw.school.busreservation.entity.Member;
import ikw.school.busreservation.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // ✅ 페널티 증가 (userId 기반)
    public void addPenaltyToMember(String userId) {
        Member member = findByUserId(userId);
        if (member != null) {
            member.setPenaltyCount(member.getPenaltyCount() + 1);
            memberRepository.save(member);
        }
    }

    // ✅ userId로 회원 정보 조회 (null, 공백 방지 처리)
    public Member findByUserId(String userId) {
        if (userId == null || userId.isBlank()) return null;
        return memberRepository.findById(userId).orElse(null);
    }

    // ✅ 연락처 업데이트
    public void updatePhoneNumber(String userId, String newPhoneNumber) {
        Member member = findByUserId(userId);
        if (member != null) {
            member.setPhoneNumber(newPhoneNumber);
            memberRepository.save(member);
        }
    }

    // ✅ 회원정보 저장 (신규 또는 기존)
    public void save(Member member) {
        memberRepository.save(member);
    }

    // ✅ 회원 삭제
    public void deleteByUserId(String userId) {
        if (userId != null && !userId.isBlank()) {
            memberRepository.deleteById(userId);
        }
    }

    // ✅ 회원 존재 확인
    public boolean existsByUserId(String userId) {
        return memberRepository.existsById(userId);
    }
}
