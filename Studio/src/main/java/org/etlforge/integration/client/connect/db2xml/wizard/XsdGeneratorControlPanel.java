package org.etlforge.integration.client.connect.db2xml.wizard;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.ui.Empty;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.DialogLayoutBuilder;
import org.etlforge.ui.property.ActionEnabledBinding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.property.TextBinding;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextArea;

final class XsdGeneratorControlPanel implements UiProvider {
   private final XsdGeneratorModel model;
   private final TextInputField2 fileNameField;
   private final KongaTextArea contentArea;
   private final GenerateXsdAction generateXsdAction;

   public XsdGeneratorControlPanel(XsdGeneratorModel var1, GenerateXsdAction var2) {
      this.model = var1;
      this.fileNameField = new FileNameField(Size.columns(20));
      this.contentArea = KongaTextArea.viewer(Size.rows(5), Size.columns(20));
      this.generateXsdAction = var2;
      this.bindProperties();
   }

   private void bindProperties() {
      Bindings var1 = new Bindings(
         TextBinding.bind(this.model, "filename", this.fileNameField.getInputComponent()),
         TextBinding.bind(this.model, "content", this.contentArea),
         ActionEnabledBinding.forBoolean(this.generateXsdAction, this.model, "validSettings")
      );
      var1.syncUi();
   }

   @Override
   public void decorate(UiCanvas var1) {
      XsdGeneratorControlPanel.Layout var2 = new XsdGeneratorControlPanel.Layout();
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.fileNameField.requestFocus();
   }

   public void setWaitService(WaitService var1) {
      this.generateXsdAction.setWaitService(var1);
   }

   private class Layout {
      private final BorderLayoutBuilder layout = new BorderLayoutBuilder(0, 10);

      public Layout() {
         this.layoutControls();
         this.layoutContentArea();
         this.layout.withBorder(Empty.border(0, 30, 30, 30));
      }

      private void layoutControls() {
         DialogLayoutBuilder var1 = new DialogLayoutBuilder(5, 5);
         BoxBuilder var2 = BoxBuilder.horizontal();
         var2.addAllWithSpace(10, XsdGeneratorControlPanel.this.fileNameField.getInputComponent(), this.createOptionsButton());
         var1.addRow(XsdGeneratorControlPanel.this.fileNameField.getLabel(), var2.container());
         JPanel var3 = InvisiblePanel.of(XsdGeneratorControlPanel.this.generateXsdAction);
         var1.addRow(new JLabel(" "), var3);
         this.layout.north(var1);
      }

      private JButton createOptionsButton() {
         KongaButton var1 = new KongaButton("&Options...");
         var1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent var1) {
               XsdOptionsDialog var2 = new XsdOptionsDialog(XsdGeneratorControlPanel.this.model);
               var2.open((Component)var1.getSource());
            }
         });
         return var1;
      }

      private void layoutContentArea() {
         XsdGeneratorControlPanel.this.contentArea.setLabel("XSD:");
         this.layout.center(XsdGeneratorControlPanel.this.contentArea.asUiProvider());
      }

      public void decorate(UiCanvas var1) {
         this.layout.decorate(var1);
      }
   }
}
