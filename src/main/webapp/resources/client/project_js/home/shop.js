var PRODUCTS = [
            { id: 1, name: "Classic Tee", price: 19, sold: 320, sizes: ["S", "M", "L"], colors: ["Black", "White"], img: "https://picsum.photos/seed/p1/800/600" },
            { id: 2, name: "Linen Shirt", price: 45, sold: 155, sizes: ["M", "L", "XL"], colors: ["Beige", "White"], img: "https://picsum.photos/seed/p2/800/600" },
            { id: 3, name: "Slim Jeans", price: 59, sold: 410, sizes: ["S", "M", "L", "XL"], colors: ["Blue", "Navy"], img: "https://picsum.photos/seed/p3/800/600" },
            { id: 4, name: "Chino Pants", price: 39, sold: 210, sizes: ["S", "M", "L"], colors: ["Beige", "Brown"], img: "https://picsum.photos/seed/p4/800/600" },
            { id: 5, name: "Fleece Hoodie", price: 49, sold: 380, sizes: ["M", "L", "XL"], colors: ["Grey", "Black", "Green"], img: "https://picsum.photos/seed/p5/800/600" },
            { id: 6, name: "Puffer Jacket", price: 109, sold: 95, sizes: ["S", "M", "L"], colors: ["Navy", "Black"], img: "https://picsum.photos/seed/p6/800/600" },
            { id: 7, name: "Athletic Shorts", price: 25, sold: 240, sizes: ["S", "M", "L"], colors: ["Black", "Grey", "Blue"], img: "https://picsum.photos/seed/p7/800/600" },
            { id: 8, name: "Pleated Skirt", price: 42, sold: 130, sizes: ["XS", "S", "M"], colors: ["Blue", "White"], img: "https://picsum.photos/seed/p8/800/600" },
            { id: 9, name: "Oversized Sweater", price: 55, sold: 205, sizes: ["S", "M", "L", "XL"], colors: ["Grey", "Beige"], img: "https://picsum.photos/seed/p9/800/600" },
            { id: 10, name: "Crop Tee", price: 22, sold: 300, sizes: ["XS", "S", "M"], colors: ["White", "Red"], img: "https://picsum.photos/seed/p10/800/600" },
            { id: 11, name: "Denim Jacket", price: 79, sold: 275, sizes: ["S", "M", "L"], colors: ["Blue"], img: "https://picsum.photos/seed/p11/800/600" },
            { id: 12, name: "Oxford Shirt", price: 49, sold: 165, sizes: ["S", "M", "L", "XL"], colors: ["White", "Blue"], img: "https://picsum.photos/seed/p12/800/600" },
            { id: 13, name: "Track Pants", price: 35, sold: 198, sizes: ["S", "M", "L"], colors: ["Black", "Grey"], img: "https://picsum.photos/seed/p13/800/600" },
            { id: 14, name: "Polo Shirt", price: 29, sold: 260, sizes: ["S", "M", "L", "XL"], colors: ["Navy", "Green", "White"], img: "https://picsum.photos/seed/p14/800/600" },
            { id: 15, name: "Cargo Pants", price: 52, sold: 145, sizes: ["M", "L", "XL"], colors: ["Green", "Brown"], img: "https://picsum.photos/seed/p15/800/600" },
            { id: 16, name: "Maxi Dress", price: 69, sold: 120, sizes: ["XS", "S", "M", "L"], colors: ["Red", "Beige"], img: "https://picsum.photos/seed/p16/800/600" },
            { id: 17, name: "Tailored Blazer", price: 129, sold: 90, sizes: ["M", "L", "XL"], colors: ["Navy", "Black"], img: "https://picsum.photos/seed/p17/800/600" },
            { id: 18, name: "Knit Cardigan", price: 57, sold: 160, sizes: ["S", "M", "L"], colors: ["Beige", "Brown"], img: "https://picsum.photos/seed/p18/800/600" },
            { id: 19, name: "Ribbed Tank", price: 15, sold: 340, sizes: ["XS", "S", "M"], colors: ["White", "Black"], img: "https://picsum.photos/seed/p19/800/600" },
            { id: 20, name: "Windbreaker", price: 84, sold: 110, sizes: ["S", "M", "L"], colors: ["Blue", "Grey"], img: "https://picsum.photos/seed/p20/800/600" },
            { id: 21, name: "Joggers", price: 41, sold: 225, sizes: ["S", "M", "L", "XL"], colors: ["Grey", "Black"], img: "https://picsum.photos/seed/p21/800/600" },
            { id: 22, name: "Flannel Shirt", price: 46, sold: 175, sizes: ["S", "M", "L", "XL"], colors: ["Red", "Brown"], img: "https://picsum.photos/seed/p22/800/600" },
            { id: 23, name: "Trench Coat", price: 119, sold: 88, sizes: ["M", "L", "XL"], colors: ["Beige", "Brown"], img: "https://picsum.photos/seed/p23/800/600" },
            { id: 24, name: "Workwear Jacket", price: 89, sold: 140, sizes: ["M", "L", "XL"], colors: ["Brown", "Navy"], img: "https://picsum.photos/seed/p24/800/600" }
        ];

       
        var state = {
            sort: "price-asc",
            page: 1,
            perPage: 12,
            filters: {
                priceFrom: null,
                priceTo: null,
                sizes: new Set(),
                colors: new Set()
            }
        };

        
        function getCheckedValues(prefix) {
            var values = [];
            var inputs = document.querySelectorAll('input[id^="' + prefix + '"]');
            inputs.forEach(function (el) {
                if (el.checked) {
                    values.push(el.value);
                }
            });
            return values;
        }

        function parseNumberOrNull(value) {
            var num = Number(value);
            if (isNaN(num)) { return null; }
            return num;
        }

        
        function applyFilters(products) {
            return products.filter(function (p) {
                
                if (state.filters.priceFrom !== null && p.price < state.filters.priceFrom) { return false; }
                if (state.filters.priceTo !== null && p.price > state.filters.priceTo) { return false; }
                
                if (state.filters.sizes.size > 0) {
                    var okSize = p.sizes.some(function (s) { return state.filters.sizes.has(s); });
                    if (!okSize) { return false; }
                }
                
                if (state.filters.colors.size > 0) {
                    var okColor = p.colors.some(function (c) { return state.filters.colors.has(c); });
                    if (!okColor) { return false; }
                }
                return true;
            });
        }

        function sortProducts(list) {
            var arr = list.slice();
            var key = state.sort;
            arr.sort(function (a, b) {
                if (key === "price-asc") { return a.price - b.price; }
                if (key === "price-desc") { return b.price - a.price; }
                if (key === "sold-asc") { return a.sold - b.sold; }
                if (key === "sold-desc") { return b.sold - a.sold; }
                return 0;
            });
            return arr;
        }

        function paginate(list) {
            var start = (state.page - 1) * state.perPage;
            var end = start + state.perPage;
            return list.slice(start, end);
        }

       
        function renderProducts(list, total) {
            var grid = document.getElementById("productGrid");
            grid.innerHTML = "";
            if (list.length === 0) {
                var empty = document.createElement("div");
                empty.className = "col-12";
                empty.innerHTML = '<div class="text-center text-secondary py-5">No products match your filters.</div>';
                grid.appendChild(empty);
                return;
            }

            list.forEach(function (p) {
                var col = document.createElement("div");
                col.className = "col-12 col-sm-6 col-lg-4";
                col.innerHTML =
                    '<div class="card h-100 shadow-sm">' +
                    '<img class="card-img-top" src="' + p.img + '" alt="' + p.name + '" loading="lazy" />' +
                    '<div class="card-body d-flex flex-column">' +
                    '<h3 class="h6 mb-1">' + p.name + '</h3>' +
                    '<p class="small text-secondary mb-2">Sizes: ' + p.sizes.join(", ") + ' · Colors: ' + p.colors.join(", ") + '</p>' +
                    '<div class="d-flex justify-content-between align-items-center mt-auto">' +
                    '<span class="fw-semibold">$' + p.price.toFixed(2) + '</span>' +
                    '<span class="badge text-bg-light">Sold ' + p.sold + '</span>' +
                    '</div>' +
                    '<a href="#product-' + p.id + '" class="btn btn-dark w-100 mt-3" role="button">Add to Cart</a>' +
                    '</div>' +
                    '</div>';
                grid.appendChild(col);
            });

            var resultsCount = document.getElementById("resultsCount");
            var first = (state.page - 1) * state.perPage + 1;
            var last = Math.min(state.page * state.perPage, total);
            resultsCount.textContent = "Showing " + first + "–" + last + " of " + total + " results";
        }

        function renderPagination(total) {
            var totalPages = Math.max(1, Math.ceil(total / state.perPage));
            if (state.page > totalPages) { state.page = totalPages; }

            var list = document.getElementById("pagination");
            list.innerHTML = "";

            // Prev
            var prevLi = document.createElement("li");
            prevLi.className = "page-item" + (state.page === 1 ? " disabled" : "");
            prevLi.innerHTML = '<a class="page-link" href="#" aria-label="Previous">«</a>';
            prevLi.addEventListener("click", function (e) {
                e.preventDefault();
                if (state.page > 1) {
                    state.page -= 1;
                    updateResults();
                    scrollToTop();
                }
            });
            list.appendChild(prevLi);

            // Page numbers (compact window)
            var windowSize = 5;
            var start = Math.max(1, state.page - Math.floor(windowSize / 2));
            var end = Math.min(totalPages, start + windowSize - 1);
            start = Math.max(1, Math.min(start, end - windowSize + 1));

            if (start > 1) {
                list.appendChild(makePageItem(1));
                if (start > 2) { appendEllipsis(list); }
            }

            for (var i = start; i <= end; i++) {
                list.appendChild(makePageItem(i));
            }

            if (end < totalPages) {
                if (end < totalPages - 1) { appendEllipsis(list); }
                list.appendChild(makePageItem(totalPages));
            }

            // Next
            var nextLi = document.createElement("li");
            nextLi.className = "page-item" + (state.page === totalPages ? " disabled" : "");
            nextLi.innerHTML = '<a class="page-link" href="#" aria-label="Next">»</a>';
            nextLi.addEventListener("click", function (e) {
                e.preventDefault();
                if (state.page < totalPages) {
                    state.page += 1;
                    updateResults();
                    scrollToTop();
                }
            });
            list.appendChild(nextLi);

            function makePageItem(n) {
                var li = document.createElement("li");
                li.className = "page-item" + (state.page === n ? " active" : "");
                li.innerHTML = '<a class="page-link" href="#">' + n + "</a>";
                li.addEventListener("click", function (e) {
                    e.preventDefault();
                    if (state.page !== n) {
                        state.page = n;
                        updateResults();
                        scrollToTop();
                    }
                });
                return li;
            }

            function appendEllipsis(ul) {
                var li = document.createElement("li");
                li.className = "page-item disabled";
                li.innerHTML = '<span class="page-link">…</span>';
                ul.appendChild(li);
            }
        }

        function scrollToTop() {
            var topEl = document.querySelector("main");
            if (topEl) {
                topEl.scrollIntoView({ behavior: "smooth", block: "start" });
            }
        }

        
        function updateFiltersFromForm() {
            state.filters.priceFrom = parseNumberOrNull(document.getElementById("priceFrom").value);
            state.filters.priceTo = parseNumberOrNull(document.getElementById("priceTo").value);
            state.filters.sizes = new Set(getCheckedValues("size"));
            state.filters.colors = new Set(getCheckedValues("color"));
        }

        function updateResults() {
            var filtered = applyFilters(PRODUCTS);
            var sorted = sortProducts(filtered);
            var pageItems = paginate(sorted);
            renderProducts(pageItems, filtered.length);
            renderPagination(filtered.length);
        }

        function attachEvents() {
            document.getElementById("applyFilters").addEventListener("click", function () {
                updateFiltersFromForm();
                state.page = 1;
                updateResults();
            });

            document.getElementById("resetFilters").addEventListener("click", function () {
                document.getElementById("filtersForm").reset();
                state.filters.priceFrom = null;
                state.filters.priceTo = null;
                state.filters.sizes = new Set();
                state.filters.colors = new Set();
                state.page = 1;
                updateResults();
            });

            document.getElementById("sortSelect").addEventListener("change", function (e) {
                state.sort = e.target.value;
                state.page = 1;
                updateResults();
            });

            document.getElementById("perPageSelect").addEventListener("change", function (e) {
                state.perPage = Number(e.target.value);
                state.page = 1;
                updateResults();
            });

            // Allow pressing Enter in price inputs to apply quickly
            ["priceFrom", "priceTo"].forEach(function (id) {
                var el = document.getElementById(id);
                el.addEventListener("keydown", function (ev) {
                    if (ev.key === "Enter") {
                        ev.preventDefault();
                        document.getElementById("applyFilters").click();
                    }
                });
            });
        }

       
        (function init() {
            // Footer year
            var yEl = document.getElementById("year");
            if (yEl) { yEl.textContent = new Date().getFullYear().toString(); }

            // Defaults
            document.getElementById("sortSelect").value = state.sort;
            document.getElementById("perPageSelect").value = String(state.perPage);

            attachEvents();
            updateResults();
        })();