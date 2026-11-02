package org.etlforge.integration.server.implementation.webservice.interchange.deploy.client;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface DeployInterchangeDataService extends Service {
   String getkonga_deploy_interchange_dataAddress();

   DeployInterchangeData getkonga_deploy_interchange_data() throws ServiceException;

   DeployInterchangeData getkonga_deploy_interchange_data(URL var1) throws ServiceException;
}
