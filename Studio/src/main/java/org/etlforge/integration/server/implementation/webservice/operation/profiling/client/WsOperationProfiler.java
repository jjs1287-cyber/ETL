package org.etlforge.integration.server.implementation.webservice.operation.profiling.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WsOperationProfiler extends Remote {
   WsProfilingResult[] runProfiler(String var1, String var2, String var3) throws RemoteException;
}
