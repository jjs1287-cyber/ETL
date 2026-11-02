package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.database.TargetUpdateKeysEditor;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DatabaseStructure;

public final class EditTargetUpdateKeysAction extends AbstractApplicationAction {
   public static final String ID = "action.ic.tx.EditUpdateKeys";
   private final TransformationPage page;
   private TargetUpdateKeysEditor editor;

   public EditTargetUpdateKeysAction(TransformationPage var1) {
      super("action.ic.tx.EditUpdateKeys", TransformationActionDictionary.DICTIONARY);
      this.page = var1;
      boolean var2 = this.isApplicable();
      this.setEnabled(var2);
   }

   private boolean isApplicable() {
      Transformation var1 = this.page.getObject();
      DataStructure var2 = var1.getOutputStructure();
      return var2 instanceof DatabaseStructure;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            EditTargetUpdateKeysAction.this.startEdit();
         }
      });
   }

   private void startEdit() {
      if (this.editor == null) {
         this.editor = new TargetUpdateKeysEditor(this.page);
      }

      this.editor.edit();
   }
}
