package org.etlforge.integration.client.ui.entity.selector;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.integration.client.ui.entity.EntityPageDisplayer;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.selection.AbstractSelectionAction;

public class ExamineSelectedEntityAction extends AbstractSelectionAction {
   private final EntitySelector<?> selector;
   private EntityPageDisplayer examiner;

   public ExamineSelectedEntityAction(EntitySelector<?> var1) {
      this.selector = var1;
      ActionDecorators.VIEW_DETAILS.decorate(this);
      this.putValue("Name", null);
      this.setSelectionSource(var1.getSelectionSource());
   }

   public void setExaminer(EntityPageDisplayer var1) {
      this.examiner = var1;
      this.updateEnabledState();
   }

   public void updateEnabledState() {
      this.handleSelectionChange();
   }

   @Override
   protected void handleSelectionChange() {
      this.setEnabled(this.examiner != null && this.selector.getSelectedEntity() != null);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            IntegrationEntity var1x = ExamineSelectedEntityAction.this.selector.getSelectedEntity();
            if (var1x != null) {
               ExamineSelectedEntityAction.this.examiner.openPage(var1x);
            }
         }
      });
   }
}
