package org.etlforge.integration.client.ui.project.copy;

import java.util.HashSet;
import java.util.Set;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.entity.settings.EntityResources;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RequiredObjectsTree;
import org.etlforge.integration.data.project.DeleteRequestHandler;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.WaitService;

public final class SelectiveDeepCopyUi {
   private final IntegrationEntity startPoint;
   private final RequiredObjectsTree cascade;
   private final DeepCopyUi copier;

   public SelectiveDeepCopyUi(IntegrationEntity var1, EntityResources var2) {
      this.startPoint = var1;
      this.cascade = RequiredObjectsTree.fromStartPoint(var1);
      this.copier = new DeepCopyUi(ProjectUtils.getProject(var1), var2);
   }

   public void setWaitService(WaitService var1) {
      this.copier.setWaitService(var1);
   }

   public void setDeleteHandler(DeleteRequestHandler var1) {
      this.copier.setDeleteHandler(var1);
   }

   public void start() {
      Set var1 = this.getItemsToExclude();
      if (var1 != null) {
         this.copier.setItemsToExclude(var1);
         this.copier.copy(this.startPoint);
      }
   }

   private Set<IntegrationEntity> getItemsToExclude() {
      if (this.cascade.size() == 1) {
         Answer var3 = Ask.continueOrCancel(
            null,
            "\"" + this.startPoint.getName() + "\" has no dependencies, so no other objects will be copied.",
            "No Dependencies",
            Alert.Option.CONTINUE,
            Alert.Type.INFORMATION
         );
         return var3.isContinue() ? new HashSet<>() : null;
      } else {
         DeepCopyItemSelector var1 = new DeepCopyItemSelector(this.cascade);
         WizardStyleDialog var2 = new WizardStyleDialog(UiUtils.getActiveWindow(), "Select Which Objects To Copy", var1, KongaDialog.CONTINUE_CANCEL, true);
         var2.setContinueButtonAsDefault();
         var2.manageLocation(SelectiveDeepCopyUi.class);
         var2.setVisible(true);
         return var2.wasOkPressed() ? var1.getItemsToExclude() : null;
      }
   }
}
