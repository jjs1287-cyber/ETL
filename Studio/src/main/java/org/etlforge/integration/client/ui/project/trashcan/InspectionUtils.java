package org.etlforge.integration.client.ui.project.trashcan;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.etlforge.collections.IdentityWrapper;
import org.etlforge.integration.data.entity.IntegrationEntity;

final class InspectionUtils {
   public static Set<InspectionUtils.Key> getSet(Collection<?> var0) {
      HashSet var1 = Sets.newHashSet();

      for (Object var3 : var0) {
         var1.add(new InspectionUtils.Key((IntegrationEntity)var3));
      }

      return var1;
   }

   public static List<IntegrationEntity> fromKeys(Collection<InspectionUtils.Key> var0) {
      ArrayList var1 = Lists.newArrayList();

      for (InspectionUtils.Key var3 : var0) {
         var1.add(var3.getObject());
      }

      return var1;
   }

   private InspectionUtils() {
   }

   public static class Key extends IdentityWrapper<IntegrationEntity> {
      public Key(IntegrationEntity var1) {
         super(var1);
      }
   }
}
