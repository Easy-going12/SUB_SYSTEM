document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.querySelector(".login-form");

    loginForm.addEventListener("submit", (e) => {
        e.preventDefault();

        const studentId = document.getElementById("studentId").value.trim();
        const password = document.getElementById("password").value.trim();

        // ✅ 필수 입력 체크
        if (!studentId || !password) {
            alert("학번/교번과 비밀번호를 입력해주세요.");
            return;
        }

        // ✅ 간단한 길이 체크 (4자리 이상)
        if (studentId.length < 4 || password.length < 4) {
            alert("입력 정보를 다시 확인해주세요.");
            return;
        }

        // ✅ 로그인 성공 처리 (임시)
        console.log("로그인 시도:", studentId, password);

        // ✅ 로그인 성공 시 main 페이지(index.html)로 이동
        // ✅ 정적 경로가 아닌 컨트롤러 경로로 이동
        window.location.href = "/main";  // 또는 "/index"처럼 실제 mapping된 경로



    });
});

// ✅ 회원가입 버튼 클릭 시
function goToJoin() {
    alert("회원가입 기능은 현재 구현 중입니다.");
}
