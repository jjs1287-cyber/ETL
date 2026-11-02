package org.etlforge.integration.client.ui.interchange.entity.transformation.page;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import org.etlforge.application.Application;
import org.etlforge.cli.Command;
import org.etlforge.cli.CommandFactory;
import org.etlforge.cli.NoArgumentCommand;
import org.etlforge.cli.ui.ActionCommand;
import org.etlforge.collections.Collections2;
import org.etlforge.collections.Pair;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.cli.AddMappingCommand;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.cli.DeleteAllMappingsExecutor;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.cli.DeleteMappingExecutor;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.cli.LoadLastSourceFileExecutor;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.cli.LoadSourceCommandExecutor;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.cli.OpenFormulaBuilderExecutor;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.cli.SetDisplayModeExecutor;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.cli.TargetNodePathCommand;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.cli.ViewMapListExecutor;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperMode;

final class TransformationPageCommands {
   private final TransformationPage page;
   private final List<Command> commands;

   public TransformationPageCommands(TransformationPage var1) {
      this.page = var1;
      this.commands = Lists.newArrayList();
   }

   public Command[] build() {
      this.addActions();
      this.addLoadSource();
      this.addMappingCommands();
      this.addDeleteMappingsCommands();
      this.addSetDisplayModeCommands();
      return this.commands.toArray(new Command[this.commands.size()]);
   }

   private void addActions() {
      for (Pair var2 : Collections2.zip(
         Arrays.asList(
            "test",
            "test transformation",
            "test op",
            "test operation",
            "remove test data",
            "edit source",
            "edit target",
            "edit where clause",
            "edit update keys",
            "refresh",
            "search",
            "create operation",
            "import mappings",
            "export mappings",
            "migrate mappings"
         ),
         Arrays.asList(
            "action.ic.tx.TestTransformation",
            "action.ic.tx.TestTransformation",
            "action.ic.tx.TestOperation",
            "action.ic.tx.TestOperation",
            "action.ic.tx.RemoveData",
            "action.ic.tx.EditSource",
            "action.ic.tx.EditTarget",
            "action.ic.tx.EditWhereClause",
            "action.ic.tx.EditUpdateKeys",
            "action.ic.tx.Refresh",
            "action.ic.tx.Search",
            "action.ic.tx.CreateOperation",
            "action.ic.tx.ImportMappings",
            "action.ic.tx.ExportMappings",
            "action.ic.tx.Migrate"
         )
      )) {
         Action var3 = this.lookupAction((String)var2.second);
         if (var3 != null) {
            this.commands.add(new ActionCommand((String)var2.first, var3));
         }
      }
   }

   private Action lookupAction(String var1) {
      return Application.containsBean(var1) ? Application.getBean(var1, Action.class) : this.page.getPageAction(var1);
   }

   private void addLoadSource() {
      this.commands.add(CommandFactory.newCommand("load source [{0}]...", new LoadSourceCommandExecutor(this.page)));
      this.commands.add(CommandFactory.newCommand("load last source file", new LoadLastSourceFileExecutor(this.page)));
      this.commands.add(new NoArgumentCommand("view_map_list", new ViewMapListExecutor(this.page.getObject())));
   }

   private void addMappingCommands() {
      this.commands.add(new TargetNodePathCommand(this.page, "maptarget", new OpenFormulaBuilderExecutor(this.page)));
      this.commands.add(new AddMappingCommand(this.page));
   }

   private void addDeleteMappingsCommands() {
      DeleteMappingExecutor var1 = new DeleteMappingExecutor(this.page);

      for (String var5 : new String[]{"delete mapping", "unmap"}) {
         this.commands.add(new TargetNodePathCommand(this.page, var5, var1));
      }

      DeleteAllMappingsExecutor var7 = new DeleteAllMappingsExecutor(this.page);

      for (String var6 : new String[]{"del all mappings", "delete all mappings"}) {
         this.commands.add(CommandFactory.newCommand(var6, var7));
      }
   }

   private void addSetDisplayModeCommands() {
      for (TreeMapperMode var4 : TreeMapperMode.values()) {
         String var5 = "set " + var4.name() + " display";
         SetDisplayModeExecutor var6 = new SetDisplayModeExecutor(this.page, var4);
         this.commands.add(new NoArgumentCommand(var5, var6));
      }
   }
}
