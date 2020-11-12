package in.kamalaenterprize.model;

public class NotificationModel {
    String id, meeter_user_id, sender_user_id, notification_title, type, history_id;
    String status, meeter_remark;
    public boolean expanded = false;

    public NotificationModel(String id, String meeter_user_id, String sender_user_id, String notification_title, String type, String history_id,
                             boolean expanded, String status, String meeter_remark) {
        this.id = id;
        this.meeter_user_id = meeter_user_id;
        this.sender_user_id = sender_user_id;
        this.notification_title = notification_title;
        this.type = type;
        this.history_id = history_id;
        this.expanded = expanded;
        this.status = status;
        this.meeter_remark = meeter_remark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMeeter_user_id() {
        return meeter_user_id;
    }

    public void setMeeter_user_id(String meeter_user_id) {
        this.meeter_user_id = meeter_user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMeeter_remark() {
        return meeter_remark;
    }

    public void setMeeter_remark(String meeter_remark) {
        this.meeter_remark = meeter_remark;
    }

    public String getSender_user_id() {
        return sender_user_id;
    }

    public void setSender_user_id(String sender_user_id) {
        this.sender_user_id = sender_user_id;
    }

    public String getNotification_title() {
        return notification_title;
    }

    public void setNotification_title(String notification_title) {
        this.notification_title = notification_title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHistory_id() {
        return history_id;
    }

    public void setHistory_id(String history_id) {
        this.history_id = history_id;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
