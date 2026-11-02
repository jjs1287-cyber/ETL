package org.etlforge.integration.server.implementation.webservice.interchange.db.client;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface OdbcInfoProviderService extends Service {
   String getkonga_odbc_infoproviderAddress();

   OdbcInfoProvider getkonga_odbc_infoprovider() throws ServiceException;

   OdbcInfoProvider getkonga_odbc_infoprovider(URL var1) throws ServiceException;
}
