package korbit.tran.vo;

import korbit.tran.util.Utils;

public class BaseVO {
	String timestamp = "";
	String dateTime = "";
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getDateTime() {
		return Utils.timeStamp2Date(this.timestamp);
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
}
