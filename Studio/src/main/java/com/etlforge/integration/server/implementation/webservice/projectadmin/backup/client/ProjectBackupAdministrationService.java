package com.etlforge.integration.server.implementation.webservice.projectadmin.backup.client;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface ProjectBackupAdministrationService extends Service {
   String getkonga_projectbackupadminAddress();

   ProjectBackupAdministration getkonga_projectbackupadmin() throws ServiceException;

   ProjectBackupAdministration getkonga_projectbackupadmin(URL var1) throws ServiceException;
}
