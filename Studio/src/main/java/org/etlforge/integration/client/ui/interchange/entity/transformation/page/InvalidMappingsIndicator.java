package org.etlforge.integration.client.ui.interchange.entity.transformation.page;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.InvalidMappingsListener;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.InvalidMappingsViewer;
import org.etlforge.integration.structure.mapping.InvalidMapping;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;

public class InvalidMappingsIndicator implements UiProvider {
   private final TreeMapper treeMapper;
   private final Action viewDetailsAction;
   private final JComponent ui;
   private JComponent initialFocusOwner;

   public InvalidMappingsIndicator(TreeMapper var1) {
      this.treeMapper = var1;
      this.viewDetailsAction = new InvalidMappingsIndicator.ViewDetailsAction();
      this.ui = this.createUi();
      this.treeMapper.getMappingManager().addInvalidMappingsListener(new InvalidMappingsListener() {
         @Override
         public void invalidMappingsChanged(TreeMapper var1, InvalidMapping[] var2) {
            InvalidMappingsIndicator.this.updateState(var2);
         }
      });
      this.updateState(var1.getMappingManager().getInvalidMappings());
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.ui);
   }

   @Override
   public void requestFocus() {
      this.initialFocusOwner.requestFocusInWindow();
   }

   public void setVisible(final boolean var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            InvalidMappingsIndicator.this.ui.setVisible(var1);
            JRootPane var1x = SwingUtilities.getRootPane(InvalidMappingsIndicator.this.ui);
            if (var1x != null) {
               var1x.invalidate();
               var1x.repaint();
            }
         }
      });
   }

   private void updateState(InvalidMapping[] var1) {
      boolean var2 = var1.length > 0;
      this.setVisible(var2);
      this.viewDetailsAction.setEnabled(var2);
   }

   private JComponent createUi() {
      KongaButton var1 = new KongaButton(this.viewDetailsAction);
      this.initialFocusOwner = var1;
      BoxBuilder var2 = BoxBuilder.horizontal();
      var2.strut(5).add(this.createLabel()).strut(10).add(var1);
      return var2.container();
   }

   private JLabel createLabel() {
      KongaLabel var1 = TextStyles.DefaultErrorText.makeLabel(Strings.get("Page.InvalidMappings.Label"));
      var1.setIcon(CommonIcons.ERROR_16);
      return var1;
   }

   private class ViewDetailsAction extends AbstractAction {
      public ViewDetailsAction() {
         super(Strings.get("Page.InvalidMappings.View"));
         this.putValue("ShortDescription", Strings.get("Page.InvalidMappings.View.ToolTip"));
      }

      @Override
      public void actionPerformed(final ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ViewDetailsAction.this.displayMappings(var1);
            }
         });
      }

      private void displayMappings(ActionEvent var1) {
         InvalidMappingsViewer var2 = new InvalidMappingsViewer(InvalidMappingsIndicator.this.treeMapper);
         if (var1.getSource() instanceof JComponent) {
            var2.setTrigger((JComponent)var1.getSource());
         }

         var2.show();
      }
   }
}
