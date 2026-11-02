package org.etlforge.integration.client.mapping.serverapi.xml;

import java.util.Collection;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.client.ServerFileFolder;

public final class StandardFolderFactory implements FolderFactory {
   @Override
   public boolean accepts(String var1) {
      return true;
   }

   @Override
   public ServerFileFolder createFolder(Collection<ServerFile> var1) {
      return new ServerFileFolder("XML Schemas & DTDs", "Schemas and DTDs used for standard XML transformations", var1);
   }
}
