package org.etlforge.integration.client.ui.interchange.entity.webservice.util;

import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.Application;
import org.etlforge.application.ui.widget.AnimationPreference;
import org.etlforge.application.ui.widget.HtmlDisplayer;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.wsdl.OperationDocumentationExtractor;
import org.etlforge.integration.data.entity.webservicecall.WebServiceInfo;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;
import org.etlforge.ui.Empty;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.overlay.OverlayAlert;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayCornerPositioner;
import org.etlforge.ui.widget.overlay.OverlayPositioner;

public final class WebServiceOperationDocumentationDisplayer {
   private OverlayContainer overlayContainer;

   public void displayDocumentation(WebServiceInfo var1, WebServiceOperationInfo var2) {
      Runnable var3 = this.getExtractionJob(var1, var2);
      if (EventQueue.isDispatchThread()) {
         ApplicationWorker var4 = Application.getWorker();
         var4.submitForParallel(var3);
      } else {
         var3.run();
      }
   }

   public void setOverlayContainer(OverlayContainer var1) {
      this.overlayContainer = var1;
   }

   private Runnable getExtractionJob(final WebServiceInfo var1, final WebServiceOperationInfo var2) {
      return new Runnable() {
         @Override
         public void run() {
            OperationDocumentationExtractor var1x = new OperationDocumentationExtractor();
            String var2x = var1x.getDocumentation(var1, var2);
            if (var2x == null || var2x.length() == 0) {
               var2x = PackageResources.DocumentationDisplayer.EMPTY;
            }

            WebServiceOperationDocumentationDisplayer.this.displayDialog(var2, var2x);
         }
      };
   }

   private void displayDialog(final WebServiceOperationInfo var1, final String var2) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            WebServiceOperationDocumentationDisplayer.this.getDisplayer(var1, var2).display();
         }
      });
   }

   private WebServiceOperationDocumentationDisplayer.Displayer getDisplayer(WebServiceOperationInfo var1, String var2) {
      return this.overlayContainer != null
         ? new WebServiceOperationDocumentationDisplayer.OverlayDisplayer(var1, var2, this.overlayContainer)
         : new WebServiceOperationDocumentationDisplayer.DialogDisplayer(var1, var2);
   }

   private static class DialogDisplayer extends WebServiceOperationDocumentationDisplayer.Displayer {
      public DialogDisplayer(WebServiceOperationInfo var1, String var2) {
         super(var1, var2);
      }

      @Override
      public void display() {
         KongaDialog var1 = new KongaDialog(UiUtils.getActiveWindow(), PackageResources.DocumentationDisplayer.TITLE);
         this.layoutDialog(var1);
         var1.manageLocation(WebServiceOperationDocumentationDisplayer.class);
         var1.setVisible(true);
      }

      private void layoutDialog(KongaDialog var1) {
         BorderLayoutBuilder var2 = this.layout();
         var2.south(this.getButtons(var1));
         var2.withEmptyBorder(10).asContentPaneFor(var1);
      }

      private JComponent getButtons(KongaDialog var1) {
         JPanel var2 = var1.getButtonPanel(KongaDialog.CLOSE_ONLY, 0);
         var1.setCloseButtonAsDefault();
         var2.setBorder(Empty.border(10, 0, 10, 0));
         return var2;
      }
   }

   private abstract static class Displayer {
      private final WebServiceOperationInfo opInfo;
      private final String documentation;

      protected Displayer(WebServiceOperationInfo var1, String var2) {
         this.opInfo = var1;
         this.documentation = var2;
      }

      public abstract void display();

      protected final BorderLayoutBuilder layout() {
         BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 5);
         var1.north(PackageResources.DocumentationDisplayer.getCaption(this.opInfo)).center(this.getTextBox(this.documentation));
         return var1;
      }

      protected final JComponent getTextBox(String var1) {
         HtmlDisplayer var2 = new HtmlDisplayer();
         var2.setText(var1);
         var2.setCaretPosition(0);
         var2.setPreferredSize(new Dimension(300, 250));
         return BorderLayoutBuilder.fillWith(var2);
      }
   }

   private static class OverlayDisplayer extends WebServiceOperationDocumentationDisplayer.Displayer {
      private final OverlayContainer container;

      public OverlayDisplayer(WebServiceOperationInfo var1, String var2, OverlayContainer var3) {
         super(var1, var2);
         this.container = var3;
      }

      @Override
      public void display() {
         new OverlayAlert(this.layout(), "Documentation").show(this.container, this.getPositioner());
      }

      private OverlayPositioner getPositioner() {
         OverlayCornerPositioner var1 = OverlayCornerPositioner.UPPER_RIGHT;
         return AnimationPreference.get() ? var1.animated() : var1;
      }
   }
}
