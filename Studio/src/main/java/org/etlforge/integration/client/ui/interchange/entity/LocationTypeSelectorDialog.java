package org.etlforge.integration.client.ui.interchange.entity;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.awt.Component;
import java.awt.Window;
import java.util.Collection;
import javax.swing.JComponent;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.dialog.KongaDialog;

public final class LocationTypeSelectorDialog {
   private final ImmutableList<DataLocationType> choices;
   private Optional<Component> invoker = Optional.absent();

   public LocationTypeSelectorDialog(Collection<DataLocationType> var1) {
      Preconditions.checkArgument(var1.size() > 1, "At least two types must be provided, otherwise there is nothing to select!");
      this.choices = ImmutableList.copyOf(var1);
   }

   public void setInvoker(Component var1) {
      this.invoker = Optional.of(var1);
   }

   public Optional<DataLocationType> selectType() {
      LocationTypeSelectorUi var1 = new LocationTypeSelectorUi(this.choices);
      var1.selectFirst();
      KongaDialog var2 = this.createDialog(var1);
      var2.setVisible(true);
      return var2.wasOkPressed() ? var1.getSelectedType() : Optional.absent();
   }

   private KongaDialog createDialog(LocationTypeSelectorUi var1) {
      Window var2 = ApplicationUi.getWindowAncestor(this.invoker);
      JComponent var3 = WizardStyleDialog.wrapUi(var1);
      final WizardStyleDialog var4 = new WizardStyleDialog(var2, "Select Type", var3, KongaDialog.CONTINUE_CANCEL, true) {
         @Override protected String getCaptionTitle() { return null; }
      };
      var1.setSelectionCallback(new Receiver<DataLocationType>() {
         public void handle(DataLocationType var1) {
            var4.getContinueButton().setEnabled(var1 != null);
         }
      });
      var1.installKeyNavigation(var4.getRootPane());
      var4.setResizable(false);
      var4.manageLocation(LocationTypeSelectorDialog.class);
      var4.setContinueButtonAsDefault();
      return var4;
   }
}
