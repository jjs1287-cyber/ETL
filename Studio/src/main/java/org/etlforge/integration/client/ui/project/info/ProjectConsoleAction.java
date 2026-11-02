package org.etlforge.integration.client.ui.project.info;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import javax.swing.Action;
import org.apache.commons.lang.StringUtils;
import org.etlforge.system.EnumWithId;

public final class ProjectConsoleAction {
   private final String category;
   private final Action action;

   public ProjectConsoleAction(String var1, Action var2) {
      Preconditions.checkArgument(StringUtils.isNotBlank(var1), "category may not be null or empty");
      this.category = var1;
      this.action = Preconditions.checkNotNull(var2, "action");
   }

   public String getCategory() {
      return this.category;
   }

   public Action getAction() {
      return this.action;
   }

   public enum Category implements EnumWithId<String>, Function<Action, ProjectConsoleAction> {
      PROJECT("project"),
      OPERATION("operation"),
      SCHEDULE("schedule");

      private final String id;

      Category(String var3) {
         this.id = var3;
      }

      public String getId() {
         return this.id;
      }

      public ProjectConsoleAction apply(Action var1) {
         return new ProjectConsoleAction(this.id, var1);
      }
   }
}
