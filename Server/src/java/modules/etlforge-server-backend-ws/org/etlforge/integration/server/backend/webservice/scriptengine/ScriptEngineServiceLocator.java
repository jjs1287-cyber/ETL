package org.etlforge.integration.server.backend.webservice.scriptengine;

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

public class ScriptEngineServiceLocator extends Service implements ScriptEngineService {
   private String konga_script_engine_address = "http://localhost/axis/konga_script_engine";
   private String konga_script_engineWSDDServiceName = "konga_script_engine";
   private HashSet ports = null;

   public ScriptEngineServiceLocator() {
   }

   public ScriptEngineServiceLocator(EngineConfiguration var1) {
      super(var1);
   }

   public ScriptEngineServiceLocator(String var1, QName var2) throws ServiceException {
      super(var1, var2);
   }

   @Override
   public String getkonga_script_engineAddress() {
      return this.konga_script_engine_address;
   }

   public String getkonga_script_engineWSDDServiceName() {
      return this.konga_script_engineWSDDServiceName;
   }

   public void setkonga_script_engineWSDDServiceName(String var1) {
      this.konga_script_engineWSDDServiceName = var1;
   }

   @Override
   public ScriptEngine getkonga_script_engine() throws ServiceException {
      URL var1;
      try {
         var1 = new URL(this.konga_script_engine_address);
      } catch (MalformedURLException var3) {
         throw new ServiceException(var3);
      }

      return this.getkonga_script_engine(var1);
   }

   @Override
   public ScriptEngine getkonga_script_engine(URL var1) throws ServiceException {
      try {
         Konga_script_engineSoapBindingStub var2 = new Konga_script_engineSoapBindingStub(var1, this);
         var2.setPortName(this.getkonga_script_engineWSDDServiceName());
         return var2;
      } catch (AxisFault var3) {
         return null;
      }
   }

   public void setkonga_script_engineEndpointAddress(String var1) {
      this.konga_script_engine_address = var1;
   }

   @Override
   public Remote getPort(Class var1) throws ServiceException {
      try {
         if (ScriptEngine.class.isAssignableFrom(var1)) {
            Konga_script_engineSoapBindingStub var2 = new Konga_script_engineSoapBindingStub(new URL(this.konga_script_engine_address), this);
            var2.setPortName(this.getkonga_script_engineWSDDServiceName());
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
      if ("konga_script_engine".equals(var3)) {
         return this.getkonga_script_engine();
      }

      Remote var4 = this.getPort(var2);
      ((Stub)var4).setPortName(var1);
      return var4;
   }

   @Override
   public QName getServiceName() {
      return new QName("http://scriptengine.webservice.backend.server.integration.etlforge.invalid", "ScriptEngineService");
   }

   @Override
   public Iterator getPorts() {
      if (this.ports == null) {
         this.ports = new HashSet();
         this.ports.add(new QName("http://scriptengine.webservice.backend.server.integration.etlforge.invalid", "konga_script_engine"));
      }

      return this.ports.iterator();
   }

   public void setEndpointAddress(String var1, String var2) throws ServiceException {
      if ("konga_script_engine".equals(var1)) {
         this.setkonga_script_engineEndpointAddress(var2);
      } else {
         throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + var1);
      }
   }

   public void setEndpointAddress(QName var1, String var2) throws ServiceException {
      this.setEndpointAddress(var1.getLocalPart(), var2);
   }
}
