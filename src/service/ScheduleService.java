package service;

import exceptions.NotFoundException;
import exceptions.ValidationException;
import model.Schedule;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScheduleService {

    private final DataStore store;

    public ScheduleService(DataStore store) { this.store = store; }

    public void addSchedule(String name, LocalDate date, String memo) {
        if (name == null || name.isBlank()) throw new ValidationException("일정 이름(키)은 비어 있을 수 없습니다.");
        if (date == null) throw new ValidationException("일정 날짜가 필요합니다.");
        if (store.getSchedules().containsKey(name))
            throw new ValidationException("중복된 일정 키입니다: " + name);
        store.putSchedule(name, new Schedule(name, date, memo == null ? "" : memo));
    }

    public void removeSchedule(String name) {
        if (!store.getSchedules().containsKey(name))
            throw new NotFoundException("존재하지 않는 일정 키입니다: " + name);
        store.removeSchedule(name);
    }

    public Map<String, Schedule> all() {
        return java.util.Collections.unmodifiableMap(store.getSchedules());
    }

    public List<Schedule> byMonth(int year, int month) {
        return store.getSchedules().values().stream()
                .filter(s -> s.getDate().getYear() == year && s.getDate().getMonthValue() == month)
                .sorted(Comparator.comparing(Schedule::getDate))
                .collect(Collectors.toList());
    }

    public List<Schedule> upcoming(int limit) {
        return store.getSchedules().values().stream()
                .sorted(Comparator.comparing(Schedule::getDate))
                .limit(Math.max(limit, 0))
                .collect(Collectors.toList());
    }
}
