package in.kamalaenterprize.model;

public class StateModel {
	String strStateId, strStatename, strStateDesc, strStatus;

	public StateModel() {}

	public StateModel(String strStateId, String strStatename, String strStateDesc, String strStatus) {
		this.strStateId = strStateId;
		this.strStatename = strStatename;
		this.strStateDesc = strStateDesc;
		this.strStatus = strStatus;
	}

	public String getStrStateId() {
		return strStateId;
	}

	public void setStrStateId(String strStateId) {
		this.strStateId = strStateId;
	}

	public String getStrStatename() {
		return strStatename;
	}

	public void setStrStatename(String strStatename) {
		this.strStatename = strStatename;
	}

	public String getStrStateDesc() {
		return strStateDesc;
	}

	public void setStrStateDesc(String strStateDesc) {
		this.strStateDesc = strStateDesc;
	}

	public String getStrStatus() {
		return strStatus;
	}

	public void setStrStatus(String strStatus) {
		this.strStatus = strStatus;
	}
}
