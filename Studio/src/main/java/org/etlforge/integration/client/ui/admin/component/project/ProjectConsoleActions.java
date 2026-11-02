package org.etlforge.integration.client.ui.admin.component.project;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import javax.swing.Action;
import org.etlforge.integration.client.ui.project.info.ProjectConsoleAction;

public final class ProjectConsoleActions {
   private final ImmutableList<ProjectConsoleAction> actions;

   public ProjectConsoleActions(Iterable<Action> var1, Iterable<Action> var2, Iterable<Action> var3) {
      this.actions = ImmutableList.copyOf(
         Iterables.concat(
            Iterables.transform(var1, ProjectConsoleAction.Category.PROJECT),
            Iterables.transform(var2, ProjectConsoleAction.Category.OPERATION),
            Iterables.transform(var3, ProjectConsoleAction.Category.SCHEDULE)
         )
      );
   }

   public Iterable<ProjectConsoleAction> getActions() {
      return this.actions;
   }
}
