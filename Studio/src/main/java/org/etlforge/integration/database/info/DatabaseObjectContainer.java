package org.etlforge.integration.database.info;

import com.google.common.collect.Lists;
import java.util.List;

public final class DatabaseObjectContainer<E extends DatabaseObject> {
   private final DatabaseObjectType objectType;
   private final List<E> objects;

   public DatabaseObjectContainer(DatabaseObjectType var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("objectType cannot be null.");
      }

      this.objectType = var1;
      this.objects = Lists.newArrayList();
   }

   public DatabaseObjectType getObjectType() {
      return this.objectType;
   }

   public void addObject(E var1) {
      this.objects.add((E)var1);
   }

   public List<E> getAllObjects() {
      return Lists.newArrayList(this.objects);
   }
}
