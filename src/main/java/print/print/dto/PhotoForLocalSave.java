package print.print.dto;


import print.print.DateParser;
import print.print.HelloController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PhotoForLocalSave {
    private Long id;
    private String createdAt;
    private String size;
    private String imagePath;
    private boolean status;
    private Long userId;
    private int amount;

    public PhotoForLocalSave(Long id, String createdAt, String size, String imagePath, boolean status, Long userId, int amount) {
        this.id = id;
        this.createdAt = createdAt;
        this.size = size;
        this.imagePath = imagePath;
        this.status = status;
        this.userId = userId;
        this.amount = amount;
    }

    public PhotoForLocalSave() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAtOrig() {
        return DateParser.parseDateTime(this.createdAt);
    }

    @Override
    public String toString() {
        return "PhotoForLocalSave{" +
                "id=" + id +
                ", createdAt='" + createdAt + '\'' +
                ", size='" + size + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", status=" + status +
                ", userId=" + userId +
                ", amount=" + amount +
                '}';
    }
}
