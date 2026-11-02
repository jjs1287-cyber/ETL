package org.etlforge.integration.client.ui.deploy;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import java.awt.Insets;
import java.util.Collection;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.prefs.BooleanPreference;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.integration.client.deploy.DeployPrompt;
import org.etlforge.integration.client.ui.entity.IntegrationEntityList;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.renderer.IntegrationEntitySelectorTableRenderer;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.SelectorTable;
import org.etlforge.ui.widget.button.KongaCheckBox;

public final class DialogDeployPrompt implements DeployPrompt {
   private IntegrationEntityList requiredItems;
   private SelectorTable<IntegrationEntity> dirtyItems;
   private volatile BooleanPreference automaticDeployPreference;

   public void setAutomaticDeployPreference(BooleanPreference var1) {
      this.automaticDeployPreference = var1;
   }

   @Override
   public Collection<? extends IntegrationEntity> getDirtyItemsToDeploy() {
      return this.getSelectedDirtyItems();
   }

   @Override
   public boolean prompt(final Collection<? extends IntegrationEntity> var1, final Collection<? extends IntegrationEntity> var2) {
      return UiThreadSupplier.call(new Supplier<Boolean>() {
         public Boolean get() {
            DialogDeployPrompt.this.createComponents(var1, var2);
            return DialogDeployPrompt.this.askUser();
         }
      });
   }

   private boolean askUser() {
      DialogDeployPrompt.Displayer var1 = new DialogDeployPrompt.Displayer();
      var1.setVisible(true);
      return var1.wasOkPressed();
   }

   public List<IntegrationEntity> getSelectedDirtyItems() {
      return this.dirtyItems == null ? Lists.newArrayList() : this.dirtyItems.getSelectedObjects();
   }

   private void createComponents(Collection<? extends IntegrationEntity> var1, Collection<? extends IntegrationEntity> var2) {
      if (var1 != null) {
         this.requiredItems = new IntegrationEntityList(var1, 5);
      }

      if (var2 != null) {
         this.createDirtyItemsTable(Lists.newArrayList(var2));
      }
   }

   private void createDirtyItemsTable(List<IntegrationEntity> var1) {
      this.dirtyItems = new SelectorTable<>(var1, IntegrationEntity.class);
      this.dirtyItems.setStyle(TableStyles.selector());
      this.dirtyItems.setInitialSelectionState(true);
      this.dirtyItems.setObjectCellRenderer(new IntegrationEntitySelectorTableRenderer());
      this.dirtyItems.setVisibleRows(8);
   }

   private final class Displayer extends KongaDialog {
      private JCheckBox preferenceBox;

      public Displayer() {
         super(UiUtils.getActiveWindow(), Strings.get("Deploy.Checker.DialogTitle"));
         this.layoutComponents();
         this.pack();
         this.manageLocation();
         this.getRootPane().setDefaultButton(this.getContinueButton());
      }

      private void layoutComponents() {
         BorderLayoutBuilder var1 = new BorderLayoutBuilder();
         var1.center(this.getItemsPanel()).south(this.getButtons());
         var1.asContentPaneFor(this);
      }

      private UiProvider getItemsPanel() {
         Insets var1 = new Insets(2, 5, 3, 0);
         Insets var2 = new Insets(7, 5, 3, 0);
         OneColumnPanel var3 = new OneColumnPanel();
         var3.withInsets(var1);
         if (DialogDeployPrompt.this.requiredItems != null) {
            this.layoutRequiredItemsList(var3, var2);
         }

         if (DialogDeployPrompt.this.dirtyItems != null) {
            this.layoutDirtyItemsList(var3, var1);
         }

         if (DialogDeployPrompt.this.automaticDeployPreference != null) {
            BorderLayoutBuilder var4 = new BorderLayoutBuilder(0, 5);
            var4.center(var3);
            this.preferenceBox = new KongaCheckBox("&Always deploy changed items without asking first");
            var4.south(this.preferenceBox);
            var4.deeplyNonOpaque();
            var4.withBorder(Empty.border(10, 10, 0, 10));
            return var4;
         } else {
            return var3.deeplyNonOpaque().withBorder(Empty.border(10, 10, 0, 10));
         }
      }

      private void layoutRequiredItemsList(OneColumnPanel var1, Insets var2) {
         var1.add(Strings.get("Deploy.Checker.Required.Caption"));
         var1.fillOut(DialogDeployPrompt.this.requiredItems);
         var1.withInsets(var2);
      }

      private void layoutDirtyItemsList(OneColumnPanel var1, Insets var2) {
         var1.add(Strings.get("Deploy.Checker.Dirty.Caption"));
         var1.withInsets(var2);
         var1.fillOut(DialogDeployPrompt.this.dirtyItems);
      }

      private JComponent getButtons() {
         JPanel var1 = this.getButtonPanel(CONTINUE_CANCEL, 10);
         var1.setOpaque(false);
         var1.setBorder(Empty.border(10));
         return var1;
      }

      @Override
      protected void onOk() {
         if (this.preferenceBox != null) {
            DialogDeployPrompt.this.automaticDeployPreference.set(this.preferenceBox.isSelected());
         }

         super.onOk();
      }
   }
}
