package ikw.school.busreservation.service;

import ikw.school.busreservation.entity.NoticeBoard;
import ikw.school.busreservation.repository.NoticeBoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoticeBoardService {

    private final NoticeBoardRepository noticeBoardRepository;

    public NoticeBoardService(NoticeBoardRepository noticeBoardRepository) {
        this.noticeBoardRepository = noticeBoardRepository;
    }

    // 전체 공지사항 조회
    public List<NoticeBoard> findAll() {
        return noticeBoardRepository.findAll();
    }

    // 공지사항 단건 조회
    public NoticeBoard findById(Integer id) {
        Optional<NoticeBoard> result = noticeBoardRepository.findById(id);
        return result.orElse(null);
    }

    // 공지사항 저장 (작성 or 수정)
    public void save(NoticeBoard noticeBoard) {
        noticeBoardRepository.save(noticeBoard);
    }

    // 공지사항 삭제
    public void delete(Integer id) {
        noticeBoardRepository.deleteById(id);
    }

    // ✅ 최근 공지 3개만 가져오기 (홈 화면용)
    public List<NoticeBoard> findRecentNotices(int count) {
        return noticeBoardRepository.findTop3ByOrderByCreatedAtDesc();
    }
}
