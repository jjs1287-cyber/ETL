package com.etlforge.integration.server.implementation.webservice.projectadmin.backup.client;

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

public class ProjectBackupAdministrationServiceLocator extends Service implements ProjectBackupAdministrationService {
   private String konga_projectbackupadmin_address = "http://localhost/axis/konga_projectbackupadmin";
   private String konga_projectbackupadminWSDDServiceName = "konga_projectbackupadmin";
   private HashSet ports = null;

   public ProjectBackupAdministrationServiceLocator() {
   }

   public ProjectBackupAdministrationServiceLocator(EngineConfiguration var1) {
      super(var1);
   }

   public ProjectBackupAdministrationServiceLocator(String var1, QName var2) throws ServiceException {
      super(var1, var2);
   }

   @Override
   public String getkonga_projectbackupadminAddress() {
      return this.konga_projectbackupadmin_address;
   }

   public String getkonga_projectbackupadminWSDDServiceName() {
      return this.konga_projectbackupadminWSDDServiceName;
   }

   public void setkonga_projectbackupadminWSDDServiceName(String var1) {
      this.konga_projectbackupadminWSDDServiceName = var1;
   }

   @Override
   public ProjectBackupAdministration getkonga_projectbackupadmin() throws ServiceException {
      URL var1;
      try {
         var1 = new URL(this.konga_projectbackupadmin_address);
      } catch (MalformedURLException var3) {
         throw new ServiceException(var3);
      }

      return this.getkonga_projectbackupadmin(var1);
   }

   @Override
   public ProjectBackupAdministration getkonga_projectbackupadmin(URL var1) throws ServiceException {
      try {
         Konga_projectbackupadminSoapBindingStub var2 = new Konga_projectbackupadminSoapBindingStub(var1, this);
         var2.setPortName(this.getkonga_projectbackupadminWSDDServiceName());
         return var2;
      } catch (AxisFault var3) {
         return null;
      }
   }

   public void setkonga_projectbackupadminEndpointAddress(String var1) {
      this.konga_projectbackupadmin_address = var1;
   }

   @Override
   public Remote getPort(Class var1) throws ServiceException {
      try {
         if (ProjectBackupAdministration.class.isAssignableFrom(var1)) {
            Konga_projectbackupadminSoapBindingStub var2 = new Konga_projectbackupadminSoapBindingStub(new URL(this.konga_projectbackupadmin_address), this);
            var2.setPortName(this.getkonga_projectbackupadminWSDDServiceName());
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
      if ("konga_projectbackupadmin".equals(var3)) {
         return this.getkonga_projectbackupadmin();
      }

      Remote var4 = this.getPort(var2);
      ((Stub)var4).setPortName(var1);
      return var4;
   }

   @Override
   public QName getServiceName() {
      return new QName("http://backup.projectadmin.webservice.implementation.server.integration.etlforge.invalid", "ProjectBackupAdministrationService");
   }

   @Override
   public Iterator getPorts() {
      if (this.ports == null) {
         this.ports = new HashSet();
         this.ports.add(new QName("http://backup.projectadmin.webservice.implementation.server.integration.etlforge.invalid", "konga_projectbackupadmin"));
      }

      return this.ports.iterator();
   }

   public void setEndpointAddress(String var1, String var2) throws ServiceException {
      if ("konga_projectbackupadmin".equals(var1)) {
         this.setkonga_projectbackupadminEndpointAddress(var2);
      } else {
         throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + var1);
      }
   }

   public void setEndpointAddress(QName var1, String var2) throws ServiceException {
      this.setEndpointAddress(var1.getLocalPart(), var2);
   }
}
