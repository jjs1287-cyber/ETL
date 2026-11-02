package org.etlforge.integration.client.ui.interchange.location.fileshare;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import org.etlforge.integration.client.ui.interchange.location.panel.PathFieldSupport;
import org.etlforge.integration.data.location.DataLocationStore;
import org.etlforge.integration.data.location.FileShareLocation;

class LocationStorePathProvider implements PathFieldSupport.PathProvider {
   private final DataLocationStore<FileShareLocation> locationStore;

   public LocationStorePathProvider(DataLocationStore<FileShareLocation> var1) {
      this.locationStore = var1;
   }

   @Override
   public Collection<String> getPaths() {
      ArrayList var1 = Lists.newArrayList();

      for (FileShareLocation var5 : this.locationStore.locations()) {
         String var6 = var5.getPath();
         if (var6.length() > 0) {
            var1.add(var6);
         }
      }

      return var1;
   }

   @Override
   public String getPrefix() {
      return "\\\\";
   }

   @Override
   public String getSeparator() {
      return "\\";
   }

   @Override
   public void refresh() {
      this.locationStore.refresh();
   }
}
