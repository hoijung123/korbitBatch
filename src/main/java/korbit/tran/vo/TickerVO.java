package korbit.tran.vo;

public class TickerVO extends BaseVO {
	Long last = null;
	Long bid = null;
	Long ask = null;
	Long low = null;
	Long high = null;
	Float volume = null;
	
	
	public Long getBid() {
		return bid;
	}

	public void setBid(Long bid) {
		this.bid = bid;
	}

	public Long getAsk() {
		return ask;
	}

	public void setAsk(Long ask) {
		this.ask = ask;
	}

	public Long getLow() {
		return low;
	}

	public void setLow(Long low) {
		this.low = low;
	}

	public Long getHigh() {
		return high;
	}

	public void setHigh(Long high) {
		this.high = high;
	}

	public Float getVolume() {
		return volume;
	}

	public void setVolume(Float volume) {
		this.volume = volume;
	}

	public Long getLast() {
		return last;
	}

	public void setLast(Long last) {
		this.last = last;
	}

}
