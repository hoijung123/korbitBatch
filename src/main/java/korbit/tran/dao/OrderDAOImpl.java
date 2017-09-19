package korbit.tran.dao;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import korbit.tran.vo.ConfigVO;
import korbit.tran.vo.OrderVO;
import korbit.tran.vo.TranConfigVO;


@Repository
public class OrderDAOImpl extends GenericDAOImpl<OrderVO, String> implements OrderDAO {
	@Inject
	private SqlSession sqlSession;
	
	private static final String namespace = 
			"korbit.dao.DbMapper.Order";

	@Override
	public OrderVO get(OrderVO vo) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne(namespace + ".get", vo);
	}

	@Override
	public void update(OrderVO vo) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<OrderVO> getList() {
		return sqlSession.selectList(namespace + ".getList");
	}	
	
	@Override
	public void register(OrderVO vo) {
		sqlSession.insert(namespace + ".register", vo);
	}	

}
