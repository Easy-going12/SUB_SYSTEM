// ✅ Notice_write.js - DOMContentLoaded 보장
document.addEventListener("DOMContentLoaded", () => {
    const saveBtn = document.getElementById("saveBtn");

    if (!saveBtn) {
        console.warn("❗ 저장 버튼 (#saveBtn)을 찾을 수 없습니다.");
        return;
    }

    saveBtn.addEventListener("click", () => {
        const titleEl = document.getElementById("title");
        const writerEl = document.getElementById("writer");
        const contentEl = document.getElementById("content");

        if (!titleEl || !writerEl || !contentEl) {
            console.error("❌ 필수 입력 요소가 존재하지 않습니다.");
            return;
        }

        const title = titleEl.value.trim();
        const writer = writerEl.value.trim();
        const content = contentEl.value.trim();

        if (!title || !writer || !content) {
            alert("모든 항목을 입력해 주세요.");
            return;
        }

        console.log("✅ 작성 완료:", { title, writer, content });

        loadNoticePage(); // 저장 후 목록으로 이동
    });
});
