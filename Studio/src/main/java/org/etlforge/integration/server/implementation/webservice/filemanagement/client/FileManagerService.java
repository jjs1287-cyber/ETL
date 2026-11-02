package org.etlforge.integration.server.implementation.webservice.filemanagement.client;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface FileManagerService extends Service {
   String getkonga_file_managementAddress();

   FileManager getkonga_file_management() throws ServiceException;

   FileManager getkonga_file_management(URL var1) throws ServiceException;
}
