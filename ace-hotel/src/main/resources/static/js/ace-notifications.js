/**
 * ACE Hotel - Luxury Notification & Modal Engine
 * Standard: Linear / Apple Pro / Mews PMS Aesthetics (Obsidian & Champagne Gold)
 */
(function (window, document) {
  'use strict';

  // --- ICONS (Feather/Lucide Precision SVG) ---
  const SVG_ICONS = {
    success: '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>',
    error: '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="15" y1="9" x2="9" y2="15"></line><line x1="9" y1="9" x2="15" y2="15"></line></svg>',
    warning: '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"></path><line x1="12" y1="9" x2="12" y2="13"></line><line x1="12" y1="17" x2="12.01" y2="17"></line></svg>',
    info: '<svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="16" x2="12" y2="12"></line><line x1="12" y1="8" x2="12.01" y2="8"></line></svg>',
    trash: '<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><polyline points="3 6 5 6 21 6"></polyline><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path><line x1="10" y1="11" x2="10" y2="17"></line><line x1="14" y1="11" x2="14" y2="17"></line></svg>',
    alertCircle: '<svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line></svg>',
    close: '<svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg>'
  };

  const DEFAULT_TITLES = {
    success: 'Thao tác thành công',
    error: 'Thông báo lỗi',
    warning: 'Cảnh báo nghiệp vụ',
    info: 'Thông báo'
  };

  // --- ACE TOAST ENGINE ---
  const AceToast = {
    getContainer: function () {
      let container = document.getElementById('ace-toast-container');
      if (!container) {
        container = document.createElement('div');
        container.id = 'ace-toast-container';
        container.className = 'ace-toast-container';
        container.setAttribute('aria-live', 'polite');
        document.body.appendChild(container);
      }
      return container;
    },

    show: function (options) {
      const type = (options.type || 'info').toLowerCase();
      const title = options.title || DEFAULT_TITLES[type] || 'Thông báo';
      const message = options.message || '';
      const duration = typeof options.duration === 'number' ? options.duration : 4500;

      const container = this.getContainer();

      const toast = document.createElement('div');
      toast.className = `ace-toast ace-toast-${type}`;
      toast.setAttribute('role', 'alert');

      toast.innerHTML = `
        <div class="ace-toast-icon">${SVG_ICONS[type] || SVG_ICONS.info}</div>
        <div class="ace-toast-content">
          <div class="ace-toast-title">${title}</div>
          <div class="ace-toast-body">${message}</div>
        </div>
        <button type="button" class="ace-toast-close" aria-label="Đóng">${SVG_ICONS.close}</button>
        <div class="ace-toast-progress"><div class="ace-toast-progress-bar"></div></div>
      `;

      container.appendChild(toast);

      // Setup close button
      const closeBtn = toast.querySelector('.ace-toast-close');
      if (closeBtn) {
        closeBtn.addEventListener('click', () => this.dismiss(toast));
      }

      this.attachAutoDismiss(toast, duration);
      return toast;
    },

    attachAutoDismiss: function (toast, duration) {
      if (duration <= 0) return;

      const progressBar = toast.querySelector('.ace-toast-progress-bar');
      let remaining = duration;
      let startTime = Date.now();
      let timerId = null;

      if (progressBar) {
        progressBar.style.transition = `transform ${duration}ms linear`;
        // Trigger reflow to start transition
        requestAnimationFrame(() => {
          progressBar.style.transform = 'scaleX(0)';
        });
      }

      const startTimer = () => {
        startTime = Date.now();
        timerId = setTimeout(() => {
          this.dismiss(toast);
        }, remaining);
      };

      const pauseTimer = () => {
        clearTimeout(timerId);
        remaining -= Date.now() - startTime;
        if (progressBar) {
          const computedWidth = progressBar.getBoundingClientRect().width;
          const parentWidth = progressBar.parentElement.getBoundingClientRect().width;
          const currentScale = parentWidth > 0 ? (computedWidth / parentWidth) : 0;
          progressBar.style.transition = 'none';
          progressBar.style.transform = `scaleX(${currentScale})`;
        }
      };

      const resumeTimer = () => {
        if (remaining > 0) {
          if (progressBar) {
            progressBar.style.transition = `transform ${remaining}ms linear`;
            progressBar.style.transform = 'scaleX(0)';
          }
          startTimer();
        } else {
          this.dismiss(toast);
        }
      };

      toast.addEventListener('mouseenter', pauseTimer);
      toast.addEventListener('mouseleave', resumeTimer);

      startTimer();
    },

    dismiss: function (toast) {
      if (!toast || toast.classList.contains('ace-toast-leaving')) return;
      toast.classList.add('ace-toast-leaving');
      setTimeout(() => {
        if (toast.parentElement) {
          toast.parentElement.removeChild(toast);
        }
      }, 280);
    },

    success: function (msg, title) {
      return this.show({ type: 'success', message: msg, title: title });
    },
    error: function (msg, title) {
      return this.show({ type: 'error', message: msg, title: title, duration: 6000 });
    },
    warning: function (msg, title) {
      return this.show({ type: 'warning', message: msg, title: title, duration: 5500 });
    },
    info: function (msg, title) {
      return this.show({ type: 'info', message: msg, title: title });
    }
  };

  // --- ACE MODAL CONFIRM ENGINE ---
  const AceModal = {
    confirm: function (options) {
      const title = options.title || 'Xác nhận thao tác';
      const message = options.message || 'Bạn có chắc chắn muốn thực hiện hành động này?';
      const desc = options.desc || '';
      const confirmText = options.confirmText || 'Xác nhận';
      const cancelText = options.cancelText || 'Hủy bỏ';
      const type = options.type || 'danger'; // 'danger' | 'warning' | 'primary'
      const onConfirm = typeof options.onConfirm === 'function' ? options.onConfirm : function () {};
      const onCancel = typeof options.onCancel === 'function' ? options.onCancel : function () {};

      // Remove existing modal if any
      const existing = document.getElementById('ace-confirm-modal');
      if (existing) existing.remove();

      const modalBackdrop = document.createElement('div');
      modalBackdrop.id = 'ace-confirm-modal';
      modalBackdrop.className = 'ace-modal-backdrop';
      modalBackdrop.setAttribute('role', 'dialog');
      modalBackdrop.setAttribute('aria-modal', 'true');

      const isDanger = type === 'danger';
      const iconSvg = isDanger ? SVG_ICONS.trash : (type === 'warning' ? SVG_ICONS.warning : SVG_ICONS.info);
      const iconClass = isDanger ? 'icon-danger' : (type === 'warning' ? 'icon-warning' : 'icon-gold');
      const btnConfirmClass = isDanger ? 'btn-danger' : 'btn-primary';

      modalBackdrop.innerHTML = `
        <div class="ace-modal-dialog">
          <div class="ace-modal-header">
            <div class="ace-modal-icon-badge ${iconClass}">
              ${iconSvg}
            </div>
            <button type="button" class="ace-modal-close" aria-label="Đóng">${SVG_ICONS.close}</button>
          </div>
          <div class="ace-modal-body">
            <h3 class="ace-modal-title">${title}</h3>
            <p class="ace-modal-message">${message}</p>
            ${desc ? `<p class="ace-modal-desc">${desc}</p>` : ''}
          </div>
          <div class="ace-modal-actions">
            <button type="button" class="btn btn-secondary ace-modal-btn-cancel">${cancelText}</button>
            <button type="button" class="btn ${btnConfirmClass} ace-modal-btn-confirm">${confirmText}</button>
          </div>
        </div>
      `;

      document.body.appendChild(modalBackdrop);
      document.body.style.overflow = 'hidden';

      // Animate in
      requestAnimationFrame(() => {
        modalBackdrop.classList.add('is-open');
      });

      const closeModal = (confirmed) => {
        modalBackdrop.classList.remove('is-open');
        modalBackdrop.classList.add('is-closing');
        setTimeout(() => {
          modalBackdrop.remove();
          document.body.style.overflow = '';
          if (confirmed) {
            onConfirm();
          } else {
            onCancel();
          }
        }, 220);
      };

      const btnConfirm = modalBackdrop.querySelector('.ace-modal-btn-confirm');
      const btnCancel = modalBackdrop.querySelector('.ace-modal-btn-cancel');
      const btnClose = modalBackdrop.querySelector('.ace-modal-close');

      if (btnConfirm) btnConfirm.addEventListener('click', () => closeModal(true));
      if (btnCancel) btnCancel.addEventListener('click', () => closeModal(false));
      if (btnClose) btnClose.addEventListener('click', () => closeModal(false));

      // Click on backdrop to close
      modalBackdrop.addEventListener('click', (e) => {
        if (e.target === modalBackdrop) closeModal(false);
      });

      // Escape key to close
      const keyHandler = (e) => {
        if (e.key === 'Escape') {
          document.removeEventListener('keydown', keyHandler);
          closeModal(false);
        }
      };
      document.addEventListener('keydown', keyHandler);

      // Focus confirm or cancel
      if (btnConfirm) btnConfirm.focus();
    }
  };

  // --- DECLARATIVE DATA-CONFIRM HANDLER ---
  document.addEventListener('submit', function (e) {
    const form = e.target;
    if (!(form instanceof HTMLFormElement)) return;

    // Check if form is already confirmed
    if (form.getAttribute('data-is-confirmed') === 'true') {
      form.removeAttribute('data-is-confirmed');
      return;
    }

    // Check if form has data-confirm attribute
    const confirmMsg = form.getAttribute('data-confirm');
    if (confirmMsg) {
      e.preventDefault();
      const title = form.getAttribute('data-confirm-title') || 'Xác nhận thao tác';
      const desc = form.getAttribute('data-confirm-desc') || '';
      const btnText = form.getAttribute('data-confirm-btn') || 'Xác nhận';
      const type = form.getAttribute('data-confirm-type') || 'danger';

      AceModal.confirm({
        title: title,
        message: confirmMsg,
        desc: desc,
        confirmText: btnText,
        type: type,
        onConfirm: function () {
          form.setAttribute('data-is-confirmed', 'true');
          form.submit();
        }
      });
    }
  });

  // --- INITIALIZE SERVER-RENDERED TOASTS ---
  function initServerToasts() {
    const serverToasts = document.querySelectorAll('#ace-toast-container .ace-toast');
    serverToasts.forEach((toast) => {
      const closeBtn = toast.querySelector('.ace-toast-close');
      if (closeBtn) {
        closeBtn.addEventListener('click', () => AceToast.dismiss(toast));
      }
      const rawDuration = toast.getAttribute('data-auto-dismiss');
      const duration = rawDuration ? parseInt(rawDuration, 10) : 4500;
      AceToast.attachAutoDismiss(toast, duration);
    });
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initServerToasts);
  } else {
    initServerToasts();
  }

  // Expose to global scope
  window.AceToast = AceToast;
  window.AceModal = AceModal;

})(window, document);
