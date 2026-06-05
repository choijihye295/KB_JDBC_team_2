package org.team2.jdbc_team.dao;
import org.team2.jdbc_team.domain.TransactionVO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface TransactionDao {
    //전체 거래 내역 조회용 메서드
    List<TransactionVO> getTransactionsByMember(int memberNo) throws SQLException;
    //상세 거래 내역 검색용 메서드
    List<TransactionVO> searchTransactions(int memberNo, Map<String, String> searchParams) throws SQLException;

}
