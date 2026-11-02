package org.etlforge.integration.client.entity.valuecache;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;
import org.etlforge.integration.data.IntegrationData;
import org.etlforge.util.name.Nameables;

public final class DefaultValueCache<T extends IntegrationData> implements ValueCache<T> {
   private final ImmutableMap<String, DefaultHistory> parts;

   public DefaultValueCache(Iterable<DefaultHistory> var1) {
      this.parts = Nameables.toImmutableMap(var1);
   }

   @Override
   public Optional<History> getValues(String var1) {
      History var2 = this.parts.get(var1);
      return Optional.fromNullable(var2);
   }

   @Override
   public void update(Iterable<? extends T> var1) {
      for (Entry var3 : this.parts.entrySet()) {
         String var4 = (String)var3.getKey();
         Set var5 = this.collectAllValuesForProperty(var1, var4);
         if (!var5.isEmpty()) {
            DefaultHistory var6 = (DefaultHistory)var3.getValue();
            var6.addValues(var5);
         }
      }
   }

   private Set<String> collectAllValuesForProperty(Iterable<? extends T> var1, String var2) {
      HashSet var3 = Sets.newHashSet();

      for (IntegrationData var5 : var1) {
         String var6;
         synchronized (var5.getDataLock()) {
            var6 = var5.getProperty(var2);
         }

         if (!Strings.isNullOrEmpty(var6)) {
            var3.add(var6);
         }
      }

      return var3;
   }
}
