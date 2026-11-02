import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
public final class RestoreRegressionKeys {
 static ObjectMapper json=new ObjectMapper();static Path catalog=Paths.get("C:/ETL/Reports/regression-suite/catalog/Data");static int restored;
 static Document read(Path p)throws Exception{DocumentBuilderFactory f=DocumentBuilderFactory.newInstance();f.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);return f.newDocumentBuilder().parse(p.toFile());}
 static Map<String,String> props(Document d){Map<String,String> m=new HashMap<>();NodeList items=d.getElementsByTagName("Item");for(int n=0;n<items.getLength();n++){Element e=(Element)items.item(n);m.put(e.getAttribute("key"),e.getAttribute("value"));}return m;}
 static String file(Path p)throws Exception{return new String(Files.readAllBytes(p),StandardCharsets.UTF_8);}
 static void save(String id,String type,Path privateKey,Path publicKey,String pass)throws Exception{
  ObjectNode r=json.createObjectNode().put("action","ssh.save").put("id",id).put("type",type).put("name","Regression "+type+" "+id).put("passphrase",pass);
  if(privateKey!=null)r.put("privateKey",file(privateKey));if(publicKey!=null)r.put("publicKey",file(publicKey));SftpKeysLiveTest.request(r);restored++;
 }
 public static void main(String[]args)throws Exception{
  LocalVariablesRuntimeTest.init();SftpKeysLiveTest.hash=LocalVariablesRuntimeTest.hash;Map<Integer,String> ops=new HashMap<>();for(String line:Files.readAllLines(catalog.getParent().getParent().resolve("operations.txt"),StandardCharsets.UTF_8)){String[] p=line.split("\\|",5);ops.put(Integer.parseInt(p[0]),p[1]);}
  Path ssh=Paths.get("C:/ETL/Samples/SftpKeys"),pgp=Paths.get("C:/ETL/Samples/RegressionSuite/pgp-keys");if(!Files.isRegularFile(pgp.resolve("public.asc")))pgp=Paths.get(file(Paths.get("C:/ETL/Reports/pgp-fixture-path.txt")).trim());Set<String> done=new HashSet<>();
  for(int n:new int[]{168,169,173,174,184,185,188}){
   Document operation=read(catalog.resolve("Operation/"+ops.get(n)+".xml"));Element activities=(Element)operation.getElementsByTagName("Activities").item(0);
   boolean target=n==169||n==184;String kind=target?"TARGET":"SOURCE";Element activity=(Element)activities.getElementsByTagName(kind).item(0);String id=activity.getAttribute("contentId");Map<String,String> p=props(read(catalog.resolve((target?"Target/":"Source/")+id+".xml")));String key=p.get(n>=184?"pgp_key_id":"sftp_key_id");if(!done.add(key))continue;
   if(n>=184)save(key,"pgp",n==184?null:pgp.resolve("private.asc"),n==184?pgp.resolve("public.asc"):null,n==188?"incorrect":"fixture-pass");
   else {boolean encrypted=n==173||n==174;Path privateKey=ssh.resolve(encrypted?"test_ed25519_pass":"test_ed25519_plain");save(key,"ssh",privateKey,Paths.get(privateKey+".pub"),n==174?"incorrect-passphrase":encrypted?"fixture-passphrase":"");}
  }
  System.out.println("PASS restored "+restored+" persistent regression key records; no private material exported");
 }
}
