export { }

declare global {
    interface DailyIncomeStatistic {
        date: string; // Java Date/LocalDate khi serialize sang JSON thường là chuỗi
        totalIncome: number;
    }

    interface OrderStatisticByCategory {
        categoryId: number;
        categoryName: string;
        amount: number;
        totalPrice: number;
    }

    interface OrderStatisticByProduct {
        productId: number;
        productName: string;
        amount: number;
        totalPrice: number;
    }
}
