package org.etlforge.integration.server.implementation.webservice.interchange.deploy.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.util.HashSet;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import org.apache.axis.AxisFault;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;

public class DeployInterchangeDataServiceLocator extends Service implements DeployInterchangeDataService {
   private String konga_deploy_interchange_data_address = "http://localhost/axis/konga_deploy_interchange_data";
   private String konga_deploy_interchange_dataWSDDServiceName = "konga_deploy_interchange_data";
   private HashSet ports = null;

   public DeployInterchangeDataServiceLocator() {
   }

   public DeployInterchangeDataServiceLocator(EngineConfiguration var1) {
      super(var1);
   }

   public DeployInterchangeDataServiceLocator(String var1, QName var2) throws ServiceException {
      super(var1, var2);
   }

   @Override
   public String getkonga_deploy_interchange_dataAddress() {
      return this.konga_deploy_interchange_data_address;
   }

   public String getkonga_deploy_interchange_dataWSDDServiceName() {
      return this.konga_deploy_interchange_dataWSDDServiceName;
   }

   public void setkonga_deploy_interchange_dataWSDDServiceName(String var1) {
      this.konga_deploy_interchange_dataWSDDServiceName = var1;
   }

   @Override
   public DeployInterchangeData getkonga_deploy_interchange_data() throws ServiceException {
      URL var1;
      try {
         var1 = new URL(this.konga_deploy_interchange_data_address);
      } catch (MalformedURLException var3) {
         throw new ServiceException(var3);
      }

      return this.getkonga_deploy_interchange_data(var1);
   }

   @Override
   public DeployInterchangeData getkonga_deploy_interchange_data(URL var1) throws ServiceException {
      try {
         Konga_deploy_interchange_dataSoapBindingStub var2 = new Konga_deploy_interchange_dataSoapBindingStub(var1, this);
         var2.setPortName(this.getkonga_deploy_interchange_dataWSDDServiceName());
         return var2;
      } catch (AxisFault var3) {
         return null;
      }
   }

   public void setkonga_deploy_interchange_dataEndpointAddress(String var1) {
      this.konga_deploy_interchange_data_address = var1;
   }

   @Override
   public Remote getPort(Class var1) throws ServiceException {
      try {
         if (DeployInterchangeData.class.isAssignableFrom(var1)) {
            Konga_deploy_interchange_dataSoapBindingStub var2 = new Konga_deploy_interchange_dataSoapBindingStub(
               new URL(this.konga_deploy_interchange_data_address), this
            );
            var2.setPortName(this.getkonga_deploy_interchange_dataWSDDServiceName());
            return var2;
         }
      } catch (Throwable var3) {
         throw new ServiceException(var3);
      }

      throw new ServiceException("There is no stub implementation for the interface:  " + (var1 == null ? "null" : var1.getName()));
   }

   @Override
   public Remote getPort(QName var1, Class var2) throws ServiceException {
      if (var1 == null) {
         return this.getPort(var2);
      }

      String var3 = var1.getLocalPart();
      if ("konga_deploy_interchange_data".equals(var3)) {
         return this.getkonga_deploy_interchange_data();
      }

      Remote var4 = this.getPort(var2);
      ((Stub)var4).setPortName(var1);
      return var4;
   }

   @Override
   public QName getServiceName() {
      return new QName(
         "urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "DeployInterchangeDataService"
      );
   }

   @Override
   public Iterator getPorts() {
      if (this.ports == null) {
         this.ports = new HashSet();
         this.ports
            .add(
               new QName(
                  "urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "konga_deploy_interchange_data"
               )
            );
      }

      return this.ports.iterator();
   }

   public void setEndpointAddress(String var1, String var2) throws ServiceException {
      if ("konga_deploy_interchange_data".equals(var1)) {
         this.setkonga_deploy_interchange_dataEndpointAddress(var2);
      } else {
         throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + var1);
      }
   }

   public void setEndpointAddress(QName var1, String var2) throws ServiceException {
      this.setEndpointAddress(var1.getLocalPart(), var2);
   }
}
