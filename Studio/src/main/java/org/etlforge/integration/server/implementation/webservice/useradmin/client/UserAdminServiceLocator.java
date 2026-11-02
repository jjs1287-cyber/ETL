package org.etlforge.integration.server.implementation.webservice.useradmin.client;

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

public class UserAdminServiceLocator extends Service implements UserAdminService {
   private String konga_useradmin_address = "http://localhost/axis/konga_useradmin";
   private String konga_useradminWSDDServiceName = "konga_useradmin";
   private HashSet ports = null;

   public UserAdminServiceLocator() {
   }

   public UserAdminServiceLocator(EngineConfiguration var1) {
      super(var1);
   }

   public UserAdminServiceLocator(String var1, QName var2) throws ServiceException {
      super(var1, var2);
   }

   @Override
   public String getkonga_useradminAddress() {
      return this.konga_useradmin_address;
   }

   public String getkonga_useradminWSDDServiceName() {
      return this.konga_useradminWSDDServiceName;
   }

   public void setkonga_useradminWSDDServiceName(String var1) {
      this.konga_useradminWSDDServiceName = var1;
   }

   @Override
   public UserAdmin getkonga_useradmin() throws ServiceException {
      URL var1;
      try {
         var1 = new URL(this.konga_useradmin_address);
      } catch (MalformedURLException var3) {
         throw new ServiceException(var3);
      }

      return this.getkonga_useradmin(var1);
   }

   @Override
   public UserAdmin getkonga_useradmin(URL var1) throws ServiceException {
      try {
         Konga_useradminSoapBindingStub var2 = new Konga_useradminSoapBindingStub(var1, this);
         var2.setPortName(this.getkonga_useradminWSDDServiceName());
         return var2;
      } catch (AxisFault var3) {
         return null;
      }
   }

   public void setkonga_useradminEndpointAddress(String var1) {
      this.konga_useradmin_address = var1;
   }

   @Override
   public Remote getPort(Class var1) throws ServiceException {
      try {
         if (UserAdmin.class.isAssignableFrom(var1)) {
            Konga_useradminSoapBindingStub var2 = new Konga_useradminSoapBindingStub(new URL(this.konga_useradmin_address), this);
            var2.setPortName(this.getkonga_useradminWSDDServiceName());
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
      if ("konga_useradmin".equals(var3)) {
         return this.getkonga_useradmin();
      }

      Remote var4 = this.getPort(var2);
      ((Stub)var4).setPortName(var1);
      return var4;
   }

   @Override
   public QName getServiceName() {
      return new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "UserAdminService");
   }

   @Override
   public Iterator getPorts() {
      if (this.ports == null) {
         this.ports = new HashSet();
         this.ports.add(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "konga_useradmin"));
      }

      return this.ports.iterator();
   }

   public void setEndpointAddress(String var1, String var2) throws ServiceException {
      if ("konga_useradmin".equals(var1)) {
         this.setkonga_useradminEndpointAddress(var2);
      } else {
         throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + var1);
      }
   }

   public void setEndpointAddress(QName var1, String var2) throws ServiceException {
      this.setEndpointAddress(var1.getLocalPart(), var2);
   }
}
