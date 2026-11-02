package org.etlforge.integration.client.datalocation.cache;

import com.google.common.collect.Lists;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.io.Folder;

abstract class AbstractDataLocationCache<T extends DataLocation> implements DataLocationCache<T> {
   private final String id;
   private final Class<T> locationClass;
   private final List<StringCacheParticle<T>> stringCaches;

   protected AbstractDataLocationCache(String var1, Class<T> var2) {
      this.id = var1;
      this.locationClass = var2;
      this.stringCaches = Lists.newArrayList();
   }

   @Override
   public final String getID() {
      return this.id;
   }

   protected final void addStringCache(StringCacheParticle<T> var1) {
      this.stringCaches.add(var1);
   }

   @Override
   public final void update(T var1) {
      this.updateStringCaches((T)var1);
   }

   @Override
   public final void update(IntegrationProject var1) {
      ProjectDataLocationStore var2 = ProjectDataLocationStore.allLocations(var1, this.locationClass);
      List var3 = Arrays.asList(var2.locations());
      this.updateStringCaches(var3);
   }

   private void updateStringCaches(T var1) {
      for (StringCacheParticle var3 : this.stringCaches) {
         var3.update(var1);
      }
   }

   private void updateStringCaches(Collection<T> var1) {
      for (StringCacheParticle var3 : this.stringCaches) {
         var3.update(var1);
      }
   }

   protected final Folder createParent(File var1) {
      Folder var2 = new Folder(var1, this.id);
      if (!var2.createOnDisk()) {
         String var3 = "Could not create the local folder " + var2.getAbsolutePath();
         ErrorLog.log(this.getClass(), "Cache error.", new Exception(var3));
      }

      return var2;
   }
}
