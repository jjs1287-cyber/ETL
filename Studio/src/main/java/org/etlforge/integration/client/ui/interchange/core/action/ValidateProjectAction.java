package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowSectionContentProvider;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.interchange.ValidationWindowProviderFactory;
import org.etlforge.integration.data.project.validation.ValidationModel;

public final class ValidateProjectAction extends AbstractProjectBasedAction {
   public static final String ID = "action.ic.Validate";
   private final ApplicationWindow appWin;

   public ValidateProjectAction(ApplicationWindow var1, CurrentProjectService var2) {
      super(var2, "action.ic.Validate", InterchangeActionDictionary.DICTIONARY);
      this.appWin = var1;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ValidateProjectAction.this.openValidationView();
         }
      });
   }

   private void openValidationView() {
      WindowSectionContentProvider var1 = this.getContentProvider();
      this.appWin.displayContent(var1, ApplicationWindow.SOUTH, null, null);
   }

   private WindowSectionContentProvider getContentProvider() {
      ValidationModel var1 = this.getManagedProject().getValidationModel();
      ValidationWindowProviderFactory var2 = ValidationWindowProviderFactory.newFactory(var1);
      var2.setCurrentProjectService(this.getCurrentProjectService());
      return var2.newContentProvider();
   }
}
