package in.kamalaenterprize.model;

public class HelpModel {
	String steQues, strAns;
	public boolean expanded = false;

	public HelpModel() {
	}

	public HelpModel(String steQues, String strAns) {
		this.steQues = steQues;
		this.strAns = strAns;
	}

	public String getSteQues() {
		return steQues;
	}

	public String getStrAns() {
		return strAns;
	}
}
