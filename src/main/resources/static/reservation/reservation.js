// 숨겨진 input 값 설정
function setHiddenInput(id, value) {
    const input = document.getElementById(id);
    if (input) input.value = value;
}

// 날짜 없이 시간만 오는 경우 오늘 날짜로 보정
function formatTimeWithToday(timeStr) {
    const today = new Date();
    const datePart = today.toISOString().split("T")[0];
    const timeOnly = timeStr.includes("T") ? timeStr.split("T")[1] : timeStr;
    const composed = new Date(`${datePart}T${timeOnly}`);
    return composed.toLocaleString("ko-KR", {
        hour: "2-digit",
        minute: "2-digit",
        hour12: true,
    });
}

// 등교 / 하교 선택 처리
function selectMode(mode, buttonElement) {
    document.querySelectorAll('.button-group button').forEach(btn => btn.classList.remove('active'));
    buttonElement.classList.add('active');
    setHiddenInput("reservationType", mode);

    const routeSelect = document.getElementById('routeSelect');
    routeSelect.innerHTML = '<option value="">노선 선택</option>';

    fetch("/station/ajax/list")
        .then(res => res.json())
        .then(lines => {
            lines.forEach(line => {
                const option = document.createElement('option');
                option.value = line.id;
                option.textContent = `${line.name} (${line.dispatchNumber})`;
                routeSelect.appendChild(option);
            });
        });

    document.getElementById('departureList').innerHTML = '';
    document.getElementById('stationList').innerHTML = '';
    document.getElementById('stationSection').style.display = 'none';
    document.getElementById('seatSection').style.display = 'none';
}

// 노선 선택 → 출발 시간 동기화
document.getElementById('routeSelect').addEventListener('change', function () {
    const lineId = this.value;
    const type = document.getElementById("reservationType").value;
    setHiddenInput("lineId", lineId);

    if (lineId === '5') {
        document.getElementById('chartContainer').style.display = 'block';
        renderSelectedChartByLineId(lineId);  // ✅ lineId만 전달
    } else {
        document.getElementById('chartContainer').style.display = 'none';
    }

    const listContainer = document.getElementById('departureList');
    listContainer.innerHTML = '';
    document.getElementById('stationList').innerHTML = '';
    document.getElementById('stationSection').style.display = 'none';
    document.getElementById('seatSection').style.display = 'none';

    fetch(`/reservation/departures?lineId=${lineId}&type=${type}`)
        .then(res => res.json())
        .then(departures => {
            departures.forEach(dep => {
                const li = document.createElement('li');
                li.className = 'departure-item';

                const dateStr = formatTimeWithToday(dep.departure);
                li.innerHTML = `
                    <span>${dep.bus.busNumber}번 (${dateStr}) / 잔여석: ${dep.availableSeats}</span>
                    <span>&#8250;</span>
                `;

                li.addEventListener('click', () => {
                    document.querySelectorAll('.departure-item').forEach(el => el.classList.remove('active'));
                    li.classList.add('active');
                    setHiddenInput("departureTimeId", dep.id);

                    renderStationList(dep.line.id, dep.id);

                    fetch(`/reservation/seats?departureTimeId=${dep.id}&busId=${dep.bus.id}`)
                        .then(res => res.json())
                        .then(reservedSeats => {
                            console.log("예약된 좌석:", reservedSeats);
                            const totalSeats = dep.bus.capacity || 44;
                            const busType = dep.bus.busType || '';
                            renderSeats(reservedSeats, totalSeats, busType);
                        });
                });

                listContainer.appendChild(li);
            });
        });
});

// 정류장 목록 렌더링
function renderStationList(lineId, departureTimeId) {
    const stationList = document.getElementById('stationList');
    stationList.innerHTML = '';
    document.getElementById('stationSection').style.display = 'block';

    fetch(`/reservation/stations?lineId=${lineId}&departureTimeId=${departureTimeId}`)
        .then(res => res.json())
        .then(stops => {
            stops.forEach(stop => {
                const li = document.createElement('li');
                li.className = 'station-item';
                li.innerHTML = `
                    <span>
                        ${stop.name}<br>
                        <small>도착예정: ${formatTimeWithToday(stop.arriveTime)}</small>
                    </span>
                `;

                li.addEventListener('click', () => {
                    document.querySelectorAll('.station-item').forEach(el => el.classList.remove('active'));
                    li.classList.add('active');
                    setHiddenInput("stopId", stop.id);
                });

                stationList.appendChild(li);
            });
        });
}

