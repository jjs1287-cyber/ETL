package org.etlforge.integration.server.implementation.webservice.projectadmin.client;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface ProjectAdministrationService extends Service {
   String getkonga_project_administrationAddress();

   ProjectAdministration getkonga_project_administration() throws ServiceException;

   ProjectAdministration getkonga_project_administration(URL var1) throws ServiceException;
}
