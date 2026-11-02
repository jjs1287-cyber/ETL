package org.etlforge.integration.server.backend.webservice.wsexample;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WsExampleEngine extends Remote {
   WsStatus testExample(WsParameters var1) throws RemoteException;

   void testNoOutput(WsParameters var1) throws RemoteException;

   WsStatus testNoInput() throws RemoteException;

   void testNoInputOutput() throws RemoteException;
}
