import "chart.js";

import type { DataTable as DataTableClass } from "simple-datatables";

declare global {
    const simpleDatatables: {
        DataTable: typeof DataTableClass;
    };
}
