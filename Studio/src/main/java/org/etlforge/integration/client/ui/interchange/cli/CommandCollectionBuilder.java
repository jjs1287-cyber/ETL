package org.etlforge.integration.client.ui.interchange.cli;

import java.util.List;
import java.util.Map;
import javax.swing.Action;
import org.etlforge.cli.Command;
import org.etlforge.cli.CommandCollection;
import org.etlforge.cli.DefaultCommandCollection;

public final class CommandCollectionBuilder {
   private final List<Command> standardCommands;
   private final Map<String, Action> actions;

   public CommandCollectionBuilder(List<Command> var1, Map<String, Action> var2) {
      this.standardCommands = var1;
      this.actions = var2;
   }

   public void setAdditionalActions(Map<String, Action> var1) {
      this.actions.putAll(var1);
   }

   public CommandCollection buildCollection() {
      DefaultCommandCollection var1 = new DefaultCommandCollection();
      var1.add(this.standardCommands);
      var1.setActions(this.actions);
      return var1;
   }
}
