/* Thanh bên trên điện thoại: nút ba gạch mở / đóng, bấm ra ngoài thì đóng. */
(function () {
  var body = document.body;

  document.addEventListener('click', function (e) {
    if (e.target.closest('[data-nav-toggle]')) {
      body.classList.toggle('nav-open');
      return;
    }
    if (body.classList.contains('nav-open') && !e.target.closest('#sidebar')) {
      body.classList.remove('nav-open');
    }
  });

  document.addEventListener('keydown', function (e) {
    if (e.key === 'Escape') { body.classList.remove('nav-open'); }
  });
})();
