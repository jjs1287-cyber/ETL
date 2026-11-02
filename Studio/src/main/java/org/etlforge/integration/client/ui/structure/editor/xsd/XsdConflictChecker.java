package org.etlforge.integration.client.ui.structure.editor.xsd;

import java.awt.Component;
import java.util.List;
import java.util.Set;
import javax.swing.JLabel;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;

final class XsdConflictChecker {
   private final XsdConflictFinder finder;
   private final Component owner;

   public XsdConflictChecker(XsdConflictFinder var1, Component var2) {
      this.finder = var1;
      this.owner = var2;
   }

   public boolean resolveConflicts(CreateXsdResult var1, Set<ServerFile> var2) {
      List var3 = this.finder.findConflicts(var1, var2);
      return var3.isEmpty() ? true : this.askUser(var3);
   }

   private boolean askUser(List<ServerFile> var1) {
      UiProvider var2 = this.createMessage(var1);
      Answer var3 = Ask.continueOrCancel(this.owner, var2, "Overwrite files", Alert.Option.CANCEL, Alert.Type.WARNING);
      return var3.isContinue();
   }

   private UiProvider createMessage(List<ServerFile> var1) {
      KongaListFactory var2 = ComponentFactories.newKongaListFactory();
      var2.setVisibleRowCount(6);
      KongaList var3 = var2.newList(var1);
      JLabel var4 = new JLabel("The following XSD files already exist on the server and will be overwritten.");
      return new BorderLayoutBuilder(0, 5).northToSouth(var4, var3, this.createBottomMessage());
   }

   private UiProvider createBottomMessage() {
      return Grid.oneColumn(5)
         .addAll(
            "Click Continue to continue the wizard. The existing files will be overwritten.",
            "Click Cancel to return to the wizard and give the generated XSD files new names."
         );
   }
}
