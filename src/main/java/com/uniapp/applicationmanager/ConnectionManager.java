package com.uniapp.applicationmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Manages connections to your MySQL instance.
 * <p>
 *   As written, this connection manager assumes that the database is running
 *   on the local machine and listening for connections on port 3306.  It also
 *   uses the database 'root' user to connect.  To modify any of these
 *   assumptions, edit the constant definitions in this class.
 * </p>
 */
public class ConnectionManager {

  /**
   * Private default constructor to prevent instantiation.
   */
  private ConnectionManager() { }

  private static final String USER = "root";
  private static final String HOSTNAME = "localhost";
  private static final int PORT = 3306;
  private static final String TIMEZONE = "UTC";

  private static final String MYSQL_PASSWORD_VARIABLE_NAME =
      "CS5200_MYSQL_PASSWORD";

  /**
   * Open a connection to the specified schema.
   * @param schemaName name of the schema to connect to
   * @return Connection object allowing database access
   * @throws SQLException if there is a connection error, or if the environment
   * variable whose name is given in <code>MYSQL_PASSWORD_VARIABLE_NAME</code>
   * is not defined.
   */
  public static Connection getConnection(String schemaName) throws SQLException {
    Properties connectionProperties = new Properties();
    connectionProperties.put("user", USER);
    connectionProperties.put("password", getMySQLPassword());
    connectionProperties.put("serverTimezone", TIMEZONE);
    return DriverManager.getConnection(
        String.format(
            "jdbc:mysql://%s:%d/%s?useSSL=false&allowPublicKeyRetrieval=true",
            HOSTNAME,
            PORT,
            schemaName
        ),
        connectionProperties
    );
  }

  /**
   * Open a schema-less connection to the database, for use when manipulating
   * entire schemas.
   * @throws SQLException if there is a connection error, or if the environment
   * variable whose name is given in <code>MYSQL_PASSWORD_VARIABLE_NAME</code>
   * is not defined.
   */
  public static Connection getSchemalessConnection() throws SQLException {
    Properties connectionProperties = new Properties();
    connectionProperties.put("user", USER);
    connectionProperties.put("password", getMySQLPassword());
    connectionProperties.put("serverTimezone", TIMEZONE);
    return DriverManager.getConnection(
        String.format(
            "jdbc:mysql://%s:%d?useSSL=false&allowPublicKeyRetrieval=true",
            HOSTNAME,
            PORT
        ),
        connectionProperties
    );
  }

  /**
   * Retrieve the MySQL password from the system environment, in the
   * variable whose name is stored in
   * <code>MYSQL_PASSWORD_VARIABLE_NAME</code>
   * @throws SQLException if the environment variable is not defined
   */
  private static String getMySQLPassword() throws SQLException {
    String result = System.getenv(MYSQL_PASSWORD_VARIABLE_NAME);
    if (result == null) {
      throw new SQLException(
          String.format(
              "Environment variable %s not set; must contain MySQL password",
              MYSQL_PASSWORD_VARIABLE_NAME
          )
      );
    }
    return result;
  }
}
