package service;

import exceptions.NotFoundException;
import model.Expense;
import model.Project;
import model.Schedule;

import java.util.*;

public class DataStore {

    private final List<Expense> expenses = new ArrayList<>();
    private final List<Project> projects = new ArrayList<>();
    private final Map<String, Schedule> schedules = new LinkedHashMap<>();

    // ------------------- Expense -------------------
    public List<Expense> getExpenses() { return expenses; }

    public Expense getExpense(int idx) {
        if (idx < 0 || idx >= expenses.size())
            throw new NotFoundException("존재하지 않는 소비 항목 번호: " + (idx + 1));
        return expenses.get(idx);
    }

    public void removeExpense(int idx) {
        if (idx < 0 || idx >= expenses.size())
            throw new NotFoundException("존재하지 않는 소비 항목 번호: " + (idx + 1));
        expenses.remove(idx);
    }

    // ------------------- Project -------------------
    public List<Project> getProjects() { return projects; }

    public Project getProject(int idx) {
        if (idx < 0 || idx >= projects.size())
            throw new NotFoundException("존재하지 않는 프로젝트 번호: " + (idx + 1));
        return projects.get(idx);
    }

    public void removeProject(int idx) {
        if (idx < 0 || idx >= projects.size())
            throw new NotFoundException("존재하지 않는 프로젝트 번호: " + (idx + 1));
        projects.remove(idx);
    }

    // ------------------- Schedule (Map) -------------------
    public Schedule getSchedule(String key) {
        Schedule s = schedules.get(key);
        if (s == null) throw new NotFoundException("존재하지 않는 일정 키: " + key);
        return s;
    }

    public void putSchedule(String key, Schedule schedule) { schedules.put(key, schedule); }
    public Map<String, Schedule> getSchedules() { return schedules; }

    // 조회용 인덱스 접근 (values 순서 기준)
    public Schedule getSchedule(int idx) {
        if (idx < 0 || idx >= schedules.size())
            throw new NotFoundException("존재하지 않는 일정 번호: " + (idx + 1));
        return new ArrayList<>(schedules.values()).get(idx);
    }

    public void removeSchedule(String key) {
        if (!schedules.containsKey(key))
            throw new NotFoundException("존재하지 않는 일정 키: " + key);
        schedules.remove(key);
    }

    public void removeSchedule(int idx) {
        if (idx < 0 || idx >= schedules.size())
            throw new NotFoundException("존재하지 않는 일정 번호: " + (idx + 1));
        String key = new ArrayList<>(schedules.keySet()).get(idx);
        schedules.remove(key);
    }
}
