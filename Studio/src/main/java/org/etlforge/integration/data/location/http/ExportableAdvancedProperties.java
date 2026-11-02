package org.etlforge.integration.data.location.http;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.ExportableProperty;
import org.etlforge.integration.data.IntegrationData;
import org.etlforge.integration.data.location.HttpLocation;
import org.etlforge.integration.data.location.LocationMessages;

public final class ExportableAdvancedProperties implements ExportableProperty {
   private final HttpLocation from;

   public ExportableAdvancedProperties(HttpLocation var1) {
      this.from = Preconditions.checkNotNull(var1);
   }

   @Override
   public boolean exportTo(IntegrationData var1) {
      if (!(var1 instanceof HttpLocation)) {
         return false;
      }

      HttpLocation var2 = (HttpLocation)var1;
      var2.setHttpProperties(this.from.getHttpProperties());
      var2.setRequestHeaders(this.from.getRequestHeaders());
      var2.setSslVersion(this.from.getSslVersion());
      return true;
   }

   @Override
   public String getName() {
      return LocationMessages.getString("HttpLocation.Property.Advanced");
   }

   @Override
   public boolean isExportedByDefault() {
      return true;
   }
}
