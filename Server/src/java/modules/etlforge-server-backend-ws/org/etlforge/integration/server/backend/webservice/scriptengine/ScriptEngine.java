package org.etlforge.integration.server.backend.webservice.scriptengine;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ScriptEngine extends Remote {
   Result runGroovyScript(String var1, String var2, String var3, Binding[] var4) throws RemoteException;
}
