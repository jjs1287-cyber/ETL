package com.etlforge.integration.server.implementation.webservice.bulkloadinfo.client;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface BulkLoadInfoProviderService extends Service {
   String getkonga_bulkload_infoproviderAddress();

   BulkLoadInfoProvider getkonga_bulkload_infoprovider() throws ServiceException;

   BulkLoadInfoProvider getkonga_bulkload_infoprovider(URL var1) throws ServiceException;
}
