package com.etlforge.integration.client.ui.sfdc.wsdl;

import com.etlforge.integration.salesforce.wsdl.KnownWsdl;
import com.etlforge.integration.salesforce.wsdl.KnownWsdlsStore;
import java.io.File;
import java.util.Collections;
import org.etlforge.integration.salesforce.LoginCredentials;

public final class KnownWsdlsStoreFactory {
   public static KnownWsdlsStore fileBasedImplementation(File var0) {
      return new FileBasedKnownWsdlsStore(var0);
   }

   public static KnownWsdlsStore empty() {
      return new KnownWsdlsStore() {
         @Override
         public Iterable<KnownWsdl> getKnownWsdls() {
            return Collections.emptySet();
         }

         @Override
         public void addRemoteWsdl(String var1, LoginCredentials var2) {
         }

         @Override
         public void addLocalWsdl(String var1, byte[] var2, LoginCredentials var3) {
         }

         @Override
         public void replaceLocalWsdl(String var1, File var2) {
         }

         @Override
         public void updateCredentials(String var1, LoginCredentials var2) {
         }
      };
   }

   private KnownWsdlsStoreFactory() {
   }
}
