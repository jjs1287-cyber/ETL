package org.etlforge.integration.client.ui.entity.testhistory;

import java.util.Date;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.widget.BasicTwoColumnLabelDisplayer;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.test.EntityTestResult;
import org.etlforge.integration.data.test.TestResult;
import org.etlforge.integration.uidelegate.entity.renderer.RendererUtils;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.text.KongaTextArea;

public final class EntityTestResultDetailsViewer implements UiProvider {
   private final EntityTestResult result;
   private final IntegrationEntityLookup entityLookup;
   private final BasicTwoColumnLabelDisplayer ui;

   public EntityTestResultDetailsViewer(EntityTestResult var1, IntegrationEntityLookup var2) {
      this.result = var1;
      this.entityLookup = var2;
      this.ui = new BasicTwoColumnLabelDisplayer();
      this.createLayout();
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.ui.decorate(var1);
   }

   @Override
   public void requestFocus() {
   }

   private void createLayout() {
      this.ui.addRow("Tested:", this.createEntityLabel());
      this.ui.addRow("Tested at:", new Date(this.result.getTimestamp()));
      this.ui.addRow("Result:", this.result.getOutcome().name(), this.getResultIcon());
      this.ui.addRow("Details:", this.createDetailsArea(), true);
   }

   private JLabel createEntityLabel() {
      JLabel var1 = this.ui.makeValueLabel("");
      IntegrationEntity var2 = this.entityLookup.getEntity(this.result.getSubjectId());
      if (var2 != null) {
         RendererUtils.decorateRenderer(var1, var2, false);
      } else {
         var1.setText("Unknown entity - ID = " + this.result.getSubjectId() + "]");
      }

      return var1;
   }

   private Icon getResultIcon() {
      switch (this.result.getOutcome()) {
         case SUCCESS:
            return CommonIcons.SUCCESS_16;
         case PARTIAL:
            return CommonIcons.WARNING_16;
         case ERROR:
            return CommonIcons.ERROR_16;
         default:
            throw new RuntimeException("Unexpected outcome: " + this.result.getOutcome());
      }
   }

   private JComponent createDetailsArea() {
      KongaTextArea var1 = KongaTextArea.viewer(this.result.getDetails(), Size.rows(6), Size.columns(40));
      if (this.result.getOutcome() == TestResult.ERROR) {
         TextStyles.DefaultErrorText.makeOver(var1);
      }

      return new JScrollPane(var1);
   }
}
