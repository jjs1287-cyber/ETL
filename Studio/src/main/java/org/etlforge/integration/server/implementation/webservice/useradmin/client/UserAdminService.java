package org.etlforge.integration.server.implementation.webservice.useradmin.client;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface UserAdminService extends Service {
   String getkonga_useradminAddress();

   UserAdmin getkonga_useradmin() throws ServiceException;

   UserAdmin getkonga_useradmin(URL var1) throws ServiceException;
}
