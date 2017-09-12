package korbit.tran.vo;

public class BalanceDtlVO {
	Float available = null;
	Float trade_in_use = null;
	Float withdrawal_in_use = null;
	
	public Float getAvailable() {
		return available;
	}
	public void setAvailable(Float available) {
		this.available = available;
	}
	public Float getTrade_in_use() {
		return trade_in_use;
	}
	public void setTrade_in_use(Float trade_in_use) {
		this.trade_in_use = trade_in_use;
	}
	public Float getWithdrawal_in_use() {
		return withdrawal_in_use;
	}
	public void setWithdrawal_in_use(Float withdrawal_in_use) {
		this.withdrawal_in_use = withdrawal_in_use;
	}	
}
