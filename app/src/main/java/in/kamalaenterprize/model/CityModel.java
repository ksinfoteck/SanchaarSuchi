package in.kamalaenterprize.model;

public class CityModel {
	String strCityId, strCityname, strCityDesc, strStatus, strStateId;

	public CityModel() {}

	public CityModel(String strCityId, String strCityname, String strCityDesc, String strStatus, String strStateId) {
		this.strCityId = strCityId;
		this.strCityname = strCityname;
		this.strCityDesc = strCityDesc;
		this.strStatus = strStatus;
		this.strStateId = strStateId;
	}

	public String getStrCityId() {
		return strCityId;
	}

	public void setStrCityId(String strCityId) {
		this.strCityId = strCityId;
	}

	public String getStrCityname() {
		return strCityname;
	}

	public void setStrCityname(String strCityname) {
		this.strCityname = strCityname;
	}

	public String getStrCityDesc() {
		return strCityDesc;
	}

	public void setStrCityDesc(String strCityDesc) {
		this.strCityDesc = strCityDesc;
	}

	public String getStrStatus() {
		return strStatus;
	}

	public void setStrStatus(String strStatus) {
		this.strStatus = strStatus;
	}

	public String getStrStateId() {
		return strStateId;
	}

	public void setStrStateId(String strStateId) {
		this.strStateId = strStateId;
	}
}
