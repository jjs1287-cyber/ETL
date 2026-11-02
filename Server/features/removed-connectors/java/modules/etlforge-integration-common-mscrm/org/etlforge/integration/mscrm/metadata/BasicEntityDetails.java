package org.etlforge.integration.mscrm.metadata;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.system.ArgCheck;

public final class BasicEntityDetails implements ConnectorObject {
   private static final long serialVersionUID = 2526421754245061768L;
   private final String logicalName;
   private final int entityTypeCode;
   private final String displayName;
   private final String pluralName;
   private final String description;
   private final String primaryNameAttribute;
   private final String primaryIdAttribute;
   private final String ownershipType;
   private final String reportViewName;
   private final long booleanAttributes;

   public static BasicEntityDetails.Builder builder() {
      return new BasicEntityDetails.Builder();
   }

   public BasicEntityDetails(String var1, int var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, long var10) {
      this.logicalName = ArgCheck.checkNotEmpty(var1, "logicalName");
      this.entityTypeCode = var2;
      this.displayName = ArgCheck.checkNotEmpty(var3, "displayName");
      this.pluralName = ArgCheck.checkNotEmpty(var4, "pluralName");
      this.description = Strings.nullToEmpty(var5);
      this.primaryNameAttribute = Strings.nullToEmpty(var6);
      this.primaryIdAttribute = Strings.nullToEmpty(var7);
      this.ownershipType = Strings.nullToEmpty(var8);
      this.reportViewName = Strings.nullToEmpty(var9);
      this.booleanAttributes = ArgCheck.checkNotNegative(var10, "booleanAttributes");
   }

   @Override
   public String getName() {
      return this.logicalName;
   }

   public String getLogicalName() {
      return this.logicalName;
   }

   public int getEntityTypeCode() {
      return this.entityTypeCode;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public String getPluralName() {
      return this.pluralName;
   }

   @Override
   public String getDescription() {
      return this.description;
   }

   public String getPrimaryNameAttribute() {
      return this.primaryNameAttribute;
   }

   public String getPrimaryIdAttribute() {
      return this.primaryIdAttribute;
   }

   public String getOwnershipType() {
      return this.ownershipType;
   }

   public String getReportViewName() {
      return this.reportViewName;
   }

   public boolean autoRouteToOwnerQueue() {
      return (this.booleanAttributes & 1L) != 0L;
   }

   public boolean canBeInManyToMany() {
      return (this.booleanAttributes & 2L) != 0L;
   }

   public boolean canBePrimaryEntityInRelationship() {
      return (this.booleanAttributes & 4L) != 0L;
   }

   public boolean canBeRelatedEntityInRelationship() {
      return (this.booleanAttributes & 8L) != 0L;
   }

   public boolean canCreateAttributes() {
      return (this.booleanAttributes & 16L) != 0L;
   }

   public boolean canCreateCharts() {
      return (this.booleanAttributes & 32L) != 0L;
   }

   public boolean canCreateForms() {
      return (this.booleanAttributes & 64L) != 0L;
   }

   public boolean canCreateViews() {
      return (this.booleanAttributes & 128L) != 0L;
   }

   public boolean canModifyAdditionalSettings() {
      return (this.booleanAttributes & 256L) != 0L;
   }

   public boolean canTriggerWorkflow() {
      return (this.booleanAttributes & 512L) != 0L;
   }

   public boolean isActivity() {
      return (this.booleanAttributes & 1024L) != 0L;
   }

   public boolean isActivityParty() {
      return (this.booleanAttributes & 2048L) != 0L;
   }

   public boolean isAuditEnabled() {
      return (this.booleanAttributes & 4096L) != 0L;
   }

   public boolean isAvailableOffline() {
      return (this.booleanAttributes & 8192L) != 0L;
   }

   public boolean isChildEntity() {
      return (this.booleanAttributes & 16384L) != 0L;
   }

   public boolean isCustomEntity() {
      return (this.booleanAttributes & 32768L) != 0L;
   }

   public boolean isCustomizable() {
      return (this.booleanAttributes & 65536L) != 0L;
   }

   public boolean isDocumentManagementEnabled() {
      return (this.booleanAttributes & 131072L) != 0L;
   }

   public boolean isDuplicateDetectionEnabled() {
      return (this.booleanAttributes & 262144L) != 0L;
   }

   public boolean isEnabledForCharts() {
      return (this.booleanAttributes & 524288L) != 0L;
   }

   public boolean isImportable() {
      return (this.booleanAttributes & 1048576L) != 0L;
   }

   public boolean isIntersect() {
      return (this.booleanAttributes & 2097152L) != 0L;
   }

   public boolean isMailMergeEnabled() {
      return (this.booleanAttributes & 4194304L) != 0L;
   }

   public boolean isManaged() {
      return (this.booleanAttributes & 8388608L) != 0L;
   }

   public boolean isMappable() {
      return (this.booleanAttributes & 16777216L) != 0L;
   }

   public boolean isReadingPaneEnabled() {
      return (this.booleanAttributes & 33554432L) != 0L;
   }

   public boolean isRenameable() {
      return (this.booleanAttributes & 67108864L) != 0L;
   }

   public boolean isValidForAdvancedFind() {
      return (this.booleanAttributes & 134217728L) != 0L;
   }

   public boolean isValidForQueue() {
      return (this.booleanAttributes & 268435456L) != 0L;
   }

   public boolean isVisibleInMobile() {
      return (this.booleanAttributes & 536870912L) != 0L;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(
         this.booleanAttributes,
         this.entityTypeCode,
         this.logicalName,
         this.description,
         this.displayName,
         this.ownershipType,
         this.pluralName,
         this.primaryIdAttribute,
         this.primaryNameAttribute,
         this.reportViewName
      );
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof BasicEntityDetails)) {
         return false;
      }

