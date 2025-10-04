window.addEventListener("DOMContentLoaded", () => {
    // Simple-DataTables
    // https://github.com/fiduswriter/Simple-DataTables/wiki

    const datatablesSimple = document.getElementById("datatablesSimple") as HTMLTableElement | null;
    if (datatablesSimple != null) {
        new simpleDatatables.DataTable(datatablesSimple);
    }
});