// 좌석 렌더링
function renderSeats(reservedSeats = [], totalSeats = 44) {
    const seatSection = document.getElementById('seatSection');
    seatSection.style.display = 'block';

    const seatLayout = document.querySelector('.seat-layout');
    seatLayout.innerHTML = '';

    const isPremium = totalSeats === 28;

    let seatNumber = 1;

    if (isPremium) {
        // 프리미엄 28석 (왼쪽 2줄 + 오른쪽 1줄)
        while (seatNumber <= 24) {
            const row = document.createElement('div');
            row.className = 'seat-row';

            for (let i = 0; i < 2 && seatNumber <= 24; i++) {
                row.appendChild(createSeatButton(seatNumber++, reservedSeats));
            }

            const aisle = document.createElement('div');
            aisle.className = 'seat-placeholder';
            row.appendChild(aisle);

            row.appendChild(createSeatButton(seatNumber++, reservedSeats));

            seatLayout.appendChild(row);
        }

        const lastRow = document.createElement('div');
        lastRow.className = 'seat-row';
        for (let i = 0; i < 4 && seatNumber <= 28; i++) {
            lastRow.appendChild(createSeatButton(seatNumber++, reservedSeats));
        }
        seatLayout.appendChild(lastRow);

    } else {
        while (seatNumber <= totalSeats) {
            const row = document.createElement('div');
            row.className = 'seat-row';

            for (let i = 0; i < 2 && seatNumber <= totalSeats; i++) {
                row.appendChild(createSeatButton(seatNumber++, reservedSeats));
            }

            const aisle = document.createElement('div');
            aisle.className = 'seat-placeholder';
            row.appendChild(aisle);

            for (let i = 0; i < 2 && seatNumber <= totalSeats; i++) {
                row.appendChild(createSeatButton(seatNumber++, reservedSeats));
            }

            seatLayout.appendChild(row);
        }
    }
}

// 좌석 버튼 생성
function createSeatButton(num, reservedSeats) {
    const seat = document.createElement('button');
    seat.className = 'seat-btn';
    seat.innerText = `${num}번`;
    seat.dataset.seatNumber = num;

    if (Array.isArray(reservedSeats) && reservedSeats.includes(num)) {
        seat.disabled = true;
        seat.classList.add("disabled");
        return seat;
    }

    seat.addEventListener('mousedown', () => seat.classList.add('click_active'));
    seat.addEventListener('mouseup', () => {
        seat.classList.remove('click_active');
        document.querySelectorAll('.seat-btn').forEach(btn => btn.classList.remove('selected_active'));
        seat.classList.add('selected_active');
        setHiddenInput("seatNumber", seat.dataset.seatNumber);
    });

    return seat;
}

// 예약 폼 유효성 검사
const form = document.getElementById("reservationForm");
if (form) {
    form.addEventListener("submit", function (e) {
        const fields = ["reservationType", "lineId", "departureTimeId", "stopId", "seatNumber"];
        const missing = fields.find(id => !document.getElementById(id).value);
        if (missing) {
            e.preventDefault();
            alert("모든 정보를 선택해 주세요.");
        }
    });
}

function renderSelectedChartByLineId(lineId) {
    fetch(`/api/reservations/chart/line?lineId=${lineId}`)
        .then(res => res.json())
        .then(data => {
            if (!Array.isArray(data)) {
                console.error("그래프 응답 오류:", data);
                return;
            }

            const labels = data.map(d => d.time);
            const values = data.map(d => Math.round(d.avg_reserved_count));

            const ctx = document.getElementById('busChart').getContext('2d');
            if (window.currentChart) window.currentChart.destroy();

            window.currentChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: '시간대별 평균 예약자 수',
                        data: values,
                        backgroundColor: 'rgba(75, 192, 192, 0.6)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true,
                            max: 44,
                            title: { display: true, text: '평균 인원 수' }
                        },
                        x: {
                            title: { display: true, text: '시간대' }
                        }
                    },
                    plugins: {
                        title: {
                            display: true,
                            text: '시간대별 평균 예약자 수',
                            font: { size: 16 }
                        }
                    }
                }
            });
        });
}