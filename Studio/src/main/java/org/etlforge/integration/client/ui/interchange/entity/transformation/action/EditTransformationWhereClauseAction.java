package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.database.WhereClauseEditor;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DatabaseStructure;

public final class EditTransformationWhereClauseAction extends AbstractApplicationAction {
   public static final String ID = "action.ic.tx.EditWhereClause";
   private final TransformationPage page;
   private WhereClauseEditor editor;

   public EditTransformationWhereClauseAction(TransformationPage var1) {
      super("action.ic.tx.EditWhereClause", TransformationActionDictionary.DICTIONARY);
      this.page = var1;
      boolean var2 = this.isApplicable();
      this.setEnabled(var2);
   }

   private boolean isApplicable() {
      Transformation var1 = this.page.getObject();
      DataStructure var2 = var1.getInputStructure();
      if (var2 instanceof DatabaseStructure) {
         DatabaseStructure var3 = (DatabaseStructure)var2;
         return !var3.hasManualSqlStatement();
      } else {
         return false;
      }
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            EditTransformationWhereClauseAction.this.startEdit();
         }
      });
   }

   private void startEdit() {
      if (this.editor == null) {
         this.editor = new WhereClauseEditor(this.page);
      }

      this.editor.edit();
   }
}
