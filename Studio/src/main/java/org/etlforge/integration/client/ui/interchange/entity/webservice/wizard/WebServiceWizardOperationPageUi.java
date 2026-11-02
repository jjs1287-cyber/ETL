package org.etlforge.integration.client.ui.interchange.entity.webservice.wizard;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.client.mapping.webservice.WebServiceCallStructures;
import org.etlforge.integration.client.ui.interchange.entity.webservice.WebServiceCallStructuresPanel;
import org.etlforge.integration.client.ui.interchange.entity.webservice.prefs.ShowWebServiceStructuresPreference;
import org.etlforge.integration.client.ui.interchange.entity.webservice.util.WebServiceOperationComboBox;
import org.etlforge.integration.client.ui.interchange.entity.webservice.util.WebServiceOperationDocumentationDisplayer;
import org.etlforge.integration.client.ui.interchange.entity.webservice.util.WebServiceOperationInfoViewer;
import org.etlforge.integration.data.entity.webservicecall.WebServiceInfo;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.undo.UndoRedoInstaller;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class WebServiceWizardOperationPageUi implements UiProvider {
   private final WebServiceWizardOperationPage page;
   private WebServiceInfo wsInfo;
   private final WebServiceOperationComboBox operationCombo;
   private final WebServiceOperationInfoViewer infoViewer;
   private final JPanel operationPanel;
   private final Action showDocumentationAction;
   private final WebServiceCallStructuresPanel treePanel;

   public WebServiceWizardOperationPageUi(WebServiceWizardOperationPage var1) {
      this.page = var1;
      this.showDocumentationAction = new WebServiceWizardOperationPageUi.DocumentationAction();
      this.infoViewer = new WebServiceOperationInfoViewer();
      this.operationCombo = new WebServiceOperationComboBox();
      this.operationCombo.addActionListener(new OperationSelectionHandler(var1));
      this.treePanel = ShowWebServiceStructuresPreference.showStructures() ? new WebServiceCallStructuresPanel() : null;
      this.operationPanel = this.layoutOperationPanel();
      UiUtils.setOpaqueRecursivelySkipDefaults(this.operationPanel, false);
   }

   private JPanel layoutOperationPanel() {
      TwoColumnPanel var1 = new TwoColumnPanel().withInsets(5, 5, 5, 5);
      var1.addRow(this.operationCombo);
      this.infoViewer.layoutIn(var1);
      var1.addRow(new KongaButton(this.showDocumentationAction), new JLabel(" "));
      return InvisiblePanel.of(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 10);
      var2.north(this.operationPanel);
      if (this.treePanel != null) {
         var2.center(this.treePanel);
      }

      var2.deeplyNonOpaque().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.operationCombo.requestFocus();
   }

   public void setWebServiceInfo(WebServiceInfo var1) {
      if (var1 != this.wsInfo) {
         this.wsInfo = var1;
         this.operationCombo.setOperations(var1);
         if (!var1.hasOperations(true)) {
            this.displayNoOperationsMessage();
         }
      }
   }

   private void displayNoOperationsMessage() {
      Alert.error(Strings.get("Wizard.OperationPage.NoOperations"), Strings.get("Wizard.OperationPage.NoOperations.Title"));
   }

   public WebServiceOperationInfo getSelectedOperation() {
      return this.operationCombo.getSelectedOperation();
   }

   public void installUndoRedo(UndoRedoInstaller var1) {
      this.operationCombo.installUndoRedo(var1);
   }

   public void displayTreeStructures(final WebServiceCallStructures var1) {
      if (this.treePanel != null) {
         Runnable var2 = new Runnable() {
            @Override
            public void run() {
               if (var1 != null) {
                  WebServiceWizardOperationPageUi.this.treePanel.displayStructures(var1);
               }
            }
         };
         UiUtils.runOnEventThread(var2);
      }
   }

   public void displayOperationInfo(final WebServiceOperationInfo var1) {
      Runnable var2 = new Runnable() {
         @Override
         public void run() {
            WebServiceWizardOperationPageUi.this.infoViewer.displayOperationInfo(var1);
            WebServiceWizardOperationPageUi.this.showDocumentationAction.setEnabled(var1 != null);
         }
      };
      UiUtils.runOnEventThread(var2);
   }

   private final class DocumentationAction extends AbstractAction {
      private final WebServiceOperationDocumentationDisplayer displayer;

      public DocumentationAction() {
         super(Strings.get("Wizard.OperationPage.Documentation"), CommonIcons.VIEW_DETAILS_16);
         this.putValue("ShortDescription", Strings.get("Wizard.OperationPage.Documentation.ToolTip"));
         this.displayer = new WebServiceOperationDocumentationDisplayer();
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               DocumentationAction.this.executeImpl();
            }
         });
      }

      private void executeImpl() {
         WebServiceOperationInfo var1 = WebServiceWizardOperationPageUi.this.getSelectedOperation();
         if (var1 == null) {
            this.setEnabled(false);
         } else {
            this.displayer.setOverlayContainer(WebServiceWizardOperationPageUi.this.page.getOverlayContainer());
            this.displayer.displayDocumentation(WebServiceWizardOperationPageUi.this.wsInfo, var1);
         }
      }
   }
}
