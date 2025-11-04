package model;

public abstract class Work {
    private String title;
    private String status; // 예: 등록/진행중/완료/보류 등

    protected Work(String title, String status) {
        this.title = title;
        this.status = status;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
