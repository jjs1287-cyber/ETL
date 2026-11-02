package org.etlforge.integration.client.ui.interchange.cli;

import java.util.Collections;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.collections.Iterables2;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.path.ProjectPathQuery;

public abstract class AbstractProjectPathCommandExecutor implements CommandExecutor {
   private final CurrentProjectService currentProject;
   private final EntityFilter filter;

   protected AbstractProjectPathCommandExecutor(CurrentProjectService var1) {
      this(var1, EntityFilters.ACCEPTS_ALL);
   }

   protected AbstractProjectPathCommandExecutor(CurrentProjectService var1, EntityFilter var2) {
      this.currentProject = var1;
      this.filter = var2;
   }

   @Override
   public final void execute(String[] var1) throws CommandException {
      String var2 = this.getPath(var1);
      Set var3 = this.findItems(var2);
      if (var3.isEmpty()) {
         this.noMatchingItems(var2);
      } else {
         this.processMatchingItems(var3);
      }
   }

   private String getPath(String[] var1) {
      return StringUtils.join(var1, " ");
   }

   private Set<IntegrationEntity> findItems(String var1) throws CommandException {
      IntegrationProject var2 = this.getProject();
      if (var2 == null) {
         return Collections.emptySet();
      }

      try {
         ProjectPathQuery var3 = new ProjectPathQuery(var2);
         Set var4 = var3.findItems(var1);
         Iterables2.removeIfNot(var4, this.filter);
         return var4;
      } catch (IllegalArgumentException var5) {
         throw new CommandException(var5.getMessage());
      }
   }

   protected final IntegrationProject getProject() {
      return this.currentProject.getProject();
   }

   protected void noMatchingItems(String var1) throws CommandException {
   }

   protected abstract void processMatchingItems(Set<IntegrationEntity> var1) throws CommandException;
}
