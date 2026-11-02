package com.etlforge.integration.client.sfdc.data;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.BuiltInIntegrationEntity;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;

public class ImplementingObject<T extends IntegrationEntity> {
   public static final Function<ImplementingObject<?>, IntegrationEntity> TO_ENTITY = new Function<ImplementingObject<?>, IntegrationEntity>() {
      public IntegrationEntity apply(ImplementingObject<?> var1) {
         return var1.getObject();
      }
   };
   private final Folder parent;
   private final T object;
   private final boolean createdByWizard;

   public static <E extends IntegrationEntity> ImplementingObject<E> forExistingObject(E var0) {
      return new ImplementingObject<>((Folder)var0.getParent(), (E)var0);
   }

   public static <E extends IntegrationEntity> ImplementingObject<E> newObject(Folder var0, E var1) {
      return new ImplementingObject<>(var0, (E)var1);
   }

   public ImplementingObject(Folder var1, T var2) {
      this(var1, (T)var2, var2.isTransient());
   }

   public ImplementingObject(Folder var1, T var2, boolean var3) {
      Preconditions.checkArgument(var1 != null || var2 instanceof BuiltInIntegrationEntity, "Must provide a parent for non-system objects.");
      this.parent = var1;
      this.object = Preconditions.checkNotNull((T)var2, "object");
      this.createdByWizard = var3;
   }

   public final Folder getParent() {
      return this.parent;
   }

   public final T getObject() {
      return this.object;
   }

   public final boolean isCreatedByWizard() {
      return this.createdByWizard;
   }

   public void suggestNewName(String var1) {
      if (this.object.isEditable()) {
         String var2 = EntityUtils.getAvailableChildName(this.parent, var1);
         this.object.setName(var2);
      }
   }

   public final void insertInto(IntegrationProject var1) {
      var1.insertEntity(this.object, this.parent);
   }

   public final boolean isSystemEntity() {
      return this.object instanceof BuiltInIntegrationEntity;
   }

   @Override
   public String toString() {
      return this.parent == null ? this.object.getName() : this.parent.getName() + "/" + this.object.getName();
   }
}
