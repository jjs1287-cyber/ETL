package org.etlforge.integration.client.ui.entity.selector;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.JCheckBox;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.selection.DialogSelectionSupport;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.button.KongaCheckBox;

public abstract class ExistingEntitySelectionUi<T extends IntegrationEntity> implements UiProvider {
   private final String typeName;
   private final String typeNamePlural;
   private EntityFilter filter;
   private final JCheckBox applyFilterChoice;
   private final EntitySelector<T> entitySelector;
   private KongaButton createNewButton;
   private KongaDialog dialog;
   private boolean alwaysFilter;

   protected ExistingEntitySelectionUi(IntegrationProject var1, Class<T> var2, EntityType var3) {
      this(var1, var2, var3.getDisplayName(), var3.getPlural());
   }

   protected ExistingEntitySelectionUi(IntegrationProject var1, Class<T> var2, String var3, String var4) {
      this.typeName = var3;
      this.typeNamePlural = var4;
      this.entitySelector = this.createEntityDisplayer(var1, var2, var4);
      this.applyFilterChoice = this.createApplyFilterChoice();
      this.updateFilterMode();
   }

   protected abstract EntitySelector<T> createEntityDisplayer(IntegrationProject var1, Class<T> var2, String var3);

   @Override
   public final void decorate(UiCanvas var1) {
      ExistingEntitySelectionUi.Layout var2 = new ExistingEntitySelectionUi.Layout();
      var2.decorate(var1);
   }

   @Override
   public final void requestFocus() {
      this.entitySelector.requestFocus();
   }

   public final void setSelectedEntity(T var1) {
      this.entitySelector.setSelectedEntity((T)var1);
   }

   public final void setCreateNewItemAction(Action var1) {
      this.createNewButton = new KongaButton(var1);
      this.createNewButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            if (ExistingEntitySelectionUi.this.dialog != null) {
               ExistingEntitySelectionUi.this.dialog.getCancelButton().doClick();
            }
         }
      });
   }

   public final void setFilter(EntityFilter var1) {
      this.filter = var1;
      this.updateFilterMode();
   }

   public final void setAlwaysFilter() {
      this.alwaysFilter = true;
      this.updateFilterMode();
   }

   public final void select(Receiver<T> var1) {
      T var2 = null;
      this.dialog = this.createDialog();
      this.dialog.setVisible(true);
      if (this.dialog.wasOkPressed()) {
         var2 = this.entitySelector.getSelectedEntity();
      }

      var1.handle(var2);
   }

   private KongaDialog createDialog() {
      String var1 = String.format("Select %s", this.typeName);
      WizardStyleDialog var2 = new WizardStyleDialog(UiUtils.getActiveWindow(), var1, this);
      var2.manageLocation(ExistingEntitySelectionUi.class);
      new DialogSelectionSupport(this.entitySelector.getSelectionSource(), var2) {
         @Override
         protected boolean isEnabledFor(Selection var1) {
            return ExistingEntitySelectionUi.this.entitySelector.getSelectedEntity() != null;
         }
      };
      var2.setOkButtonAsDefault();
      return var2;
   }

   private JCheckBox createApplyFilterChoice() {
      String var1 = "&Show only those " + this.typeNamePlural + " that are applicable to the current location";
      KongaCheckBox var2 = new KongaCheckBox(var1, true);
      var2.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            ExistingEntitySelectionUi.this.updateFilterMode();
         }
      });
      return var2;
   }

   private void updateFilterMode() {
      this.entitySelector.setFilter(!this.alwaysFilter && !this.applyFilterChoice.isSelected() ? null : this.filter);
   }

   protected interface EntityDisplayer<E extends IntegrationEntity> extends UiProvider {
      E getSelectedEntity();

      SelectionSource getSelectionSource();

      void setFilterEnabled(boolean var1);

      void setDefaultAction(SelectionAction var1);
   }

   private class Layout {
      private final CaptionLayout panel = CaptionLayout.of(this.caption(), ExistingEntitySelectionUi.this.entitySelector);

      public Layout() {
         UiUtils.setPanelBackgrounds(this.panel.container(), Colors.NEUTRAL_BACKGROUND);
      }

      private UiProvider caption() {
         return Vertical.box()
            .leftAligned()
            .addAllWithSpace(
               10,
               ExistingEntitySelectionUi.this.createNewButton,
               ExistingEntitySelectionUi.this.alwaysFilter ? null : ExistingEntitySelectionUi.this.applyFilterChoice
            );
      }

      public void decorate(UiCanvas var1) {
         this.panel.decorate(var1);
      }
   }
}
