package org.etlforge.integration.server.backend.webservice.connector.client;

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

public class ETLForgeConnectorEngineServiceLocator extends Service implements ETLForgeConnectorEngineService {
   private String konga_connector_engine_address = "http://localhost/axis/konga_connector_engine";
   private String konga_connector_engineWSDDServiceName = "konga_connector_engine";
   private HashSet ports = null;

   public ETLForgeConnectorEngineServiceLocator() {
   }

   public ETLForgeConnectorEngineServiceLocator(EngineConfiguration var1) {
      super(var1);
   }

   public ETLForgeConnectorEngineServiceLocator(String var1, QName var2) throws ServiceException {
      super(var1, var2);
   }

   @Override
   public String getkonga_connector_engineAddress() {
      return this.konga_connector_engine_address;
   }

   public String getkonga_connector_engineWSDDServiceName() {
      return this.konga_connector_engineWSDDServiceName;
   }

   public void setkonga_connector_engineWSDDServiceName(String var1) {
      this.konga_connector_engineWSDDServiceName = var1;
   }

   @Override
   public ETLForgeConnectorEngine getkonga_connector_engine() throws ServiceException {
      URL var1;
      try {
         var1 = new URL(this.konga_connector_engine_address);
      } catch (MalformedURLException var3) {
         throw new ServiceException(var3);
      }

      return this.getkonga_connector_engine(var1);
   }

   @Override
   public ETLForgeConnectorEngine getkonga_connector_engine(URL var1) throws ServiceException {
      try {
         Konga_connector_engineSoapBindingStub var2 = new Konga_connector_engineSoapBindingStub(var1, this);
         var2.setPortName(this.getkonga_connector_engineWSDDServiceName());
         return var2;
      } catch (AxisFault var3) {
         return null;
      }
   }

   public void setkonga_connector_engineEndpointAddress(String var1) {
      this.konga_connector_engine_address = var1;
   }

   @Override
   public Remote getPort(Class var1) throws ServiceException {
      try {
         if (ETLForgeConnectorEngine.class.isAssignableFrom(var1)) {
            Konga_connector_engineSoapBindingStub var2 = new Konga_connector_engineSoapBindingStub(new URL(this.konga_connector_engine_address), this);
            var2.setPortName(this.getkonga_connector_engineWSDDServiceName());
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
      if ("konga_connector_engine".equals(var3)) {
         return this.getkonga_connector_engine();
      }

      Remote var4 = this.getPort(var2);
      ((Stub)var4).setPortName(var1);
      return var4;
   }

   @Override
   public QName getServiceName() {
      return new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ETLForgeConnectorEngineService");
   }

   @Override
   public Iterator getPorts() {
      if (this.ports == null) {
         this.ports = new HashSet();
         this.ports.add(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "konga_connector_engine"));
      }

      return this.ports.iterator();
   }

   public void setEndpointAddress(String var1, String var2) throws ServiceException {
      if ("konga_connector_engine".equals(var1)) {
         this.setkonga_connector_engineEndpointAddress(var2);
      } else {
         throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + var1);
      }
   }

   public void setEndpointAddress(QName var1, String var2) throws ServiceException {
      this.setEndpointAddress(var1.getLocalPart(), var2);
   }
}
