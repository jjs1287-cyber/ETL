package com.etlforge.integration.client.ui.sfdc;

import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.OperationTester;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.OperationTesterFactory;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;

public final class WizardItemOperationTester implements OperationTester {
   private final IntegrationEntityPage page;
   private final OperationTesterFactory opTesterFactory;

   public WizardItemOperationTester(IntegrationEntityPage var1, OperationTesterFactory var2) {
      this.page = var1;
      this.opTesterFactory = var2;
   }

   @Override
   public void testOperation(Operation var1) {
      TransformationPage var2 = this.openResponsePage();
      if (var2 != null) {
         this.runTest(var1, var2);
      }
   }

   private TransformationPage openResponsePage() {
      SalesforceWizardEntity var1 = (SalesforceWizardEntity)this.page.getObject();
      WizardItemBrowser var2 = new WizardItemBrowser(var1);
      Transformation var3 = var2.getResponseTransformation();
      if (var3 != null) {
         Editor var4 = UiCompanion.of(var3).openPage();
         if (var4 instanceof TransformationPage) {
            return (TransformationPage)var4;
         }
      }

      return null;
   }

   private void runTest(Operation var1, TransformationPage var2) {
      OperationTester var3 = this.opTesterFactory.newTester(var2);
      var3.testOperation(var1);
   }

   @Override
   public void testOperation(Transformation var1) {
      throw new UnsupportedOperationException();
   }
}
