package org.etlforge.integration.client.mapping.serverapi.xml;

import java.util.Collection;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.client.ServerFileFolder;

public interface FolderFactory {
   boolean accepts(String var1);

   ServerFileFolder createFolder(Collection<ServerFile> var1);
}
