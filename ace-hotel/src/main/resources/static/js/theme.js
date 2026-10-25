/**
 * ACE Hotel - Theme & UI Interaction Utilities (theme.js)
 */

// Immediate Anti-FOUC (Flash of Unstyled Content) theme loader
(function () {
  try {
    var saved = localStorage.getItem('ace-theme');
    var theme = (saved === 'light' || saved === 'dark') ? saved : 'dark';
    document.documentElement.setAttribute('data-theme', theme);
  } catch (e) {
    document.documentElement.setAttribute('data-theme', 'dark');
  }
})();

// Toggle theme between dark and light mode
function toggleAceTheme() {
  try {
    var current = document.documentElement.getAttribute('data-theme') || 'dark';
    var next = current === 'dark' ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', next);
    localStorage.setItem('ace-theme', next);
  } catch (e) {}
}

/**
 * Surcharge (Phụ thu nhận phòng sớm / trả phòng muộn) Helper:
 * @param {string} targetInputId ID của ô input số tiền VNĐ
 * @param {number} rate Tỉ lệ phụ thu (0, 0.3, 0.5, 1.0)
 * @param {number} baseRoomCharge Đơn giá 1 đêm phòng gốc
 * @param {HTMLElement} btnEl Nút bấm vừa click
 */
function applyFeeRate(targetInputId, rate, baseRoomCharge, btnEl) {
  var input = document.getElementById(targetInputId);
  if (!input) return;

  var calculatedFee = Math.round(baseRoomCharge * rate);
  input.value = calculatedFee;

  // Cập nhật class active cho nhóm nút
  if (btnEl && btnEl.parentElement) {
    var siblingBtns = btnEl.parentElement.querySelectorAll('.fee-btn');
    siblingBtns.forEach(function(b) { b.classList.remove('active'); });
    btnEl.classList.add('active');
  }

  // Kích hoạt sự kiện change nếu có hàm tính tổng tự động
  input.dispatchEvent(new Event('input', { bubbles: true }));
  input.dispatchEvent(new Event('change', { bubbles: true }));
}
