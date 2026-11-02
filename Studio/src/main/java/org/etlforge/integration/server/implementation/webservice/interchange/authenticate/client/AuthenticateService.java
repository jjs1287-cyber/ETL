package org.etlforge.integration.server.implementation.webservice.interchange.authenticate.client;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface AuthenticateService extends Service {
   String getkonga_authenticateAddress();

   Authenticate getkonga_authenticate() throws ServiceException;

   Authenticate getkonga_authenticate(URL var1) throws ServiceException;
}
