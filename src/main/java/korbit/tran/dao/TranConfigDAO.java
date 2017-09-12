package korbit.tran.dao;

import java.util.List;

import korbit.tran.vo.TranConfigVO;


public interface TranConfigDAO extends GenericDAO<TranConfigVO, String> {
	public List<TranConfigVO> getTranConfigList();
	public TranConfigVO getTranConfig(TranConfigVO vo);
	public void updateTranConfig(TranConfigVO vo);
}