function validateForm() {
    const userId = document.querySelector('input[name="userId"]').value;
    const password = document.querySelector('input[name="password"]').value;
    const phoneNumber = document.querySelector('input[name="phoneNumber"]').value;
    const errorDiv = document.getElementById("errorMessage");

    errorDiv.textContent = ""; // 초기화

    // 학번(숫자 9자리) 검사
    const idPattern = /^\d{9}$/;
    if (!idPattern.test(userId)) {
        errorDiv.textContent = "⚠ 학번은 숫자 9자리 형식으로 입력해주세요. (예: 201905006)";
        return false;
    }

    // 비밀번호 유효성 검사 (영문 + 숫자 조합, 10자리 이하)
    const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{1,10}$/;
    if (!passwordPattern.test(password)) {
        errorDiv.textContent = "⚠ 비밀번호는 영문과 숫자를 포함하고, 10자리 이하로 입력해야 합니다.";
        return false;
    }

    // 전화번호 유효성 검사 (010-XXXX-XXXX 형식)
    const phonePattern = /^010-\d{4}-\d{4}$/;
    if (!phonePattern.test(phoneNumber)) {
        errorDiv.textContent = "⚠ 전화번호는 010-XXXX-XXXX 형식으로 입력해주세요. (예: 010-1234-5678)";
        return false;
    }

    return true;
}

// ✅ 아이디(학번) 실시간 중복 확인
document.addEventListener("DOMContentLoaded", () => {
    const userIdInput = document.querySelector('input[name="userId"]');
    const idStatus = document.getElementById("idStatus");

    if (userIdInput) {
        userIdInput.addEventListener("input", () => {
            const userId = userIdInput.value.trim();
            const idPattern = /^\d{9}$/;

            if (!idPattern.test(userId)) {
                idStatus.textContent = "⚠ 학번은 숫자 9자리여야 합니다.";
                idStatus.style.color = "red";
                return;
            }

            fetch(`/api/auth/check-id?userId=${userId}`)
                .then(res => res.text())
                .then(result => {
                    if (result === "duplicate") {
                        idStatus.textContent = "⚠ 중복된 아이디입니다.";
                        idStatus.style.color = "red";
                    } else {
                        idStatus.textContent = "✓ 사용 가능한 아이디입니다.";
                        idStatus.style.color = "green";
                    }
                })
                .catch(err => {
                    idStatus.textContent = "⚠ 확인 중 오류 발생";
                    idStatus.style.color = "red";
                });
        });
    }

    // ✅ 전화번호 입력 시 하이픈 자동 삽입
    const phoneInput = document.querySelector('input[name="phoneNumber"]');
    if (phoneInput) {
        phoneInput.addEventListener("input", (e) => {
            let input = e.target.value.replace(/[^0-9]/g, ""); // 숫자만 추출
            if (input.length > 3 && input.length <= 7) {
                input = input.replace(/^(\d{3})(\d+)/, "$1-$2");
            } else if (input.length > 7) {
                input = input.replace(/^(\d{3})(\d{4})(\d+)/, "$1-$2-$3");
            }
            e.target.value = input;
        });
    }
});
