package org.etlforge.integration.client.ui.validation.indicator;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.ui.validation.ValidationIcons;
import org.etlforge.integration.client.ui.validation.resources.ValidationUiResources;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.validation.EntityCollectionValidationResult;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaButton;

final class ValidationResultButtonTrigger extends AbstractValidationResultTrigger {
   private final EntityType entityType;
   private JButton button;
   private ActionListener invalidActionListener;
   private ValidationResultButtonTrigger.ButtonMode buttonMode;

   public ValidationResultButtonTrigger(EntityType var1) {
      this.entityType = var1;
   }

   @Override
   public void update(EntityCollectionValidationResult var1) {
      super.update(var1);
      ValidationResultButtonTrigger.ButtonMode var2 = ValidationResultButtonTrigger.ButtonMode.from(var1);
      if (var2 != this.buttonMode) {
         this.buttonMode = var2;
         this.updateButtons(var2);
      }
   }

   private void updateButtons(ValidationResultButtonTrigger.ButtonMode var1) {
      assert var1 != null;
      switch (var1) {
         case VALID:
            this.decorateValidButton(this.button);
            break;
         case INVALID:
            this.decorateInvalidButton(this.button);
            break;
         case WARNING:
            this.decorateWarningButton(this.button);
            break;
         default:
            assert false : var1;
      }
   }

   @Override
   protected JComponent createTransientTrigger() {
      this.buttonMode = ValidationResultButtonTrigger.ButtonMode.UNKNOWN;
      this.button = createButton();
      return this.button;
   }

   @Override
   protected JComponent createValidTrigger() {
      this.buttonMode = ValidationResultButtonTrigger.ButtonMode.VALID;
      this.button = createButton();
      this.decorateValidButton(this.button);
      return this.button;
   }

   @Override
   protected JComponent createInvalidTrigger() {
      this.buttonMode = ValidationResultButtonTrigger.ButtonMode.INVALID;
      this.button = createButton();
      this.decorateInvalidButton(this.button);
      return this.button;
   }

   private void decorateValidButton(JButton var1) {
      TextStyles.DefaultValidText.makeOver(var1);
      var1.setText(ValidationUiResources.getString("Valid"));
      var1.setIcon(ValidationIcons.Indicators.VALID);
      this.configureButtonListenerWhenValid(var1);
   }

   private void decorateInvalidButton(JButton var1) {
      TextStyles.DefaultErrorText.makeOver(var1);
      var1.setText(ValidationUiResources.getString("Invalid"));
      var1.setIcon(ValidationIcons.Indicators.INVALID);
      this.configureButtonListenerWhenInvalid(var1);
   }

   private void decorateWarningButton(JButton var1) {
      TextStyles.BlueWarningText.makeOver(var1);
      if (this.entityType == EntityType.SalesforceConnector) {
         var1.setText(ValidationUiResources.getString("Unverified"));
      } else {
         var1.setText(ValidationUiResources.getString("Warning"));
      }

      var1.setIcon(ValidationIcons.Indicators.WARNING);
      this.configureButtonListenerWhenWarning(var1);
   }

   private void configureButtonListenerWhenInvalid(JButton var1) {
      if (this.invalidActionListener == null) {
         this.invalidActionListener = new ValidationResultButtonTrigger.InvalidActionListener();
      }

      var1.addActionListener(this.invalidActionListener);
   }

   private void configureButtonListenerWhenWarning(JButton var1) {
      this.configureButtonListenerWhenInvalid(var1);
   }

   private void configureButtonListenerWhenValid(JButton var1) {
      var1.removeActionListener(this.invalidActionListener);
   }

   private static JButton createButton() {
      return ButtonUtils.configureButtonForToolBar(new KongaButton());
   }

   private enum ButtonMode {
      VALID,
      INVALID,
      WARNING,
      UNKNOWN;

      public static ValidationResultButtonTrigger.ButtonMode from(EntityCollectionValidationResult var0) {
         if (var0 == null) {
            return UNKNOWN;
         }

         switch (var0.status()) {
            case VALID:
               return VALID;
            case INVALID:
               return INVALID;
            case WARNING:
               return WARNING;
            default:
               assert false : var0.status();
               return null;
         }
      }
   }

   private class InvalidActionListener implements ActionListener {
      private InvalidActionListener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ValidationResultButtonTrigger.this.showValidationProblems(ValidationResultButtonTrigger.this.button, new Point(10, 10));
      }
   }
}
