// ✅ 알림 팝업 열고 닫기 (전역 등록)
window.toggleNotificationPopup = function () {
    const popup = document.getElementById("notification-popup");
    popup.style.display = (popup.style.display === "none" || popup.style.display === "") ? "block" : "none";

    if (popup.style.display === "block") {
        loadNotifications();
    }
};

// ✅ 로그인된 사용자 ID
const currentUserId = typeof window.currentUserId !== 'undefined' ? window.currentUserId : '[[${session.userId}]]';

// ✅ 알림 불러오기
function loadNotifications() {
    const url = `/api/notifications/${currentUserId}`;
    console.log("📡 요청 URL:", url);
    console.log("🔎 currentUserId:", currentUserId);

    fetch(url)
        .then(response => {
            console.log("✅ Fetch 상태코드:", response.status);
            if (!response.ok) {
                throw new Error("서버 오류 또는 로그인 세션 누락");
            }
            return response.json();
        })
        .then(data => {
            console.log("📦 알림 데이터:", data);
            const list = document.getElementById("notification-list");
            list.innerHTML = "";

            if (data.length === 0) {
                const li = document.createElement("li");
                li.textContent = "🔔 새로운 알림이 없습니다.";
                list.appendChild(li);
                return;
            }

            // ⏫ 페널티 알림 먼저 (최대 2개)
            const penaltyNotices = data.filter(n => n.category === "penalty").slice(0, 2);
            penaltyNotices.forEach(renderNotification);

            // ✅ 일반 알림 최대 5개
            const otherNotices = data.filter(n => n.category !== "penalty").slice(0, 5);
            otherNotices.forEach(renderNotification);
        })
        .catch(error => {
            console.error("❌ 알림 불러오기 실패:", error);
        });
}

// ✅ 알림 하나 렌더링 함수
function renderNotification(notification) {
    const li = document.createElement("li");
    li.textContent = (notification.category === "penalty" ? "⚠️ " : "📌 ") + notification.content;
    li.dataset.id = notification.notificationId;

    if (notification.checked) {
        li.style.color = "#aaa";
        li.style.textDecoration = "line-through";
    }

    li.addEventListener("click", () => {
        if (!notification.checked) {
            fetch(`/api/notifications/check/${notification.notificationId}`, {
                method: "PATCH"
            }).then(() => {
                li.style.color = "#aaa";
                li.style.textDecoration = "line-through";
                notification.checked = true;
            });
        }
    });

    const delBtn = document.createElement("button");
    delBtn.textContent = "❌";
    delBtn.style.marginLeft = "10px";
    delBtn.style.background = "none";
    delBtn.style.border = "none";
    delBtn.style.cursor = "pointer";
    delBtn.style.color = "#999";

    delBtn.addEventListener("click", (e) => {
        e.stopPropagation();
        fetch(`/api/notifications/${notification.notificationId}`, {
            method: "DELETE"
        }).then(() => {
            li.remove();
        });
    });

    li.appendChild(delBtn);

    const list = document.getElementById("notification-list");
    list.appendChild(li);
}

// ✅ 로그인 후 자동 알림 로딩
document.addEventListener("DOMContentLoaded", () => {
    loadNotifications();
});
