package org.etlforge.ui.input;

import com.google.common.base.Preconditions;
import java.awt.Container;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoableEdit;
import org.apache.commons.lang.StringUtils;
import org.etlforge.ui.Enablable;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.focus.Focusable;
import org.etlforge.ui.widget.label.KongaLabel;

public abstract class InputField implements ChangeSource, Enablable, Focusable {
   private String name;
   private final JLabel label;
   private final JComponent input;
   private boolean active = true;
   private String undoRedoPresentation;
   private final ChangeSourceManager changeManager;
   private final List<InputFieldActivationConstraint> activationConstraints;

   public InputField(String var1, JComponent var2, JLabel var3) {
      this.input = Preconditions.checkNotNull(var2, "input");
      this.name = var1;
      this.label = var3;
      this.applyLabelMnemonic();
      this.changeManager = new ChangeSourceManager();
      this.activationConstraints = new ArrayList<>();
      if (StringUtils.isNotEmpty(var1)) {
         this.setUndoRedoPresentation(var1 + " Change");
      }
   }

   private void applyLabelMnemonic() {
      if (this.label != null) {
         if (this.label instanceof KongaLabel && this.input instanceof AbstractButton) {
            ((KongaLabel)this.label).setLabelForButton((AbstractButton)this.input);
         } else {
            this.label.setLabelFor(this.input);
         }
      }
   }

   public Object getValue() {
      return null;
   }

   public final String getName() {
      return this.name;
   }

   public final void setName(String var1) {
      this.name = var1;
   }

   public final JLabel getLabel() {
      return this.label;
   }

   public JComponent getInputComponent() {
      return this.input;
   }

   public void setActive(boolean var1) {
      this.active = this.getEffectiveActiveEnabledValue(var1);
   }

   public final boolean isActive() {
      return this.active;
   }

   @Override
   public final void setEnabled(boolean var1) {
      boolean var2 = this.getEffectiveActiveEnabledValue(var1);
      UiUtils.setEnabled(var2, this.input, this.label);
      this.enabledStateChanged(var2);
   }

   protected void enabledStateChanged(boolean var1) {
   }

   @Override
   public final boolean isEnabled() {
      return this.input.isEnabled();
   }

   public abstract void makeReadOnly();

   public final void addPropertyChangeListener(PropertyChangeListener var1) {
      this.input.addPropertyChangeListener(var1);
   }

   public final void addPropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.input.addPropertyChangeListener(var1, var2);
   }

   public final void removePropertyChangeListener(PropertyChangeListener var1) {
      this.input.removePropertyChangeListener(var1);
   }

   public final void removePropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.input.removePropertyChangeListener(var1, var2);
   }

   @Override
   public final void addInputChangeListener(InputChangeListener var1) {
      this.changeManager.addInputChangeListener(var1);
   }

   @Override
   public final void removeInputChangeListener(InputChangeListener var1) {
      this.changeManager.removeInputChangeListener(var1);
   }

   protected final void fireInputChangeEvent() {
      this.fireInputChangeEvent(null, null);
   }

   protected final void fireInputChangeEvent(Object var1, Object var2) {
      if (this.isSendingInputChangeEvents()) {
         InputChangeEvent var3 = new InputChangeEvent(this, this, this.getName(), var1, var2);
         this.changeManager.sendChangeEvent(var3);
      }
   }

   @Override
   public final boolean isSendingInputChangeEvents() {
      return this.changeManager.isSendingInputChangeEvents();
   }

   @Override
   public final void setSendInputChangeEvents(boolean var1) {
      this.changeManager.setSendInputChangeEvents(var1);
   }

   @Override
   public final void addUndoableEditListener(UndoableEditListener var1) {
      this.changeManager.addUndoableEditListener(var1);
   }

   @Override
   public final void removeUndoableEditListener(UndoableEditListener var1) {
      this.changeManager.removeUndoableEditListener(var1);
   }

   protected final void fireUndoableEditHappened(UndoableEdit var1) {
      this.changeManager.sendUndoableEdit(this, var1);
   }

   @Override
   public boolean isSendingUndoEvents() {
      return this.changeManager.isSendingUndoEvents();
   }

   @Override
   public final void setSendUndoEvents(boolean var1) {
      this.changeManager.setSendUndoEvents(var1);
   }

   public void dispose() {
   }

   public final void layoutVertically(Container var1) {
      BorderLayoutBuilder.from(var1, 0, 2).north(this.label).center(this.input);
   }

   public final void layoutIn(TwoColumnLayout var1) {
      var1.addRow(this.label, this.input);
   }

   public final void setUndoRedoPresentation(String var1) {
      this.undoRedoPresentation = var1;
   }

   public final String getUndoRedoPresentation() {
      return this.undoRedoPresentation;
   }

   @Override
   public final void requestFocus() {
      this.input.requestFocusInWindow();
   }

   public final void addActivationConstraint(InputFieldActivationConstraint var1) {
      this.activationConstraints.add(Preconditions.checkNotNull(var1));
   }

   public final void removeActivationConstraint(InputFieldActivationConstraint var1) {
      this.activationConstraints.remove(var1);
   }

   public final void updateSilently(Runnable var1) {
      boolean var2 = this.isSendingInputChangeEvents();
      boolean var3 = this.isSendingUndoEvents();

      try {
         this.setSendInputChangeEvents(false);
         this.setSendUndoEvents(false);
         var1.run();
      } finally {
         this.setSendInputChangeEvents(var2);
         this.setSendUndoEvents(var3);
      }
   }

   private boolean getEffectiveActiveEnabledValue(boolean var1) {
      if (var1) {
         for (InputFieldActivationConstraint var3 : this.activationConstraints) {
            if (!var3.evaluate()) {
               return false;
            }
         }
      }

      return var1;
   }

   @Override
   public final void addFocusListener(FocusListener var1) {
      this.input.addFocusListener(var1);
   }

   @Override
   public final void removeFocusListener(FocusListener var1) {
      this.input.removeFocusListener(var1);
   }
}
