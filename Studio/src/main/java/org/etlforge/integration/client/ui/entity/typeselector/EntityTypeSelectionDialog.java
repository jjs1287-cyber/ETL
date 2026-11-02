package org.etlforge.integration.client.ui.entity.typeselector;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import java.awt.Window;
import java.util.Set;
import javax.swing.JButton;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityTypeFilters;
import org.etlforge.ui.dialog.KongaDialog;

public final class EntityTypeSelectionDialog {
   private ImmutableSet<EntityType> include = EntityType.allTypes();
   private boolean includeWizardTypes;
   private Window owner;
   private String title = "Select Types";

   public EntityTypeSelectionDialog including(Set<EntityType> var1) {
      this.include = ImmutableSet.copyOf(var1);
      return this;
   }

   public EntityTypeSelectionDialog includeWizards() {
      this.includeWizardTypes = true;
      return this;
   }

   public EntityTypeSelectionDialog ownedBy(Window var1) {
      this.owner = var1;
      return this;
   }

   public EntityTypeSelectionDialog withTitle(String var1) {
      this.title = var1;
      return this;
   }

   public ImmutableSet<EntityType> selectTypes() {
      EntityTypeSelectionPanel var1 = this.createSelector();
      WizardStyleDialog var2 = this.openDialog(var1);
      return var2.wasOkPressed() ? var1.getSelection() : null;
   }

   private EntityTypeSelectionPanel createSelector() {
      Iterable var1 = this.includeWizardTypes ? this.include : Iterables.filter(this.include, EntityTypeFilters.nonWizards());
      EntityTypeSelectionPanel var2 = new EntityTypeSelectionPanel(var1);
      var2.applyMnemonics();
      return var2;
   }

   private WizardStyleDialog openDialog(EntityTypeSelectionPanel var1) {
      WizardStyleDialog var2 = new WizardStyleDialog(this.owner, this.title, var1);
      var2.setResizable(false);
      var2.setLocationRelativeTo(null);
      var2.getOKButton().setEnabled(false);
      var1.addEntityTypeSelectionListener(new EntityTypeSelectionDialog.SelectionListener(var2));
      var2.setOkButtonAsDefault();
      var2.setVisible(true);
      return var2;
   }

   private static class SelectionListener implements EntityTypeSelectionListener {
      private final KongaDialog dialog;

      public SelectionListener(KongaDialog var1) {
         this.dialog = var1;
      }

      @Override
      public void selectionChanged(EntityTypeSelectionEvent var1) {
         ImmutableSet var2 = var1.getNewSelection();
         JButton var3 = this.dialog.getOKButton();
         boolean var4 = !var2.isEmpty();
         var3.setEnabled(var4);
         if (var4) {
            this.dialog.setOkButtonAsDefault();
         }
      }
   }
}
