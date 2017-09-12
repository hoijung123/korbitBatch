package korbit.tran.dao;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import korbit.tran.vo.TranConfigVO;


@Repository
public class TranConfigDAOImpl extends GenericDAOImpl<TranConfigVO, String> implements TranConfigDAO {
	@Inject
	private SqlSession sqlSession;
	
	private static final String namespace = 
			"com.mycom.myapp.dao.DbMapper";
	@Override
	public List<TranConfigVO> getTranConfigList() {
		// TODO Auto-generated method stub
		return sqlSession.selectList(namespace + ".getTranConfigList");
	}
	@Override
	public TranConfigVO getTranConfig(TranConfigVO vo) {
		// TODO Auto-generated method stub
		return sqlSession.selectOne(namespace + ".getTranConfig", vo);
	}
	@Override
	public void updateTranConfig(TranConfigVO vo) {
		// TODO Auto-generated method stub
		sqlSession.update(namespace + ".updateTranConfig", vo);
	}
}
