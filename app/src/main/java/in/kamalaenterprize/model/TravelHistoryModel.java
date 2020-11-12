package in.kamalaenterprize.model;

public class TravelHistoryModel {
    String name, date, yoddhaId, travelType, address, locationLat, locationLong, modeOfTravel, note;
    String isVerified;
    String str_user_id;
    //			traveltype will be placevisit and travel
    public boolean expanded = false;

    public TravelHistoryModel(String name, String date, String yoddhaId, String travelType,
                              String address, String locationLat, String locationLong, String modeOfTravel, String note, String str_user_id,
                              String isVerified) {
        this.name = name;
        this.date = date;
        this.yoddhaId = yoddhaId;
        this.travelType = travelType;
        this.address = address;
        this.locationLat = locationLat;
        this.locationLong = locationLong;
        this.modeOfTravel = modeOfTravel;
        this.note = note;
        this.str_user_id = str_user_id;
        this.isVerified = isVerified;
    }

//    "name", "date", "yoddhaId", "travelType", "address", "locationLat", "locationLong", "modeOfTravel", "note"
    public TravelHistoryModel(String[] data) {
        this.name = data[0];
        this.date = data[1];
        this.yoddhaId = data[2];
        this.travelType = data[3];
        this.address = data[4];
        this.locationLat = data[5];
        this.locationLong = data[6];
        this.modeOfTravel = data[7];
        this.note = data[8];
        this.isVerified = data[10];
        this.str_user_id = data[11];
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public String getStr_user_id() {
        return str_user_id;
    }

    public void setStr_user_id(String str_user_id) {
        this.str_user_id = str_user_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getYoddhaId() {
        return yoddhaId;
    }

    public void setYoddhaId(String yoddhaId) {
        this.yoddhaId = yoddhaId;
    }

    public String getTravelType() {
        return travelType;
    }

    public void setTravelType(String travelType) {
        this.travelType = travelType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(String locationLat) {
        this.locationLat = locationLat;
    }

    public String getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(String locationLong) {
        this.locationLong = locationLong;
    }

    public String getModeOfTravel() {
        return modeOfTravel;
    }

    public void setModeOfTravel(String modeOfTravel) {
        this.modeOfTravel = modeOfTravel;
    }
}
