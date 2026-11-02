package com.etlforge.integration.salesforce.wsdl;

import java.io.File;
import org.etlforge.integration.salesforce.LoginCredentials;

public interface KnownWsdlsStore {
   Iterable<KnownWsdl> getKnownWsdls();

   void addLocalWsdl(String var1, byte[] var2, LoginCredentials var3);

   void replaceLocalWsdl(String var1, File var2);

   void addRemoteWsdl(String var1, LoginCredentials var2);

   void updateCredentials(String var1, LoginCredentials var2);
}
