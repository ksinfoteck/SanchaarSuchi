package in.kamalaenterprize.model;

public class ApproveAlertModel {
	String id, sender_id, sender_yoddha, labname, report_number, status, fullname, contactnumber;
	public boolean expanded = false;

	public ApproveAlertModel(String id, String sender_id, String sender_yoddha,
							 String labname, String report_number, String status, String fullname, String contactnumber, boolean expanded) {
		this.id = id;
		this.sender_id = sender_id;
		this.sender_yoddha = sender_yoddha;
		this.labname = labname;
		this.report_number = report_number;
		this.status = status;
		this.fullname = fullname;
		this.contactnumber = contactnumber;
		this.expanded = expanded;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getContactnumber() {
		return contactnumber;
	}

	public void setContactnumber(String contactnumber) {
		this.contactnumber = contactnumber;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSender_id() {
		return sender_id;
	}

	public void setSender_id(String sender_id) {
		this.sender_id = sender_id;
	}

	public String getSender_yoddha() {
		return sender_yoddha;
	}

	public void setSender_yoddha(String sender_yoddha) {
		this.sender_yoddha = sender_yoddha;
	}

	public String getLabname() {
		return labname;
	}

	public void setLabname(String labname) {
		this.labname = labname;
	}

	public String getReport_number() {
		return report_number;
	}

	public void setReport_number(String report_number) {
		this.report_number = report_number;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
}
