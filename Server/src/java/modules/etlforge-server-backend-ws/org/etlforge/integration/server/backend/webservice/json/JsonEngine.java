package org.etlforge.integration.server.backend.webservice.json;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JsonEngine extends Remote {
   WsStartNewJsonSessionResult startNewJsonSession(String var1, String var2) throws RemoteException;

   WsJsonError jsonToXml(WsJsonSessionId var1, WsConversionParams var2) throws RemoteException;

   WsJsonError xmlToJson(WsJsonSessionId var1, WsConversionParams var2) throws RemoteException;

   WsJsonError closeJsonSession(WsJsonSessionId var1) throws RemoteException;
}
