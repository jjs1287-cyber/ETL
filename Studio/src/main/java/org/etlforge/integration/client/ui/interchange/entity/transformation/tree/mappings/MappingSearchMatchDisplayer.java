package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.resources.TreeStrings;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.script.partition.ScriptSyntaxHighlighter;
import org.etlforge.integration.data.entity.script.ExpressionTranslator;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.ui.widget.text.KongaTextPane;
import org.etlforge.util.Interval;

public final class MappingSearchMatchDisplayer implements UiProvider {
   private final TreeMapper treeMapper;
   private final JTextField targetField;
   private final Action openMappingAction;
   private final JTextPane expressionArea;
   private final ExpressionTranslator expressionTranslator;

   public MappingSearchMatchDisplayer(TreeMapper var1, ExpressionTranslator var2) {
      this.treeMapper = var1;
      this.targetField = this.createTargetField();
      this.expressionArea = this.createExpressionArea();
      this.openMappingAction = new MappingSearchMatchDisplayer.OpenMappingAction();
      this.expressionTranslator = var2;
   }

   public void displayMatch(MappingSearchMatch var1) {
      this.targetField.setText(var1.getMapping().getTargetPath().toString());
      this.targetField.setCaretPosition(0);
      this.openMappingAction.setEnabled(true);
      this.expressionArea.setText(var1.getMapping().getTranslatedExpression(this.expressionTranslator));
      Interval var2 = var1.getExpressionSpan();
      this.expressionArea.setCaretPosition(var2.startsAt());
      this.expressionArea.moveCaretPosition(var2.startsAt() + var2.length());
      this.expressionArea.getCaret().setSelectionVisible(true);
   }

   public void clear() {
      this.targetField.setText("");
      this.expressionArea.setText("");
      this.openMappingAction.setEnabled(false);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.layout().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.targetField.requestFocusInWindow();
   }

   private UiProvider layout() {
      TwoColumnPanel var1 = new TwoColumnPanel()
         .withBorder(StyleSheet.getDefaultTitleBorder(TreeStrings.get("Search.Mappings.Found.Title")))
         .withAnchor(TwoColumnPanel.WEST)
         .withLeftInsets(0, 0, 10, 5)
         .withRightInsets(0, 0, 10, 0)
         .withRightFill(TwoColumnPanel.HORIZONTAL);
      var1.addRow(new JLabel(TreeStrings.get("Search.Mappings.TargetField")), this.layoutTargetField());
      var1.withRightWeights(1.0, 1.0).withRightFill(TwoColumnPanel.BOTH).withAnchor(TwoColumnPanel.NORTHWEST);
      var1.addRow(new JLabel("Expression:"), new JScrollPane(this.expressionArea));
      return var1;
   }

   private JComponent layoutTargetField() {
      BoxBuilder var1 = BoxBuilder.horizontal();
      return var1.addAllWithSpace(10, this.targetField, this.openMappingAction).container();
   }

   private JTextField createTargetField() {
      KongaTextField var1 = new KongaTextField(50);
      var1.setEditable(false);
      var1.setBackground(javax.swing.UIManager.getColor("Panel.background"));
      return var1;
   }

   private JTextPane createExpressionArea() {
      KongaTextPane var1 = new KongaTextPane("").disableTabs();
      var1.setFont(TextStyles.DefaultMonoSpaced.getFont());
      ScriptSyntaxHighlighter.install(var1);
      var1.setEditable(false);
      var1.setPreferredSize(new Dimension(50, 125));
      return var1;
   }

   private class OpenMappingAction extends AbstractAction {
      public OpenMappingAction() {
         super(TreeStrings.get("Search.Mappings.Open"));
         this.putValue("ShortDescription", TreeStrings.get("Search.Mappings.Open.ToolTip"));
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         String var2 = MappingSearchMatchDisplayer.this.targetField.getText();
         if (StringUtils.isNotEmpty(var2)) {
            Node var3 = MappingSearchMatchDisplayer.this.treeMapper.m_targetTreeComponent.findNode_expandTreeIfNotExist(var2);
            if (var3 != null) {
               MappingSearchMatchDisplayer.this.treeMapper.showFormulaBuilder(var3);
            }
         }
      }
   }
}

