package org.etlforge.integration.client.database.ui.primarykeys;

import com.google.common.collect.Sets;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.database.ui.DatabaseColumnsTable;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.ui.dialog.DialogTrigger;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class ColumnEntryDialog extends WizardStyleDialog {
   private final DatabaseColumnsTable columnTable;

   private ColumnEntryDialog(Window var1, DatabaseObject var2, Set<DatabaseColumn> var3) {
      super(var1, "Select the primary keys");
      this.columnTable = new DatabaseColumnsTable(var2, true);
      this.columnTable.setCheckedColumns(var3);
      this.columnTable.installToggleCheckAction();
      JComponent var4 = this.createInputArea(var2);
      this.getDefaultLayout().center(var4);
      this.pack();
      this.adjustInitialSize();
      this.manageLocation();
   }

   private JComponent createInputArea(DatabaseObject var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 5);
      JLabel var3 = new JLabel("Select the primary keys for " + var1.getName());
      this.columnTable.setCaption(null);
      var2.northToSouth(var3, this.columnTable, this.createUnselectAllButton());
      return WizardStyleDialog.wrapUi(var2);
   }

   private JComponent createUnselectAllButton() {
      KongaButton var1 = new KongaButton("&Unselect all");
      var1.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            ColumnEntryDialog.this.columnTable.setCheckedColumns(new HashSet<>());
         }
      });
      return InvisiblePanel.of(var1);
   }

   private void adjustInitialSize() {
      Dimension var1 = this.getSize();
      if (var1.height > 500) {
         var1.height = 500;
         this.setSize(var1);
      }
   }

   public static ColumnEntryDialog display(DatabaseObject var0, Set<DatabaseColumn> var1, DialogTrigger var2) {
      ColumnEntryDialog var3 = new ColumnEntryDialog(var2.getDialogOwner(), var0, var1);
      var3.setVisible(true);
      return var3;
   }

   public Set<DatabaseColumn> getSelectedKeys() {
      return this.wasOkPressed() ? Sets.newLinkedHashSet(this.columnTable.getCheckedColumns()) : null;
   }
}
