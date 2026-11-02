package org.etlforge.integration.server.implementation.webservice.interchange.json.impl;

import java.io.File;
import java.util.Collections;
import org.etlforge.integration.client.mapping.serverapi.json.JsonInfoProvider;
import org.etlforge.integration.client.mapping.serverapi.xml.GetRootNamesCallback;
import org.etlforge.integration.client.mapping.serverapi.xml.GetXmlStructureFilesCallback;
import org.etlforge.integration.client.mapping.serverapi.xml.JsonFolderFactory;
import org.etlforge.integration.client.mapping.serverapi.xml.ServerFileFolderOrganizer;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.client.ServerFileFolder;
import org.etlforge.integration.filetransfer.serverapi.UploadServerFileCallback;
import org.etlforge.integration.server.implementation.webservice.interchange.xml.impl.XmlInfoProviderWsImpl;

public final class JsonInfoProviderWsImpl implements JsonInfoProvider {
   private final XmlInfoProviderWsImpl impl;

   public JsonInfoProviderWsImpl(CppWebServiceConfiguration var1) {
      ServerFileFolderOrganizer var2 = ServerFileFolderOrganizer.standalone(new JsonFolderFactory());
      this.impl = new XmlInfoProviderWsImpl(var1, var2);
   }

   @Override
   public void getXmlStructureFiles(GetXmlStructureFilesCallback var1) {
      this.impl.getXmlStructureFiles(var1);
   }

   @Override
   public void getPossibleRootNames(ServerFile var1, GetRootNamesCallback var2) {
      var2.rootNamesRetrieved(Collections.singletonList("json"));
   }

   @Override
   public void uploadFile(File var1, ServerFileFolder var2, String var3, UploadServerFileCallback var4) {
      this.impl.uploadFile(var1, var2, var3, var4);
   }
}
