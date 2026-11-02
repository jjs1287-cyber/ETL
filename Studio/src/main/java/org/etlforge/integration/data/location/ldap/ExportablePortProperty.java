package org.etlforge.integration.data.location.ldap;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.ExportableProperty;
import org.etlforge.integration.data.IntegrationData;
import org.etlforge.integration.data.location.LdapLocation;

public class ExportablePortProperty implements ExportableProperty {
   private final LdapLocation origin;

   public ExportablePortProperty(LdapLocation var1) {
      Preconditions.checkNotNull(var1, "origin");
      this.origin = var1;
   }

   @Override
   public boolean exportTo(IntegrationData var1) {
      if (var1 instanceof LdapLocation) {
         ((LdapLocation)var1).setPort(this.origin.getPort());
         return true;
      } else {
         return false;
      }
   }

   @Override
   public String getName() {
      return Messages.getString("ExportablePortProperty.Name");
   }

   @Override
   public boolean isExportedByDefault() {
      return true;
   }
}
