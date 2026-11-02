package org.etlforge.integration.client.ui.structure.editor.xml;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import org.etlforge.integration.client.ui.structure.editor.resources.StructureEditorResources;
import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.text.KongaTextField;

public final class ReadOnlyXmlStructureInfoDisplayer implements XmlStructureInfoDisplayer {
   private final JTextField filePathField = createTextField();
   private final JTextField rootNameField = createTextField();
   private Border border;

   @Override
   public void display(XmlStructure var1) {
      String var2;
      String var3;
      if (var1 == null) {
         var2 = null;
         var3 = null;
      } else {
         var2 = var1.getFilePath();
         var3 = var1.getRootNodeName();
      }

      setText(this.filePathField, var2);
      setText(this.rootNameField, var3);
   }

   @Override
   public XmlStructure getDisplayedStructure() {
      return null;
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.createDisplayer().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.filePathField.requestFocusInWindow();
   }

   public void setBorder(Border var1) {
      this.border = var1;
   }

   private TwoColumnPanel createDisplayer() {
      TwoColumnPanel var1 = new TwoColumnPanel();
      var1.withAnchor(TwoColumnPanel.NORTHWEST);
      if (this.border != null) {
         var1.withBorder(this.border);
      }

      var1.withLeftInsets(0, 0, 10, 10);
      var1.withRightInsets(0, 0, 10, 0);
      var1.withRightFill(TwoColumnPanel.HORIZONTAL);
      var1.withRightWeights(1.0, 0.0);
      var1.addRow(createLabel("XmlStructure.InfoDisplayer.FilePath"), this.filePathField);
      var1.withLeftInsets(0, 0, 0, 10);
      var1.withRightInsets(0, 0, 0, 0);
      var1.addRow(createLabel("XmlStructure.InfoDisplayer.RootName"), this.rootNameField);
      return var1;
   }

   private static void setText(JTextField var0, String var1) {
      var0.setText(var1);
      var0.setCaretPosition(0);
      var0.setToolTipText(var1);
   }

   private static JTextField createTextField() {
      KongaTextField var0 = new KongaTextField(30);
      var0.setEditable(false);
      return var0;
   }

   private static JLabel createLabel(String var0) {
      String var1 = StructureEditorResources.getString(var0);
      return new JLabel(var1);
   }
}
