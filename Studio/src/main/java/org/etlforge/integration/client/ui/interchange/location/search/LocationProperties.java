package org.etlforge.integration.client.ui.interchange.location.search;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.etlforge.collections.Pair;
import org.etlforge.integration.client.entity.search.SearchableProperty;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.lang.exception.NotImplementedYetException;

final class LocationProperties {
   private static final LocationProperties.LocationPropertiesMap locationProperties = loadLocationProperties();

   private static LocationProperties.LocationPropertiesMap loadLocationProperties() {
      LocationProperties.LocationPropertiesMap var0 = new LocationProperties.LocationPropertiesMap();

      for (SourceTarget var4 : SourceTarget.values()) {
         for (DataLocationType var8 : DataLocationType.values()) {
            ArrayList var9 = Lists.newArrayList();
            switch (var8) {
               case Database:
                  DatabaseLocationProperties.collect(var4, var9);
                  break;
               case FTP:
                  FtpLocationProperties.collect(var4, var9);
                  break;
               case FileShare:
                  FileShareLocationProperties.collect(var4, var9);
                  break;
               case HTTP:
                  HttpLocationProperties.collect(var4, var9);
               case AmazonS3:
               case GlobalVariable:
               case LDAP:
               case None:
                  break;
               case TemporaryStorage:
                  TemporaryStorageLocationProperties.collect(var4, var9);
                  break;
               case LocalFile:
                  LocalFileLocationProperties.collect(var4, var9);
                  break;
               default:
                  throw new NotImplementedYetException(var8.name());
            }

            var0.put(var8, var4, var9);
         }
      }

      return var0;
   }

   public static List<SearchableProperty> getSupportedProperties(DataLocationType var0, SourceTarget var1) {
      return locationProperties.get(var0, var1);
   }

   private LocationProperties() {
   }

   private static final class LocationPropertiesMap {
      private final Map<Pair<DataLocationType, SourceTarget>, List<SearchableProperty>> map = Maps.newHashMap();

      private LocationPropertiesMap() {
      }

      public void put(DataLocationType var1, SourceTarget var2, List<SearchableProperty> var3) {
         this.map.put(key(var1, var2), var3);
      }

      private static Pair<DataLocationType, SourceTarget> key(DataLocationType var0, SourceTarget var1) {
         return Pair.of(var0, var1);
      }

      public List<SearchableProperty> get(DataLocationType var1, SourceTarget var2) {
         List var3 = this.map.get(key(var1, var2));
         return var3 == null ? Lists.newArrayList() : Lists.newArrayList(var3);
      }
   }
}
