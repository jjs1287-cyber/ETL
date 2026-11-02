package org.etlforge.integration.server.engine.connector.netsuite.wsdl;

import com.google.common.base.Strings;
import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.integration.server.netsuite.wsdl.NetSuiteWsdlContents;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.etlforge.xml.sax.StartElementHandler;
import org.xml.sax.SAXException;

public class NetSuiteWsdlContentsHasherReader {
   private final File hashFile;

   public NetSuiteWsdlContentsHasherReader(String var1) {
      this.hashFile = new File(NetSuiteWsdlFile.getNetSuiteWsdlCacheFolder(), var1);
   }

   public final File getHashFile() {
      return this.hashFile;
   }

   public static NetSuiteWsdlContentsHasher readFromContents(NetSuiteWsdlContents var0) {
      NetSuiteWsdlContentsHasher var1 = new NetSuiteWsdlContentsHasher();
      var1.netsuiteWsdlKey = var0.getNetsuiteWsdlKey();

      for (Entry var3 : var0.getWsdlContents().entrySet()) {
         String var4 = var0.getXsdSchema((String)var3.getKey());
         if (Strings.isNullOrEmpty(var4)) {
            return new NetSuiteWsdlContentsHasher();
         }

         var1.put((String)var3.getKey(), var4);
      }

      return var1;
   }

   public NetSuiteWsdlContentsHasher read() {
      try {
         return this.parse();
      } catch (SAXException var2) {
         var2.printStackTrace();
         throw new ConnectorServerRuntimeException(var2);
      } catch (IOException var3) {
         var3.printStackTrace();
         throw new ConnectorServerRuntimeException(var3);
      }
   }

   private NetSuiteWsdlContentsHasher parse() throws SAXException, IOException {
      NetSuiteWsdlContentsHasherReader.Handler var1 = new NetSuiteWsdlContentsHasherReader.Handler();
      if (!this.hashFile.canRead()) {
         return var1.hasher;
      }

      KongaSaxParser var2 = new KongaSaxParser();
      var2.registerHandler(var1);
      var2.parseFile(this.hashFile);
      return var1.hasher;
   }

   @RelativePathRoot("wsdlhash")
   private static class Handler {
      private final NetSuiteWsdlContentsHasher hasher = new NetSuiteWsdlContentsHasher();
      private String key;
      private String md5Content;

      private Handler() {
      }

      @EndElementHandler("wsdlkey")
      public void wsdlkey(String var1) {
         this.hasher.netsuiteWsdlKey = var1;
      }

      @StartElementHandler("wsdlrows/wsdlrow")
      public void wsdlrowStart() {
         this.key = this.md5Content = null;
      }

      @EndElementHandler("wsdlrows/wsdlrow")
      public void wsdlrowEnd() {
         this.hasher.putMd5(this.key, this.md5Content);
      }

      @EndElementHandler("wsdlrows/wsdlrow/name")
      public void key(String var1) {
         this.key = var1;
      }

      @EndElementHandler("wsdlrows/wsdlrow/hash")
      public void md5Content(String var1) {
         this.md5Content = var1;
      }
   }
}
