(function () {
    var page = document.querySelector('.page');
    if (!page) return;

    // If we arrived here via a nav-trigger on the previous page, play the entrance.
    // The inline script at the top of .page already snapped us into the
    // enter-from-* position before first paint; here we release it so the
    // page's own transition animates it back to normal.
    var enterClass = null;
    page.classList.forEach(function (c) {
        if (c.indexOf('enter-from-') === 0) enterClass = c;
    });
    if (enterClass) {
        requestAnimationFrame(function () {
            requestAnimationFrame(function () {
                page.classList.remove(enterClass);
            });
        });
    }

    // Wire up every login <-> register link so it slides out before navigating.
    document.querySelectorAll('.nav-trigger').forEach(function (el) {
        el.addEventListener('click', function (e) {
            e.preventDefault();
            var destination = el.getAttribute('href') || el.getAttribute('data-href');
            var exitDirection = el.getAttribute('data-exit') || 'left';
            var incomingDirection = exitDirection === 'left' ? 'right' : 'left';

            page.classList.add('exit-' + exitDirection);
            sessionStorage.setItem('authPageEnterDirection', incomingDirection);

            setTimeout(function () {
                window.location.href = destination;
            }, 260);
        });
    });
})();
