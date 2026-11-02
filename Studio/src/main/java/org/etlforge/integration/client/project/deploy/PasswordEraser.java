package org.etlforge.integration.client.project.deploy;

import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.LoginProtectedResource;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.LdapStructure;

final class PasswordEraser {
   public static void eraseFromStructure(DataStructure var0) {
      DataLocation var1 = null;
      if (var0 instanceof DatabaseStructure) {
         var1 = ((DatabaseStructure)var0).getUnprotectedDbLocation();
      } else if (var0 instanceof LdapStructure) {
         var1 = ((LdapStructure)var0).getUnprotectedLdapLocation();
      }

      if (var1 != null) {
         eraseFromLocation(var1);
      }
   }

   public static void eraseFromLocation(DataLocation var0) {
      if (var0 instanceof LoginProtectedResource) {
         String var1 = ((LoginProtectedResource)var0).getPasswordPropertyName();
         var0.setStringProperty(var1, null);
      }
   }

   private PasswordEraser() {
   }
}
