(() => {
    const yearEl = document.getElementById("year");
    if (yearEl) {
        yearEl.textContent = new Date().getFullYear().toString();
    }

    const form = document.getElementById("loginForm") as HTMLFormElement;
    if (form) {
        form.addEventListener("submit", (e: Event) => {
            if (!form.checkValidity()) {
                e.preventDefault();
                e.stopPropagation();
            }
            form.classList.add("was-validated");
        });
    }

    const toggleBtn = document.getElementById("togglePwd");
    const pwdInput = document.getElementById("password") as HTMLInputElement;

    if (toggleBtn && pwdInput) {
        toggleBtn.addEventListener("click", () => {
            const isPasswordType = pwdInput.type === "password";
            pwdInput.type = isPasswordType ? "text" : "password";
            toggleBtn.textContent = isPasswordType ? "Hide" : "Show";
        });
    }

    const STORAGE_KEY = "aurora_username";
    const inputUser = document.getElementById("inputUsername") as HTMLInputElement;

    const savedUser = sessionStorage.getItem(STORAGE_KEY);
    if (savedUser && inputUser) {
        inputUser.value = savedUser;
    }

    if (form && inputUser) {
        form.addEventListener("submit", () => {
            if (inputUser.value) {
                sessionStorage.setItem(STORAGE_KEY, inputUser.value);
            }
        });
    }
})();
