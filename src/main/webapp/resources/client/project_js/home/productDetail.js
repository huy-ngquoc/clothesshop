// ===== Sample product with variants (replace with real data from your backend) =====
console.log("Hello");  
var PRODUCT = {
            id: 101,
            name: "Everyday Denim Jacket",
            description: "A relaxed denim jacket built for daily wear.",
            imagesByColor: {
                "Black": "https://picsum.photos/seed/denim-black/1000/800",
                "Navy": "https://picsum.photos/seed/denim-navy/1000/800",
                "White": "https://picsum.photos/seed/denim-white/1000/800"
            },
            variants: [
                // color, size, price, stock, sku
                { color: "Black", size: "S", price: 79, stock: 5, sku: "DEN-JKT-BLK-S" },
                { color: "Black", size: "M", price: 79, stock: 0, sku: "DEN-JKT-BLK-M" },
                { color: "Black", size: "L", price: 79, stock: 8, sku: "DEN-JKT-BLK-L" },
                { color: "Black", size: "XL", price: 82, stock: 3, sku: "DEN-JKT-BLK-XL" },

                { color: "Navy", size: "S", price: 79, stock: 2, sku: "DEN-JKT-NVY-S" },
                { color: "Navy", size: "M", price: 79, stock: 6, sku: "DEN-JKT-NVY-M" },
                { color: "Navy", size: "L", price: 79, stock: 0, sku: "DEN-JKT-NVY-L" },
                { color: "Navy", size: "XL", price: 82, stock: 1, sku: "DEN-JKT-NVY-XL" },

                { color: "White", size: "S", price: 79, stock: 4, sku: "DEN-JKT-WHT-S" },
                { color: "White", size: "M", price: 79, stock: 5, sku: "DEN-JKT-WHT-M" },
                { color: "White", size: "L", price: 79, stock: 4, sku: "DEN-JKT-WHT-L" },
                { color: "White", size: "XL", price: 82, stock: 0, sku: "DEN-JKT-WHT-XL" }
            ]
        };

        // ===== State =====
        var state = {
            color: null,
            size: null,
            variant: null
        };

        // ===== Helpers =====
        function getUnique(list, key) {
            var set = new Set(list.map(function (v) { return v[key]; }));
            return Array.from(set);
        }

        function findVariant(color, size) {
            return PRODUCT.variants.find(function (v) {
                return v.color === color && v.size === size;
            }) || null;
        }

        function variantsForColor(color) {
            return PRODUCT.variants.filter(function (v) { return v.color === color; });
        }

        function sizesAvailableForColor(color) {
            return getUnique(variantsForColor(color), "size");
        }

        function imageForColor(color) {
            var url = PRODUCT.imagesByColor[color];
            return url || "https://picsum.photos/seed/denim-main/1000/800";
        }

        function setMainImage(url) {
            var img = document.getElementById("mainImage");
            if (img && url) { img.src = url; }
        }

        function formatPrice(n) {
            return "$" + n.toFixed(2);
        }

        // ===== Rendering =====
        function renderColors() {
            var colors = getUnique(PRODUCT.variants, "color");
            var container = document.getElementById("colorOptions");
            container.innerHTML = "";
            colors.forEach(function (c, i) {
                var id = "color_" + c;
                var wrapper = document.createElement("div");
                wrapper.className = "form-check";

                var input = document.createElement("input");
                input.className = "form-check-input";
                input.type = "radio";
                input.name = "color";
                input.id = id;
                input.value = c;

                var label = document.createElement("label");
                label.className = "form-check-label";
                label.setAttribute("for", id);
                var dot = document.createElement("span");
                dot.className = "color-dot color-" + c;
                label.appendChild(dot);
                label.appendChild(document.createTextNode(c));

                input.addEventListener("change", function () {
                    state.color = input.value;
                    state.size = null; // reset size when color changes
                    state.variant = null;
                    setMainImage(imageForColor(state.color));
                    renderSizes();
                    updateVariantUI();
                });

                wrapper.appendChild(input);
                wrapper.appendChild(label);
                container.appendChild(wrapper);

                // Preselect the first color
                if (i === 0) {
                    input.checked = true;
                    input.dispatchEvent(new Event("change"));
                }
            });
        }

        function renderSizes() {
            var allSizes = getUnique(PRODUCT.variants, "size");
            var container = document.getElementById("sizeOptions");
            container.innerHTML = "";

            allSizes.forEach(function (s) {
                var sizesForColor = state.color ? variantsForColor(state.color) : [];
                var match = sizesForColor.find(function (v) { return v.size === s; });
                var isAvailable = !!match;
                var inStock = match ? match.stock > 0 : false;

                var btn = document.createElement("button");
                btn.type = "button";
                btn.className = "size-pill";
                btn.textContent = s;
                btn.setAttribute("data-size", s);

                if (!isAvailable || !inStock) {
                    btn.classList.add("disabled");
                    btn.disabled = true;
                }

                btn.addEventListener("click", function () {
                    if (btn.disabled) { return; }
                    state.size = s;
                    state.variant = findVariant(state.color, state.size);
                    // Toggle active style
                    container.querySelectorAll(".size-pill").forEach(function (el) { el.classList.remove("active"); });
                    btn.classList.add("active");
                    updateVariantUI();
                });

                container.appendChild(btn);
            });
        }

        function updateVariantUI() {
            var priceEl = document.getElementById("price");
            var skuEl = document.getElementById("sku");
            var stockBadge = document.getElementById("stockBadge");
            var stockHint = document.getElementById("stockHint");
            var qtyEl = document.getElementById("qty");
            var addBtn = document.getElementById("btnAddToCart");

            if (state.variant) {
                priceEl.textContent = formatPrice(state.variant.price);
                skuEl.textContent = "SKU: " + state.variant.sku;

                var inStock = state.variant.stock > 0;
                stockBadge.textContent = inStock ? "In stock" : "Out of stock";
                stockBadge.className = "badge " + (inStock ? "text-bg-success" : "text-bg-secondary");

                qtyEl.max = String(Math.max(1, state.variant.stock));
                if (Number(qtyEl.value) < 1) { qtyEl.value = "1"; }
                if (Number(qtyEl.value) > state.variant.stock) { qtyEl.value = String(state.variant.stock); }

                stockHint.textContent = inStock ? ("Available: " + state.variant.stock) : "Currently unavailable";

                addBtn.disabled = !inStock;
            } else {
                // No variant fully selected yet
                priceEl.textContent = "Select color & size";
                skuEl.textContent = "SKU: —";
                stockBadge.textContent = "—";
                stockBadge.className = "badge text-bg-light";
                stockHint.textContent = "Pick a color and size to see availability.";
                addBtn.disabled = true;
            }
        }

        // ===== Quantity controls =====
        function attachQtyEvents() {
            var qtyEl = document.getElementById("qty");
            var minus = document.getElementById("btnMinus");
            var plus = document.getElementById("btnPlus");
            
            function clamp() {
                var min = Number(qtyEl.min) || 1;
                var max = state.variant ? Number(qtyEl.max || state.variant.stock) : 99;
                var val = Number(qtyEl.value);
                if (isNaN(val) || val < min) { val = min; }
                if (val > max) { val = max; }
                qtyEl.value = String(val);
                var cartRequestAmount = document.getElementById("cartAmount");
                cartRequestAmount.value = val;
            }

            minus.addEventListener("click", function () {
                qtyEl.value = String(Math.max(1, Number(qtyEl.value) - 1));
                clamp();
            });
            plus.addEventListener("click", function () {
                var max = state.variant ? state.variant.stock : 99;
                qtyEl.value = String(Math.min(max, Number(qtyEl.value) + 1));
                clamp();
            });
            qtyEl.addEventListener("change", clamp);
            qtyEl.addEventListener("input", clamp);
        }

        // ===== Add to cart / Buy now (demo only) =====
        function attachCartButtons() {
            var toastEl = document.getElementById("cartToast");
            var toast = new bootstrap.Toast(toastEl);
            var addBtn = document.getElementById("btnAddToCart");
            var buyBtn = document.getElementById("btnBuyNow");
            var qtyEl = document.getElementById("qty");

            addBtn.addEventListener("click", function () {
                if (!state.variant) { return; }
                var qty = Number(qtyEl.value);
                if (qty < 1 || qty > state.variant.stock) { return; }

                // Hook: send to backend here
                // fetch('/cart', { method:'POST', body: JSON.stringify({...}) })

                toastEl.querySelector(".toast-body").textContent =
                    "Added " + qty + " × " + PRODUCT.name + " (" + state.color + ", " + state.size + ") to cart.";
                toast.show();
            });

            buyBtn.addEventListener("click", function () {
                if (!state.variant) { return; }
                // Redirect to checkout with selected variant (demo)
                window.location.href = "#checkout?sku=" + encodeURIComponent(state.variant.sku) + "&qty=" + encodeURIComponent(document.getElementById("qty").value);
            });
        }

        // ===== Thumbnails =====
        function attachThumbClicks() {
            var thumbs = document.querySelectorAll(".thumb");
            thumbs.forEach(function (t) {
                t.addEventListener("click", function () {
                    var url = t.getAttribute("data-img");
                    setMainImage(url);
                });
            });
        }
        function logicFroChooseVariant() {
    console.log("Function called");
  const checkboxes = document.querySelectorAll(".variant-checkbox");
  const hiddenInput = document.getElementById("selectedVariant");

  checkboxes.forEach(cb => {
    cb.addEventListener("change", function() {
      if (this.checked) {
        
        checkboxes.forEach(other => {
          if (other !== this) other.checked = false;
        });

        
        const elementId = cb.id;
        const variantId = parseInt(elementId.substring(7));
        const variantInput = document.getElementById("pvId");
        variantInput.value = variantId;

      } else {
        
        hiddenInput.value = "";
      }
    });
  });
}
function listenToCartChange() {

}

        // ===== Init =====
        attachQtyEvents();
        logicFroChooseVariant();
 
