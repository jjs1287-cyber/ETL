package org.etlforge.integration.server.implementation.webservice.interchange.connection.client;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface TestConnectionService extends Service {
   String getkonga_test_connectionAddress();

   TestConnection getkonga_test_connection() throws ServiceException;

   TestConnection getkonga_test_connection(URL var1) throws ServiceException;
}
