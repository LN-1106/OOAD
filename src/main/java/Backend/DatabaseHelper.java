package Backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.InputStream;

public class DatabaseHelper {

    private static String DB_URL;
    private static String USER;
    private static String PASS;

    static {
        try (
                InputStream is = DatabaseHelper.class
                        .getClassLoader()
                        .getResourceAsStream("db.properties")) {
            Properties props = new Properties();
            props.load(is);

            DB_URL = props.getProperty("db.url");
            USER = props.getProperty("db.user");
            PASS = props.getProperty("db.pass");

        } catch (Exception e) {
            System.out.println("Không đọc được file db.properties");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, USER, PASS);

        } catch (Exception e) {
            System.out.println("Lỗi kết nối Database!");
            e.printStackTrace();
            return null;
        }
    }
}
