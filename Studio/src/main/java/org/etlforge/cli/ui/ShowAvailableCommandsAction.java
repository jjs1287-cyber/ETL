package org.etlforge.cli.ui;

import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import org.etlforge.cli.Command;
import org.etlforge.cli.CommandCollection;
import org.etlforge.cli.CommandModule;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.list.DefaultKongaListFactory;
import org.etlforge.ui.widget.list.KongaList;

public final class ShowAvailableCommandsAction extends AbstractAction {
   private final CommandModule commands;

   public ShowAvailableCommandsAction(CommandModule var1) {
      this.commands = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ShowAvailableCommandsAction.this.displayAvailableCommands();
         }
      });
   }

   private void displayAvailableCommands() {
      List var1 = this.collectCommandSyntaxes();
      KongaList var2 = this.createListUi(var1);
      UiProviderDialogDisplayer.display(UiUtils.getActiveWindow(), var2, "Available Commands");
   }

   private List<String> collectCommandSyntaxes() {
      ArrayList var1 = Lists.newArrayList();
      this.collectCommandSyntaxes(this.commands, var1);
      Collections.sort(var1, String.CASE_INSENSITIVE_ORDER);
      return var1;
   }

   private void collectCommandSyntaxes(CommandCollection var1, List<String> var2) {
      for (Command var4 : var1) {
         if (var4 instanceof CommandCollection) {
            this.collectCommandSyntaxes((CommandCollection)var4, var2);
         } else {
            var2.add(var4.getSyntax());
         }
      }
   }

   private KongaList createListUi(List<String> var1) {
      DefaultKongaListFactory var2 = new DefaultKongaListFactory();
      var2.setVisibleRowCount(12);
      return var2.newList(var1);
   }
}
