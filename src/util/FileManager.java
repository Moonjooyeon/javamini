package util;

import exceptions.StorageException;
import model.Expense;
import model.Project;
import model.Schedule;
import service.DataStore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileManager {

    private static final String DATA_DIR       = "data";
    private static final String EXPENSE_FILE   = DATA_DIR + "/expenses.txt";
    private static final String PROJECT_FILE   = DATA_DIR + "/projects.txt";
    private static final String SCHEDULE_FILE  = DATA_DIR + "/schedules.txt";
    private static final String SEP            = "\\|"; // split용
    private static final String JOIN_SEP       = "|";   // write용

    private final DataStore store;

    public FileManager(DataStore store) {
        this.store = store;
    }

    public void loadAll() {
        ensureDataDir();
        loadExpenses();
        loadProjects();
        loadSchedules();
    }

    public void saveAll() {
        ensureDataDir();
        saveExpenses();
        saveProjects();
        saveSchedules();
        System.out.println("💾 저장 완료");
    }

    private void ensureDataDir() {
        try { Files.createDirectories(Paths.get(DATA_DIR)); }
        catch (IOException e) { throw new StorageException("data 디렉터리 생성 실패", e); }
    }

    /* ================= EXPENSES ================= */
    private void loadExpenses() {
        store.getExpenses().clear();
        Path p = Paths.get(EXPENSE_FILE);
        if (!Files.exists(p)) return;
        try (BufferedReader br = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] a = line.split(SEP, -1); // -1: 공백 필드 유지
                if (a.length < 5) continue;
                try {
                    store.getExpenses().add(new Expense(
                            a[0], a[1], Integer.parseInt(a[2]), a[3], LocalDate.parse(a[4])
                    ));
                } catch (Exception ignore) {}
            }
        } catch (IOException e) {
            throw new StorageException("소비 내역 불러오기 실패", e);
        }
    }

    private void saveExpenses() {
        Path p = Paths.get(EXPENSE_FILE);
        try (BufferedWriter bw = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
            for (Expense e : store.getExpenses()) {
                bw.write(String.join(JOIN_SEP,
                        nz(e.getTitle()),
                        nz(e.getStatus()),
                        String.valueOf(e.getPrice()),
                        nz(e.getCategory()),
                        e.getPurchaseDate().toString()
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new StorageException("소비 내역 저장 실패", e);
        }
    }

    /* ================= PROJECTS ================= */
    private void loadProjects() {
        store.getProjects().clear();
        Path p = Paths.get(PROJECT_FILE);
        if (!Files.exists(p)) return;
        try (BufferedReader br = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] a = line.split(SEP, -1);
                if (a.length < 5) continue;
                try {
                    store.getProjects().add(new Project(
                            a[0], a[1], a[2], LocalDate.parse(a[3]), LocalDate.parse(a[4])
                    ));
                } catch (Exception ignore) {}
            }
        } catch (IOException e) {
            throw new StorageException("프로젝트 불러오기 실패", e);
        }
    }

    private void saveProjects() {
        Path p = Paths.get(PROJECT_FILE);
        try (BufferedWriter bw = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
            for (Project prj : store.getProjects()) {
                bw.write(String.join(JOIN_SEP,
                        nz(prj.getTitle()),
                        nz(prj.getStatus()),
                        nz(prj.getOwner()),
                        prj.getStartDate().toString(),
                        prj.getDueDate().toString()
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new StorageException("프로젝트 저장 실패", e);
        }
    }

    /* ================= SCHEDULES ================= */
    private void loadSchedules() {
        store.getSchedules().clear();
        Path p = Paths.get(SCHEDULE_FILE);
        if (!Files.exists(p)) return;
        try (BufferedReader br = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] a = line.split(SEP, -1);
                if (a.length < 3) continue;
                try {
                    Schedule s = new Schedule(a[0], LocalDate.parse(a[1]), a[2]);
                    store.getSchedules().put(s.getName(), s);
                } catch (Exception ignore) {}
            }
        } catch (IOException e) {
            throw new StorageException("일정 불러오기 실패", e);
        }
    }

    private void saveSchedules() {
        Path p = Paths.get(SCHEDULE_FILE);
        try (BufferedWriter bw = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
            for (Map.Entry<String, Schedule> ent : store.getSchedules().entrySet()) {
                Schedule s = ent.getValue();
                bw.write(String.join(JOIN_SEP,
                        nz(s.getName()),
                        s.getDate().toString(),
                        nz(s.getMemo())
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new StorageException("일정 저장 실패", e);
        }
    }


    private String nz(String s) { return s == null ? "" : s; }
}
