// ✅ 공지사항 글쓰기 버튼 이벤트 바인딩 함수
function handleNoticeBoardEvents() {
    const writeBtn = document.getElementById("writeBtn");

    if (!writeBtn) {
        console.warn("❗ #writeBtn 버튼이 DOM에 존재하지 않습니다.");
        return;
    }

    console.log("🖊️ 글쓰기 버튼 바인딩 완료");

    writeBtn.addEventListener("click", () => {
        console.log("🟢 글쓰기 버튼 클릭됨");

        // 1. 글쓰기 폼 HTML 로드
        fetch("/Notice_board/Notice_write.html")
            .then(res => res.text())
            .then(html => {
                const parser = new DOMParser();
                const doc = parser.parseFromString(html, "text/html");
                const content = doc.querySelector(".container");

                const main = document.querySelector("main");
                main.innerHTML = '';
                if (content) {
                    main.appendChild(content);
                }

                // 2. CSS 동적 로드
                const cssHref = "/Notice_board/Notice_write.css";
                if (!document.querySelector(`link[href="${cssHref}"]`)) {
                    const link = document.createElement("link");
                    link.rel = "stylesheet";
                    link.href = cssHref;
                    document.head.appendChild(link);
                    console.log("🎨 Notice_write.css 로드 완료");
                }

                // 3. JS 동적 로드
                const jsSrc = "/Notice_board/Notice_write.js";
                if (!document.querySelector(`script[src="${jsSrc}"]`)) {
                    const script = document.createElement("script");
                    script.src = jsSrc;
                    script.defer = true;
                    script.onload = () => {
                        console.log("✅ Notice_write.js 로드 완료");
                    };
                    document.body.appendChild(script);
                }
            })
            .catch(err => console.error("❌ 글쓰기 페이지 로드 실패:", err));
    });
}
