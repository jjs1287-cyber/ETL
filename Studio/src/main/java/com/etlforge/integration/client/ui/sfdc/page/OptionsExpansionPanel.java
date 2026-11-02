package com.etlforge.integration.client.ui.sfdc.page;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JSeparator;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;

public final class OptionsExpansionPanel {
   private final UiProvider optionsLayout;
   private final BorderLayoutBuilder container = new BorderLayoutBuilder().nonOpaque();
   private final OptionsExpansionPanel.ExpandAction expandAction = new OptionsExpansionPanel.ExpandAction();
   private final OptionsExpansionPanel.CollapseAction collapseAction = new OptionsExpansionPanel.CollapseAction();
   private final KongaButton expander = new KongaButton(this.expandAction).makeToolbarButton();

   public OptionsExpansionPanel(final OptionsPanel var1) {
      this.optionsLayout = new UiProviderAdapter(var1.getUi()) {
         @Override
         public void requestFocus() {
            var1.requestFocus();
         }
      };
   }

   public void layoutIn(TwoColumnLayout var1) {
      var1.addRow(this.layoutLabel(), this.container.container());
   }

   private JComponent layoutLabel() {
      KongaLabel var1 = KongaLabel.withMnemonic("Advanced &Options");
      var1.setLabelForButton(this.expander);
      return Horizontal.box().of(var1, this.expander).deeplyNonOpaque().container();
   }

   private class CollapseAction extends AbstractAction {
      public CollapseAction() {
         super("", UiResourceBundle.getIcon("DropDownExpanded.16"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               CollapseAction.this.collapse();
            }
         });
      }

      private void collapse() {
         OptionsExpansionPanel.this.expander.setAction(OptionsExpansionPanel.this.expandAction);
         OptionsExpansionPanel.this.container.removeCenter();
         OptionsExpansionPanel.this.container.removeSouth();
         OptionsExpansionPanel.this.container.refreshLayout();
      }
   }

   private class ExpandAction extends AbstractAction {
      public ExpandAction() {
         super("", UiResourceBundle.getIcon("DropDownCollapsedRight.16"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ExpandAction.this.expand();
            }
         });
      }

      private void expand() {
         OptionsExpansionPanel.this.expander.setAction(OptionsExpansionPanel.this.collapseAction);
         JSeparator var1 = new JSeparator();
         var1.setBorder(Empty.border(0, 0, 12, 0));
         OptionsExpansionPanel.this.container.center(OptionsExpansionPanel.this.optionsLayout).south(var1);
         OptionsExpansionPanel.this.container.refreshLayout();
         OptionsExpansionPanel.this.optionsLayout.requestFocus();
      }
   }
}
