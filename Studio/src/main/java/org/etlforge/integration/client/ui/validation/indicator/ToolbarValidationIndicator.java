package org.etlforge.integration.client.ui.validation.indicator;

import javax.swing.Box;
import javax.swing.JComponent;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.ui.validation.ValidationResultAcceptor;
import org.etlforge.integration.client.ui.validation.resources.ValidationUiResources;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.validation.EntityCollectionValidationResult;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.label.KongaLabel;

public final class ToolbarValidationIndicator {
   private final ToolbarValidationIndicator.Ui ui;

   public ToolbarValidationIndicator(EntityType var1) {
      this.ui = new ToolbarValidationIndicator.Ui(var1);
   }

   public UiProvider getUi() {
      return this.ui;
   }

   public void update(EntityCollectionValidationResult var1) {
      this.ui.accept(var1);
   }

   public void setEnabled(boolean var1) {
      this.ui.setEnabled(var1);
   }

   public void dispose() {
   }

   private static class Ui implements ValidationResultAcceptor, UiProvider {
      private final JComponent capsule;
      private final ValidationResultTrigger trigger;

      public Ui(EntityType var1) {
         this.trigger = new ValidationResultButtonTrigger(var1);
         this.capsule = Box.createHorizontalBox();
         this.capsule.add(Box.createHorizontalGlue());
         this.addCaption();
         this.addTrigger(var1);
         this.capsule.add(Box.createHorizontalStrut(5));
      }

      @Override
      public void decorate(UiCanvas var1) {
         var1.display(this.capsule);
      }

      @Override
      public void requestFocus() {
      }

      @Override
      public void accept(final EntityCollectionValidationResult var1) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               Ui.this.trigger.update(var1);
            }
         });
      }

      public void setEnabled(boolean var1) {
         this.trigger.setEnabled(var1);
      }

      private void addCaption() {
         KongaLabel var1 = TextStyles.DefaultBold.makeLabel(ValidationUiResources.getString("Status") + ":");
         this.capsule.add(var1);
      }

      private void addTrigger(EntityType var1) {
         this.capsule.add(this.trigger.getComponent());
         UiUtils.setOpaqueRecursively(this.capsule, false);
      }
   }
}
