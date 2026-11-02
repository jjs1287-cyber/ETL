package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.resources.TreeStrings;
import org.etlforge.integration.client.ui.script.partition.ScriptSyntaxHighlighter;
import org.etlforge.integration.structure.mapping.InvalidMapping;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.text.KongaTextArea;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.ui.widget.text.KongaTextPane;

public final class InvalidMappingDetailsViewer implements UiProvider {
   private final InvalidMapping mapping;

   public InvalidMappingDetailsViewer(InvalidMapping var1) {
      this.mapping = var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      TwoColumnPanel var2 = TwoColumnPanel.forFormEntry(10, 10).withLeftAnchor(GridBagLayoutPanel.Anchor.EAST);
      var2.addRow(getCaption(TreeStrings.get("InvalidMappings.Viewer.TargetPath")), this.getPathField());
      var2.withLeftAnchor(GridBagLayoutPanel.Anchor.NORTHEAST);
      var2.withRightAnchor(GridBagLayoutPanel.Anchor.NORTHWEST);
      var2.withRightWeights(1.0, 0.5);
      var2.withRightFill(GridBagLayoutPanel.Fill.BOTH);
      var2.addRow(getCaption(TreeStrings.get("InvalidMappings.Viewer.Expression")), this.getExpressionField());
      var2.addRow(getCaption(TreeStrings.get("InvalidMappings.Viewer.Error")), this.getErrorField());
      var2.decorate(var1);
   }

   private static JLabel getCaption(String var0) {
      return new JLabel(var0 + ":");
   }

   private JTextField getPathField() {
      KongaTextField var1 = new KongaTextField(this.mapping.getTargetPath().toString());
      var1.setEditable(false);
      return var1;
   }

   @Override
   public void requestFocus() {
   }

   private JComponent getExpressionField() {
      KongaTextPane var1 = new KongaTextPane("").disableTabs();
      var1.setFont(TextStyles.DefaultMonoSpaced.getFont());
      ScriptSyntaxHighlighter.install(var1);
      var1.setText(this.mapping.getExpression());
      var1.setEditable(false);
      return new JScrollPane(var1);
   }

   private JComponent getErrorField() {
      KongaTextArea var1 = KongaTextArea.viewer(this.mapping.getError(), Size.rows(5), Size.columns(40));
      return new JScrollPane(var1);
   }
}
