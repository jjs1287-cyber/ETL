package com.etlforge.integration.server.implementation.webservice.serveradmin;

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

public class ServerAdministrationServiceLocator extends Service implements ServerAdministrationService {
   private String konga_serveradmin_address = "http://localhost/axis/konga_serveradmin";
   private String konga_serveradminWSDDServiceName = "konga_serveradmin";
   private HashSet ports = null;

   public ServerAdministrationServiceLocator() {
   }

   public ServerAdministrationServiceLocator(EngineConfiguration var1) {
      super(var1);
   }

   public ServerAdministrationServiceLocator(String var1, QName var2) throws ServiceException {
      super(var1, var2);
   }

   @Override
   public String getkonga_serveradminAddress() {
      return this.konga_serveradmin_address;
   }

   public String getkonga_serveradminWSDDServiceName() {
      return this.konga_serveradminWSDDServiceName;
   }

   public void setkonga_serveradminWSDDServiceName(String var1) {
      this.konga_serveradminWSDDServiceName = var1;
   }

   @Override
   public ServerAdministration getkonga_serveradmin() throws ServiceException {
      URL var1;
      try {
         var1 = new URL(this.konga_serveradmin_address);
      } catch (MalformedURLException var3) {
         throw new ServiceException(var3);
      }

      return this.getkonga_serveradmin(var1);
   }

   @Override
   public ServerAdministration getkonga_serveradmin(URL var1) throws ServiceException {
      try {
         Konga_serveradminSoapBindingStub var2 = new Konga_serveradminSoapBindingStub(var1, this);
         var2.setPortName(this.getkonga_serveradminWSDDServiceName());
         return var2;
      } catch (AxisFault var3) {
         return null;
      }
   }

   public void setkonga_serveradminEndpointAddress(String var1) {
      this.konga_serveradmin_address = var1;
   }

   @Override
   public Remote getPort(Class var1) throws ServiceException {
      try {
         if (ServerAdministration.class.isAssignableFrom(var1)) {
            Konga_serveradminSoapBindingStub var2 = new Konga_serveradminSoapBindingStub(new URL(this.konga_serveradmin_address), this);
            var2.setPortName(this.getkonga_serveradminWSDDServiceName());
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
      if ("konga_serveradmin".equals(var3)) {
         return this.getkonga_serveradmin();
      }

      Remote var4 = this.getPort(var2);
      ((Stub)var4).setPortName(var1);
      return var4;
   }

   @Override
   public QName getServiceName() {
      return new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "ServerAdministrationService");
   }

   @Override
   public Iterator getPorts() {
      if (this.ports == null) {
         this.ports = new HashSet();
         this.ports.add(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "konga_serveradmin"));
      }

      return this.ports.iterator();
   }

   public void setEndpointAddress(String var1, String var2) throws ServiceException {
      if ("konga_serveradmin".equals(var1)) {
         this.setkonga_serveradminEndpointAddress(var2);
      } else {
         throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + var1);
      }
   }

   public void setEndpointAddress(QName var1, String var2) throws ServiceException {
      this.setEndpointAddress(var1.getLocalPart(), var2);
   }
}
