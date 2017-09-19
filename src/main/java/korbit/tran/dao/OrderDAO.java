package korbit.tran.dao;

import java.util.List;

import korbit.tran.vo.ConfigVO;
import korbit.tran.vo.OrderVO;


public interface OrderDAO extends GenericDAO<OrderVO, String> {
	public OrderVO get(OrderVO vo);
	public void update(OrderVO vo);
	public void register(OrderVO vo);
}