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
    applyAceThemeDirect(next);
  } catch (e) {}
}

// Áp dụng theme trực tiếp ('dark' hoặc 'light') & đồng bộ UI toàn diện
function applyAceThemeDirect(themeName) {
  try {
    var theme = (themeName === 'light' || themeName === 'dark') ? themeName : 'dark';
    document.documentElement.setAttribute('data-theme', theme);
    localStorage.setItem('ace-theme', theme);
    syncAceThemeUI(theme);
  } catch (e) {}
}

// Đồng bộ trạng thái visual của các nút bấm theme trong Dropdown và Header
function syncAceThemeUI(currentTheme) {
  try {
    var theme = currentTheme || document.documentElement.getAttribute('data-theme') || localStorage.getItem('ace-theme') || 'dark';
    var btnDark = document.getElementById('btnThemeDark');
    var btnLight = document.getElementById('btnThemeLight');
    var tagStatus = document.getElementById('pmsThemeStatusTag');

    if (btnDark) {
      if (theme === 'dark') {
        btnDark.classList.add('active');
      } else {
        btnDark.classList.remove('active');
      }
    }

    if (btnLight) {
      if (theme === 'light') {
        btnLight.classList.add('active');
      } else {
        btnLight.classList.remove('active');
      }
    }

    if (tagStatus) {
      tagStatus.textContent = theme === 'dark' ? 'Chế độ Tối' : 'Chế độ Sáng';
    }
  } catch (e) {}
}

// Tự động đồng bộ UI khi tải trang
if (typeof document !== 'undefined') {
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', function () {
      syncAceThemeUI();
    });
  } else {
    syncAceThemeUI();
  }
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
