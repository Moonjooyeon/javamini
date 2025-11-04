package service;

import exceptions.NotFoundException;
import exceptions.ValidationException;
import model.Expense;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ExpenseService {

    private final DataStore store;

    public ExpenseService(DataStore store) { this.store = store; }

    public void addExpense(String title, String category, int price, LocalDate date) {
        if (title == null || title.isBlank()) throw new ValidationException("소비 제목이 비었습니다.");
        if (category == null || category.isBlank()) throw new ValidationException("분류를 입력하세요.");
        if (price < 0) throw new ValidationException("금액은 0 이상이어야 합니다.");
        if (date == null) throw new ValidationException("구매일이 필요합니다.");
        store.getExpenses().add(new Expense(title, "등록", price, category, date));
    }

    public void removeExpense(int indexZeroBased) {
        if (indexZeroBased < 0 || indexZeroBased >= store.getExpenses().size())
            throw new NotFoundException("존재하지 않는 소비 항목 번호: " + (indexZeroBased + 1));
        store.removeExpense(indexZeroBased);
    }

    public List<Expense> getExpensesSortedByDate() {
        return store.getExpenses().stream()
                .sorted(Comparator.comparing(Expense::getPurchaseDate))
                .collect(Collectors.toList());
    }

    public List<Expense> getExpensesSortedByPriceDesc() {
        return store.getExpenses().stream()
                .sorted(Comparator.comparingInt(Expense::getPrice).reversed())
                .collect(Collectors.toList());
    }

    public List<Expense> searchByTitle(String keyword) {
        String kw = (keyword == null) ? "" : keyword.trim().toLowerCase(Locale.ROOT);
        return store.getExpenses().stream()
                .filter(e -> e.getTitle().toLowerCase(Locale.ROOT).contains(kw))
                .collect(Collectors.toList());
    }

    public List<Expense> searchByCategory(String keyword) {
        String kw = (keyword == null) ? "" : keyword.trim().toLowerCase(Locale.ROOT);
        return store.getExpenses().stream()
                .filter(e -> e.getCategory().toLowerCase(Locale.ROOT).contains(kw))
                .collect(Collectors.toList());
    }
}
