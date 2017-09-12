package korbit.tran.dao;

import java.util.List;

import korbit.tran.vo.ConfigVO;
import korbit.tran.vo.OrdersBuyVO;


public interface OrdersBuyDAO extends GenericDAO<OrdersBuyVO, String> {
	public List<OrdersBuyVO> getOrdersBuyList(OrdersBuyVO vo);
	public OrdersBuyVO getOrdersBuy(OrdersBuyVO vo);
	public void updateOrdersBuy(OrdersBuyVO vo);
	public void registerOrdersBuy(OrdersBuyVO vo);
}