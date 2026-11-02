package org.etlforge.integration.server.backend.webservice.wsexample;

import java.rmi.RemoteException;

public class Konga_wsexample_engineSoapBindingImpl implements WsExampleEngine {
   private final konga_wsexample_engineImpl impl = new konga_wsexample_engineImpl();

   @Override
   public WsStatus testExample(WsParameters var1) throws RemoteException {
      return this.impl.testExample(var1);
   }

   @Override
   public void testNoOutput(WsParameters var1) throws RemoteException {
      this.impl.testNoOutput(var1);
   }

   @Override
   public WsStatus testNoInput() throws RemoteException {
      return this.impl.testNoInput();
   }

   @Override
   public void testNoInputOutput() throws RemoteException {
      this.impl.testNoInputOutput();
   }
}
