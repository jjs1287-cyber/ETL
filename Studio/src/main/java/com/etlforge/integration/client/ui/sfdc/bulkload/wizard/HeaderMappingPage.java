package com.etlforge.integration.client.ui.sfdc.bulkload.wizard;

import com.google.common.base.Supplier;
import com.etlforge.integration.client.ui.sfdc.bulkload.mapping.HeaderMappingsUi;
import com.etlforge.integration.client.ui.sfdc.bulkload.mapping.MappingsValidator;
import com.etlforge.integration.client.ui.sfdc.bulkload.mapping.ValidationErrorDisplayer;
import com.etlforge.integration.client.ui.sfdc.wizard.SalesforceWizard;
import com.etlforge.integration.client.ui.sfdc.wizard.file.SampleFile;
import java.awt.EventQueue;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.salesforce.bulkload.HeaderMappings;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.widget.text.KongaTextArea;

final class HeaderMappingPage extends AbstractWizardPage {
   private final HeaderMappingsUi mappingUi;
   private final KongaTextArea sampleFileViewer;

   public HeaderMappingPage(SalesforceWizard<?> var1, HeaderMappingsUi var2) {
      super(var1, "Header Mappings");
      this.mappingUi = var2;
      this.sampleFileViewer = KongaTextArea.viewer(Size.rows(5), Size.columns(60));
      TextStyles.DefaultMonoSpaced.makeOver(this.sampleFileViewer);
      this.sampleFileViewer.setLabel("Top of sample file:");
      this.setCanContinue(true);
   }

   public void setSampleFile(SampleFile var1) {
      this.sampleFileViewer.setText(var1 == null ? "" : var1.getTopOfFile());
      this.sampleFileViewer.setCaretPosition(0);
   }

   public HeaderMappings getHeaderMappings() {
      return this.mappingUi.getHeaderMappings();
   }

   @Override
   protected void onFirstDisplay() {
      this.mappingUi.setWaitService(this.getWaitService());
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(
         "Map Headers", "Map the headers in the source file to the corresponding fields in the Salesforce object."
      );
      var1.add(this.mappingUi, true, true);
      var1.add(this.sampleFileViewer.asUiProvider(), true, false);
      return var1.asCenterBorderLayout();
   }

   @Override
   public void requestFocus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            HeaderMappingPage.this.mappingUi.requestFocus();
         }
      });
   }

   @Override
   public boolean prepareForForwardNavigation() {
      return UiThreadSupplier.call(new Supplier<Boolean>() {
         public Boolean get() {
            HeaderMappings var1 = HeaderMappingPage.this.mappingUi.getHeaderMappings();
            ValidationErrorDisplayer var2 = new ValidationErrorDisplayer(HeaderMappingPage.this.getMainPanel());
            MappingsValidator var3 = new MappingsValidator(var2);
            return var3.validate(var1);
         }
      });
   }
}
