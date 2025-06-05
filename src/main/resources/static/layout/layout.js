// ✅ 사이드바 토글
function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    sidebar.style.left = (sidebar.style.left === '0px') ? '-250px' : '0px';
}

// ✅ QR 모달 열기/닫기
function openQRModal() {
    document.getElementById('qrModal').style.display = 'block';
}
function closeQRModal() {
    document.getElementById('qrModal').style.display = 'none';
}

// ✅ 홈화면으로 이동 (중복 방지용 함수 이름 통일)
function goToHome() {
    window.location.href = "/home";
}

// ✅ 각 기능별 페이지 이동 (서버 렌더링 방식)
function loadMemberPage() {
    window.location.href = "/member/mypage";
}
function loadMessageboxPage() {
    window.location.href = "/messagebox";
}
function loadBoardingPage() {
    window.location.href = "/boarding";
}
function loadBusReservationPage() {
    window.location.href = "/reservation";
}
function loadBusLogPage() {
    window.location.href = "/bus/log";
}
function loadNoticePage() {
    window.location.href = "/notice";
}
function loadStationPage() {
    window.location.href = "/station";
}

// ✅ 탭 전환
function showTab(tabName) {
    const logSection = document.getElementById('log-section');
    const historySection = document.getElementById('history-section');

    if (logSection && historySection) {
        logSection.style.display = (tabName === 'log') ? 'block' : 'none';
        historySection.style.display = (tabName === 'history') ? 'block' : 'none';
    }

    const buttons = document.querySelectorAll('.tab');
    buttons.forEach(btn => btn.classList.remove('active'));
    if (tabName === 'log') buttons[0].classList.add('active');
    else buttons[1].classList.add('active');
}

// ✅ 초기 사이드 메뉴 바인딩
document.addEventListener("DOMContentLoaded", () => {
    const menuMap = {
        "menu-member": loadMemberPage,
        "menu-messagebox": loadMessageboxPage,
        "menu-boarding": loadBoardingPage,
        "menu-bus-reservation": loadBusReservationPage,
        "menu-bus-log": loadBusLogPage,
        "menu-notice": loadNoticePage,
        "menu-station": loadStationPage
    };

    for (const [id, handler] of Object.entries(menuMap)) {
        const el = document.getElementById(id);
        if (el) el.addEventListener("click", handler);
    }

    // ✅ 헤더 타이틀 클릭 → 홈 이동
    const title = document.querySelector(".center-title");
    if (title) title.addEventListener("click", goToHome);

    // ✅ 사이드바 "경운대학교" 클릭 → 홈 이동
    const sidebarTitle = document.querySelector(".sidebar-header h2");
    if (sidebarTitle) sidebarTitle.addEventListener("click", goToHome);

    // ✅ 로그아웃 버튼
    const logoutMenu = document.getElementById("menu-logout");
    if (logoutMenu) {
        logoutMenu.addEventListener("click", () => {
            window.location.href = "/login";
        });
    }
});
