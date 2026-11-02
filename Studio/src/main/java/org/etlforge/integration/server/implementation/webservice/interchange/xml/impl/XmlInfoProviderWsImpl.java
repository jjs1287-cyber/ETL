package org.etlforge.integration.server.implementation.webservice.interchange.xml.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.xml.rpc.ServiceException;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.client.mapping.serverapi.xml.GetRootNamesCallback;
import org.etlforge.integration.client.mapping.serverapi.xml.GetXmlStructureFilesCallback;
import org.etlforge.integration.client.mapping.serverapi.xml.ServerFileFolderOrganizer;
import org.etlforge.integration.client.mapping.serverapi.xml.XmlInfoProvider;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.CppWebServiceServerCall;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.client.ServerFileFolder;
import org.etlforge.integration.filetransfer.client.ServerFileUploadResult;
import org.etlforge.integration.filetransfer.serverapi.UploadServerFileCallback;
import org.etlforge.integration.server.implementation.webservice.interchange.xml.client.CXmlStructureFile;
import org.etlforge.integration.server.implementation.webservice.interchange.xml.client.CXmlStructureFileUploadResult;
import org.etlforge.integration.server.implementation.webservice.interchange.xml.client.XmlInfoProviderServiceLocator;
import org.etlforge.util.crypto.DigestUtils;
import org.etlforge.util.zip.ZipUtils;

public final class XmlInfoProviderWsImpl extends CppWebServiceServerCall implements XmlInfoProvider {
   private static final String SERVICE_NAME = "konga_xml_infoprovider";
   private final ServerFileFolderOrganizer folderOrganizer;

   public XmlInfoProviderWsImpl(CppWebServiceConfiguration var1) {
      this(var1, ServerFileFolderOrganizer.defaultOrganizer());
   }

   public XmlInfoProviderWsImpl(CppWebServiceConfiguration var1, ServerFileFolderOrganizer var2) {
      super(var1);
      this.folderOrganizer = Preconditions.checkNotNull(var2);
   }

   @Override
   public void getXmlStructureFiles(GetXmlStructureFilesCallback var1) {
      try {
         org.etlforge.integration.server.implementation.webservice.interchange.xml.client.XmlInfoProvider var2 = this.getXmlInfoProvider(var1);
         if (var2 == null) {
            return;
         }

         ServerInfo var3 = this.getConfiguration().getServerInfo();
         CXmlStructureFile[] var4 = var2.getXmlStructureFiles(this.getConfiguration().getUserName(), this.getConfiguration().getPasswordHash());
         ArrayList var5 = Lists.newArrayList();
         if (var4 != null) {
            for (CXmlStructureFile var9 : var4) {
               String[] var10 = var9.getPath().split("/", 2);
               String var11 = var10[1];
               if (!var11.startsWith("etlforge.soap.")) {
                  ServerFile var12 = ServerFile.builder(var11).withHash(var9.getMD5()).build();
                  var5.add(var12);
               }
            }
         }

         Collection var15 = this.folderOrganizer.organizeFiles(var5);
         var1.structureFilesRetrieved(var3, var15);
      } catch (RemoteException var13) {
         var1.caught(this.convert(var13));
      } catch (IntegrationServerException var14) {
         var1.caught(new IntegrationServerException("Server exception: " + var14.getMessage(), var14));
      }
   }

   private org.etlforge.integration.server.implementation.webservice.interchange.xml.client.XmlInfoProvider getXmlInfoProvider(ServerCallback var1) throws IntegrationServerException {
      XmlInfoProviderServiceLocator var2 = new XmlInfoProviderServiceLocator();

      try {
         StringBuilder var3 = new StringBuilder();
         if (!this.getConfiguration().configureWebservice(var2, "konga_xml_infoprovider", var3, var1)) {
            return null;
         }

         var2.setkonga_xml_infoproviderEndpointAddress(var3.toString());
         return var2.getkonga_xml_infoprovider();
      } catch (ServiceException var4) {
         throw new IntegrationServerException("Failed to call web service: " + var4.getMessage(), var4);
      }
   }

   @Override
   public void getPossibleRootNames(ServerFile var1, GetRootNamesCallback var2) {
      try {
         org.etlforge.integration.server.implementation.webservice.interchange.xml.client.XmlInfoProvider var3 = this.getXmlInfoProvider(var2);
         if (var3 == null) {
            return;
         }

         CXmlStructureFile var4 = new CXmlStructureFile();
         var4.setPath(var1.getFilePath());
         var4.setMD5(var1.getMD5());
         String[] var5 = var3.getPossibleRootNames(this.getConfiguration().getUserName(), this.getConfiguration().getPasswordHash(), var4);
         var2.rootNamesRetrieved(Arrays.asList(var5));
      } catch (RemoteException var6) {
         var2.caught(this.convert(var6));
      } catch (IntegrationServerException var7) {
         var2.caught(new IntegrationServerException("Server exception: " + var7.getMessage(), var7));
      }
   }

   @Override
   public void uploadFile(File var1, ServerFileFolder var2, String var3, UploadServerFileCallback var4) {
      try {
         org.etlforge.integration.server.implementation.webservice.interchange.xml.client.XmlInfoProvider var5 = this.getXmlInfoProvider(var4);
         if (var5 == null) {
            return;
         }

         ServerInfo var6 = new ServerInfo(this.getConfiguration().getServerId(), this.getConfiguration().getServerName());
         String var7 = ZipUtils.deflateAndBase64EncodeFileContents(var1);
         String var8 = var1.getName();
         long var9 = var1.lastModified();
         CXmlStructureFile var11 = new CXmlStructureFile();
         String var12 = DigestUtils.getMd5FileDigestString(var1);
         var11.setMD5(var12);
         var11.setModifiedTime(var9);
         var11.setPath(var8);
         var11.setSchemaType(0);
         var11.setZippedBase64Contents(var7);
         CXmlStructureFile var13 = null;
         if (StringUtils.isNotEmpty(var3)) {
            var13 = new CXmlStructureFile();
            var13.setPath(var3);
         }

         CXmlStructureFileUploadResult var14 = var5.uploadStructureFile(
            this.getConfiguration().getUserName(), this.getConfiguration().getPasswordHash(), var11, var13
         );
         Object var25 = null;
         Object var26 = null;
         CXmlStructureFile var15 = var14.getXmlStructureFile();
         ServerFile var16 = ServerFile.builder(var15.getPath()).withHash(var12).build();
         ArrayList var17 = Lists.newArrayList();
         if (var14.getExistingDependencies() != null) {
            for (CXmlStructureFile var21 : var14.getExistingDependencies()) {
               var17.add(ServerFile.builder(var21.getPath()).withHash(var21.getMD5()).build());
            }
         }

         ServerFileUploadResult var27 = new ServerFileUploadResult(var6, var16, var14.getMissingStructureFiles(), var17);
         var4.fileWasUploaded(var27);
      } catch (RemoteException var22) {
         var4.caught(this.convert(var22));
      } catch (IntegrationServerException var23) {
         var4.caught(new IntegrationServerException("WebService failed. " + var23.getMessage(), var23));
      } catch (IOException var24) {
         var4.caught(new IntegrationServerException("Failed to read from input file. " + var24.getMessage(), var24));
      }
   }
}
