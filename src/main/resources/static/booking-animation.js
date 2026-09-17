/* ==========================================================================
   Aurelia Salon — booking page motion
   Drop in at /js/booking-animations.js and load with defer.
   Requires no changes to the existing markup: it wraps #priceInput itself
   and reads the values your updateCalculatedPrice() already writes.
   ========================================================================== */

(function () {
  "use strict";

  var calm = window.matchMedia("(prefers-reduced-motion: reduce)").matches;

  document.addEventListener("DOMContentLoaded", function () {
    var priceInput   = document.getElementById("priceInput");
    var serviceSelect = document.getElementById("serviceSelect");
    var tierSelect   = document.getElementById("tierSelect");
    var form         = priceInput ? priceInput.closest("form") : null;
    var button       = form ? form.querySelector(".btn-primary") : null;

    /* the hairline rule under the title */
    var title = document.querySelector(".page-title");
    if (title && !document.querySelector(".title-rule")) {
      var rule = document.createElement("div");
      rule.className = "title-rule";
      title.insertAdjacentElement("afterend", rule);
    }

    /* keep the button text in its own span so it can fade on confirm */
    if (button && !button.querySelector(".label")) {
      button.innerHTML = '<span class="label">' + button.textContent.trim() + "</span>";
    }

    if (!priceInput) return;

    /* ---- build the ghost layer over the price field ------------------- */
    var shell = document.createElement("div");
    shell.className = "price-shell";
    priceInput.parentNode.insertBefore(shell, priceInput);
    shell.appendChild(priceInput);

    var ghost = document.createElement("div");
    ghost.className = "price-ghost";
    shell.appendChild(ghost);

    var previous = "";

    function format(n) {
      return n.toLocaleString("en-LK", {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      });
    }

    function basePrice() {
      if (!serviceSelect) return 0;
      var opt = serviceSelect.options[serviceSelect.selectedIndex];
      return opt ? parseFloat(opt.getAttribute("data-price")) || 0 : 0;
    }

    function render() {
      var total = parseFloat(priceInput.value) || 0;
      var vip = tierSelect && tierSelect.value === "VIP";
      var base = basePrice();

      document.body.classList.toggle("is-vip", vip && total > 0);

      if (!total) {
        ghost.innerHTML = '<span class="empty">Choose a treatment to see the price</span>';
        previous = "";
        return;
      }

      var text = format(total);
      var html = '<span class="unit">LKR</span>';

      if (vip && base) {
        html += '<span class="was">' + format(base) + "</span>";
      }

      html += '<span class="digits">';
      for (var i = 0; i < text.length; i++) {
        /* only characters that actually changed roll, so a 15% surcharge
           moves the digits it touches and leaves the rest still */
        var changed = calm ? false : text[i] !== previous[i];
        html += '<span class="d' + (changed ? " roll" : "") + '"' +
                (changed ? ' style="animation-delay:' + (i * 38) + 'ms"' : "") +
                ">" + text[i] + "</span>";
      }
      html += "</span>";

      if (vip) html += '<span class="tier-chip">VIP +15%</span>';

      ghost.innerHTML = html;
      previous = text;

      if (!calm) {
        shell.classList.remove("is-updating");
        void shell.offsetWidth;
        shell.classList.add("is-updating");
      }
    }

    /* the inline onchange handler runs first (it sits on the element);
       this listener bubbles up afterwards, so the value is already fresh */
    document.addEventListener("change", function (e) {
      if (e.target === serviceSelect || e.target === tierSelect) render();
    });

    render();

    /* ---- confirmation bloom ------------------------------------------- */
    if (!form || !button) return;

    form.addEventListener("submit", function (e) {
      if (button.classList.contains("is-booking")) return;
      if (!form.checkValidity()) return;          // let the browser complain
      if (calm) return;

      e.preventDefault();
      button.classList.add("is-booking");
      var label = button.querySelector(".label");
      if (label) {
        setTimeout(function () { label.textContent = "Reserving your slot"; }, 260);
      }

      setTimeout(function () { bloom(button); }, 300);
      setTimeout(function () { form.submit(); }, 1150);
    });

    function bloom(el) {
      var box = el.getBoundingClientRect();
      var cx = box.left + box.width / 2;
      var cy = box.top + box.height / 2;

      for (var i = 0; i < 16; i++) {
        var p = document.createElement("span");
        var angle = (Math.PI * 2 * i) / 16 + Math.random() * 0.4;
        var dist = 90 + Math.random() * 130;

        p.className = "petal";
        p.style.left = cx + "px";
        p.style.top = cy + "px";
        p.style.setProperty("--dx", Math.cos(angle) * dist + "px");
        p.style.setProperty("--dy", (Math.sin(angle) * dist - 40) + "px");
        p.style.setProperty("--rot", Math.round(Math.random() * 540 - 270) + "deg");
        p.style.animationDelay = (i * 22) + "ms";

        document.body.appendChild(p);
        setTimeout(function (node) {
          return function () { node.remove(); };
        }(p), 1600);
      }
    }
  });
})();