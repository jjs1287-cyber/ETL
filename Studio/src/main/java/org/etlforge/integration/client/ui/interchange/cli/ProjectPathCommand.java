package org.etlforge.integration.client.ui.interchange.cli;

import org.apache.commons.lang.StringUtils;
import org.etlforge.cli.Command;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.cli.CommandFactory;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.path.ProjectPathCompleter;

public final class ProjectPathCommand implements Command {
   private final String name;
   private final Command impl;
   private final CurrentProjectService currentProject;

   public ProjectPathCommand(String var1, CurrentProjectService var2, CommandExecutor var3) {
      this.name = var1;
      this.currentProject = var2;
      this.impl = CommandFactory.newCommand(var1 + " {0}...", var3);
   }

   @Override
   public String complete(String var1) {
      String var2 = this.name + " ";
      return var1.startsWith(var2) ? this.completePath(var1, var2) : this.completeCommand(var1);
   }

   private String completePath(String var1, String var2) {
      String var3 = var1.substring(var2.length());
      IntegrationProject var4 = this.currentProject.getProject();
      if (var4 == null) {
         return var3;
      }

      ProjectPathCompleter var5 = new ProjectPathCompleter(var4);
      String var6 = var5.complete(var3);
      return var6.substring(var3.length());
   }

   private String completeCommand(String var1) {
      String var2 = this.impl.complete(var1);
      if (StringUtils.isNotEmpty(var2) && !var2.endsWith(" ")) {
         var2 = var2 + " ";
      }

      return var2;
   }

   @Override
   public CommandExecutor executor() {
      return this.impl.executor();
   }

   @Override
   public String getSyntax() {
      return this.impl.getSyntax();
   }

   @Override
   public String[] parse(String var1) {
      return this.impl.parse(var1);
   }

   @Override
   public String toString() {
      return this.impl.toString();
   }
}
