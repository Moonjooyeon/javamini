package main;

import exceptions.NotFoundException;
import exceptions.StorageException;
import exceptions.ValidationException;
import service.*;
import util.FileManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class MainAppGUI extends JFrame {

    private final DataStore store;
    private final ExpenseService expenseService;
    private final ProjectService projectService;
    private final ScheduleService scheduleService;
    private final ReportService reportService;
    private final FileManager fileManager;

    @FunctionalInterface
    private interface UISafe { void run(); }

    private void uiSafe(UISafe block) {
        try { block.run(); }
        catch (ValidationException ve) { JOptionPane.showMessageDialog(this, ve.getMessage(), "입력 오류", JOptionPane.WARNING_MESSAGE); }
        catch (NotFoundException nfe) { JOptionPane.showMessageDialog(this, nfe.getMessage(), "찾을 수 없음", JOptionPane.WARNING_MESSAGE); }
        catch (StorageException se) { JOptionPane.showMessageDialog(this, se.getMessage(), "저장/불러오기 오류", JOptionPane.ERROR_MESSAGE); }
        catch (RuntimeException re) { re.printStackTrace(); JOptionPane.showMessageDialog(this, "예상치 못한 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE); }
    }

    public MainAppGUI() {
        this.store = new DataStore();
        this.expenseService = new ExpenseService(store);
        this.projectService = new ProjectService(store);
        this.scheduleService = new ScheduleService(store);
        this.reportService = new ReportService(store);
        this.fileManager = new FileManager(store);

        uiSafe(fileManager::loadAll);

        setTitle("CreativeWork Manager");
        setSize(420, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "치명적 오류: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        });
        SwingUtilities.invokeLater(() -> new MainAppGUI().setVisible(true));
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));
        JButton btnExpense = new JButton("1. 소비 내역 관리");
        JButton btnProject = new JButton("2. 프로젝트 관리");
        JButton btnSchedule = new JButton("3. 일정 캘린더");
        JButton btnReport = new JButton("4. 월간 활동 리포트");
        JButton btnSave = new JButton("💾 저장");
        JButton btnExit = new JButton("종료");

        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        panel.add(btnExpense); panel.add(btnProject); panel.add(btnSchedule);
        panel.add(btnReport); panel.add(btnSave); panel.add(btnExit);
        add(panel);

        btnExpense.addActionListener(e -> uiSafe(this::openExpenseDialog));
        btnProject.addActionListener(e -> uiSafe(this::openProjectDialog));
        btnSchedule.addActionListener(e -> uiSafe(this::openScheduleDialog));
        btnReport.addActionListener(e -> uiSafe(this::openReportDialog));
        btnSave.addActionListener(e -> uiSafe(() -> { fileManager.saveAll(); JOptionPane.showMessageDialog(this, "저장되었습니다."); }));
        btnExit.addActionListener(e -> uiSafe(() -> { fileManager.saveAll(); System.exit(0); }));
    }

    // ---------------- 소비 ----------------
    private void openExpenseDialog() {
        String[] options = {"소비 추가", "전체 보기", "삭제", "닫기"};
        while (true) {
            int sel = JOptionPane.showOptionDialog(this, "소비 내역 관리", "소비",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (sel == 0) addExpenseByDialog();
            else if (sel == 1) showAllExpenses();
            else if (sel == 2) deleteExpenseByDialog();
            else break;
        }
    }

    private void addExpenseByDialog() {
        String title = JOptionPane.showInputDialog(this, "소비 항목 이름:");
        if (title == null) return;
        if (title.isBlank()) throw new ValidationException("제목이 비었습니다.");

        String category = JOptionPane.showInputDialog(this, "분류:");
        if (category == null) return;
        if (category.isBlank()) throw new ValidationException("분류를 입력해주세요.");

        String priceStr = JOptionPane.showInputDialog(this, "금액:");
        if (priceStr == null) return;
        int price;
        try { price = Integer.parseInt(priceStr); }
        catch (NumberFormatException e) { throw new ValidationException("금액은 숫자로 입력해야 합니다."); }

        String dateStr = JOptionPane.showInputDialog(this, "구매일 (yyyy-MM-dd):");
        if (dateStr == null) return;
        LocalDate purchaseDate;
        try { purchaseDate = LocalDate.parse(dateStr); }
        catch (Exception e) { throw new ValidationException("날짜 형식이 잘못됐습니다. 예) 2025-11-03"); }

        expenseService.addExpense(title, category, price, purchaseDate);
        fileManager.saveAll();
        JOptionPane.showMessageDialog(this, "추가되었습니다.");
    }

    private void showAllExpenses() {
        String[] options = {"기본순", "날짜순", "금액 높은순"};
        int sel = JOptionPane.showOptionDialog(this, "어떻게 볼까요?", "소비 정렬",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        List<model.Expense> list =
                (sel == 1) ? expenseService.getExpensesSortedByDate()
                        : (sel == 2) ? expenseService.getExpensesSortedByPriceDesc()
                        : store.getExpenses();

        StringBuilder sb = new StringBuilder();
        if (list.isEmpty()) sb.append("등록된 소비 내역이 없습니다.");
        else {
            int i = 1;
            for (model.Expense e : list) {
                sb.append(i++).append(") ")
                  .append(e.getTitle()).append(" / ")
                  .append(e.getCategory()).append(" / ")
                  .append(e.getPrice()).append("원 / ")
                  .append(e.getPurchaseDate()).append(" / ")
                  .append("상태: ").append(e.getStatus()).append("\n");
            }
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "전체 소비 내역", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteExpenseByDialog() {
        if (store.getExpenses().isEmpty()) { JOptionPane.showMessageDialog(this, "삭제할 항목이 없습니다."); return; }

        StringBuilder sb = new StringBuilder("삭제할 번호를 입력하세요:\n");
        int i = 1;
        for (model.Expense e : store.getExpenses()) {
            sb.append(i++).append(") ").append(e.getTitle()).append(" / ")
              .append(e.getCategory()).append(" / ").append(e.getPrice()).append("원\n");
        }
        String in = JOptionPane.showInputDialog(this, sb.toString());
        if (in == null) return;

        int idx;
        try { idx = Integer.parseInt(in) - 1; }
        catch (NumberFormatException e) { throw new ValidationException("숫자를 입력해주세요."); }

        expenseService.removeExpense(idx);
        fileManager.saveAll();
        JOptionPane.showMessageDialog(this, "삭제되었습니다.");
    }

    // ---------------- 프로젝트 ----------------
    private void openProjectDialog() {
        String[] options = {"프로젝트 추가", "전체 보기", "상태 변경", "닫기"};
        while (true) {
            int sel = JOptionPane.showOptionDialog(this, "프로젝트 관리", "프로젝트",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (sel == 0) addProjectByDialog();
            else if (sel == 1) showAllProjects();
            else if (sel == 2) changeProjectStatusByDialog();
            else break;
        }
    }

    private void addProjectByDialog() {
        String title = JOptionPane.showInputDialog(this, "프로젝트 제목:");
        if (title == null) return;
        if (title.isBlank()) throw new ValidationException("제목이 비었습니다.");

        String owner = JOptionPane.showInputDialog(this, "담당자 이름:");
        if (owner == null) return;
        if (owner.isBlank()) throw new ValidationException("담당자 이름이 비었습니다.");

        String startStr = JOptionPane.showInputDialog(this, "시작일 (yyyy-MM-dd):");
        if (startStr == null) return;

        String dueStr = JOptionPane.showInputDialog(this, "마감일 (yyyy-MM-dd):");
        if (dueStr == null) return;

        LocalDate startDate, dueDate;
        try { startDate = LocalDate.parse(startStr); dueDate = LocalDate.parse(dueStr); }
        catch (Exception e) { throw new ValidationException("날짜 형식이 잘못됐습니다. 예) 2025-11-03"); }

        projectService.addProject(title, owner, startDate, dueDate);
        fileManager.saveAll();
        JOptionPane.showMessageDialog(this, "프로젝트가 추가되었습니다.");
    }

    private void showAllProjects() {
        String kw = JOptionPane.showInputDialog(this, "검색어(제목/담당자/상태). 비우면 전체:");
        List<model.Project> list =
                (kw != null && !kw.isBlank()) ? projectService.search(kw) : store.getProjects();

        StringBuilder sb = new StringBuilder();
        if (list.isEmpty()) sb.append("조건에 맞는 프로젝트가 없습니다.");
        else {
            int i = 1;
            for (model.Project p : list) {
                sb.append(i++).append(") ")
                  .append(p.getTitle()).append(" / ")
                  .append("담당: ").append(p.getOwner()).append(" / ")
                  .append("마감: ").append(p.getDueDate()).append(" / ")
                  .append("상태: ").append(p.getStatus()).append("\n");
            }
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "프로젝트 목록", JOptionPane.INFORMATION_MESSAGE);
    }

    private void changeProjectStatusByDialog() {
        if (store.getProjects().isEmpty()) { JOptionPane.showMessageDialog(this, "변경할 프로젝트가 없습니다."); return; }

        StringBuilder sb = new StringBuilder("상태를 변경할 프로젝트 번호를 선택하세요:\n");
        int i = 1;
        for (model.Project p : store.getProjects()) {
            sb.append(i++).append(") ").append(p.getTitle()).append(" (").append(p.getStatus()).append(")\n");
        }

        String in = JOptionPane.showInputDialog(this, sb.toString());
        if (in == null) return;

        int idx;
        try { idx = Integer.parseInt(in) - 1; }
        catch (NumberFormatException e) { throw new ValidationException("숫자를 입력해주세요."); }

        String[] statusOptions = {"진행중", "완료", "보류"};
        String newStatus = (String) JOptionPane.showInputDialog(
                this, "새 상태를 선택하세요:", "상태 변경",
                JOptionPane.PLAIN_MESSAGE, null, statusOptions, store.getProject(idx).getStatus()
        );
        if (newStatus == null) return;

        projectService.changeStatus(idx, newStatus);
        fileManager.saveAll();
        JOptionPane.showMessageDialog(this, "상태가 변경되었습니다.");
    }

    // ---------------- 일정 ----------------
    private void openScheduleDialog() {
        String[] options = {"일정 등록", "전체 보기", "삭제", "닫기"};
        while (true) {
            int sel = JOptionPane.showOptionDialog(this, "일정 캘린더", "일정",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (sel == 0) addScheduleByDialog();
            else if (sel == 1) showAllSchedules();
            else if (sel == 2) deleteScheduleByDialog();
            else break;
        }
    }

    private void addScheduleByDialog() {
        String key = JOptionPane.showInputDialog(this, "일정 이름(고유키):");
        if (key == null || key.isBlank()) return;

        String dateStr = JOptionPane.showInputDialog(this, "날짜 (yyyy-MM-dd):");
        if (dateStr == null) return;

        LocalDate date;
        try { date = LocalDate.parse(dateStr); }
        catch (Exception e) { throw new ValidationException("날짜 형식이 잘못되었습니다. 예) 2025-11-03"); }

        String memo = JOptionPane.showInputDialog(this, "메모:");
        if (memo == null) memo = "";

        scheduleService.addSchedule(key, date, memo);
        fileManager.saveAll();
        JOptionPane.showMessageDialog(this, "일정이 추가되었습니다.");
    }

    private void showAllSchedules() {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Map.Entry<String, model.Schedule> e : store.getSchedules().entrySet()) {
            sb.append(i++).append(") ").append(e.getKey()).append(" : ")
              .append(e.getValue().getDate()).append(" / ").append(e.getValue().getMemo()).append("\n");
        }
        if (sb.length() == 0) sb.append("일정이 없습니다.");
        JOptionPane.showMessageDialog(this, sb.toString(), "전체 일정", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteScheduleByDialog() {
        if (store.getSchedules().isEmpty()) { JOptionPane.showMessageDialog(this, "삭제할 일정이 없습니다."); return; }
        StringBuilder sb = new StringBuilder("삭제할 키를 입력하세요:\n");
        for (String key : store.getSchedules().keySet()) sb.append("- ").append(key).append("\n");
        String k = JOptionPane.showInputDialog(this, sb.toString());
        if (k == null) return;
        scheduleService.removeSchedule(k);
        fileManager.saveAll();
        JOptionPane.showMessageDialog(this, "삭제되었습니다.");
    }

    private void openReportDialog() {
        if (store.getExpenses().isEmpty() && store.getProjects().isEmpty() && store.getSchedules().isEmpty()) {
            JOptionPane.showMessageDialog(this, "리포트에 표시할 데이터가 없습니다."); return;
        }
        var ym = java.time.YearMonth.now();
        String content = reportService.buildMonthlySummary(ym);
        JOptionPane.showMessageDialog(this, content, "월간 활동 리포트", JOptionPane.INFORMATION_MESSAGE);
    }
}
