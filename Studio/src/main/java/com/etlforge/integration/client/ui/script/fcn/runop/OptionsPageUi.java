package com.etlforge.integration.client.ui.script.fcn.runop;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import org.etlforge.application.ui.wizard.NumberedSectionsLayout;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.button.KongaRadioButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class OptionsPageUi implements UiProvider {
   private final JRadioButton synchronous = new KongaRadioButton("&Run immediately and wait until done (synchronous)", true);
   private final JRadioButton asynchronous = new KongaRadioButton("Place operation on the &queue (asynchronous)", false);
   private final JCheckBox raiseError = new KongaCheckBox("Raise &error if operation fails");
   private final JCheckBox cancelChain = new KongaCheckBox("&Cancel operation chain if operation fails");

   public OptionsPageUi() {
      this.configureModeButtons();
   }

   private void configureModeButtons() {
      ButtonUtils.makeMutuallyExclusive(this.synchronous, this.asynchronous);
      this.synchronous.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent var1) {
            OptionsPageUi.this.updateErrorHandlingState();
         }
      });
      this.updateErrorHandlingState();
   }

   private void updateErrorHandlingState() {
      boolean var1 = this.isSynchronous();
      this.raiseError.setEnabled(var1);
      this.cancelChain.setEnabled(var1);
   }

   public boolean isSynchronous() {
      return this.synchronous.isSelected();
   }

   public boolean raiseErrorOnFailure() {
      return this.raiseError.isSelected();
   }

   public boolean cancelChainOnFailure() {
      return this.cancelChain.isSelected();
   }

   @Override
   public void decorate(UiCanvas var1) {
      OptionsPageUi.Layout var2 = new OptionsPageUi.Layout();
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.synchronous.requestFocusInWindow();
   }

   private class Layout {
      private final NumberedSectionsLayout layout = new NumberedSectionsLayout();

      public Layout() {
         this.layoutMode();
         this.layoutErrorHandling();
      }

      private void layoutMode() {
         Grid var1 = Grid.oneColumn(10);
         var1.addAll(OptionsPageUi.this.synchronous, OptionsPageUi.this.asynchronous);
         this.layout.addSection("How should the operation run", InvisiblePanel.of(var1));
      }

      private void layoutErrorHandling() {
         Grid var1 = Grid.oneColumn(10);
         var1.addAll(OptionsPageUi.this.raiseError, OptionsPageUi.this.cancelChain);
         this.layout.addSection("Error handling", InvisiblePanel.of(var1));
      }

      public void decorate(UiCanvas var1) {
         this.layout.decorate(var1);
      }
   }
}
