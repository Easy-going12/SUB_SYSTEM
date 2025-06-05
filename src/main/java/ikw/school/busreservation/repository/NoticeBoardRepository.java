package ikw.school.busreservation.repository;

import ikw.school.busreservation.entity.NoticeBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Integer> {

    // ✅ 최근 공지사항 3개를 작성일 기준으로 가져오기
    List<NoticeBoard> findTop3ByOrderByCreatedAtDesc();
}
