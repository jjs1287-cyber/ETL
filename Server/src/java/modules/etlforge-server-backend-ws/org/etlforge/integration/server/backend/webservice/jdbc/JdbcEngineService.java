package org.etlforge.integration.server.backend.webservice.jdbc;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface JdbcEngineService extends Service {
   String getkonga_jdbc_engineAddress();

   JdbcEngine getkonga_jdbc_engine() throws ServiceException;

   JdbcEngine getkonga_jdbc_engine(URL var1) throws ServiceException;
}
