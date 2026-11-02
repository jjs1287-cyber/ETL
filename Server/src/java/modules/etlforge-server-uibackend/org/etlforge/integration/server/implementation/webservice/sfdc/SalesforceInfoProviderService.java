package org.etlforge.integration.server.implementation.webservice.sfdc;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface SalesforceInfoProviderService extends Service {
   String getkonga_salesforce_infoproviderAddress();

   SalesforceInfoProvider getkonga_salesforce_infoprovider() throws ServiceException;

   SalesforceInfoProvider getkonga_salesforce_infoprovider(URL var1) throws ServiceException;
}
