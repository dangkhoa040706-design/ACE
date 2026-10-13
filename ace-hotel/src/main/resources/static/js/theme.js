/* Đổi giao diện Dark / Light. Lựa chọn lưu ở localStorage (bọc try/catch vì có thể bị chặn).
   Việc đặt data-theme ban đầu làm bằng script nhỏ trong <head> (fragments/layout.html)
   để trang không bị nháy sáng khi tải. */
(function () {
  var root = document.documentElement;

  function current() {
    return root.getAttribute('data-theme') === 'light' ? 'light' : 'dark';
  }

  function apply(theme) {
    root.setAttribute('data-theme', theme);
    try { localStorage.setItem('ace-theme', theme); } catch (e) { /* bỏ qua */ }
    document.querySelectorAll('[data-theme-toggle]').forEach(function (b) {
      b.setAttribute('aria-pressed', String(theme === 'light'));
    });
  }

  document.addEventListener('click', function (e) {
    var btn = e.target.closest('[data-theme-toggle]');
    if (btn) { apply(current() === 'dark' ? 'light' : 'dark'); }
  });

  document.querySelectorAll('[data-theme-toggle]').forEach(function (b) {
    b.setAttribute('aria-pressed', String(current() === 'light'));
  });
})();
