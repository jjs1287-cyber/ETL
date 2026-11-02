package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.transformation.SalesforceWizardRole;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DataStructureType;
import org.etlforge.integration.salesforce.wsdl.SalesforceWsdlConstants;
import org.etlforge.xml.ExpandedName;

abstract class AbstractEditTransformationAction extends AbstractApplicationAction {
   private final TransformationPage page;
   private final SourceTarget st;

   protected AbstractEditTransformationAction(String var1, TransformationPage var2, SourceTarget var3) {
      super(var1, TransformationActionDictionary.DICTIONARY);
      this.page = var2;
      this.st = var3;
      boolean var4 = this.isApplicable();
      this.setEnabled(var4);
   }

   private boolean isApplicable() {
      Transformation var1 = this.page.getObject();
      return this.isApplicableForStructure(var1) && !this.isFixedByWizard();
   }

   private boolean isApplicableForStructure(Transformation var1) {
      DataStructure var2 = this.st == SourceTarget.Source ? var1.getInputStructure() : var1.getOutputStructure();
      return var2 != null && var2.getStructureType() != DataStructureType.None;
   }

   private boolean isFixedByWizard() {
      if (!this.isPartOfSalesforcePartnerWsdl()) {
         return false;
      } else {
         SalesforceWizardRole var1 = this.page.getObject().getSalesforceWizardRole();
         if (var1 == null) {
            return false;
         } else {
            return this.st == SourceTarget.Source
               ? var1.position == SalesforceWizardRole.Position.RESPONSE
               : var1.position == SalesforceWizardRole.Position.REQUEST;
         }
      }
   }

   private boolean isPartOfSalesforcePartnerWsdl() {
      Transformation var1 = this.page.getObject();
      if (!var1.isPartOfSalesforceWizard()) {
         return false;
      }

      ExpandedName var2 = var1.getSalesforceObject();
      return var2 != null && SalesforceWsdlConstants.isPartnerWsdlObject(var2);
   }

   @Override
   public final void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            TransformationEditorLauncher.startEditing(AbstractEditTransformationAction.this.page, AbstractEditTransformationAction.this.st);
         }
      });
   }
}
