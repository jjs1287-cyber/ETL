package org.etlforge.integration.client.mapping.serverapi.xml;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import java.util.Collection;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.client.ServerFileFolder;
import org.etlforge.lang.KongaStringUtils;

public final class JsonFolderFactory implements FolderFactory {
   private final Function<ServerFile, ServerFile> displayNameTransform = new Function<ServerFile, ServerFile>() {
      public ServerFile apply(ServerFile var1) {
         String var2 = KongaStringUtils.substringAfterIfPresent(var1.getName(), "etlforge.json.");
         return new ServerFile.Builder(var1).withDisplayName(var2).build();
      }
   };

   @Override
   public boolean accepts(String var1) {
      return var1.startsWith("etlforge.json.");
   }

   @Override
   public ServerFileFolder createFolder(Collection<ServerFile> var1) {
      return new ServerFileFolder("JSON", "JSON Schemas", Iterables.transform(var1, this.displayNameTransform));
   }
}
