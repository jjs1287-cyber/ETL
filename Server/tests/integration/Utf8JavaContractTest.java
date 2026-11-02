import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.security.MessageDigest;
import java.util.*;
import java.util.zip.GZIPOutputStream;
import java.sql.*;
import org.etlforge.util.csv.FileContents;
import org.etlforge.util.crypto.DigestUtils;
import org.etlforge.integration.structure.mapping.MappingTreeStructureFactory;
import org.etlforge.integration.structure.reader.FixFieldParser;
import org.etlforge.integration.server.BackendEncoding;
import org.etlforge.integration.server.backend.webservice.jdbc.WsDbLookupParams;

public final class Utf8JavaContractTest {
 static int checks;static void require(boolean condition){if(!condition)throw new AssertionError("UTF8 Java check "+(checks+1)+" failed");checks++;}
 public static void main(String[] args)throws Exception {
  String text="A\u00e9\ud83d\ude00Z",line=text+"\n";Path file=Files.createTempFile("etlforge-utf8-", ".txt"),zip=Files.createTempFile("etlforge-utf8-", ".gz");
  try {
   Files.write(file,line.getBytes(StandardCharsets.UTF_8));
   try(FileContents contents=new FileContents(file.toFile())){require(contents.getStr(0,text.getBytes(StandardCharsets.UTF_8).length).equals(text));}
   try(FileContents contents=new FileContents(file.toFile())){FixFieldParser parser=new FixFieldParser(contents,1,2,false);parser.readRecord(0);require(parser.getIdentifier().equals("\u00e9\ud83d\ude00"));}
   try(GZIPOutputStream gzip=new GZIPOutputStream(Files.newOutputStream(zip))){gzip.write(line.getBytes(StandardCharsets.UTF_8));}
   try(BufferedReader reader=new BufferedReader(MappingTreeStructureFactory.fromFile(zip.toFile()).getReader())){require(reader.readLine().equals(text));}
   require(Arrays.equals(DigestUtils.getMd5Digest(text),MessageDigest.getInstance("MD5").digest(text.getBytes(StandardCharsets.UTF_8))));
   {
    require(BackendEncoding.AXIS_CHARSET==BackendEncoding.CHARSET);
    WsDbLookupParams sql=new WsDbLookupParams();sql.setSql("select '"+text+"'");Class<?> helper=Class.forName("org.etlforge.integration.server.backend.webservice.jdbc.JdbcEngineUtils");java.lang.reflect.Method method=helper.getDeclaredMethod("getSqlStatement",WsDbLookupParams.class);method.setAccessible(true);require(method.invoke(null,sql).equals(sql.getSql()));
    Class.forName("org.postgresql.Driver");String[] credential=null;for(String value:Files.readAllLines(Paths.get("C:/ETL/Server/private/pgbouncer-dev.pgpass"),StandardCharsets.UTF_8)){if(value.contains(":etlforge_dev:")){credential=value.split(":",-1);break;}}
    if(credential==null)throw new AssertionError("Missing local database test credentials");
    try(Connection connection=DriverManager.getConnection("jdbc:postgresql://127.0.0.1:6543/etlforge_dev_tran","etlforge_dev",credential[credential.length-1]);PreparedStatement statement=connection.prepareStatement("select ?::text")){statement.setString(1,text);try(ResultSet result=statement.executeQuery()){require(result.next()&&result.getString(1).equals(text));}}
    Arrays.fill(credential,null);
   }
   System.out.println("PASS "+checks+" Java UTF8 checks with JVM default "+Charset.defaultCharset().name()+".");
  } finally {Files.deleteIfExists(file);Files.deleteIfExists(zip);}
 }
}
