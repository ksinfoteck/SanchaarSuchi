package in.kamalaenterprize.model;

public class LabDetailModel {
	String str_recordid, str_city, str_descriptionandorserviceprovided,
			str_contact, str_phonenumber, str_nameoftheorganisation, str_state;

	public LabDetailModel() {}

	public LabDetailModel(String str_recordid, String str_city, String str_descriptionandorserviceprovided,
						  String str_contact, String str_phonenumber, String str_nameoftheorganisation, String str_state) {
		this.str_recordid = str_recordid;
		this.str_city = str_city;
		this.str_descriptionandorserviceprovided = str_descriptionandorserviceprovided;
		this.str_contact = str_contact;
		this.str_phonenumber = str_phonenumber;
		this.str_nameoftheorganisation = str_nameoftheorganisation;
		this.str_state = str_state;
	}

	public String getStr_recordid() {
		return str_recordid;
	}

	public void setStr_recordid(String str_recordid) {
		this.str_recordid = str_recordid;
	}

	public String getStr_city() {
		return str_city;
	}

	public void setStr_city(String str_city) {
		this.str_city = str_city;
	}

	public String getStr_descriptionandorserviceprovided() {
		return str_descriptionandorserviceprovided;
	}

	public void setStr_descriptionandorserviceprovided(String str_descriptionandorserviceprovided) {
		this.str_descriptionandorserviceprovided = str_descriptionandorserviceprovided;
	}

	public String getStr_contact() {
		return str_contact;
	}

	public void setStr_contact(String str_contact) {
		this.str_contact = str_contact;
	}

	public String getStr_phonenumber() {
		return str_phonenumber;
	}

	public void setStr_phonenumber(String str_phonenumber) {
		this.str_phonenumber = str_phonenumber;
	}

	public String getStr_nameoftheorganisation() {
		return str_nameoftheorganisation;
	}

	public void setStr_nameoftheorganisation(String str_nameoftheorganisation) {
		this.str_nameoftheorganisation = str_nameoftheorganisation;
	}

	public String getStr_state() {
		return str_state;
	}

	public void setStr_state(String str_state) {
		this.str_state = str_state;
	}
}
