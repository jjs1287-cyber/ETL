package com.etlforge.integration.client.project.share.history;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.util.persist.Persistor;

public class ImportedEntity {
   private final EntityDescriptor from;
   private final EntityDescriptor to;
   private final List<String> notes;

   static ImportedEntity restore(Persistor var0) {
      EntityType var1 = restoreType(var0);
      if (var1 == null) {
         return null;
      }

      EntityDescriptor var2 = restoreEntity(var0, "From", var1);
      if (var2 == null) {
         return null;
      }

      EntityDescriptor var3 = restoreEntity(var0, "To", var1);
      if (var3 == null) {
         return null;
      }

      List var4 = restoreNotes(var0);
      return new ImportedEntity(var2, var3, var4);
   }

   private static EntityType restoreType(Persistor var0) {
      String var1 = var0.getString("type");
      if (var1 != null) {
         try {
            return EntityType.valueOf(var1);
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

      return null;
   }

   private static EntityDescriptor restoreEntity(Persistor var0, String var1, EntityType var2) {
      Persistor var3 = var0.getFirstChild(var1);
      return var3 != null ? EntityDescriptor.restore(var3, var2) : null;
   }

   private static List<String> restoreNotes(Persistor var0) {
      ArrayList var1 = Lists.newArrayList();
      Persistor var2 = var0.getFirstChild("Notes");
      if (var2 != null) {
         for (Persistor var4 : var2.getChildren("Note")) {
            String var5 = var4.getText("value");
            if (var5 != null) {
               var1.add(var5);
            }
         }
      }

      return var1;
   }

   public ImportedEntity(IntegrationEntity var1, IntegrationEntity var2) {
      this(new EntityDescriptor(var1), new EntityDescriptor(var2), new ArrayList<>());
   }

   private ImportedEntity(EntityDescriptor var1, EntityDescriptor var2, List<String> var3) {
      this.from = var1;
      this.to = var2;
      if (this.from.getType() != this.to.getType()) {
         throw new IllegalArgumentException("The two entities must be of the same type");
      }

      this.notes = var3;
   }

   public EntityType getEntityType() {
      return this.from.getType();
   }

   public EntityDescriptor getOriginalEntity() {
      return this.from;
   }

   public EntityDescriptor getCopiedEntity() {
      return this.to;
   }

   public List<String> getNotes() {
      return Lists.newArrayList(this.notes);
   }

   public void addNote(String var1) {
      this.notes.add(var1);
   }

   void writeTo(Persistor var1) {
      var1.putString("type", this.getEntityType().name());
      this.from.writeTo(var1.createChild("From"), false);
      this.to.writeTo(var1.createChild("To"), false);
      if (!this.notes.isEmpty()) {
         Persistor var2 = var1.createChild("Notes");

         for (String var4 : this.notes) {
            var2.createChild("Note").putText("value", var4);
         }
      }
   }
}
