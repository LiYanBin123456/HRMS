package database;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnUtil {
    protected static ComboPooledDataSource ds = null;
    static {
        try {
            ds = new ComboPooledDataSource();
            ds.setDriverClass("com.mysql.jdbc.Driver");
            //ds.setJdbcUrl("jdbc:mysql://39.103.135.238:3306/hrms?useUnicode=true&characterEncoding=UTF8");
            ds.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/hrms?useUnicode=true&characterEncoding=UTF8");
            ds.setUser("root");
            ds.setPassword("FB9CA5C6BC44BCB5B45DE45E504052A1");
            ds.setAcquireIncrement(5);
            ds.setInitialPoolSize(20);
            ds.setMaxPoolSize(40);
            ds.setMaxStatementsPerConnection(5);
            ds.setIdleConnectionTestPeriod(18000);
            //MySQL连接默认最大空闲时间为8小时，超过8小时，MySQL连接实际上已经无效了，但连接池并不知道，使用无效的连接就会出现异常
            ds.setMaxIdleTime(25000);
            ds.setTestConnectionOnCheckin(true);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
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
