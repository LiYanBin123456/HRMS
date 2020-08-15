package database;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class RecruitConnUtil {
    protected static DataSource ds = null;
    static {
        ds = new ComboPooledDataSource("myc3p0_recruit");
    }

    /**
     * 建立数据库连接,将数据库连接公有化,当要用到数据库连接时,就调这个方法,代码得到重用
     * @return 返回的就是数据库的连接对象
     */
    public static Connection getConnection() {
        //建立数据库连接
        Connection conn = null;
        try {
            conn = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeAutoCommit(Connection conn){
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void commit(Connection conn){
        try {
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rollback(Connection conn){
        try {
            conn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
