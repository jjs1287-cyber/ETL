package org.etlforge.integration.data.entity;

import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import java.util.LinkedList;
import org.etlforge.collections.Collections2;
import org.etlforge.util.name.Nameables;

public final class EntityPath implements Iterable<IntegrationEntity> {
   private final ImmutableList<IntegrationEntity> path;

   public EntityPath(IntegrationEntity var1) {
      this(var1, true);
   }

   public EntityPath(IntegrationEntity var1, boolean var2) {
      this.path = this.buildPath(var1, var2);
   }

   private ImmutableList<IntegrationEntity> buildPath(IntegrationEntity var1, boolean var2) {
      LinkedList var3 = new LinkedList();

      while (!this.isStop(var1, var2)) {
         var3.addFirst(var1);
         var1 = var1.getParent();
      }

      return ImmutableList.copyOf(var3);
   }

   private boolean isStop(IntegrationEntity var1, boolean var2) {
      if (var1 == null) {
         return true;
      } else {
         return var2 ? false : var1.getEntityType() == EntityType.Project;
      }
   }

   public IntegrationEntity root() {
      return this.path.get(0);
   }

   @Override
   public Iterator<IntegrationEntity> iterator() {
      return this.path.iterator();
   }

   @Override
   public String toString() {
      return this.toString("/");
   }

   public String toString(String var1) {
      return Collections2.join(this.path, Nameables.toNameFunction(), var1);
   }
}
