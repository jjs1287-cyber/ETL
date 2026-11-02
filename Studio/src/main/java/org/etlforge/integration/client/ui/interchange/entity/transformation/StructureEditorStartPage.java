package org.etlforge.integration.client.ui.interchange.entity.transformation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.label.KongaLabel;

final class StructureEditorStartPage extends AbstractWizardPage {
   private final JCheckBox backupChoice = this.createBackupChoice();
   private final JCheckBox migrationChoice = this.createMigrationChoice();
   private final JComponent migrationSection = this.createMigrationSection();

   public StructureEditorStartPage(Wizard var1) {
      super(var1, Strings.get("StructureEditor.StartPage.Title"));
      this.installMigrationEnabledListener();
      this.setCanContinue(true);
      this.layoutPage();
   }

   @Override
   public void requestFocus() {
      this.backupChoice.requestFocusInWindow();
   }

   public boolean isBackupRequested() {
      return this.backupChoice.isSelected();
   }

   public boolean isAutomaticMigrationSelected() {
      return this.isBackupRequested() && this.migrationChoice.isSelected();
   }

   private JCheckBox createBackupChoice() {
      KongaCheckBox var1 = new KongaCheckBox(Strings.get("StructureEditor.StartPage.Backup.Choice"));
      var1.setSelected(EditStructurePreferences.CREATE_BACKUP.get());
      var1.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            EditStructurePreferences.CREATE_BACKUP.set(StructureEditorStartPage.this.isBackupRequested());
         }
      });
      this.getUndoRedoInstaller().install(var1);
      return var1;
   }

   private JCheckBox createMigrationChoice() {
      KongaCheckBox var1 = new KongaCheckBox(Strings.get("StructureEditor.StartPage.Migrate.Choice"));
      var1.setSelected(EditStructurePreferences.AUTOMATIC_MIGRATION.get());
      var1.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            EditStructurePreferences.AUTOMATIC_MIGRATION.set(StructureEditorStartPage.this.isAutomaticMigrationSelected());
         }
      });
      this.getUndoRedoInstaller().install(var1);
      return var1;
   }

   private JComponent createMigrationSection() {
      StructureEditorStartPage.Section var1 = new StructureEditorStartPage.Section(
         Strings.get("StructureEditor.StartPage.Migrate.Title"), Strings.get("StructureEditor.StartPage.Migrate.Caption"), this.migrationChoice
      );
      return var1.getLayout();
   }

   @Override
   protected JPanel layoutPage() {
      StructureEditorStartPage.Section var1 = new StructureEditorStartPage.Section(
         this.getTitle(), Strings.get("StructureEditor.StartPage.Backup.Caption"), this.backupChoice
      );
      BoxBuilder var2 = BoxBuilder.vertical().nonOpaque().leftAligned();
      var2.addAllWithSpace(20, var1.getLayout(), this.migrationSection);
      return var2.invisible();
   }

   private void installMigrationEnabledListener() {
      this.backupChoice.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent var1) {
            StructureEditorStartPage.this.updateMigrationEnabled();
         }
      });
      this.updateMigrationEnabled();
   }

   private void updateMigrationEnabled() {
      boolean var1 = this.backupChoice.isSelected();
      UiUtils.setEnabledRecursively(this.migrationSection, var1);
   }

   private static class Section {
      private final OneColumnPanel col = new OneColumnPanel();

      public Section(String var1, String var2, JCheckBox var3) {
         this.col.withInsets(5, 5, 5, 5);
         this.addTitle(var1);
         this.addCaption(var2);
         this.col.noFill(var3);
      }

      public JComponent getLayout() {
         BorderLayoutBuilder var1 = new BorderLayoutBuilder().north(this.col);
         return var1.deeplyNonOpaque().container();
      }

      private void addTitle(String var1) {
         KongaLabel var2 = TextStyles.InfoTextHeader.makeLabel(var1);
         this.col.add(var2);
      }

      private void addCaption(String var1) {
         String var2 = "<html><table width=500px><tr><td>" + var1 + "</td></tr></table>";
         KongaLabel var3 = TextStyles.InfoText.makeLabel(var2);
         this.col.add(var3);
      }
   }
}
