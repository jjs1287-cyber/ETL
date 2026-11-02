package org.etlforge.integration.server.implementation.webservice.admin.client;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface AdminService extends Service {
   String getkonga_adminAddress();

   Admin getkonga_admin() throws ServiceException;

   Admin getkonga_admin(URL var1) throws ServiceException;
}
