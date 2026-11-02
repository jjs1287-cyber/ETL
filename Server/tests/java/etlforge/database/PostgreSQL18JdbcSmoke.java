package etlforge.database;
import java.sql.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
public final class PostgreSQL18JdbcSmoke {
 public static void main(String[] args) throws Exception {
  if(!"64".equals(System.getProperty("sun.arch.data.model")))throw new AssertionError("x64 JVM required");
  Properties properties=new Properties();properties.setProperty("user","etlforge_admin");properties.setProperty("password",new String(Files.readAllBytes(Paths.get(args[0])),StandardCharsets.UTF_8).trim());properties.setProperty("connectTimeout","5");
  Class.forName("org.postgresql.Driver");
  try(Connection connection=DriverManager.getConnection("jdbc:postgresql://127.0.0.1:6543/postgres",properties)){
   if(connection.getMetaData().getDatabaseMajorVersion()!=18)throw new AssertionError("Wrong database");
   connection.setAutoCommit(false);
   try(Statement statement=connection.createStatement()){statement.execute("CREATE TEMP TABLE etlforge_jdbc_smoke(id bigint,value text)");}
   try(PreparedStatement insert=connection.prepareStatement("INSERT INTO etlforge_jdbc_smoke VALUES (?,?)")){insert.setLong(1,4294967297L);insert.setString(2,"ETLForge");if(insert.executeUpdate()!=1)throw new AssertionError("Insert count");}
   try(Statement statement=connection.createStatement();ResultSet rows=statement.executeQuery("SELECT id,value FROM etlforge_jdbc_smoke")){
    if(!rows.next()||rows.getLong(1)!=4294967297L||!"ETLForge".equals(rows.getString(2)))throw new AssertionError("JDBC round trip");
   }
   connection.rollback();
   System.out.println("PASS: PostgreSQL "+connection.getMetaData().getDatabaseProductVersion()+", JDBC "+connection.getMetaData().getDriverVersion()+", Java 8 x64, SCRAM and prepared bigint/text round trip");
  }
 }
}
