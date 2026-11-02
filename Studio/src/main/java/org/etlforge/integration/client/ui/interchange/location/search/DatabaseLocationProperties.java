package org.etlforge.integration.client.ui.interchange.location.search;

import java.util.List;
import org.etlforge.integration.client.entity.search.AbstractSearchableProperty;
import org.etlforge.integration.client.entity.search.ReplaceableKeyValueBooleanProperty;
import org.etlforge.integration.client.entity.search.ReplaceableKeyValueIntegerProperty;
import org.etlforge.integration.client.entity.search.ReplaceableKeyValueStringProperty;
import org.etlforge.integration.client.entity.search.SearchableProperty;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.integration.database.driver.DriverType;
import org.etlforge.util.net.Port;

final class DatabaseLocationProperties {
   public static void collect(SourceTarget var0, List<SearchableProperty> var1) {
      var1.add(new DatabaseLocationProperties.DriverTypeProperty());
      var1.add(new DatabaseLocationProperties.DriverProperty());
      var1.add(stringProperty("Server", "locator"));
      var1.add(stringProperty("Database", "dbname"));
      var1.add(stringProperty("Login", "login"));
      var1.add(intProperty("Port", "port", Port.DEFAULT.getPort()));
      var1.add(intProperty("Timeut", "timeout", 300));
      var1.add(stringProperty("Additional params", "optional_connectstring"));
      var1.add(new ReplaceableKeyValueBooleanProperty("Use manual connection", "manual_mode", false, TargetFunctions.TO_LOCATION));
      var1.add(stringProperty("Manual connection string", "connectstring"));
      if (var0 == SourceTarget.Source) {
         var1.add(intProperty("Fetch Size", "fetch_size", 0));
      }
   }

   private static SearchableProperty<String> stringProperty(String var0, String var1) {
      return new ReplaceableKeyValueStringProperty(var0, var1, TargetFunctions.TO_LOCATION);
   }

   private static SearchableProperty<Integer> intProperty(String var0, String var1, int var2) {
      return new ReplaceableKeyValueIntegerProperty(var0, var1, var2, TargetFunctions.TO_LOCATION);
   }

   private DatabaseLocationProperties() {
   }

   private static final class DriverProperty extends AbstractSearchableProperty<String> {
      public DriverProperty() {
         super("Driver", String.class);
      }

      public String getValue(IntegrationEntity var1) {
         DataLocation var2 = ((Locatable)var1).getLocation();
         DriverDescriptor var3 = ((DatabaseLocation)var2).getDriver();
         return var3 != null ? var3.getName() : null;
      }
   }

   private static final class DriverTypeProperty extends AbstractSearchableProperty<DriverType> {
      public DriverTypeProperty() {
         super("Driver type", DriverType.class);
      }

      public DriverType getValue(IntegrationEntity var1) {
         DataLocation var2 = ((Locatable)var1).getLocation();
         DriverDescriptor var3 = ((DatabaseLocation)var2).getDriver();
         return var3 != null ? var3.getType() : null;
      }
   }
}
