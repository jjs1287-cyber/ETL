package org.etlforge.integration.client.ui.structure.editor.db;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.ui.undo.UndoRedoInstaller;
import org.etlforge.ui.widget.button.KongaCheckBox;

final class ManualModeSwitch {
   private final JCheckBox writeSQL;

   public ManualModeSwitch() {
      String var1 = PackageResources.SourceStartPage.Labels.WRITE_SQL;
      this.writeSQL = new KongaCheckBox(var1);
   }

   public JComponent getUi() {
      return this.writeSQL;
   }

   public void installUndoRedo(UndoRedoInstaller var1) {
      var1.install(this.writeSQL);
   }

   public boolean isSelected() {
      return this.writeSQL.isEnabled() && this.writeSQL.isSelected();
   }

   public void setSelected(boolean var1) {
      this.writeSQL.setSelected(var1);
   }

   public void setDbLocation(DatabaseLocation var1) {
      boolean var2 = true;
      if (var1 != null) {
         DriverDescriptor var3 = var1.getDriver();
         var2 = var3 != null && var3.isManualSqlStatementSupported();
      }

      this.writeSQL.setEnabled(var2);
      this.writeSQL.setToolTipText(var2 ? null : "Manual SQL statements are supported for JDBC only.");
   }

   public void requestFocus() {
      this.writeSQL.requestFocusInWindow();
   }
}
