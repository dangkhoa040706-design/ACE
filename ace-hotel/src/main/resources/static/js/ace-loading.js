/**
 * =========================================================================
 * ACE HOTEL - LUXURY LOADING ANIMATION CONTROLLER (ace-loading.js)
 * TIER 1: Button Golden Spinner & Double-Submit Protection
 * TIER 2: Section Skeleton Shimmer Support
 * TIER 3: Top Slim Golden Progress Bar & Cold-Start Luxury Splash Transition
 * =========================================================================
 */
(function (window, document) {
  'use strict';

  var SPLASH_STORAGE_KEY = 'ace_splash_seen';
  var hasSeenSplash = false;
  try {
    hasSeenSplash = sessionStorage.getItem(SPLASH_STORAGE_KEY) === 'true';
  } catch (e) {
    hasSeenSplash = false;
  }

  // -------------------------------------------------------------------------
  // 1. TIER 3: TOP SLIM GOLDEN PROGRESS BAR
  // -------------------------------------------------------------------------
  var AceTopBar = (function () {
    var bar = null;
    var timer = null;
    var currentWidth = 0;

    function getOrCreateBar() {
      if (!bar) {
        bar = document.getElementById('aceTopBar');
        if (!bar) {
          bar = document.createElement('div');
          bar.id = 'aceTopBar';
          document.body.appendChild(bar);
        }
      }
      return bar;
    }

    return {
      start: function () {
        var el = getOrCreateBar();
        clearInterval(timer);
        currentWidth = 25;
        el.classList.add('is-active');
        el.style.width = currentWidth + '%';

        timer = setInterval(function () {
          if (currentWidth < 90) {
            currentWidth += Math.random() * 10 + 5;
            if (currentWidth > 90) currentWidth = 90;
            el.style.width = currentWidth + '%';
          }
        }, 120);
      },

      set: function (percent) {
        var el = getOrCreateBar();
        el.classList.add('is-active');
        currentWidth = Math.min(100, Math.max(0, percent));
        el.style.width = currentWidth + '%';
      },

      done: function () {
        var el = getOrCreateBar();
        clearInterval(timer);
        currentWidth = 100;
        el.style.width = '100%';

        setTimeout(function () {
          el.classList.remove('is-active');
          setTimeout(function () {
            el.style.width = '0%';
            currentWidth = 0;
          }, 300);
        }, 180);
      }
    };
  })();

  // -------------------------------------------------------------------------
  // 2. TIER 3: CINEMATIC LUXURY SPLASH SCREEN (Cold-Start Only)
  // -------------------------------------------------------------------------
  var AceSplash = (function () {
    var progressTimer = null;
    var currentProgress = 0;
    var isDismissed = false;

    function getSplash() {
      return document.getElementById('aceSplashLoader');
    }

    function getProgressFill() {
      return document.getElementById('aceSplashProgress');
    }

    function setProgress(val) {
      currentProgress = Math.min(100, Math.max(0, val));
      var fill = getProgressFill();
      if (fill) {
        fill.style.width = currentProgress + '%';
      }
    }

    function runProgressAnimation() {
      clearInterval(progressTimer);
      setProgress(20);

      progressTimer = setInterval(function () {
        if (currentProgress < 95) {
          var step = (95 - currentProgress) * 0.25;
          if (step < 2) step = 2;
          setProgress(currentProgress + step);
        }
      }, 60);
    }

    function dismiss() {
      if (isDismissed) return;
      isDismissed = true;
      clearInterval(progressTimer);
      setProgress(100);

      var splash = getSplash();
      if (!splash) return;

      splash.classList.add('is-hidden');
      setTimeout(function () {
        splash.style.display = 'none';
      }, 250);
    }

    return {
      init: function () {
        var splash = getSplash();
        if (!splash) return;

        // Nếu đã từng mở trang trong phiên duyệt web này, tắt ngay lập tức (0ms)
        if (hasSeenSplash) {
          splash.classList.add('is-hidden');
          splash.style.display = 'none';
          return;
        }

        // Lần đầu mở ứng dụng: cho xuất hiện nhẹ nhàng ~200-250ms rồi lưu session
        isDismissed = false;
        runProgressAnimation();

        function scheduleDismiss() {
          try {
            sessionStorage.setItem(SPLASH_STORAGE_KEY, 'true');
          } catch (e) {}
          setTimeout(dismiss, 200);
        }

        if (document.readyState === 'complete') {
          scheduleDismiss();
        } else {
          window.addEventListener('load', scheduleDismiss);
          // Failsafe tối đa 800ms
          setTimeout(dismiss, 800);
        }
      },

      show: function () {
        // Chỉ hỗ trợ show nếu thực sự cần thiết theo lời gọi thủ công
        var splash = getSplash();
        if (!splash) return;
        isDismissed = false;
        splash.style.display = 'flex';
        splash.classList.remove('is-hidden');
        runProgressAnimation();
      },

      dismiss: dismiss,

      setProgress: setProgress
    };
  })();

  // -------------------------------------------------------------------------
  // 3. TIER 1: BUTTON LOADING & DOUBLE-SUBMIT PREVENTION
  // -------------------------------------------------------------------------
  function setupFormSubmissions() {
    document.addEventListener('submit', function (e) {
      var form = e.target;
      if (!form || form.tagName !== 'FORM') return;

      if (e.defaultPrevented) return;

      // Bỏ qua form có cờ data-no-loading hoặc mở tab mới
      if (form.getAttribute('data-no-loading') === 'true' || form.target === '_blank') {
        return;
      }

      // Kiểm tra HTML5 validation nếu có
      if (typeof form.checkValidity === 'function' && !form.checkValidity()) {
        return;
      }

      // Xác định nút submit
      var submitBtn = e.submitter || form.querySelector('button[type="submit"]') || form.querySelector('input[type="submit"]');
      if (submitBtn) {
        if (submitBtn.classList.contains('btn-loading')) {
          e.preventDefault();
          return;
        }

        var rect = submitBtn.getBoundingClientRect();
        if (rect.width > 0) {
          submitBtn.style.minWidth = rect.width + 'px';
        }

        submitBtn.classList.add('btn-loading');
        submitBtn.setAttribute('aria-busy', 'true');
      }

      // Chỉ kích hoạt Top Progress Bar, KHÔNG bật màn hình đen Splash
      AceTopBar.start();
    }, false);
  }

  // -------------------------------------------------------------------------
  // 4. LINK NAVIGATION SMOOTH TRANSITIONS (Top Slim Bar Only)
  // -------------------------------------------------------------------------
  function setupLinkNavigation() {
    document.addEventListener('click', function (e) {
      var target = e.target;
      var link = target.closest('a');
      if (!link) return;

      var href = link.getAttribute('href');
      if (!href) return;

      // Bỏ qua anchor link, javascript, external links, hoặc mở tab mới
      if (href.startsWith('#') || href.startsWith('javascript:') || href.startsWith('mailto:') || href.startsWith('tel:')) {
        return;
      }
      // Bỏ qua nếu là anchor hash trên cùng trang hiện tại (ví dụ /#dich-vu khi đang ở /)
      if (link.pathname === window.location.pathname && link.hash) {
        return;
      }
      if (link.target === '_blank' || link.hasAttribute('download')) {
        return;
      }
      if (e.metaKey || e.ctrlKey || e.shiftKey || e.altKey) {
        return;
      }

      // Bỏ qua nếu link có data-no-loading="true"
      if (link.getAttribute('data-no-loading') === 'true') {
        return;
      }

      // Kích hoạt thanh chỉ báo Top Bar mượt mà, KHÔNG BAO GIỜ bật màn đen Splash che mắt người dùng
      AceTopBar.start();
    }, true);

    // Khi trang tải xong, hoàn thành Top Bar
    if (document.readyState === 'complete') {
      AceTopBar.done();
    } else {
      window.addEventListener('load', function () {
        AceTopBar.done();
      });
    }

    // Xử lý khi người dùng nhấn Back / Forward trên trình duyệt
    window.addEventListener('pageshow', function (e) {
      AceSplash.dismiss();
      AceTopBar.done();
      // Gỡ bỏ trạng thái loading của nút nếu quay lại từ cache
      document.querySelectorAll('.btn-loading').forEach(function (btn) {
        btn.classList.remove('btn-loading');
        btn.removeAttribute('aria-busy');
        btn.style.minWidth = '';
      });
    });
  }

  // Khởi động toàn bộ hệ thống Loading khi DOM sẵn sàng
  function init() {
    AceSplash.init();
    setupFormSubmissions();
    setupLinkNavigation();
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }

  // Xuất API toàn cục để các script khác có thể tương tác
  window.AceLoading = {
    topBar: AceTopBar,
    splash: AceSplash,
    setButtonLoading: function (buttonEl, isLoading) {
      if (!buttonEl) return;
      if (isLoading) {
        var rect = buttonEl.getBoundingClientRect();
        if (rect.width > 0) buttonEl.style.minWidth = rect.width + 'px';
        buttonEl.classList.add('btn-loading');
        buttonEl.setAttribute('aria-busy', 'true');
      } else {
        buttonEl.classList.remove('btn-loading');
        buttonEl.removeAttribute('aria-busy');
        buttonEl.style.minWidth = '';
      }
    }
  };

})(window, document);
