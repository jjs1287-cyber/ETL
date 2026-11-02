package org.etlforge.integration.client.ui.interchange.entity.transformation.page.cli;

import org.apache.commons.lang.StringUtils;
import org.etlforge.cli.Command;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.cli.SingleArgumentCommand;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.NodePathCompleter;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperTargetTree;

public final class TargetNodePathCommand implements Command {
   private final TransformationPage page;
   private final String name;
   private final Command impl;

   public TargetNodePathCommand(TransformationPage var1, String var2, CommandExecutor var3) {
      this.page = var1;
      this.name = var2;
      this.impl = new SingleArgumentCommand(var2, var3);
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
   public String complete(String var1) {
      String var2 = this.name + " ";
      return var1.startsWith(var2) ? this.completePath(var1, var2) : this.completeCommand(var1);
   }

   private String completePath(String var1, String var2) {
      String[] var3 = var1.split(" ");
      if (var3.length > 2) {
         return null;
      }

      TreeMapperTargetTree var4 = this.page.getTreeMapper().m_targetTreeComponent;
      NodePathCompleter.Completion var5 = NodePathUtils.completePath(var4, var1, var2);
      return var5 != null ? var5.value : null;
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
}
