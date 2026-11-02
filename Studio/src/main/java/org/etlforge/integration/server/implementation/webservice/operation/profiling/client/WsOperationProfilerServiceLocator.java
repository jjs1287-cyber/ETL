package org.etlforge.integration.server.implementation.webservice.operation.profiling.client;

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

public class WsOperationProfilerServiceLocator extends Service implements WsOperationProfilerService {
   private String konga_operation_profiler_address = "http://localhost/axis/konga_operation_profiler";
   private String konga_operation_profilerWSDDServiceName = "konga_operation_profiler";
   private HashSet ports = null;

   public WsOperationProfilerServiceLocator() {
   }

   public WsOperationProfilerServiceLocator(EngineConfiguration var1) {
      super(var1);
   }

   public WsOperationProfilerServiceLocator(String var1, QName var2) throws ServiceException {
      super(var1, var2);
   }

   @Override
   public String getkonga_operation_profilerAddress() {
      return this.konga_operation_profiler_address;
   }

   public String getkonga_operation_profilerWSDDServiceName() {
      return this.konga_operation_profilerWSDDServiceName;
   }

   public void setkonga_operation_profilerWSDDServiceName(String var1) {
      this.konga_operation_profilerWSDDServiceName = var1;
   }

   @Override
   public WsOperationProfiler getkonga_operation_profiler() throws ServiceException {
      URL var1;
      try {
         var1 = new URL(this.konga_operation_profiler_address);
      } catch (MalformedURLException var3) {
         throw new ServiceException(var3);
      }

      return this.getkonga_operation_profiler(var1);
   }

   @Override
   public WsOperationProfiler getkonga_operation_profiler(URL var1) throws ServiceException {
      try {
         Konga_operation_profilerSoapBindingStub var2 = new Konga_operation_profilerSoapBindingStub(var1, this);
         var2.setPortName(this.getkonga_operation_profilerWSDDServiceName());
         return var2;
      } catch (AxisFault var3) {
         return null;
      }
   }

   public void setkonga_operation_profilerEndpointAddress(String var1) {
      this.konga_operation_profiler_address = var1;
   }

   @Override
   public Remote getPort(Class var1) throws ServiceException {
      try {
         if (WsOperationProfiler.class.isAssignableFrom(var1)) {
            Konga_operation_profilerSoapBindingStub var2 = new Konga_operation_profilerSoapBindingStub(new URL(this.konga_operation_profiler_address), this);
            var2.setPortName(this.getkonga_operation_profilerWSDDServiceName());
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
      if ("konga_operation_profiler".equals(var3)) {
         return this.getkonga_operation_profiler();
      }

      Remote var4 = this.getPort(var2);
      ((Stub)var4).setPortName(var1);
      return var4;
   }

   @Override
   public QName getServiceName() {
      return new QName("urn:org.etlforge.integration.server.implementation.webservice.operation.profiling.WsOperationProfiler", "WsOperationProfilerService");
   }

   @Override
   public Iterator getPorts() {
      if (this.ports == null) {
         this.ports = new HashSet();
         this.ports
            .add(
               new QName("urn:org.etlforge.integration.server.implementation.webservice.operation.profiling.WsOperationProfiler", "konga_operation_profiler")
            );
      }

      return this.ports.iterator();
   }

   public void setEndpointAddress(String var1, String var2) throws ServiceException {
      if ("konga_operation_profiler".equals(var1)) {
         this.setkonga_operation_profilerEndpointAddress(var2);
      } else {
         throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + var1);
      }
   }

   public void setEndpointAddress(QName var1, String var2) throws ServiceException {
      this.setEndpointAddress(var1.getLocalPart(), var2);
   }
}
