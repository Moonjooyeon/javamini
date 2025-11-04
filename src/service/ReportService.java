package service;

import exceptions.StorageException;
import model.Expense;
import model.Schedule;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

public class ReportService {

    private final DataStore store;

    public ReportService(DataStore store) { this.store = store; }

    public String buildMonthlySummary(YearMonth ym) {
        StringBuilder sb = new StringBuilder();
        sb.append("📅 월간 활동 리포트 (").append(ym).append(")\n\n");

        // ---- (A) null-세이프 월 합계 (범위 필터) ----
        LocalDate start = ym.atDay(1);
        LocalDate endExclusive = ym.plusMonths(1).atDay(1);

        int totalExpense = store.getExpenses().stream()
                .filter(e -> {
                    var d = e.getPurchaseDate();
                    return d != null && !d.isBefore(start) && d.isBefore(endExclusive);
                })
                .mapToInt(Expense::getPrice)
                .sum();
        sb.append("💰 총 소비액: ").append(totalExpense).append("원\n");

        long ongoing = store.getProjects().stream().filter(p -> "진행중".equals(p.getStatus())).count();
        long done = store.getProjects().stream().filter(p -> "완료".equals(p.getStatus())).count();

        sb.append("\n📂 프로젝트 현황:\n")
          .append(" - 진행중: ").append(ongoing).append("개\n")
          .append(" - 완료: ").append(done).append("개\n");

        var upcoming = store.getSchedules().values().stream()
                .sorted(java.util.Comparator.comparing(Schedule::getDate))
                .limit(5).collect(Collectors.toList());

        if (!upcoming.isEmpty()) {
            sb.append("\n🗓️ 다가오는 일정:\n");
            for (Schedule s : upcoming) {
                sb.append(" - ").append(s.getName()).append(": ")
                  .append(s.getDate()).append(" (")
                  .append(s.getRemainingDays()).append("일 남음)\n");
            }
        }
        return sb.toString();
    }

    public Path saveMonthlySummaryToTxt(YearMonth ym) {
        String content = buildMonthlySummary(ym);
        Path dir = Paths.get("data");
        Path path = dir.resolve("report_" + ym.getYear() + "_" + String.format("%02d", ym.getMonthValue()) + ".txt");
        try {
            Files.createDirectories(dir);
            try (BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                bw.write(content);
            }
            return path;
        } catch (IOException e) {
            throw new StorageException("리포트 저장 실패: " + path.toAbsolutePath(), e);
        }
    }
}
