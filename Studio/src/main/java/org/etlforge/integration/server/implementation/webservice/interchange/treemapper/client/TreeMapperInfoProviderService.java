package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface TreeMapperInfoProviderService extends Service {
   String getkonga_treemapper_infoproviderAddress();

   TreeMapperInfoProvider getkonga_treemapper_infoprovider() throws ServiceException;

   TreeMapperInfoProvider getkonga_treemapper_infoprovider(URL var1) throws ServiceException;
}
