package com.etlforge.integration.salesforce.wsdl;

import java.util.List;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.filestore.FileStoreException;
import org.etlforge.integration.salesforce.LoginCredentials;

public interface KnownWsdl {
   String getName();

   void addToProjectFiles(FileStore var1) throws FileStoreException;

   void uploadToServer();

   List<LoginCredentials> getListOfCredentials();
}
