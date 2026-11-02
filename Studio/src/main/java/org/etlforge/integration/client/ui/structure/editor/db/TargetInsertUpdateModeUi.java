package org.etlforge.integration.client.ui.structure.editor.db;

import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.integration.client.database.ui.primarykeys.PrimaryKeyDefiner;
import org.etlforge.integration.client.database.ui.primarykeys.PrimaryKeys;
import org.etlforge.integration.client.database.ui.primarykeys.TreeBasedPrimaryKeyDefiner;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.database.DbFields;
import org.etlforge.integration.database.DbInsertMode;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class TargetInsertUpdateModeUi implements UiProvider {
   private final DatabaseObject[] dbObjects;
   private final PrimaryKeyDefiner primaryKeyDefiner;
   private final InsertUpdateModeSelector modeSelector;
   private final JCheckBox autoTruncateChoice;
   private boolean includeAutoTruncateChoice;

   public TargetInsertUpdateModeUi(DatabaseObject[] var1) {
      this(var1, null);
   }

   public TargetInsertUpdateModeUi(DatabaseObject[] var1, PrimaryKeys[] var2) {
      this.dbObjects = var1;
      this.modeSelector = this.createModeSelector(var1, var2);
      this.primaryKeyDefiner = this.createPrimaryKeyDefiner(var1, var2);
      this.autoTruncateChoice = new KongaCheckBox("Allow truncation of character fields to avoid field length errors");
      this.includeAutoTruncateChoice = true;
   }

   private InsertUpdateModeSelector createModeSelector(DatabaseObject[] var1, PrimaryKeys[] var2) {
      boolean var3 = var2 != null ? atLeastOnePrimaryKeyExists(var2) : atLeastOnePrimaryKeyExists(var1);
      return new InsertUpdateModeSelector(var3);
   }

   private static boolean atLeastOnePrimaryKeyExists(PrimaryKeys[] var0) {
      for (PrimaryKeys var4 : var0) {
         if (var4.getPrimaryKeys().length > 0) {
            return true;
         }
      }

      return false;
   }

   private static boolean atLeastOnePrimaryKeyExists(DatabaseObject[] var0) {
      for (DatabaseObject var4 : var0) {
         if (var4.hasPrimaryKeys()) {
            return true;
         }
      }

      return false;
   }

   private PrimaryKeyDefiner createPrimaryKeyDefiner(DatabaseObject[] var1, PrimaryKeys[] var2) {
      TreeBasedPrimaryKeyDefiner var3 = new TreeBasedPrimaryKeyDefiner(var1, this.modeSelector);
      if (var2 != null && var2.length > 0) {
         var3.setPrimaryKeys(var2);
      }

      return var3;
   }

   public void setIncludeAutoTruncateChoice(boolean var1) {
      this.includeAutoTruncateChoice = var1;
   }

   public DbInsertMode getInsertMode() {
      return this.modeSelector.getMode();
   }

   public boolean isAutoTruncatingFields() {
      return this.autoTruncateChoice.isSelected();
   }

   public void setInsertMode(DbInsertMode var1) {
      this.modeSelector.setMode(var1);
   }

   public void editStructure(StructureToEdit var1) {
      PrimaryKeys[] var2 = this.getPrimaryKeysFromDbFields(var1.getStructure(), var1.getObjects());
      this.setUpdateKeys(var2);
      this.setInsertMode(var1.getTransformation().getDbInsertMode());
      this.autoTruncateChoice.setSelected(var1.getStructure().isTruncatingTargetFields());
   }

   private PrimaryKeys[] getPrimaryKeysFromDbFields(final DatabaseStructure var1, DatabaseObject[] var2) {
      HashSet var3 = Sets.newHashSet(this.dbObjects);
      final DatabaseObject[] var4 = (DatabaseObject[]) var3.toArray(new DatabaseObject[var3.size()]);
      PrimaryKeysCreator var5 = new PrimaryKeysCreator(new TransformationDatabaseInfo() {
         @Override
         public DatabaseObject[] getDatabaseObjects() {
            return var4;
         }

         @Override
         public DatabaseStructure getStructure() {
            return var1;
         }
      });
      return var5.getPrimaryKeys();
   }

   public void setUpdateKeys(PrimaryKeys[] var1) {
      this.primaryKeyDefiner.setPrimaryKeys(var1);
   }

   public JComponent[] getDisplayers() {
      return new JComponent[]{this.createModeSelectorDecor(), this.createPrimaryKeyDefinerDecor()};
   }

   private JComponent createModeSelectorDecor() {
      return InvisiblePanel.of(this.modeSelector);
   }

   private JComponent createPrimaryKeyDefinerDecor() {
      return new KongaScrollPane(this.primaryKeyDefiner);
   }

   public PrimaryKeys[] getSelectedKeys() {
      return this.primaryKeyDefiner.getPrimaryKeys();
   }

   public DbFields createDbFields() {
      DbFields var1 = new DbFields();
      var1.addUpdateKeys(this.getSelectedKeyColumns());
      return var1;
   }

   private Set<DatabaseColumn> getSelectedKeyColumns() {
      HashSet var1 = Sets.newHashSet();

      for (PrimaryKeys var5 : this.getSelectedKeys()) {
         for (DatabaseColumn var9 : var5.getPrimaryKeys()) {
            var1.add(var9);
         }
      }

      return var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      OneColumnPanel var2 = new OneColumnPanel().nonOpaque().withInsets(0, 0, 10, 0);
      var2.addAll(this.createModeSelectorDecor(), this.primaryKeyDefiner);
      if (this.includeAutoTruncateChoice) {
         var2.addAll(" ", this.autoTruncateChoice);
      }

      JPanel var3 = InvisiblePanel.newPanel();
      var2.addToParent(var3, null);
      var1.display(var3);
   }

   @Override
   public void requestFocus() {
      this.primaryKeyDefiner.requestFocus();
   }
}