      BasicEntityDetails var2 = (BasicEntityDetails)var1;
      return this.booleanAttributes == var2.booleanAttributes
         && this.entityTypeCode == var2.entityTypeCode
         && this.logicalName.equals(var2.logicalName)
         && this.description.equals(var2.description)
         && this.displayName.equals(var2.displayName)
         && this.ownershipType.equals(var2.ownershipType)
         && this.pluralName.equals(var2.pluralName)
         && this.primaryIdAttribute.equals(var2.primaryIdAttribute)
         && this.primaryNameAttribute.equals(var2.primaryNameAttribute)
         && this.reportViewName.equals(var2.reportViewName);
   }

   public static final class Builder {
      private String logicalName;
      private int entityTypeCode;
      private String displayName;
      private String pluralName;
      private String description;
      private String primaryNameAttribute;
      private String primaryIdAttribute;
      private String ownershipType;
      private String reportViewName;
      private long booleanAttributes;

      public BasicEntityDetails.Builder logicalName(String var1) {
         this.logicalName = ArgCheck.checkNotEmpty(var1);
         return this;
      }

      public BasicEntityDetails.Builder displayName(String var1) {
         this.displayName = Strings.nullToEmpty(var1);
         return this;
      }

      public BasicEntityDetails.Builder pluralName(String var1) {
         this.pluralName = Strings.nullToEmpty(var1);
         return this;
      }

      public BasicEntityDetails.Builder entityTypeCode(int var1) {
         this.entityTypeCode = var1;
         return this;
      }

      public BasicEntityDetails.Builder description(String var1) {
         this.description = Strings.nullToEmpty(var1);
         return this;
      }

      public BasicEntityDetails.Builder primaryNameAttribute(String var1) {
         this.primaryNameAttribute = Strings.nullToEmpty(var1);
         return this;
      }

      public BasicEntityDetails.Builder primaryIdAttribute(String var1) {
         this.primaryIdAttribute = Strings.nullToEmpty(var1);
         return this;
      }

      public BasicEntityDetails.Builder ownershipType(String var1) {
         this.ownershipType = Strings.nullToEmpty(var1);
         return this;
      }

      public BasicEntityDetails.Builder reportViewName(String var1) {
         this.reportViewName = Strings.nullToEmpty(var1);
         return this;
      }

      private void setBit(long var1, boolean var3) {
         if (var3) {
            this.booleanAttributes |= var1;
         } else {
            this.booleanAttributes &= ~var1;
         }
      }

      public BasicEntityDetails.Builder autoRouteToOwnerQueue(boolean var1) {
         this.setBit(1L, var1);
         return this;
      }

      public BasicEntityDetails.Builder canBeInManyToMany(boolean var1) {
         this.setBit(2L, var1);
         return this;
      }

      public BasicEntityDetails.Builder canBePrimaryEntityInRelationship(boolean var1) {
         this.setBit(4L, var1);
         return this;
      }

      public BasicEntityDetails.Builder canBeRelatedEntityInRelationship(boolean var1) {
         this.setBit(8L, var1);
         return this;
      }

      public BasicEntityDetails.Builder canCreateAttributes(boolean var1) {
         this.setBit(16L, var1);
         return this;
      }

      public BasicEntityDetails.Builder canCreateCharts(boolean var1) {
         this.setBit(32L, var1);
         return this;
      }

      public BasicEntityDetails.Builder canCreateForms(boolean var1) {
         this.setBit(64L, var1);
         return this;
      }

      public BasicEntityDetails.Builder canCreateViews(boolean var1) {
         this.setBit(128L, var1);
         return this;
      }

      public BasicEntityDetails.Builder canModifyAdditionalSettings(boolean var1) {
         this.setBit(256L, var1);
         return this;
      }

      public BasicEntityDetails.Builder canTriggerWorkflow(boolean var1) {
         this.setBit(512L, var1);
         return this;
      }

      public BasicEntityDetails.Builder activity(boolean var1) {
         this.setBit(1024L, var1);
         return this;
      }

      public BasicEntityDetails.Builder activityParty(boolean var1) {
         this.setBit(2048L, var1);
         return this;
      }

      public BasicEntityDetails.Builder auditEnabled(boolean var1) {
         this.setBit(4096L, var1);
         return this;
      }

      public BasicEntityDetails.Builder availableOffline(boolean var1) {
         this.setBit(8192L, var1);
         return this;
      }

      public BasicEntityDetails.Builder childEntity(boolean var1) {
         this.setBit(16384L, var1);
         return this;
      }

      public BasicEntityDetails.Builder customEntity(boolean var1) {
         this.setBit(32768L, var1);
         return this;
      }

      public BasicEntityDetails.Builder customizable(boolean var1) {
         this.setBit(65536L, var1);
         return this;
      }

      public BasicEntityDetails.Builder documentManagementEnabled(boolean var1) {
         this.setBit(131072L, var1);
         return this;
      }

      public BasicEntityDetails.Builder duplicateDetectionEnabled(boolean var1) {
         this.setBit(262144L, var1);
         return this;
      }

      public BasicEntityDetails.Builder enabledForCharts(boolean var1) {
         this.setBit(524288L, var1);
         return this;
      }

      public BasicEntityDetails.Builder importable(boolean var1) {
         this.setBit(1048576L, var1);
         return this;
      }

      public BasicEntityDetails.Builder intersect(boolean var1) {
         this.setBit(2097152L, var1);
         return this;
      }

      public BasicEntityDetails.Builder mailMergeEnabled(boolean var1) {
         this.setBit(4194304L, var1);
         return this;
      }

      public BasicEntityDetails.Builder managed(boolean var1) {
         this.setBit(8388608L, var1);
         return this;
      }

      public BasicEntityDetails.Builder mappable(boolean var1) {
         this.setBit(16777216L, var1);
         return this;
      }

      public BasicEntityDetails.Builder readingPaneEnabled(boolean var1) {
         this.setBit(33554432L, var1);
         return this;
      }

      public BasicEntityDetails.Builder renameable(boolean var1) {
         this.setBit(67108864L, var1);
         return this;
      }

      public BasicEntityDetails.Builder validForAdvancedFind(boolean var1) {
         this.setBit(134217728L, var1);
         return this;
      }

      public BasicEntityDetails.Builder validForQueue(boolean var1) {
         this.setBit(268435456L, var1);
         return this;
      }

      public BasicEntityDetails.Builder visibleInMobile(boolean var1) {
         this.setBit(536870912L, var1);
         return this;
      }

      public BasicEntityDetails build() {
         Preconditions.checkState(this.logicalName != null, "Must provide a logical name before building");
         if (this.displayName == null) {
            this.displayName = this.logicalName;
         }

         if (this.pluralName == null) {
            this.pluralName = this.logicalName;
         }

         return new BasicEntityDetails(
            this.logicalName,
            this.entityTypeCode,
            this.displayName,
            this.pluralName,
            this.description,
            this.primaryNameAttribute,
            this.primaryIdAttribute,
            this.ownershipType,
            this.reportViewName,
            this.booleanAttributes
         );
      }
   }
}
