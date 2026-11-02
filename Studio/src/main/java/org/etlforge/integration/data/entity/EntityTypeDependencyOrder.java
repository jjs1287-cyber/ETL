package org.etlforge.integration.data.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import org.etlforge.integration.data.functors.EntityFunctions;

public final class EntityTypeDependencyOrder implements Comparator<IntegrationEntity> {
   private final Ordering<IntegrationEntity> impl;

   public EntityTypeDependencyOrder() {
      List var1 = this.getOrder();
      this.impl = Ordering.<EntityType>explicit(var1).onResultOf(EntityFunctions.TO_TYPE);
   }

   private List<EntityType> getOrder() {
      LinkedHashSet var1 = Sets.newLinkedHashSet();
      var1.addAll(
         Arrays.asList(
            EntityType.Document,
            EntityType.EmailMessage,
            EntityType.Schedule,
            EntityType.WebServiceCall,
            EntityType.Source,
            EntityType.Target,
            EntityType.Transformation,
            EntityType.ProjectVariable, EntityType.XsltTransform, EntityType.Script,
            EntityType.Operation,
            EntityType.HttpEndpoint
         )
      );

      for (EntityType var5 : EntityType.values()) {
         if (!var1.contains(var5)) {
            switch (var5) {
               case Project:
               case RootFolder:
               case Folder:
                  break;
               default:
                  var1.add(var5);
            }
         }
      }

      return ImmutableList.copyOf(var1);
   }

   public int compare(IntegrationEntity var1, IntegrationEntity var2) {
      return this.impl.compare(var1, var2);
   }
}
