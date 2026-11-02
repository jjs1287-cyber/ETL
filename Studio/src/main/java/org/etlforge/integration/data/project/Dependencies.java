package org.etlforge.integration.data.project;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.system.Disposable;

public final class Dependencies implements Disposable {
   private final Table<IntegrationEntity, IntegrationEntity, EntityLink> mainToDependants = HashBasedTable.create();
   private final Table<IntegrationEntity, IntegrationEntity, EntityLink> dependantToMains = HashBasedTable.create();

   public EntityLink getLink(IntegrationEntity var1, IntegrationEntity var2) {
      return this.mainToDependants.get(var1, var2);
   }

   public void addLink(EntityLink var1) {
      IntegrationEntity var2 = var1.getMainObject();
      IntegrationEntity var3 = var1.getDependantObject();
      EntityLink var4 = this.mainToDependants.get(var2, var3);
      if (var4 != null) {
         this.removeLink(var4);
      }

      this.mainToDependants.put(var2, var3, var1);
      this.dependantToMains.put(var3, var2, var1);
   }

   public FluentIterable<EntityLink> getAllLinksFrom(IntegrationEntity var1) {
      Map var2 = this.mainToDependants.row(var1);
      return FluentIterable.from(ImmutableList.copyOf(var2.values()));
   }

   public FluentIterable<EntityLink> getAllLinksTo(IntegrationEntity var1) {
      Map var2 = this.dependantToMains.row(var1);
      return FluentIterable.from(ImmutableList.copyOf(var2.values()));
   }

   public boolean isActive(IntegrationEntity var1) {
      Map var2 = this.mainToDependants.row(var1);
      return !var2.isEmpty();
   }

   public FluentIterable<IntegrationEntity> getRequiredObjects(final IntegrationEntity var1) {
      return this.getAllLinksTo(var1).transform(new Function<EntityLink, IntegrationEntity>() {
         public IntegrationEntity apply(EntityLink var1x) {
            return var1x.getOtherObject(var1);
         }
      });
   }

   public Set<IntegrationEntity> getRequiredObjectsRecursively(IntegrationEntity var1) {
      HashSet var2 = Sets.newHashSet();
      this.collectRequiredObjects(var1, var2);
      return var2;
   }

   private void collectRequiredObjects(IntegrationEntity var1, Set<IntegrationEntity> var2) {
      for (IntegrationEntity var4 : this.getRequiredObjects(var1)) {
         if (var2.add(var4)) {
            this.collectRequiredObjects(var4, var2);
         }
      }
   }

   public Set<EntityLink> objectDeleted(IntegrationEntity var1) {
      HashSet var2 = Sets.newHashSet();
      Map var3 = this.mainToDependants.row(var1);
      if (!var3.isEmpty()) {
         ArrayList var4 = Lists.newArrayList(var3.values());
         this.removeLinks(var4);
         var2.addAll(var4);
      }

      var3 = this.dependantToMains.row(var1);
      if (!var3.isEmpty()) {
         ArrayList var6 = Lists.newArrayList(var3.values());
         this.removeLinks(var6);
         var2.addAll(var6);
      }

      return var2;
   }

   public Set<EntityLink> updateLinks(IntegrationEntity var1, IntegrationEntityLookup var2) {
      HashSet var3 = Sets.newHashSet(this.getAllLinksTo(var1));
      this.removeLinks(var3);
      HashSet var4 = Sets.newHashSet(var1.getLinks(var2));

      for (Object __etlItem100 : var4) {
         EntityLink var6 = (EntityLink) __etlItem100;
         this.addLink(var6);
      }

      var3.removeAll(var4);
      return var3;
   }

   private void removeLinks(Iterable<EntityLink> var1) {
      for (EntityLink var3 : var1) {
         this.removeLink(var3);
      }
   }

   private void removeLink(EntityLink var1) {
      this.mainToDependants.remove(var1.getMainObject(), var1.getDependantObject());
      this.dependantToMains.remove(var1.getDependantObject(), var1.getMainObject());
      var1.dispose();
   }

   @Override
   public void dispose() {
      for (EntityLink var2 : this.mainToDependants.values()) {
         var2.dispose();
      }

      for (EntityLink var4 : this.dependantToMains.values()) {
         var4.dispose();
      }

      this.mainToDependants.clear();
      this.dependantToMains.clear();
   }
}
