package org.etlforge.integration.server.implementation.webservice.interchange.webservice.client;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface WebServiceInfoProviderService extends Service {
   String getkonga_webservice_infoproviderAddress();

   WebServiceInfoProvider getkonga_webservice_infoprovider() throws ServiceException;

   WebServiceInfoProvider getkonga_webservice_infoprovider(URL var1) throws ServiceException;
}
