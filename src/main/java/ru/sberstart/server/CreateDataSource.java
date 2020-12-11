package ru.sberstart.server;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.h2.tools.RunScript;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;

public class CreateDataSource {

    private static DataSource dataSource;

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static DataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        HikariDataSource ds;

        config.setJdbcUrl("jdbc:h2:~/Bank_api");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);

        try {
            RunScript.execute(ds.getConnection(), new FileReader("src/main/resources/schema.sql"));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException exception) {
            System.out.println(exception.getMessage());
        }

        dataSource = ds;
        return ds;
    }
}
