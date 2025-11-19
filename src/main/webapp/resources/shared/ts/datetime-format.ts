function pad(n: number): string {
    return n.toString().padStart(2, "0");
}

function formatEpochMillis(millis: number | null | undefined): string | null {
    if (millis == null || isNaN(millis) || (millis < 0)) {
        return null;
    }

    const d = new Date(millis);

    const datePart =
        pad(d.getDate()) + "/" +
        pad(d.getMonth() + 1) + "/" +
        d.getFullYear();

    const timePart =
        pad(d.getHours()) + ":" +
        pad(d.getMinutes()) + ":" +
        pad(d.getSeconds());

    return datePart + " " + timePart;
}

function initOrderDateTimeFormatter(): void {
    const elements = document.querySelectorAll<HTMLSpanElement>(".date-time");

    elements.forEach((el) => {
        const raw = el.dataset["value"];
        if (raw == null) {
            el.textContent = "";
            return;
        }

        const millis = Number.parseInt(raw, 10);
        const formatted = formatEpochMillis(millis);
        if (formatted != null) {
            el.textContent = formatted;
        }
    });
}

document.addEventListener("DOMContentLoaded", () => {
    initOrderDateTimeFormatter();
});
