declare const incomeDataRaw: string;
declare const categoryDataRaw: string;

const incomeData: DailyIncomeStatistic[] = JSON.parse(incomeDataRaw);
const categoryData: OrderStatisticByCategory[] = JSON.parse(categoryDataRaw);

function initAreaChart() {
    const dates = incomeData.map(d => d.date);
    const incomes = incomeData.map(d => d.totalIncome);

    const ctx = document.getElementById("myAreaChart") as HTMLCanvasElement;
    if (!ctx) return;

    new window.Chart(ctx, {
        type: 'line',
        data: {
            labels: dates,
            datasets: [{
                label: "Doanh thu",
                data: incomes,
                backgroundColor: "rgba(2,117,216,0.2)",
                borderColor: "rgba(2,117,216,1)",
            }],
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        callback: (value: number) => value.toLocaleString('vi-VN') + ' ₫'
                    }
                }]
            }
        }
    });
}

function initBarChart() {
    const labels = categoryData.map(c => c.categoryName);
    const data = categoryData.map(c => c.totalPrice);

    const ctx = document.getElementById("myBarChart") as HTMLCanvasElement;
    if (!ctx) return;

    new window.Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: "Doanh thu",
                data: data,
                backgroundColor: "rgba(2,117,216,1)",
            }],
        }
    });
}

document.addEventListener("DOMContentLoaded", () => {
    initAreaChart();
    initBarChart();
});