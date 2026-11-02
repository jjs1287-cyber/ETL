package org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface JobControlService extends Service {
   String getkonga_jobcontrolAddress();

   JobControl getkonga_jobcontrol() throws ServiceException;

   JobControl getkonga_jobcontrol(URL var1) throws ServiceException;
}
