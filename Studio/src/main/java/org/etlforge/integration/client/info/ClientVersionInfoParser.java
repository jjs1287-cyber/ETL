package org.etlforge.integration.client.info;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.binary.Base64;
import org.etlforge.application.update.HttpVersionInfoSupplier;
import org.etlforge.application.update.VersionInfo;
import org.etlforge.crypto.Providers;
import org.etlforge.util.crypto.DefaultPBEStringCryptographer;
import org.etlforge.util.crypto.PBEStringCryptographer;
import org.etlforge.util.crypto.StringCryptographerException;
import org.etlforge.util.crypto.StringDecrypterException;

public final class ClientVersionInfoParser implements HttpVersionInfoSupplier.ResponseParser {
   private final HttpVersionInfoSupplier.ResponseParser clearTextParser;

   public ClientVersionInfoParser(HttpVersionInfoSupplier.ResponseParser var1) {
      this.clearTextParser = Preconditions.checkNotNull(var1);
   }

   @Override
   public VersionInfo parse(String var1) throws Exception {
      try {
         String var2 = this.decryptResponse(var1);
         return this.clearTextParser.parse(var2);
      } catch (Exception var4) {
         throw new Exception("The Update Service returned an unexpected response. Please contact support.", var4);
      }
   }

   private String decryptResponse(String var1) throws StringCryptographerException, StringDecrypterException {
      PBEStringCryptographer var2 = createCrypto();
      Base64 var3 = new Base64();
      byte[] var4 = var3.decode(getBytes(var1));
      return var2.decrypt(var4);
   }

   private static PBEStringCryptographer createCrypto() throws StringCryptographerException {
      byte[] var0 = getBytes(".>??+,+:");
      DefaultPBEStringCryptographer var1 = new DefaultPBEStringCryptographer(var0, 1);
      var1.setAlgorithm("PBEWITHMD5AND128BITAES-CBC-OPENSSL");
      var1.setPassphrase("R~RZ{10OzUDj4]qqLgq~^o=~Dt<EhG");
      return var1;
   }

   private static byte[] getBytes(String var0) {
      try {
         return var0.getBytes(Charsets.US_ASCII.name());
      } catch (UnsupportedEncodingException var2) {
         throw new RuntimeException(var2);
      }
   }

   @VisibleForTesting
   static String encrypt(String var0) throws Exception {
      PBEStringCryptographer var1 = createCrypto();
      byte[] var2 = var1.encrypt(var0);
      Base64 var3 = new Base64();
      return new String(var3.encode(var2), Charsets.US_ASCII);
   }

   static {
      Providers.registerDefaultProvider();
   }
}
