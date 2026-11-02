package org.etlforge.ui.log;

import java.util.logging.Level;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.TableCellEditor;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;

public final class LogLevelSelector implements UiProvider {
   private final JComboBox comboBox;

   public LogLevelSelector() {
      this(new Level[]{Level.OFF, Level.ALL, Level.CONFIG, Level.SEVERE, Level.WARNING, Level.INFO, Level.FINE, Level.FINER, Level.FINEST});
   }

   public LogLevelSelector(Level[] var1) {
      this.comboBox = new JComboBox<>(var1);
      this.comboBox.setMaximumRowCount(Math.min(var1.length, 12));
   }

   public void setSelectedLevel(Level var1) {
      this.comboBox.setSelectedItem(var1);
   }

   public Level getSelectedLevel() {
      return (Level)this.comboBox.getSelectedItem();
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.comboBox);
   }

   @Override
   public void requestFocus() {
      this.comboBox.requestFocusInWindow();
   }

   public void setEnabled(boolean var1) {
      this.comboBox.setEnabled(var1);
   }

   public TableCellEditor asTableEditor() {
      return new DefaultCellEditor(this.comboBox);
   }
}
