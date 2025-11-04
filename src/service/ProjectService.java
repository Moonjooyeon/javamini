package service;

import exceptions.NotFoundException;
import exceptions.ValidationException;
import model.Project;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ProjectService {

    private final DataStore store;

    public ProjectService(DataStore store) { this.store = store; }

    public void addProject(String title, String owner, LocalDate start, LocalDate due) {
        if (title == null || title.isBlank()) throw new ValidationException("프로젝트 제목이 비었습니다.");
        if (owner == null || owner.isBlank()) throw new ValidationException("담당자 이름이 비었습니다.");
        if (start == null || due == null) throw new ValidationException("시작일/마감일이 필요합니다.");
        if (due.isBefore(start)) throw new ValidationException("마감일은 시작일 이후여야 합니다.");
        store.getProjects().add(new Project(title, "진행중", owner, start, due));
    }

    public void changeStatus(int indexZeroBased, String newStatus) {
        if (indexZeroBased < 0 || indexZeroBased >= store.getProjects().size())
            throw new NotFoundException("존재하지 않는 프로젝트 번호: " + (indexZeroBased + 1));
        if (newStatus == null || newStatus.isBlank())
            throw new ValidationException("새 상태가 비어 있습니다.");
        store.getProject(indexZeroBased).setStatus(newStatus);
    }

    public List<Project> search(String keyword) {
        String kw = (keyword == null) ? "" : keyword.trim().toLowerCase(Locale.ROOT);
        return store.getProjects().stream()
                .filter(p -> p.getTitle().toLowerCase(Locale.ROOT).contains(kw)
                        || p.getOwner().toLowerCase(Locale.ROOT).contains(kw)
                        || p.getStatus().toLowerCase(Locale.ROOT).contains(kw))
                .collect(Collectors.toList());
    }

    public List<Project> deadlineClose(int daysInclusive) {
        var now = LocalDate.now();
        var limit = now.plusDays(Math.max(daysInclusive, 0));
        return store.getProjects().stream()
                .filter(p -> !p.getDueDate().isBefore(now) && !p.getDueDate().isAfter(limit))
                .sorted(java.util.Comparator.comparing(Project::getDueDate))
                .collect(java.util.stream.Collectors.toList());
    }
}
