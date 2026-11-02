package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import org.etlforge.application.ui.widget.StringListUi;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.widget.DocumentContentChangeListener;
import org.etlforge.ui.widget.text.KongaTextField;

abstract class ConditionValueUi implements DataEntryUi<ConditionValue> {
   private final List<ChangeListener> changeListeners = Lists.newCopyOnWriteArrayList();

   public static final ConditionValueUi forType(OperatorValueType var0) {
      switch (var0) {
         case NO_VALUE:
            return new ConditionValueUi.NoValueUi();
         case SINGLE_VALUE:
            return new ConditionValueUi.SingleValueUi();
         case RANGE:
            return new ConditionValueUi.RangeValueUi();
         case MULTIPLE_VALUES:
            return new ConditionValueUi.MultiValueUi();
         default:
            throw new RuntimeException("Unexpected value type: " + var0);
      }
   }

   public abstract void setValue(ConditionValue var1);

   public final void addChangeListener(ChangeListener var1) {
      this.changeListeners.add(var1);
   }

   public final void removeChangeListener(ChangeListener var1) {
      this.changeListeners.remove(var1);
   }

   protected final void fireChangeEvent() {
      if (!this.changeListeners.isEmpty()) {
         ChangeEvent var1 = new ChangeEvent(this);

         for (ChangeListener var3 : this.changeListeners) {
            var3.stateChanged(var1);
         }
      }
   }

   protected final DocumentContentChangeListener documentChangeListener() {
      return new DocumentContentChangeListener() {
         @Override
         public void contentChanged(DocumentEvent var1) {
            ConditionValueUi.this.fireChangeEvent();
         }
      };
   }

   private static class MultiValueUi extends ConditionValueUi {
      private final StringListUi stringListUi = new StringListUi();

      public MultiValueUi() {
         this.stringListUi.asChangeSource().addInputChangeListener(new InputChangeListener() {
            @Override
            public void inputChanged(InputChangeEvent var1) {
               MultiValueUi.this.fireChangeEvent();
            }
         });
      }

      @Override
      public void decorate(UiCanvas var1) {
         this.stringListUi.decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.stringListUi.requestFocus();
      }

      @Override
      public Optional<ConditionValue> getEnteredValue() {
         MultiConditionValue var1 = new MultiConditionValue(this.stringListUi.getStringList());
         return Optional.of(var1);
      }

      @Override
      public void setValue(ConditionValue var1) {
         ArgCheck.checkInstance(var1, MultiConditionValue.class);
         MultiConditionValue var2 = (MultiConditionValue)var1;
         this.stringListUi.setStringList(var2.getValues());
      }

      @Override
      public EnteredDataValidation validate() {
         return this.stringListUi.isEmpty() ? EnteredDataValidation.error("Please enter a value.") : EnteredDataValidation.OK;
      }

      @Override
      public void focusInvalidComonent() {
         this.requestFocus();
      }
   }

   private static class NoValueUi extends ConditionValueUi {
      private NoValueUi() {
      }

      @Override
      public void decorate(UiCanvas var1) {
         JLabel var2 = new JLabel("[The selected operator does not take an input value]");
         Vertical.Anchor.north(var2).decorate(var1);
      }

      @Override
      public void requestFocus() {
      }

      @Override
      public Optional<ConditionValue> getEnteredValue() {
         return Optional.of(ConditionValue.NONE);
      }

      @Override
      public void setValue(ConditionValue var1) {
         Preconditions.checkArgument(var1 == ConditionValue.NONE, "Only ConditionValue.NONE accepted");
      }

      @Override
      public EnteredDataValidation validate() {
         return EnteredDataValidation.OK;
      }

      @Override
      public void focusInvalidComonent() {
         throw new UnsupportedOperationException();
      }
   }

   private static class RangeValueUi extends ConditionValueUi {
      private final KongaTextField fromField = new KongaTextField();
      private final KongaTextField toField = new KongaTextField();

      public RangeValueUi() {
         this.fromField.setLabel("&From: ");
         this.toField.setLabel("&To: ");
         DocumentContentChangeListener var1 = this.documentChangeListener();
         this.fromField.getDocument().addDocumentListener(var1);
         this.toField.getDocument().addDocumentListener(var1);
      }

      @Override
      public void decorate(UiCanvas var1) {
         Grid var2 = Vertical.grid(8).of(this.fromField.asUiProvider(), this.toField.asUiProvider());
         Vertical.Anchor.north(var2.nonOpaque()).decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.fromField.requestFocusInWindow();
      }

      @Override
      public Optional<ConditionValue> getEnteredValue() {
         String var1 = this.fromField.getText().trim();
         String var2 = this.toField.getText().trim();
         RangeConditionValue var3 = new RangeConditionValue(var1, var2);
         return Optional.of(var3);
      }

      @Override
      public void setValue(ConditionValue var1) {
         ArgCheck.checkInstance(var1, RangeConditionValue.class);
         RangeConditionValue var2 = (RangeConditionValue)var1;
         this.fromField.setText(var2.lower());
         this.fromField.setCaretPosition(0);
         this.toField.setText(var2.upper());
         this.toField.setCaretPosition(0);
      }

      @Override
      public EnteredDataValidation validate() {
         if (this.hasNoValue(this.fromField)) {
            return EnteredDataValidation.error("Please enter a From value.");
         } else {
            return this.hasNoValue(this.toField) ? EnteredDataValidation.error("Please enter a To value.") : EnteredDataValidation.OK;
         }
      }

      private boolean hasNoValue(KongaTextField var1) {
         String var2 = var1.getText().trim();
         return var2.isEmpty();
      }

      @Override
      public void focusInvalidComonent() {
         if (this.hasNoValue(this.fromField)) {
            this.fromField.requestFocusInWindow();
         } else if (this.hasNoValue(this.toField)) {
            this.toField.requestFocusInWindow();
         }
      }
   }

   private static class SingleValueUi extends ConditionValueUi {
      private final KongaTextField textField = new KongaTextField();

      public SingleValueUi() {
         this.textField.setLabel("&Value: ");
         this.textField.getDocument().addDocumentListener(this.documentChangeListener());
      }

      @Override
      public void decorate(UiCanvas var1) {
         Vertical.Anchor.north(this.textField.asUiProvider()).decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.textField.requestFocusInWindow();
      }

      @Override
      public Optional<ConditionValue> getEnteredValue() {
         SingleConditionValue var1 = new SingleConditionValue(this.textField.getText().trim());
         return Optional.of(var1);
      }

      @Override
      public void setValue(ConditionValue var1) {
         ArgCheck.checkInstance(var1, SingleConditionValue.class);
         String var2 = ((SingleConditionValue)var1).getValue();
         this.textField.setText(var2);
         this.textField.setCaretPosition(0);
      }

      @Override
      public EnteredDataValidation validate() {
         return EnteredDataValidation.OK;
      }

      @Override
      public void focusInvalidComonent() {
         throw new UnsupportedOperationException();
      }
   }
}
