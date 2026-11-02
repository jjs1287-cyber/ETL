package org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl;

import java.net.URL;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.ui.Empty;
import org.etlforge.ui.input.UrlInputField;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.undo.UndoRedoInstaller;

public final class RemoteWsdlFileSelector extends AbstractWsdlSelector {
   private final UrlInputField urlField = this.createUrlField();

   public RemoteWsdlFileSelector() {
      this.doLayout();
   }

   public WsdlFile getSelectedFile() {
      WsdlFile var1 = null;
      URL var2 = this.urlField.getValue();
      if (var2 != null) {
         var1 = new WsdlFile();
         var1.setLocator(var2.toString());
      }

      return var1;
   }

   @Override
   public boolean isValid() {
      return this.urlField.isFieldValid();
   }

   @Override
   public void setActive(boolean var1) {
      this.urlField.setActive(var1);
      if (var1) {
         this.requestFocus();
      }
   }

   @Override
   public void installUndoRedo(UndoRedoInstaller var1) {
      var1.install(this.urlField.getInputComponent());
   }

   @Override
   public void requestFocus() {
      this.urlField.requestFocus();
   }

   @Override
   public WsdlProcessingJob getProcessingJob() {
      String var1 = this.urlField.getText().trim();
      return var1.length() > 0 ? new ProcessRemoteWsdlJob(var1) : null;
   }

   private UrlInputField createUrlField() {
      AbstractWsdlSelector.WsdlInputField var1 = new AbstractWsdlSelector.WsdlInputField();
      UrlInputField var2 = new UrlInputField("URL", var1, null);
      var2.setRequired(true);
      return var2;
   }

   private void doLayout() {
      JTextComponent var1 = this.urlField.getInputComponent();
      JComponent var2 = new BorderLayoutBuilder().north(var1).withBorder(Empty.border(FIELD_INSETS)).container();
      this.setDisplayer(var2);
   }
}
