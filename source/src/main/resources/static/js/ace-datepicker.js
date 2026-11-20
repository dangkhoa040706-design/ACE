/**
 * ACE HOTEL - LUXURY OBSIDIAN CALENDAR DATEPICKER (ace-datepicker.js)
 * Thiết kế giao diện cuốn lịch độc quyền chuẩn 5 sao Luxury cho ACE Hotel.
 * Hỗ trợ chọn ngày sinh, ngày check-in/check-out nhanh chóng với bộ chọn Tháng/Năm trực quan.
 */
(function() {
  'use strict';

  var MONTH_NAMES = [
    'Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4',
    'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8',
    'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'
  ];

  var DAY_NAMES = ['T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'CN'];

  function pad(num) {
    return num < 10 ? '0' + num : String(num);
  }

  function parseISODate(str) {
    if (!str) return null;
    var parts = str.split('-');
    if (parts.length === 3) {
      var y = parseInt(parts[0], 10);
      var m = parseInt(parts[1], 10) - 1;
      var d = parseInt(parts[2], 10);
      if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
        return new Date(y, m, d);
      }
    }
    return null;
  }

  function formatDisplay(date) {
    if (!date) return '';
    return pad(date.getDate()) + '/' + pad(date.getMonth() + 1) + '/' + date.getFullYear();
  }

  function formatISO(date) {
    if (!date) return '';
    return date.getFullYear() + '-' + pad(date.getMonth() + 1) + '-' + pad(date.getDate());
  }

  function isLightTheme() {
    return document.documentElement.getAttribute('data-theme') === 'light';
  }

  function AceDatePicker(originalInput, options) {
    this.originalInput = originalInput;
    this.options = options || {};
    this.minYear = this.options.minYear || 1930;
    this.maxYear = this.options.maxYear || (new Date().getFullYear() + 10);

    var initVal = this.originalInput.value;
    this.selectedDate = parseISODate(initVal);
    
    var viewBase = this.selectedDate || new Date();
    this.viewYear = viewBase.getFullYear();
    this.viewMonth = viewBase.getMonth();

    this.initDOM();
    this.bindEvents();
  }

  AceDatePicker.prototype.initDOM = function() {
    // Ẩn input gốc nhưng giữ nguyên name & id để form submit dữ liệu
    var origWidth = this.originalInput.style.width;
    this.originalInput.style.display = 'none';

    // Tạo wrapper cho custom input
    this.wrapper = document.createElement('div');
    this.wrapper.className = 'ace-dp-wrapper';
    this.wrapper.style.position = 'relative';
    this.wrapper.style.display = 'inline-block';
    this.wrapper.style.width = (origWidth && origWidth !== 'auto') ? origWidth : '100%';

    // Tạo visible display input
    this.displayInput = document.createElement('input');
    this.displayInput.type = 'text';
    this.displayInput.readOnly = true;
    this.displayInput.className = this.originalInput.className || 'form-control';
    this.displayInput.placeholder = this.options.placeholder || 'Chọn ngày (dd/mm/yyyy)...';
    this.displayInput.style.width = '100%';
    this.displayInput.style.cursor = 'pointer';
    this.displayInput.style.paddingRight = '36px';
    this.displayInput.style.fontFamily = "'Inter', sans-serif";
    this.displayInput.style.fontWeight = '600';
    this.displayInput.style.color = 'var(--text, #F8FAFC)';
    this.displayInput.style.background = 'var(--surface, #121826)';
    this.displayInput.style.borderColor = 'var(--border, rgba(245, 158, 11, 0.35))';
    if (this.originalInput.style.padding) {
      this.displayInput.style.padding = this.originalInput.style.padding;
      this.displayInput.style.paddingRight = '36px';
    }
    if (this.originalInput.style.fontSize) {
      this.displayInput.style.fontSize = this.originalInput.style.fontSize;
    }

    if (this.selectedDate) {
      this.displayInput.value = formatDisplay(this.selectedDate);
    }

    // Icon cuốn lịch ánh kim sang trọng
    this.iconBtn = document.createElement('button');
    this.iconBtn.type = 'button';
    this.iconBtn.className = 'ace-dp-icon';
    this.iconBtn.innerHTML = '<svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#FDBF2D" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect><line x1="16" y1="2" x2="16" y2="6"></line><line x1="8" y1="2" x2="8" y2="6"></line><line x1="3" y1="10" x2="21" y2="10"></line></svg>';
    this.iconBtn.style.position = 'absolute';
    this.iconBtn.style.right = '10px';
    this.iconBtn.style.top = '50%';
    this.iconBtn.style.transform = 'translateY(-50%)';
    this.iconBtn.style.background = 'none';
    this.iconBtn.style.border = 'none';
    this.iconBtn.style.cursor = 'pointer';
    this.iconBtn.style.padding = '4px';
    this.iconBtn.style.display = 'flex';
    this.iconBtn.style.alignItems = 'center';
    this.iconBtn.style.justifyContent = 'center';

    this.originalInput.parentNode.insertBefore(this.wrapper, this.originalInput);
    this.wrapper.appendChild(this.originalInput);
    this.wrapper.appendChild(this.displayInput);
    this.wrapper.appendChild(this.iconBtn);

    // Tạo popup popover cuốn lịch
    this.popover = document.createElement('div');
    this.popover.className = 'ace-calendar-popover';
    this.popover.style.display = 'none';
    this.popover.style.position = 'absolute';
    this.popover.style.zIndex = '9999999';
    this.popover.style.width = '320px';
    this.popover.style.background = '#0F172A';
    this.popover.style.border = '1px solid rgba(245, 158, 11, 0.45)';
    this.popover.style.borderRadius = '14px';
    this.popover.style.boxShadow = '0 20px 45px rgba(0, 0, 0, 0.75), 0 0 15px rgba(245, 158, 11, 0.15)';
    this.popover.style.padding = '14px';
    this.popover.style.userSelect = 'none';
    this.popover.style.fontFamily = "'Inter', -apple-system, BlinkMacSystemFont, sans-serif";

    document.body.appendChild(this.popover);
  };

  AceDatePicker.prototype.bindEvents = function() {
    var self = this;

    function toggle(e) {
      if (e) e.stopPropagation();
      if (self.popover.style.display === 'block') {
        self.close();
      } else {
        self.open();
      }
    }

    this.displayInput.addEventListener('click', toggle);
    this.iconBtn.addEventListener('click', toggle);

    document.addEventListener('click', function(e) {
      if (!self.wrapper.contains(e.target) && !self.popover.contains(e.target)) {
        self.close();
      }
    });

    document.addEventListener('keydown', function(e) {
      if (e.key === 'Escape' && self.popover.style.display === 'block') {
        self.close();
      }
    });

    window.addEventListener('resize', function() {
      if (self.popover.style.display === 'block') {
        self.updatePosition();
      }
    });

    window.addEventListener('scroll', function() {
      if (self.popover.style.display === 'block') {
        self.updatePosition();
      }
    }, true);
  };

  AceDatePicker.prototype.updatePosition = function() {
    var rect = this.displayInput.getBoundingClientRect();
    var popoverHeight = 360;
    var top = rect.bottom + window.scrollY + 6;

    // Nếu bị tràn cạnh dưới màn hình, hiển thị phía trên input
    if (rect.bottom + popoverHeight > window.innerHeight && rect.top - popoverHeight > 0) {
      top = rect.top + window.scrollY - popoverHeight - 6;
    }

    var left = rect.left + window.scrollX;
    if (left + 320 > window.innerWidth) {
      left = window.innerWidth - 330;
    }

    this.popover.style.top = top + 'px';
    this.popover.style.left = left + 'px';
  };

  AceDatePicker.prototype.open = function() {
    // Đóng tất cả các datepicker khác đang mở
    var openPickers = document.querySelectorAll('.ace-calendar-popover');
    for (var i = 0; i < openPickers.length; i++) {
      openPickers[i].style.display = 'none';
    }

    this.sync();
    if (this.selectedDate) {
      this.viewYear = this.selectedDate.getFullYear();
      this.viewMonth = this.selectedDate.getMonth();
    }
    this.render();
    this.popover.style.display = 'block';
    this.updatePosition();
  };

  AceDatePicker.prototype.sync = function() {
    this.selectedDate = parseISODate(this.originalInput.value);
    this.displayInput.value = this.selectedDate ? formatDisplay(this.selectedDate) : '';
  };

  AceDatePicker.prototype.close = function() {
    this.popover.style.display = 'none';
  };

  AceDatePicker.prototype.render = function() {
    var self = this;
    var light = isLightTheme();
    var cBg = light ? '#FFFFFF' : '#0F172A';
    var cBorder = light ? '#CBD5E1' : 'rgba(245, 158, 11, 0.45)';
    var cText = light ? '#0F172A' : '#E2E8F0';
    var cMuted = light ? '#64748B' : '#94A3B8';
    var cSubtleBg = light ? '#F1F5F9' : 'rgba(255, 255, 255, 0.06)';
    var cSelectBg = light ? '#F8FAFC' : '#1E293B';
    var cAccent = light ? '#B45309' : '#FDBF2D';
    var cDivider = light ? '#E2E8F0' : 'rgba(255, 255, 255, 0.1)';

    this.popover.style.background = cBg;
    this.popover.style.border = '1px solid ' + cBorder;
    this.popover.style.boxShadow = light
      ? '0 20px 40px rgba(15, 23, 42, 0.16), 0 0 0 1px rgba(180, 83, 9, 0.08)'
      : '0 20px 45px rgba(0, 0, 0, 0.75), 0 0 15px rgba(245, 158, 11, 0.15)';
    this.popover.innerHTML = '';

    var minDate = parseISODate(this.originalInput.getAttribute('min'));
    var maxDate = parseISODate(this.originalInput.getAttribute('max'));

    // 1. Header (Nút Prev, Chọn Tháng, Chọn Năm, Nút Next)
    var header = document.createElement('div');
    header.style.display = 'flex';
    header.style.alignItems = 'center';
    header.style.justifyContent = 'space-between';
    header.style.marginBottom = '12px';
    header.style.paddingBottom = '10px';
    header.style.borderBottom = '1px solid ' + cDivider;

    var btnPrev = document.createElement('button');
    btnPrev.type = 'button';
    btnPrev.innerHTML = '&#9664;';
    btnPrev.style.background = cSubtleBg;
    btnPrev.style.border = '1px solid rgba(245, 158, 11, 0.28)';
    btnPrev.style.color = cAccent;
    btnPrev.style.borderRadius = '6px';
    btnPrev.style.width = '30px';
    btnPrev.style.height = '30px';
    btnPrev.style.cursor = 'pointer';
    btnPrev.style.display = 'flex';
    btnPrev.style.alignItems = 'center';
    btnPrev.style.justifyContent = 'center';
    btnPrev.style.fontSize = '11px';
    btnPrev.onclick = function(e) {
      e.stopPropagation();
      self.viewMonth--;
      if (self.viewMonth < 0) {
        self.viewMonth = 11;
        self.viewYear--;
      }
      self.render();
    };

    var controlsCenter = document.createElement('div');
    controlsCenter.style.display = 'flex';
    controlsCenter.style.gap = '6px';
    controlsCenter.style.alignItems = 'center';

    // Dropdown chọn Tháng
    var selectMonth = document.createElement('select');
    selectMonth.style.background = cSelectBg;
    selectMonth.style.color = cText;
    selectMonth.style.border = '1px solid rgba(245, 158, 11, 0.35)';
    selectMonth.style.borderRadius = '6px';
    selectMonth.style.padding = '4px 6px';
    selectMonth.style.fontSize = '0.825rem';
    selectMonth.style.fontWeight = '700';
    selectMonth.style.cursor = 'pointer';
    selectMonth.style.outline = 'none';

    MONTH_NAMES.forEach(function(mName, idx) {
      var opt = document.createElement('option');
      opt.value = idx;
      opt.textContent = mName;
      if (idx === self.viewMonth) opt.selected = true;
      selectMonth.appendChild(opt);
    });

    selectMonth.onchange = function(e) {
      e.stopPropagation();
      self.viewMonth = parseInt(this.value, 10);
      self.render();
    };

    // Dropdown chọn Năm
    var selectYear = document.createElement('select');
    selectYear.style.background = cSelectBg;
    selectYear.style.color = cAccent;
    selectYear.style.border = '1px solid rgba(245, 158, 11, 0.35)';
    selectYear.style.borderRadius = '6px';
    selectYear.style.padding = '4px 6px';
    selectYear.style.fontSize = '0.825rem';
    selectYear.style.fontWeight = '800';
    selectYear.style.cursor = 'pointer';
    selectYear.style.outline = 'none';

    for (var y = self.maxYear; y >= self.minYear; y--) {
      var optY = document.createElement('option');
      optY.value = y;
      optY.textContent = y;
      if (y === self.viewYear) optY.selected = true;
      selectYear.appendChild(optY);
    }

    selectYear.onchange = function(e) {
      e.stopPropagation();
      self.viewYear = parseInt(this.value, 10);
      self.render();
    };

    controlsCenter.appendChild(selectMonth);
    controlsCenter.appendChild(selectYear);

    var btnNext = document.createElement('button');
    btnNext.type = 'button';
    btnNext.innerHTML = '&#9654;';
    btnNext.style.background = cSubtleBg;
    btnNext.style.border = '1px solid rgba(245, 158, 11, 0.28)';
    btnNext.style.color = cAccent;
    btnNext.style.borderRadius = '6px';
    btnNext.style.width = '30px';
    btnNext.style.height = '30px';
    btnNext.style.cursor = 'pointer';
    btnNext.style.display = 'flex';
    btnNext.style.alignItems = 'center';
    btnNext.style.justifyContent = 'center';
    btnNext.style.fontSize = '11px';
    btnNext.onclick = function(e) {
      e.stopPropagation();
      self.viewMonth++;
      if (self.viewMonth > 11) {
        self.viewMonth = 0;
        self.viewYear++;
      }
      self.render();
    };

    header.appendChild(btnPrev);
    header.appendChild(controlsCenter);
    header.appendChild(btnNext);
    this.popover.appendChild(header);

    // 2. Weekday Header (T2 -> CN)
    var weekGrid = document.createElement('div');
    weekGrid.style.display = 'grid';
    weekGrid.style.gridTemplateColumns = 'repeat(7, 1fr)';
    weekGrid.style.textAlign = 'center';
    weekGrid.style.marginBottom = '8px';
    weekGrid.style.gap = '2px';

    DAY_NAMES.forEach(function(day, idx) {
      var cell = document.createElement('div');
      cell.textContent = day;
      cell.style.fontSize = '0.72rem';
      cell.style.fontWeight = '700';
      cell.style.color = (idx === 6) ? '#EF4444' : '#94A3B8'; // CN màu đỏ nhẹ
      cell.style.padding = '4px 0';
      weekGrid.appendChild(cell);
    });
    this.popover.appendChild(weekGrid);

    // 3. Days Grid
    var daysGrid = document.createElement('div');
    daysGrid.style.display = 'grid';
    daysGrid.style.gridTemplateColumns = 'repeat(7, 1fr)';
    daysGrid.style.gap = '4px';

    var firstDay = new Date(this.viewYear, this.viewMonth, 1);
    // JS: 0=Chủ nhật, 1=Thứ hai ...
    var dayOfWeek = firstDay.getDay();
    var offset = (dayOfWeek === 0) ? 6 : dayOfWeek - 1; // Đổi về thứ Hai = 0

    var totalDays = new Date(this.viewYear, this.viewMonth + 1, 0).getDate();
    var prevMonthTotalDays = new Date(this.viewYear, this.viewMonth, 0).getDate();

    var today = new Date();
    var todayStr = today.getFullYear() + '-' + today.getMonth() + '-' + today.getDate();

    // Ngày của tháng trước (mờ)
    for (var p = offset - 1; p >= 0; p--) {
      var dCell = document.createElement('div');
      dCell.textContent = prevMonthTotalDays - p;
      dCell.style.textAlign = 'center';
      dCell.style.padding = '8px 0';
      dCell.style.fontSize = '0.8rem';
      dCell.style.color = '#475569';
      dCell.style.borderRadius = '8px';
      dCell.style.opacity = '0.4';
      daysGrid.appendChild(dCell);
    }

    // Các ngày trong tháng hiện tại
    for (var d = 1; d <= totalDays; d++) {
      (function(dayNum) {
        var cellDate = new Date(self.viewYear, self.viewMonth, dayNum);
        var isDisabled = (minDate && cellDate < minDate) || (maxDate && cellDate > maxDate);

        var cell = document.createElement('button');
        cell.type = 'button';
        cell.textContent = dayNum;
        cell.disabled = !!isDisabled;
        cell.style.textAlign = 'center';
        cell.style.padding = '8px 0';
        cell.style.fontSize = '0.825rem';
        cell.style.fontWeight = '600';
        cell.style.borderRadius = '8px';
        cell.style.border = 'none';
        cell.style.cursor = isDisabled ? 'not-allowed' : 'pointer';
        cell.style.transition = 'all 0.15s ease';
        cell.style.background = 'transparent';
        cell.style.color = isDisabled ? cMuted : cText;
        if (isDisabled) {
          cell.style.opacity = '0.28';
        }

        var cellDateStr = self.viewYear + '-' + self.viewMonth + '-' + dayNum;
        var isToday = (cellDateStr === todayStr);

        var isSelected = self.selectedDate &&
          self.selectedDate.getFullYear() === self.viewYear &&
          self.selectedDate.getMonth() === self.viewMonth &&
          self.selectedDate.getDate() === dayNum;

        if (isSelected) {
          cell.style.background = 'linear-gradient(135deg, #F59E0B, #D97706)';
          cell.style.color = '#0B0F17';
          cell.style.fontWeight = '800';
          cell.style.boxShadow = '0 0 10px rgba(245, 158, 11, 0.45)';
          cell.style.opacity = '1';
        } else if (isToday && !isDisabled) {
          cell.style.border = '1px solid ' + cAccent;
          cell.style.color = cAccent;
        }

        if (!isDisabled) {
          cell.onmouseenter = function() {
            if (!isSelected) {
              cell.style.background = 'rgba(245, 158, 11, 0.16)';
              cell.style.color = cAccent;
            }
          };

          cell.onmouseleave = function() {
            if (!isSelected) {
              cell.style.background = 'transparent';
              cell.style.color = isToday ? cAccent : cText;
            }
          };

          cell.onclick = function(e) {
            e.stopPropagation();
            self.setDate(cellDate);
            self.close();
          };
        }

        daysGrid.appendChild(cell);
      })(d);
    }

    // Các ngày của tháng sau để làm đầy ô (42 ô chuẩn)
    var usedCells = offset + totalDays;
    var remainingCells = (usedCells % 7 === 0) ? 0 : 7 - (usedCells % 7);
    for (var n = 1; n <= remainingCells; n++) {
      var nextCell = document.createElement('div');
      nextCell.textContent = n;
      nextCell.style.textAlign = 'center';
      nextCell.style.padding = '8px 0';
      nextCell.style.fontSize = '0.8rem';
      nextCell.style.color = '#475569';
      nextCell.style.borderRadius = '8px';
      nextCell.style.opacity = '0.4';
      daysGrid.appendChild(nextCell);
    }

    this.popover.appendChild(daysGrid);

    // 4. Footer Quick Actions (Hôm nay, Xóa, Đóng)
    var footer = document.createElement('div');
    footer.style.display = 'flex';
    footer.style.justifyContent = 'space-between';
    footer.style.alignItems = 'center';
    footer.style.marginTop = '12px';
    footer.style.paddingTop = '10px';
    footer.style.borderTop = '1px solid rgba(255, 255, 255, 0.1)';

    var btnClear = document.createElement('button');
    btnClear.type = 'button';
    btnClear.textContent = 'Xóa';
    btnClear.style.background = 'none';
    btnClear.style.border = 'none';
    btnClear.style.color = '#94A3B8';
    btnClear.style.fontSize = '0.75rem';
    btnClear.style.fontWeight = '600';
    btnClear.style.cursor = 'pointer';
    btnClear.onclick = function(e) {
      e.stopPropagation();
      self.clear();
      self.close();
    };

    var btnToday = document.createElement('button');
    btnToday.type = 'button';
    btnToday.textContent = 'Hôm nay';
    btnToday.style.background = 'rgba(245, 158, 11, 0.12)';
    btnToday.style.border = '1px solid rgba(245, 158, 11, 0.35)';
    btnToday.style.color = cAccent;
    btnToday.style.fontSize = '0.75rem';
    btnToday.style.fontWeight = '700';
    btnToday.style.padding = '3px 8px';
    btnToday.style.borderRadius = '5px';
    btnToday.style.cursor = 'pointer';
    var todayMidnight = new Date(today.getFullYear(), today.getMonth(), today.getDate());
    var todayDisabled = (minDate && todayMidnight < minDate) || (maxDate && todayMidnight > maxDate);
    if (todayDisabled) {
      btnToday.disabled = true;
      btnToday.style.opacity = '0.35';
      btnToday.style.cursor = 'not-allowed';
    } else {
      btnToday.onclick = function(e) {
        e.stopPropagation();
        self.setDate(todayMidnight);
        self.close();
      };
    }

    var btnClose = document.createElement('button');
    btnClose.type = 'button';
    btnClose.textContent = 'Đóng';
    btnClose.style.background = 'none';
    btnClose.style.border = 'none';
    btnClose.style.color = '#94A3B8';
    btnClose.style.fontSize = '0.75rem';
    btnClose.style.fontWeight = '600';
    btnClose.style.cursor = 'pointer';
    btnClose.onclick = function(e) {
      e.stopPropagation();
      self.close();
    };

    footer.appendChild(btnClear);
    footer.appendChild(btnToday);
    footer.appendChild(btnClose);
    this.popover.appendChild(footer);
  };

  AceDatePicker.prototype.setDate = function(date) {
    this.selectedDate = date;
    var isoStr = formatISO(date);
    var displayStr = formatDisplay(date);

    this.originalInput.value = isoStr;
    this.displayInput.value = displayStr;

    // Kích hoạt change event trên input gốc
    var evt = new Event('change', { bubbles: true });
    this.originalInput.dispatchEvent(evt);
  };

  AceDatePicker.prototype.clear = function() {
    this.selectedDate = null;
    this.originalInput.value = '';
    this.displayInput.value = '';

    var evt = new Event('change', { bubbles: true });
    this.originalInput.dispatchEvent(evt);
  };

  // Tự động khởi tạo cho .ace-datepicker, data-ace-datepicker và toàn bộ input[type="date"]
  function autoInit() {
    var inputs = document.querySelectorAll('input.ace-datepicker, input[data-ace-datepicker], input[type="date"]:not([data-native-date])');
    for (var i = 0; i < inputs.length; i++) {
      if (!inputs[i]._aceDatePicker) {
        inputs[i]._aceDatePicker = new AceDatePicker(inputs[i]);
      }
    }
  }

  window.AceDatePicker = AceDatePicker;
  window.initAceDatePickers = autoInit;

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', autoInit);
  } else {
    autoInit();
  }
})();
