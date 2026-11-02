package org.etlforge.integration.client.connector.serverapi;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import java.util.Collection;
import org.etlforge.integration.client.mapping.serverapi.xml.FolderFactory;
import org.etlforge.integration.data.connector.XsdNamingStrategy;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.client.ServerFileFolder;

public final class SapServerFileFolder implements FolderFactory {
   private final XsdNamingStrategy namingStrategy = new XsdNamingStrategy("SAP", "");
   private final Function<ServerFile, ServerFile> displayNameTransform = new Function<ServerFile, ServerFile>() {
      public ServerFile apply(ServerFile var1) {
         String var2 = SapServerFileFolder.this.namingStrategy.getDisplayName(var1.getFilePath());
         return new ServerFile.Builder(var1).withDisplayName(var2).build();
      }
   };

   @Override
   public boolean accepts(String var1) {
      return this.namingStrategy.isRecognizedFile(var1);
   }

   @Override
   public ServerFileFolder createFolder(Collection<ServerFile> var1) {
      return new ServerFileFolder("SAP Schemas", "Schemas for the XML input and output when calling SAP", Iterables.transform(var1, this.displayNameTransform));
   }
}
