/* ==========================================================================
   Aurelia Salon — appointments history
   Drop in at /js/appointments-table.js and load before </body>.
   Formats the raw prices, splits the timestamp, and counts the total up.
   ========================================================================== */

(function () {
  "use strict";

  var calm = window.matchMedia("(prefers-reduced-motion: reduce)").matches;

  function money(n) {
    return n.toLocaleString("en-LK", {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    });
  }

  document.addEventListener("DOMContentLoaded", function () {
    var table = document.querySelector(".styled-table");
    if (!table) return;

    var rows = table.querySelectorAll("tbody tr");
    var total = 0;
    var counted = 0;

    rows.forEach(function (row) {
      var priceCell = row.querySelector(".cell-price");
      var whenCell  = row.querySelector(".cell-when");
      var status    = (row.querySelector(".badge") || {}).textContent || "";

      /* "LKR 8500.0" -> LKR 8,500.00 with the unit set quietly beside it */
      if (priceCell) {
        var value = parseFloat(priceCell.textContent.replace(/[^0-9.]/g, "")) || 0;
        priceCell.innerHTML = '<span class="cur">LKR</span>' + money(value);

        if (!/CANCEL/i.test(status)) {
          total += value;
          counted++;
        }
      }

      /* "2026-09-25 22:02" -> date on top, time beneath it */
      if (whenCell) {
        var parts = whenCell.textContent.trim().split(/\s+/);
        if (parts.length === 2) {
          whenCell.innerHTML = parts[0] + '<span class="clock">' + parts[1] + "</span>";
        }
      }
    });

    /* ---- the total, counting up once on load ------------------------- */
    var out  = document.getElementById("totalSpent");
    var meta = document.getElementById("totalMeta");

    if (meta) {
      meta.textContent = counted === 1
        ? "across one treatment"
        : "across " + counted + " treatments";
    }

    if (!out) return;

    function paint(n) {
      out.innerHTML = '<span class="cur">LKR</span>' + money(n);
    }

    if (calm || total === 0) { paint(total); return; }

    var start = null;
    var span = 1200;

    function tick(now) {
      if (start === null) start = now;
      var t = Math.min((now - start) / span, 1);
      var eased = 1 - Math.pow(1 - t, 4);       // settles rather than stops
      paint(total * eased);
      if (t < 1) requestAnimationFrame(tick);
    }

    paint(0);
    setTimeout(function () { requestAnimationFrame(tick); }, 620);
  });
})();