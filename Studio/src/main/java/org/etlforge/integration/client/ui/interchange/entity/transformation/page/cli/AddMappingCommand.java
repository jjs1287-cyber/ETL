package org.etlforge.integration.client.ui.interchange.entity.transformation.page.cli;

import org.etlforge.cli.Command;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.cli.CommandFactory;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.NodePathCompleter;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperSourceTree;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperTargetTree;
import org.etlforge.integration.structure.mapping.PathSeparator;

public final class AddMappingCommand implements Command {
   private static final String NAME = "map";
   private final TransformationPage page;
   private final Command impl;

   public AddMappingCommand(TransformationPage var1) {
      this.page = var1;
      this.impl = CommandFactory.newCommand("map {0} to {1}", new AddMappingExecutor(var1));
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
   public CommandExecutor executor() {
      return this.impl.executor();
   }

   @Override
   public String complete(String var1) {
      if (var1.startsWith("map ")) {
         String[] var2 = var1.split(" ");
         switch (var2.length) {
            case 2:
               if (endsWithWhitespace(var1)) {
                  return "to ";
               }

               TreeMapperSourceTree var7 = this.getSourceTree();
               NodePathCompleter.Completion var8 = NodePathUtils.completePath(var7, var1, "map ");
               if (var8 != null) {
                  if (isLeaf(var8)) {
                     return var8.value + " to ";
                  }

                  return var8.value;
               }
               break;
            case 3:
               if (endsWithWhitespace(var1)) {
                  return null;
               }

               String var6 = var2[2];
               if (var6.equals("to")) {
                  return " ";
               }

               if (var6.equals("t")) {
                  return "o ";
               }

               return null;
            case 4:
               if (endsWithWhitespace(var1)) {
                  return null;
               }

               TreeMapperTargetTree var3 = this.getTargetTree();
               String var4 = "map " + var2[1] + " to ";
               NodePathCompleter.Completion var5 = NodePathUtils.completePath(var3, var1, var4);
               if (var5 != null) {
                  return var5.value;
               }
            default:
               return null;
         }
      }

      return null;
   }

   private static boolean endsWithWhitespace(String var0) {
      return Character.isWhitespace(var0.charAt(var0.length() - 1));
   }

   private TreeMapperSourceTree getSourceTree() {
      return this.page.getTreeMapper().m_sourceTreeComponent;
   }

   private static boolean isLeaf(NodePathCompleter.Completion var0) {
      return !var0.multiMatch && !PathSeparator.terminates(var0.value);
   }

   private TreeMapperTargetTree getTargetTree() {
      return this.page.getTreeMapper().m_targetTreeComponent;
   }
}
