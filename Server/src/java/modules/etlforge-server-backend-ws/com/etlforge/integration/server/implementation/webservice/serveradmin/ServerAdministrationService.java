package com.etlforge.integration.server.implementation.webservice.serveradmin;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface ServerAdministrationService extends Service {
   String getkonga_serveradminAddress();

   ServerAdministration getkonga_serveradmin() throws ServiceException;

   ServerAdministration getkonga_serveradmin(URL var1) throws ServiceException;
}
