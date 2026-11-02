package org.etlforge.integration.server.backend.webservice.salesforce;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface SalesforceEngineService extends Service {
   String getkonga_salesforce_engineAddress();

   SalesforceEngine getkonga_salesforce_engine() throws ServiceException;

   SalesforceEngine getkonga_salesforce_engine(URL var1) throws ServiceException;
}
