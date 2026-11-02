package com.etlforge.integration.server.implementation.webservice.bulkload;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface BulkLoadEngineService extends Service {
   String getkonga_bulkload_engineAddress();

   BulkLoadEngine getkonga_bulkload_engine() throws ServiceException;

   BulkLoadEngine getkonga_bulkload_engine(URL var1) throws ServiceException;
}
