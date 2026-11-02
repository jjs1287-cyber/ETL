package org.etlforge.integration.client.ui.connector.wizard;

import com.google.common.base.Function;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.ValidStateBinding;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.property.Binding;
import org.etlforge.ui.selection.AbstractSelectionAction;

public class ObjectSelectionPage extends AbstractWizardPage {
   private final ObjectSelectionView view;
   private final ObjectSelectionController controller;

   public ObjectSelectionPage(Wizard var1, String var2, ObjectSelectionModel var3, ObjectSelectionController var4, Action var5) {
      this(var1, var2, var3, var4, var5, false);
   }

   public ObjectSelectionPage(Wizard var1, String var2, ObjectSelectionModel var3, ObjectSelectionController var4, Action var5, boolean var6) {
      super(var1, var2);
      this.controller = var4;
      var4.setWaitService(this.getWaitService());
      this.view = new ObjectSelectionView(var3, var5, var6);
      this.view.setDefaultAction(new ObjectSelectionPage.NextPageAction());
      Binding var7 = ValidStateBinding.notNull(this, var3, "selected_object");
      var7.syncUi();
   }

   public final ObjectSelectionModel getModel() {
      return this.view.getModel();
   }

   public final void populateList() {
      this.controller.getObjectList();
   }

   @Override
   protected final JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(this.getTitle());
      JPanel var2 = BorderLayoutBuilder.fillWith(this.view);
      var1.add(var2, true, true);
      return var1.asCenterBorderLayout();
   }

   @Override
   public final void requestFocus() {
      this.view.requestFocus();
   }

   @Override
   protected final void dispose() {
      this.controller.cancelRunningJobs();
   }

   @Override
   public final boolean prepareForBackwardNavigation() {
      this.controller.cancelRunningJobs();
      return true;
   }

   protected final void setToolTipProducer(Function<ConnectorObject, String> var1) {
      this.view.setToolTipProducer(var1);
   }

   private final class NextPageAction extends AbstractSelectionAction {
      private NextPageAction() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (ObjectSelectionPage.this.canContinue()) {
            ObjectSelectionPage.this.getWizard().nextPageRequested();
         }
      }
   }
}
