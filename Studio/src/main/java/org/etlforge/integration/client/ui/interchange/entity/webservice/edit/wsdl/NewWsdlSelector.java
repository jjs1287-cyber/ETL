package org.etlforge.integration.client.ui.interchange.entity.webservice.edit.wsdl;

import com.google.common.base.Supplier;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl.LocalWsdlFileSelector;
import org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl.RemoteWsdlFileSelector;
import org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl.WsdlOption;
import org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl.WsdlSelector;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.option.InputOptionPanel;

public final class NewWsdlSelector implements UiProvider, Disposable {
   private final WebServiceCall wsCall;
   private final LocalWsdlFileSelector localSelector;
   private final RemoteWsdlFileSelector remoteSelector;
   private final InputOptionPanel<WsdlSelector> inputPanel;

   public NewWsdlSelector(WebServiceCall var1, FileStore var2) {
      this.wsCall = var1;
      this.localSelector = new LocalWsdlFileSelector(var2);
      this.remoteSelector = new RemoteWsdlFileSelector();
      this.inputPanel = this.createInputPanel();
   }

   @Override
   public void dispose() {
      this.inputPanel.dispose();
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 20);
      var2.north(Strings.format("EditWsdl.Selector.Current", this.wsCall.getWsdlFile().getLocator()));
      var2.center(this.inputPanel);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.inputPanel.requestFocus();
   }

   public WsdlFile getWsdlFile() {
      WsdlSelector var1 = this.inputPanel.getValue();
      if (var1 == this.localSelector) {
         return this.localSelector.getSelectedFile();
      } else {
         return var1 == this.remoteSelector ? this.remoteSelector.getSelectedFile() : null;
      }
   }

   public boolean isInputValid() {
      return this.localSelector.isValid() || this.remoteSelector.isValid();
   }

   public void addChangeListener(PropertyChangeListener var1) {
      this.localSelector.addChangeListener(var1);
      this.remoteSelector.addChangeListener(var1);
   }

   public void addActionListener(ActionListener var1) {
      this.localSelector.addActionListener(var1);
      this.remoteSelector.addActionListener(var1);
   }

   private InputOptionPanel<WsdlSelector> createInputPanel() {
      WsdlOption[] var1 = new WsdlOption[]{
         new WsdlOption(this.localSelector, "Select Local WSDL:"), new WsdlOption(this.remoteSelector, "Select Remote WSDL:")
      };
      InputOptionPanel var2 = new InputOptionPanel<>(var1);
      var2.setSelectedOption(var1[0]);
      return var2;
   }

   public static WsdlFile selectFile(final WebServiceCall var0, final FileStore var1) {
      return UiThreadSupplier.call(new Supplier<WsdlFile>() {
         public WsdlFile get() {
            NewWsdlSelector var1x = new NewWsdlSelector(var0, var1);
            NewWsdlSelector.DialogSelector var2 = new NewWsdlSelector.DialogSelector(var1x);
            return var2.show();
         }
      });
   }

   private static final class DialogSelector implements ActionListener, PropertyChangeListener {
      private final NewWsdlSelector ui;
      private final KongaDialog dlg;

      public DialogSelector(NewWsdlSelector var1) {
         this.ui = var1;
         this.dlg = UiProviderDialogDisplayer.createDialog(
            UiUtils.getActiveWindow(), var1, "Select the new WSDL", ModalityType.APPLICATION_MODAL, KongaDialog.OK_CANCEL, NewWsdlSelector.class
         );
         this.dlg.setResizable(false);
         this.dlg.setOkButtonAsDefault();
         this.listenToSelector();
         this.checkOkStatus();
      }

      public WsdlFile show() {
         this.dlg.setVisible(true);

         try {
            WsdlFile var1 = null;
            if (this.dlg.wasOkPressed()) {
               var1 = this.ui.getWsdlFile();
               if (var1 == null) {
                  Alert.error("No file was selected.", "Error");
               }
            }

            return var1;
         } finally {
            this.ui.dispose();
         }
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.dlg.getOKButton().doClick();
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         this.checkOkStatus();
      }

      private void checkOkStatus() {
         this.dlg.getOKButton().setEnabled(this.ui.isInputValid());
      }

      private void listenToSelector() {
         this.ui.addChangeListener(this);
         this.ui.addActionListener(this);
      }
   }
}
