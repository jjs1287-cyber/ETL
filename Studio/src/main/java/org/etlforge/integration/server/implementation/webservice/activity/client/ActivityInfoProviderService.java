package org.etlforge.integration.server.implementation.webservice.activity.client;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface ActivityInfoProviderService extends Service {
   String getkonga_activity_infoproviderAddress();

   ActivityInfoProvider getkonga_activity_infoprovider() throws ServiceException;

   ActivityInfoProvider getkonga_activity_infoprovider(URL var1) throws ServiceException;
}
