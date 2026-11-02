package org.etlforge.integration.server.engine.connector.netsuite.wsdl;

import java.io.File;
import java.util.Map.Entry;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;

public class NetSuiteWsdlContentsHasherWriter {
   private final File hashFile;
   private final NetSuiteWsdlContentsHasher hasher;

   public NetSuiteWsdlContentsHasherWriter(String var1, NetSuiteWsdlContentsHasher var2) {
      this.hashFile = new File(NetSuiteWsdlFile.getNetSuiteWsdlCacheFolder(), var1);
      this.hasher = var2;
   }

   void write() {
      ConnectorExecutionSystem.createParentDirs(this.hashFile);
      ConnectorExecutionSystem.writeFileUTF8(this.hashFile, this.hashString());
   }

   String hashString() {
      StringBuilder var1 = new StringBuilder();
      XmlElementUtils.startElementTag(var1, "wsdlhash");
      XmlElementUtils.elementValue(var1, "wsdlkey", this.hasher.netsuiteWsdlKey);
      XmlElementUtils.startElementTag(var1, "wsdlrows");

      for (Entry var3 : this.hasher.md5.entrySet()) {
         XmlElementUtils.startElementTag(var1, "wsdlrow");
         XmlElementUtils.elementValue(var1, "name", (String)var3.getKey());
         XmlElementUtils.elementValue(var1, "hash", (String)var3.getValue());
         XmlElementUtils.endElementTag(var1, "wsdlrow");
      }

      XmlElementUtils.endElementTag(var1, "wsdlrows");
      XmlElementUtils.endElementTag(var1, "wsdlhash");
      return var1.toString();
   }
}
