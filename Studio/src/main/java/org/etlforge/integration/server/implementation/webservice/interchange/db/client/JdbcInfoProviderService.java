package org.etlforge.integration.server.implementation.webservice.interchange.db.client;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface JdbcInfoProviderService extends Service {
   String getkonga_jdbc_infoproviderAddress();

   JdbcInfoProvider getkonga_jdbc_infoprovider() throws ServiceException;

   JdbcInfoProvider getkonga_jdbc_infoprovider(URL var1) throws ServiceException;
}
