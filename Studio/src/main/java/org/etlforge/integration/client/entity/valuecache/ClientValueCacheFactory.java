package org.etlforge.integration.client.entity.valuecache;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.io.File;
import java.util.ArrayList;
import org.etlforge.integration.data.IntegrationData;
import org.etlforge.system.ArgCheck;

public final class ClientValueCacheFactory {
   private final File applicationCacheFolder;
   private final int maxSize;

   public ClientValueCacheFactory(File var1, int var2) {
      this.applicationCacheFolder = Preconditions.checkNotNull(var1, "applicationCacheFolder");
      this.maxSize = ArgCheck.checkPositive(var2, "maxSize");
   }

   public <T extends IntegrationData> ValueCache<T> newValueCache(String var1, Iterable<String> var2) {
      File var3 = new File(this.applicationCacheFolder, ArgCheck.checkNotEmpty(var1, "cacheName"));
      ArrayList var4 = Lists.newArrayList();

      for (String var6 : var2) {
         FileStorage var7 = new FileStorage(new File(var3, var6));
         DefaultHistory var8 = new DefaultHistory(var6, var7, this.maxSize);
         var4.add(var8);
      }

      return new DefaultValueCache<>(var4);
   }
}
