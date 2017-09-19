package korbit.tran.vo;

public class OrderVO extends BaseVO {
	String id = "";
	String side = "";
	Long avg_price = null;
	Long price = null;
	Float order_amount = null;
	Float filled_amount = null;
	String created_at = "";
	String status = null;
	Float order_total = null;
	Float filled_total = null;
	String last_filled_at = "";
	Float fee = null;
	
	
	public Float getOrder_total() {
		return order_total;
	}
	public void setOrder_total(Float order_total) {
		this.order_total = order_total;
	}
	public Float getFilled_total() {
		return filled_total;
	}
	public void setFilled_total(Float filled_total) {
		this.filled_total = filled_total;
	}
	public String getLast_filled_at() {
		return last_filled_at;
	}
	public void setLast_filled_at(String last_filled_at) {
		this.last_filled_at = last_filled_at;
	}
	public Float getFee() {
		return fee;
	}
	public void setFee(Float fee) {
		this.fee = fee;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSide() {
		return side;
	}
	public void setSide(String side) {
		this.side = side;
	}
	public Long getAvg_price() {
		return avg_price;
	}
	public void setAvg_price(Long avg_price) {
		this.avg_price = avg_price;
	}
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	public Float getOrder_amount() {
		return order_amount;
	}
	public void setOrder_amount(Float order_amount) {
		this.order_amount = order_amount;
	}
	public Float getFilled_amount() {
		return filled_amount;
	}
	public void setFilled_amount(Float filled_amount) {
		this.filled_amount = filled_amount;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
