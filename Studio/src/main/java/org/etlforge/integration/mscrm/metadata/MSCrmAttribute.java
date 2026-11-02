package org.etlforge.integration.mscrm.metadata;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.io.Serializable;
import org.etlforge.system.ArgCheck;
import org.etlforge.util.name.Nameable;

public final class MSCrmAttribute implements Nameable, Serializable {
   private static final long serialVersionUID = -4211161232587614306L;
   private static final int VALID_FOR_CREATE = 1;
   private static final int VALID_FOR_READ = 2;
   private static final int VALID_FOR_UPDATE = 4;
   private static final int PRIMARY_ID = 8;
   private static final int PRIMARY_NAME = 16;
   private static final int IS_MANAGED = 32;
   private static final int IS_VALID_FOR_ADVANCED_FIND = 64;
   private final String logicalName;
   private final String schemaName;
   private final MSCrmDataType type;
   private final String displayName;
   private final String description;
   private final AttributeRequiredLevel requiredLevel;
   private final int booleanAttrs;

   public static MSCrmAttribute.Builder builder() {
      return new MSCrmAttribute.Builder();
   }

   public static MSCrmAttribute.Builder builder(String var0) {
      MSCrmAttribute.Builder var1 = new MSCrmAttribute.Builder();
      return var1.setLogicalName(var0);
   }

   public MSCrmAttribute(
      String var1,
      MSCrmDataType var2,
      String var3,
      String var4,
      String var5,
      AttributeRequiredLevel var6,
      boolean var7,
      boolean var8,
      boolean var9,
      boolean var10,
      boolean var11,
      boolean var12,
      boolean var13
   ) {
      this.logicalName = ArgCheck.checkNotEmpty(var1, "logicalName");
      this.type = Preconditions.checkNotNull(var2, "type");
      this.schemaName = ArgCheck.checkNotEmpty(var3, "schemaName");
      this.displayName = ArgCheck.checkNotEmpty(var4, "displayName");
      this.description = Strings.nullToEmpty(var5);
      this.requiredLevel = Preconditions.checkNotNull(var6, "requiredLevel");
      this.booleanAttrs = encodeBooleanAttrs(var7, var8, var9, var10, var11, var12, var13);
   }

   private static int encodeBooleanAttrs(boolean var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      byte var7 = 0;
      if (var0) {
         var7 |= 1;
      }

      if (var1) {
         var7 |= 2;
      }

      if (var2) {
         var7 |= 4;
      }

      if (var3) {
         var7 |= 8;
      }

      if (var4) {
         var7 |= 16;
      }

      if (var5) {
         var7 |= 32;
      }

      if (var6) {
         var7 |= 64;
      }

      return var7;
   }

   @Override
   public String getName() {
      return this.logicalName;
   }

   public String getLogicalName() {
      return this.logicalName;
   }

   public String getSchemaName() {
      return this.schemaName;
   }

   public MSCrmDataType getType() {
      return this.type;
   }

   public String getXsdType() {
      return this.type.getXsdType();
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public String getDescription() {
      return this.description;
   }

   public AttributeRequiredLevel getRequiredLevel() {
      return this.requiredLevel;
   }

   public boolean isValidForCreate() {
      return (this.booleanAttrs & 1) != 0;
   }

   public boolean isValidForRead() {
      return (this.booleanAttrs & 2) != 0;
   }

   public boolean isValidForUpdate() {
      return (this.booleanAttrs & 4) != 0;
   }

   public boolean isValidForUpsert() {
      return (this.booleanAttrs & 5) != 0;
   }

   public boolean isPrimaryId() {
      return (this.booleanAttrs & 8) != 0;
   }

   public boolean isPrimaryName() {
      return (this.booleanAttrs & 16) != 0;
   }

   public boolean isManaged() {
      return (this.booleanAttrs & 32) != 0;
   }

   public boolean isValidForAdvancedFind() {
      return (this.booleanAttrs & 64) != 0;
   }

   @Override
   public String toString() {
      return this.logicalName;
   }

   public static final class Builder {
      private String logicalName;
      private MSCrmDataType type = MSCrmDataType.OTHER;
      private String schemaName;
      private String displayName;
      private String description;
      private AttributeRequiredLevel requiredLevel = AttributeRequiredLevel.None;
      private boolean validForCreate = true;
      private boolean validForRead = true;
      private boolean validForUpdate = true;
      private boolean primaryId;
      private boolean primaryName;
      private boolean managed = true;
      private boolean validForAdvancedFind = true;

      public MSCrmAttribute.Builder setLogicalName(String var1) {
         this.logicalName = ArgCheck.checkNotEmpty(var1, "name");
         return this;
      }

      public MSCrmAttribute.Builder setType(MSCrmDataType var1) {
         this.type = Preconditions.checkNotNull(var1, "type");
         return this;
      }

      public MSCrmAttribute.Builder setSchemaName(String var1) {
         this.schemaName = ArgCheck.checkNotEmpty(var1, "name");
         return this;
      }

      public MSCrmAttribute.Builder setDisplayName(String var1) {
         this.displayName = ArgCheck.checkNotEmpty(var1, "displayName");
         return this;
      }

      public MSCrmAttribute.Builder setDescription(String var1) {
         this.description = var1;
         return this;
      }

      public MSCrmAttribute.Builder setRequiredLevel(AttributeRequiredLevel var1) {
         this.requiredLevel = Preconditions.checkNotNull(var1, "requiredLevel");
         return this;
      }

      public MSCrmAttribute.Builder setValidForCreate(boolean var1) {
         this.validForCreate = var1;
         return this;
      }

      public MSCrmAttribute.Builder setValidForRead(boolean var1) {
         this.validForRead = var1;
         return this;
      }

      public MSCrmAttribute.Builder setValidForUpdate(boolean var1) {
         this.validForUpdate = var1;
         return this;
      }

      public MSCrmAttribute.Builder setPrimaryId(boolean var1) {
         this.primaryId = var1;
         return this;
      }

      public MSCrmAttribute.Builder setPrimaryName(boolean var1) {
         this.primaryName = var1;
         return this;
      }

      public MSCrmAttribute.Builder setManaged(boolean var1) {
         this.managed = var1;
         return this;
      }

      public MSCrmAttribute.Builder setValidForAdvancedFind(boolean var1) {
         this.validForAdvancedFind = var1;
         return this;
      }

      public MSCrmAttribute build() {
         Preconditions.checkState(!Strings.isNullOrEmpty(this.logicalName), "Must provide a name");
         if (this.schemaName == null) {
            this.schemaName = this.logicalName;
         }

         if (this.displayName == null) {
            this.displayName = this.logicalName;
         }

         return new MSCrmAttribute(
            this.logicalName,
            this.type,
            this.schemaName,
            this.displayName,
            this.description,
            this.requiredLevel,
            this.validForCreate,
            this.validForRead,
            this.validForUpdate,
            this.primaryId,
            this.primaryName,
            this.managed,
            this.validForAdvancedFind
         );
      }
   }
}
