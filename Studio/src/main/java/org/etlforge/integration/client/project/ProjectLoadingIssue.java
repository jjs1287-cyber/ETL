package org.etlforge.integration.client.project;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.entity.EntityType;

public final class ProjectLoadingIssue implements Comparable<ProjectLoadingIssue> {
   private ProjectLoadingIssue.Category category;
   private final ProjectLoadingIssue.Level level;
   private final String message;
   private String itemName;
   private EntityType itemType;

   public static ProjectLoadingIssue warning(String var0) {
      return new ProjectLoadingIssue(ProjectLoadingIssue.Level.WARNING, var0);
   }

   public static ProjectLoadingIssue fatal(String var0) {
      return new ProjectLoadingIssue(ProjectLoadingIssue.Level.FATAL, var0);
   }

   private ProjectLoadingIssue(ProjectLoadingIssue.Level var1, String var2) {
      Preconditions.checkArgument(StringUtils.isNotBlank(var2), "message may not be null or empty");
      this.category = ProjectLoadingIssue.Category.OTHER;
      this.level = var1;
      this.message = var2;
   }

   public String getItemName() {
      return this.itemName;
   }

   public EntityType getItemType() {
      return this.itemType;
   }

   public void setEntityInfo(String var1, EntityType var2) {
      this.itemName = var1;
      this.itemType = var2;
   }

   public ProjectLoadingIssue.Level getLevel() {
      return this.level;
   }

   public String getMessage() {
      return this.message;
   }

   public ProjectLoadingIssue.Category getCategory() {
      return this.category;
   }

   public void setCategory(ProjectLoadingIssue.Category var1) {
      Preconditions.checkNotNull(var1, "category");
      this.category = var1;
   }

   public int compareTo(ProjectLoadingIssue var1) {
      return ComparisonChain.start().compare(this.level, var1.level).compare(this.message, var1.message).result();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof ProjectLoadingIssue)) {
         return false;
      }

      ProjectLoadingIssue var2 = (ProjectLoadingIssue)var1;
      return this.category == var2.category
         && this.level == var2.level
         && this.message.equals(var2.message)
         && Objects.equal(this.itemName, var2.itemName)
         && Objects.equal(this.itemType, var2.itemType);
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.category.hashCode();
      var1 = 31 * var1 + this.level.hashCode();
      var1 = 31 * var1 + this.message.hashCode();
      var1 = 31 * var1 + (this.itemName != null ? this.itemName.hashCode() : 0);
      return 31 * var1 + (this.itemType != null ? this.itemType.hashCode() : 0);
   }

   @Override
   public String toString() {
      return this.category + " - " + this.level + ": " + this.message + (this.itemName != null ? " [" + this.itemName + "]" : "");
   }

   public enum Category {
      VERSION_INCOMPATIBILITY {
         @Override
         public String toString() {
            return "Incompatible versions";
         }
      },
      OTHER {
         @Override
         public String toString() {
            return "Other";
         }
      };

      Category() {
      }
   }

   public enum Level {
      WARNING,
      FATAL;
   }
}
