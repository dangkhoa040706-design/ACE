/**
 * ACE HOTEL PMS - EXECUTIVE CUSTOM SELECT ENGINE (AceSelect)
 * Standard: Obsidian Glass & Champagne Gold (Linear / Stripe / Swiss Grid Standard)
 * Features:
 * - 100% synchronized with native <select> (preserves name, value, required validation, FormData)
 * - Auto-detects Room selections & Folio Service items to render executive badge chips
 * - Auto-flip positioning (opens upward if near screen bottom)
 * - Custom Champagne Gold slim scrollbar
 * - Search filter for option lists > 6 items
 * - Full keyboard navigation (ArrowUp, ArrowDown, Enter, Space, Escape, Tab)
 * - Form reset & dynamic AJAX modal auto-sync
 */
(function() {
  'use strict';

  var AceSelect = {
    instances: new WeakMap(),

    initAll: function(container) {
      var root = container || document;
      var selects = root.querySelectorAll('select.form-control, select.form-select, select[data-ace-select]');
      selects.forEach(function(select) {
        if (!select.dataset.aceInitialized) {
          AceSelect.create(select);
        }
      });
    },

    sync: function(select) {
      if (!select) return;
      var inst = AceSelect.instances.get(select);
      if (inst && typeof inst.rebuild === 'function') {
        inst.rebuild();
      }
    },

    create: function(select) {
      if (!select || select.dataset.aceInitialized === 'true') return;
      select.dataset.aceInitialized = 'true';

      // 1. Create Wrapper
      var wrapper = document.createElement('div');
      wrapper.className = 'ace-select-wrapper';
      if (select.className) {
        if (select.classList.contains('form-select-sm') || select.style.fontSize === '0.82rem' || select.style.height === '38px') {
          wrapper.classList.add('ace-select-sm');
        }
      }

      if (select.style.width) wrapper.style.width = select.style.width;

      // Move select inside wrapper
      select.parentNode.insertBefore(wrapper, select);
      wrapper.appendChild(select);
      select.classList.add('ace-select-native');

      // 2. Create Trigger Button
      var trigger = document.createElement('button');
      trigger.type = 'button';
      trigger.className = 'ace-select-trigger';
      trigger.setAttribute('aria-haspopup', 'listbox');
      trigger.setAttribute('aria-expanded', 'false');

      var labelSpan = document.createElement('span');
      labelSpan.className = 'ace-select-trigger-label';

      var arrowSpan = document.createElement('span');
      arrowSpan.className = 'ace-select-trigger-arrow';
      arrowSpan.innerHTML = '<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="6 9 12 15 18 9"></polyline></svg>';

      trigger.appendChild(labelSpan);
      trigger.appendChild(arrowSpan);
      wrapper.appendChild(trigger);

      // 3. Create Dropdown Panel
      var dropdown = document.createElement('div');
      dropdown.className = 'ace-select-dropdown';
      dropdown.setAttribute('role', 'listbox');

      // Search input if options count > 6
      var hasSearch = select.options.length > 6;
      var searchInput = null;
      if (hasSearch) {
        var searchBox = document.createElement('div');
        searchBox.className = 'ace-select-search-box';
        searchBox.innerHTML = '<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#94A3B8" stroke-width="2" style="position: absolute; left: 12px; top: 50%; transform: translateY(-50%); pointer-events: none;"><circle cx="11" cy="11" r="8"></circle><line x1="21" y1="21" x2="16.65" y2="16.65"></line></svg>';

        searchInput = document.createElement('input');
        searchInput.type = 'text';
        searchInput.className = 'ace-select-search-input';
        searchInput.placeholder = 'Tìm kiếm nhanh...';
        searchBox.appendChild(searchInput);
        dropdown.appendChild(searchBox);
      }

      // Options List
      var optionsList = document.createElement('div');
      optionsList.className = 'ace-select-options-list';
      dropdown.appendChild(optionsList);
      wrapper.appendChild(dropdown);

      var isOpen = false;

      // 4. Render Options Function
      function buildOptions() {
        optionsList.innerHTML = '';
        var selectedOpt = select.selectedOptions[0] || select.options[0];
        updateTriggerDisplay(selectedOpt);

        Array.from(select.options).forEach(function(opt, index) {
          var item = document.createElement('div');
          item.className = 'ace-select-option';
          item.setAttribute('role', 'option');
          item.dataset.index = index;
          item.dataset.value = opt.value;

          if (opt.disabled) {
            item.classList.add('is-disabled');
          }
          if (opt.selected) {
            item.classList.add('is-selected');
            item.setAttribute('aria-selected', 'true');
          } else {
            item.setAttribute('aria-selected', 'false');
          }

          item.innerHTML = renderOptionContent(opt.text, opt.value, opt.disabled);

          item.addEventListener('click', function(e) {
            e.stopPropagation();
            if (opt.disabled) return;
            selectOption(index);
          });

          optionsList.appendChild(item);
        });
      }

      // Format Option Content
      function renderOptionContent(rawText, val, isDisabled) {
        var trimmed = rawText.trim();
        if (isDisabled || val === '') {
          return '<span class="ace-opt-placeholder">' + escapeHtml(trimmed) + '</span>';
        }

        // Room Pattern 1: P.102 - Tầng 1 (Standard - Trống sạch)
        var roomMatch1 = trimmed.match(/^P\.(\d+)\s*[-—–]\s*Tầng\s*(\d+)\s*\(([^)]+?)\s*[-—–]\s*([^)]+)\)$/i);
        if (roomMatch1) {
          var roomNum = roomMatch1[1];
          var floor = roomMatch1[2];
          var rType = roomMatch1[3].trim();
          var statusText = roomMatch1[4].trim();
          return '<div class="ace-room-rich-row">' +
                   '<div class="ace-room-rich-left">' +
                     '<span class="ace-chip-room">P.' + roomNum + '</span>' +
                     '<span class="ace-chip-floor">Tầng ' + floor + '</span>' +
                     '<span class="ace-chip-type">' + escapeHtml(rType) + '</span>' +
                   '</div>' +
                   '<div class="ace-room-rich-right">' +
                     '<span class="ace-badge-avail"><span class="ace-dot-green"></span> ' + escapeHtml(statusText.toUpperCase()) + '</span>' +
                     '<span class="ace-opt-check">✓</span>' +
                   '</div>' +
                 '</div>';
        }

        // Room Pattern 2: P.101 - Standard (Tầng 1 - 780,000đ/đêm)
        var roomMatch2 = trimmed.match(/^P\.(\d+)\s*[-—–]\s*([^(]+?)\s*\(Tầng\s*(\d+)\s*[-—–]\s*([^)]+)\)$/i);
        if (roomMatch2) {
          var rNum = roomMatch2[1];
          var rName = roomMatch2[2].trim();
          var fl = roomMatch2[3];
          var price = roomMatch2[4].trim();
          return '<div class="ace-room-rich-row">' +
                   '<div class="ace-room-rich-left">' +
                     '<span class="ace-chip-room">P.' + rNum + '</span>' +
                     '<span class="ace-chip-type">' + escapeHtml(rName) + '</span>' +
                     '<span class="ace-chip-floor">Tầng ' + fl + '</span>' +
                   '</div>' +
                   '<div class="ace-room-rich-right">' +
                     '<span class="ace-chip-price">' + escapeHtml(price) + '</span>' +
                     '<span class="ace-opt-check">✓</span>' +
                   '</div>' +
                 '</div>';
        }

        // Folio Service Pattern: [MINIBAR] Nước khoáng Lavie 500ml — 20,000đ / Chai
        var srvMatch = trimmed.match(/^\[(MINIBAR|LAUNDRY|FB|DAMAGES)\]\s*(.+?)\s*[-—–]\s*(.+)$/i);
        if (srvMatch) {
          var cat = srvMatch[1].toUpperCase();
          var name = srvMatch[2].trim();
          var pr = srvMatch[3].trim();
          var catClass = 'ace-cat-' + cat.toLowerCase();
          return '<div class="ace-srv-rich-row">' +
                   '<div class="ace-srv-rich-left">' +
                     '<span class="ace-chip-cat ' + catClass + '">' + cat + '</span>' +
                     '<span class="ace-srv-name">' + escapeHtml(name) + '</span>' +
                   '</div>' +
                   '<div class="ace-srv-rich-right">' +
                     '<span class="ace-srv-price">' + escapeHtml(pr) + '</span>' +
                     '<span class="ace-opt-check">✓</span>' +
                   '</div>' +
                 '</div>';
        }

        // Standard Row
        return '<div class="ace-opt-standard-row">' +
                 '<span class="ace-opt-text">' + escapeHtml(trimmed) + '</span>' +
                 '<span class="ace-opt-check">✓</span>' +
               '</div>';
      }

      function escapeHtml(text) {
        var div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
      }

      function updateTriggerDisplay(opt) {
        if (!opt || opt.disabled || opt.value === '') {
          labelSpan.textContent = opt ? opt.text.trim() : '-- Chọn --';
          labelSpan.className = 'ace-select-trigger-label is-placeholder';
          return;
        }

        labelSpan.className = 'ace-select-trigger-label';
        var trimmed = opt.text.trim();

        // Room Pattern 1
        var rm1 = trimmed.match(/^P\.(\d+)\s*[-—–]\s*Tầng\s*(\d+)\s*\(([^)]+?)\s*[-—–]\s*([^)]+)\)$/i);
        if (rm1) {
          labelSpan.innerHTML = '<span class="ace-chip-room" style="margin-right: 6px;">P.' + rm1[1] + '</span>' +
                                '<span class="ace-chip-type" style="margin-right: 6px;">' + escapeHtml(rm1[3].trim()) + '</span>' +
                                '<span class="ace-chip-floor">Tầng ' + rm1[2] + '</span>';
          return;
        }

        // Room Pattern 2
        var rm2 = trimmed.match(/^P\.(\d+)\s*[-—–]\s*([^(]+?)\s*\(Tầng\s*(\d+)\s*[-—–]\s*([^)]+)\)$/i);
        if (rm2) {
          labelSpan.innerHTML = '<span class="ace-chip-room" style="margin-right: 6px;">P.' + rm2[1] + '</span>' +
                                '<span class="ace-chip-type" style="margin-right: 6px;">' + escapeHtml(rm2[2].trim()) + '</span>' +
                                '<span class="ace-chip-floor">Tầng ' + rm2[3] + '</span>';
          return;
        }

        // Folio Service Pattern
        var sm = trimmed.match(/^\[(MINIBAR|LAUNDRY|FB|DAMAGES)\]\s*(.+?)\s*[-—–]\s*(.+)$/i);
        if (sm) {
          var cat = sm[1].toUpperCase();
          var catClass = 'ace-cat-' + cat.toLowerCase();
          labelSpan.innerHTML = '<span class="ace-chip-cat ' + catClass + '" style="margin-right: 6px;">' + cat + '</span>' +
                                '<span class="ace-srv-name" style="margin-right: 8px;">' + escapeHtml(sm[2].trim()) + '</span>' +
                                '<span class="ace-srv-price">' + escapeHtml(sm[3].trim()) + '</span>';
          return;
        }

        // Generic text
        labelSpan.textContent = trimmed;
      }

      function selectOption(index) {
        select.selectedIndex = index;
        updateTriggerDisplay(select.options[index]);

        var items = optionsList.querySelectorAll('.ace-select-option');
        items.forEach(function(el, i) {
          if (i === index) {
            el.classList.add('is-selected');
            el.setAttribute('aria-selected', 'true');
          } else {
            el.classList.remove('is-selected');
            el.setAttribute('aria-selected', 'false');
          }
        });

        wrapper.classList.remove('has-error');

        // Fire native events
        select.dispatchEvent(new Event('change', { bubbles: true }));
        select.dispatchEvent(new Event('input', { bubbles: true }));

        close();
        trigger.focus();
      }

      function open() {
        if (isOpen || select.disabled) return;
        document.querySelectorAll('.ace-select-wrapper.is-open').forEach(function(w) {
          if (w !== wrapper && w.__aceSelectClose) w.__aceSelectClose();
        });

        isOpen = true;
        wrapper.classList.add('is-open');
        trigger.setAttribute('aria-expanded', 'true');

        // Check if near bottom of screen
        var rect = trigger.getBoundingClientRect();
        var dropdownHeight = 280;
        var spaceBelow = window.innerHeight - rect.bottom;
        var spaceAbove = rect.top;

        if (spaceBelow < dropdownHeight && spaceAbove > spaceBelow) {
          wrapper.classList.add('is-dropup');
        } else {
          wrapper.classList.remove('is-dropup');
        }

        if (hasSearch && searchInput) {
          searchInput.value = '';
          filterOptions('');
          setTimeout(function() { searchInput.focus(); }, 40);
        } else {
          var selItem = optionsList.querySelector('.ace-select-option.is-selected');
          if (selItem) {
            selItem.scrollIntoView({ block: 'nearest' });
          }
        }
      }

      function close() {
        if (!isOpen) return;
        isOpen = false;
        wrapper.classList.remove('is-open');
        trigger.setAttribute('aria-expanded', 'false');
      }

      wrapper.__aceSelectClose = close;

      function toggle() {
        if (isOpen) close();
        else open();
      }

      function filterOptions(query) {
        var q = query.toLowerCase().trim();
        var items = optionsList.querySelectorAll('.ace-select-option');
        items.forEach(function(item) {
          if (!q) {
            item.style.display = '';
          } else {
            var text = item.textContent.toLowerCase();
            item.style.display = text.indexOf(q) !== -1 ? '' : 'none';
          }
        });
      }

      // Event Listeners
      trigger.addEventListener('click', function(e) {
        e.preventDefault();
        e.stopPropagation();
        toggle();
      });

      if (hasSearch && searchInput) {
        searchInput.addEventListener('input', function(e) {
          filterOptions(e.target.value);
        });
        searchInput.addEventListener('keydown', function(e) {
          if (e.key === 'Escape') {
            close();
            trigger.focus();
          } else if (e.key === 'Enter') {
            e.preventDefault();
            var visible = optionsList.querySelectorAll('.ace-select-option:not([style*="display: none"]):not(.is-disabled)');
            if (visible.length > 0) {
              selectOption(parseInt(visible[0].dataset.index, 10));
            }
          }
        });
      }

      // Keyboard navigation
      trigger.addEventListener('keydown', function(e) {
        if (e.key === 'ArrowDown' || e.key === 'ArrowUp' || e.key === 'Enter' || e.key === ' ') {
          e.preventDefault();
          if (!isOpen) {
            open();
          } else {
            var visible = Array.from(optionsList.querySelectorAll('.ace-select-option:not([style*="display: none"]):not(.is-disabled)'));
            if (visible.length === 0) return;
            var curIndex = visible.findIndex(function(el) { return el.classList.contains('is-focused'); });
            if (e.key === 'ArrowDown') {
              curIndex = (curIndex + 1) % visible.length;
            } else if (e.key === 'ArrowUp') {
              curIndex = (curIndex - 1 + visible.length) % visible.length;
            } else if (e.key === 'Enter' || e.key === ' ') {
              if (curIndex >= 0 && visible[curIndex]) {
                selectOption(parseInt(visible[curIndex].dataset.index, 10));
              }
              return;
            }
            visible.forEach(function(el) { el.classList.remove('is-focused'); });
            visible[curIndex].classList.add('is-focused');
            visible[curIndex].scrollIntoView({ block: 'nearest' });
          }
        } else if (e.key === 'Escape' && isOpen) {
          e.preventDefault();
          close();
        }
      });

      // HTML5 validation
      select.addEventListener('invalid', function() {
        wrapper.classList.add('has-error');
        trigger.focus();
      });

      // Synchronize with external changes
      select.addEventListener('change', function() {
        var selectedOpt = select.selectedOptions[0] || select.options[0];
        updateTriggerDisplay(selectedOpt);
        var items = optionsList.querySelectorAll('.ace-select-option');
        items.forEach(function(el, i) {
          if (i === select.selectedIndex) {
            el.classList.add('is-selected');
            el.setAttribute('aria-selected', 'true');
          } else {
            el.classList.remove('is-selected');
            el.setAttribute('aria-selected', 'false');
          }
        });
      });

      // Listen to parent form reset
      if (select.form) {
        select.form.addEventListener('reset', function() {
          setTimeout(function() {
            var selectedOpt = select.selectedOptions[0] || select.options[0];
            updateTriggerDisplay(selectedOpt);
            var items = optionsList.querySelectorAll('.ace-select-option');
            items.forEach(function(el, i) {
              if (i === select.selectedIndex) {
                el.classList.add('is-selected');
                el.setAttribute('aria-selected', 'true');
              } else {
                el.classList.remove('is-selected');
                el.setAttribute('aria-selected', 'false');
              }
            });
            wrapper.classList.remove('has-error');
          }, 20);
        });
      }

      // If associated label clicked, focus trigger
      if (select.id) {
        var label = document.querySelector('label[for="' + select.id + '"]');
        if (label) {
          label.addEventListener('click', function(e) {
            e.preventDefault();
            trigger.focus();
          });
        }
      }

      // Outside click
      document.addEventListener('click', function(e) {
        if (isOpen && !wrapper.contains(e.target)) {
          close();
        }
      });

      buildOptions();

      AceSelect.instances.set(select, {
        rebuild: buildOptions,
        close: close,
        open: open
      });
    }
  };

  window.AceSelect = AceSelect;

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', function() {
      AceSelect.initAll();
    });
  } else {
    AceSelect.initAll();
  }
})();
