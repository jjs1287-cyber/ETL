package org.etlforge.integration.server.implementation.webservice.admin.client;

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

public class AdminServiceLocator extends Service implements AdminService {
   private String konga_admin_address = "http://localhost:46908/axis/konga_admin";
   private String konga_adminWSDDServiceName = "konga_admin";
   private HashSet ports = null;

   public AdminServiceLocator() {
   }

   public AdminServiceLocator(EngineConfiguration var1) {
      super(var1);
   }

   public AdminServiceLocator(String var1, QName var2) throws ServiceException {
      super(var1, var2);
   }

   @Override
   public String getkonga_adminAddress() {
      return this.konga_admin_address;
   }

   public String getkonga_adminWSDDServiceName() {
      return this.konga_adminWSDDServiceName;
   }

   public void setkonga_adminWSDDServiceName(String var1) {
      this.konga_adminWSDDServiceName = var1;
   }

   @Override
   public Admin getkonga_admin() throws ServiceException {
      URL var1;
      try {
         var1 = new URL(this.konga_admin_address);
      } catch (MalformedURLException var3) {
         throw new ServiceException(var3);
      }

      return this.getkonga_admin(var1);
   }

   @Override
   public Admin getkonga_admin(URL var1) throws ServiceException {
      try {
         Konga_adminSoapBindingStub var2 = new Konga_adminSoapBindingStub(var1, this);
         var2.setPortName(this.getkonga_adminWSDDServiceName());
         return var2;
      } catch (AxisFault var3) {
         return null;
      }
   }

   public void setkonga_adminEndpointAddress(String var1) {
      this.konga_admin_address = var1;
   }

   @Override
   public Remote getPort(Class var1) throws ServiceException {
      try {
         if (Admin.class.isAssignableFrom(var1)) {
            Konga_adminSoapBindingStub var2 = new Konga_adminSoapBindingStub(new URL(this.konga_admin_address), this);
            var2.setPortName(this.getkonga_adminWSDDServiceName());
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
      if ("konga_admin".equals(var3)) {
         return this.getkonga_admin();
      }

      Remote var4 = this.getPort(var2);
      ((Stub)var4).setPortName(var1);
      return var4;
   }

   @Override
   public QName getServiceName() {
      return new QName("urn:org.etlforge.integration.server.implementation.webservice.admin.Admin", "AdminService");
   }

   @Override
   public Iterator getPorts() {
      if (this.ports == null) {
         this.ports = new HashSet();
         this.ports.add(new QName("urn:org.etlforge.integration.server.implementation.webservice.admin.Admin", "konga_admin"));
      }

      return this.ports.iterator();
   }

   public void setEndpointAddress(String var1, String var2) throws ServiceException {
      if ("konga_admin".equals(var1)) {
         this.setkonga_adminEndpointAddress(var2);
      } else {
         throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + var1);
      }
   }

   public void setEndpointAddress(QName var1, String var2) throws ServiceException {
      this.setEndpointAddress(var1.getLocalPart(), var2);
   }
}
