package etlforge.crypto;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.spec.*;
import java.util.*;
public final class LegacyCryptoFixture {
 public static void main(String[] args) throws Exception {
  byte[] password="compatibility-pass".getBytes(StandardCharsets.UTF_8);
  byte[] salt="(..\n\t \n:".getBytes(StandardCharsets.UTF_8);
  byte[] material=new byte[32];byte[] previous=new byte[0];int position=0;
  while(position<material.length){MessageDigest md5=MessageDigest.getInstance("MD5");md5.update(previous);md5.update(password);md5.update(salt);previous=md5.digest();System.arraycopy(previous,0,material,position,16);position+=16;}
  Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");cipher.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(Arrays.copyOfRange(material,0,16),"AES"),new IvParameterSpec(Arrays.copyOfRange(material,16,32)));
  System.out.println(Base64.getEncoder().encodeToString(cipher.doFinal("ETLForge compatibility".getBytes(StandardCharsets.UTF_8))));
 }
}
