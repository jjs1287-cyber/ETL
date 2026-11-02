package org.etlforge.integration.client.ui.structure.editor.db;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.database.DbInsertMode;
import org.etlforge.ui.Enablable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class InsertUpdateModeSelector implements UiProvider, Enablable {
   private static final Boolean UPDATE_INSERT_SUPPORTED = false;
   private JComponent selectorPanel;
   private JRadioButton insertUpdateSelector;
   private JRadioButton insertOnlySelector;
   private JRadioButton updateOnlySelector;
   private JRadioButton updateInsertSelector;

   public InsertUpdateModeSelector(boolean var1) {
      this.createRadioPanel();
      this.setAvailableOptions(var1);
      this.setInitialMode(var1);
   }

   public DbInsertMode getMode() {
      if (this.insertUpdateSelector.isSelected()) {
         return DbInsertMode.INSERT_UPDATE;
      } else if (this.insertOnlySelector.isSelected()) {
         return DbInsertMode.INSERT_ONLY;
      } else if (this.updateOnlySelector.isSelected()) {
         return DbInsertMode.UPDATE_ONLY;
      } else if (UPDATE_INSERT_SUPPORTED && this.updateInsertSelector.isSelected()) {
         return DbInsertMode.UPDATE_INSERT;
      } else {
         throw new IllegalStateException("An insert/update mode should have been selected by now");
      }
   }

   public void setMode(DbInsertMode var1) {
      JRadioButton var2 = null;
      switch (var1) {
         case INSERT_UPDATE:
            var2 = this.insertUpdateSelector;
            break;
         case INSERT_ONLY:
            var2 = this.insertOnlySelector;
            break;
         case UPDATE_ONLY:
            var2 = this.updateOnlySelector;
            break;
         case UPDATE_INSERT:
            var2 = this.updateInsertSelector;
            break;
         default:
            throw new RuntimeException(var1 + " is not supported");
      }

      var2.setSelected(true);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.selectorPanel);
   }

   @Override
   public void requestFocus() {
   }

   @Override
   public boolean isEnabled() {
      return false;
   }

   @Override
   public void setEnabled(boolean var1) {
      this.setAvailableOptions(var1);
   }

   private void createRadioPanel() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.setAnchor(OneColumnPanel.WEST);
      var1.withInsets(2, 2, 0, 2);
      String[] var2 = getRadioButtonLabels();
      JRadioButton[] var3 = ButtonUtils.getExclusiveRadioButtons(var2);

      for (JRadioButton var7 : var3) {
         TextStyles.DefaultRadioButtonText.makeOver(var7);
         var1.add(var7);
      }

      this.insertUpdateSelector = var3[0];
      this.insertOnlySelector = var3[1];
      this.updateOnlySelector = var3[2];
      if (UPDATE_INSERT_SUPPORTED) {
         this.updateInsertSelector = var3[3];
      }

      this.selectorPanel = InvisiblePanel.of(var1.deeplyNonOpaque());
   }

   private void setAvailableOptions(boolean var1) {
      this.insertOnlySelector.setEnabled(true);
      this.insertUpdateSelector.setEnabled(var1);
      this.updateOnlySelector.setEnabled(var1);
      if (UPDATE_INSERT_SUPPORTED) {
         this.updateInsertSelector.setEnabled(var1);
      }

      if (!var1) {
         this.insertOnlySelector.setSelected(true);
      }
   }

   private void setInitialMode(boolean var1) {
      if (var1) {
         this.insertUpdateSelector.setSelected(true);
      } else {
         this.insertOnlySelector.setSelected(true);
      }
   }

   private static String[] getRadioButtonLabels() {
      ArrayList var0 = Lists.newArrayList();
      var0.add(Strings.get("Db.InsertUpdateMode.InsertUpdate"));
      var0.add(Strings.get("Db.InsertUpdateMode.InsertOnly"));
      var0.add(Strings.get("Db.InsertUpdateMode.UpdateOnly"));
      if (UPDATE_INSERT_SUPPORTED) {
         var0.add(Strings.get("Db.InsertUpdateMode.UpdateInsert"));
      }

      return (String[]) var0.toArray(new String[var0.size()]);
   }
}
