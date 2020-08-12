package dao.finance;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;

public class FinanceDao {
    //到账确认
    private String arrive(Connection conn, float balance, long id) {
        return null;
    }

    //获取资金往来明细
    private String getTransactions(Connection conn, HttpServletRequest request) {
        return null;
    }

    //插入资金往来明细
    private String insertTransactions(){
        return null;
    }
}
