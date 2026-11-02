package org.etlforge.integration.server.implementation.webservice.projectadmin.client;

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

public class ProjectAdministrationServiceLocator extends Service implements ProjectAdministrationService {
   private String konga_project_administration_address = "http://localhost/axis/konga_project_administration";
   private String konga_project_administrationWSDDServiceName = "konga_project_administration";
   private HashSet ports = null;

   public ProjectAdministrationServiceLocator() {
   }

   public ProjectAdministrationServiceLocator(EngineConfiguration var1) {
      super(var1);
   }

   public ProjectAdministrationServiceLocator(String var1, QName var2) throws ServiceException {
      super(var1, var2);
   }

   @Override
   public String getkonga_project_administrationAddress() {
      return this.konga_project_administration_address;
   }

   public String getkonga_project_administrationWSDDServiceName() {
      return this.konga_project_administrationWSDDServiceName;
   }

   public void setkonga_project_administrationWSDDServiceName(String var1) {
      this.konga_project_administrationWSDDServiceName = var1;
   }

   @Override
   public ProjectAdministration getkonga_project_administration() throws ServiceException {
      URL var1;
      try {
         var1 = new URL(this.konga_project_administration_address);
      } catch (MalformedURLException var3) {
         throw new ServiceException(var3);
      }

      return this.getkonga_project_administration(var1);
   }

   @Override
   public ProjectAdministration getkonga_project_administration(URL var1) throws ServiceException {
      try {
         Konga_project_administrationSoapBindingStub var2 = new Konga_project_administrationSoapBindingStub(var1, this);
         var2.setPortName(this.getkonga_project_administrationWSDDServiceName());
         return var2;
      } catch (AxisFault var3) {
         return null;
      }
   }

   public void setkonga_project_administrationEndpointAddress(String var1) {
      this.konga_project_administration_address = var1;
   }

   @Override
   public Remote getPort(Class var1) throws ServiceException {
      try {
         if (ProjectAdministration.class.isAssignableFrom(var1)) {
            Konga_project_administrationSoapBindingStub var2 = new Konga_project_administrationSoapBindingStub(
               new URL(this.konga_project_administration_address), this
            );
            var2.setPortName(this.getkonga_project_administrationWSDDServiceName());
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
      if ("konga_project_administration".equals(var3)) {
         return this.getkonga_project_administration();
      }

      Remote var4 = this.getPort(var2);
      ((Stub)var4).setPortName(var1);
      return var4;
   }

   @Override
   public QName getServiceName() {
      return new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "ProjectAdministrationService");
   }

   @Override
   public Iterator getPorts() {
      if (this.ports == null) {
         this.ports = new HashSet();
         this.ports
            .add(new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "konga_project_administration"));
      }

      return this.ports.iterator();
   }

   public void setEndpointAddress(String var1, String var2) throws ServiceException {
      if ("konga_project_administration".equals(var1)) {
         this.setkonga_project_administrationEndpointAddress(var2);
      } else {
         throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + var1);
      }
   }

   public void setEndpointAddress(QName var1, String var2) throws ServiceException {
      this.setEndpointAddress(var1.getLocalPart(), var2);
   }
}
