package org.etlforge.ui.input;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.undo.KongaCompoundEdit;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public class InputPanel implements UndoableEditListener, InputChangeListener, InputPanelSite {
   private final InputPanel.Ui ui;
   private final List<InputField> inputFields = new ArrayList<>();
   private final List<InputPanelSite> inputPanels = new ArrayList<>();
   private final List<ChangeSource> otherChangeSources = new ArrayList<>();
   private final ChangeSourceManager changeManager = new ChangeSourceManager();
   private final List<InputFieldValidator> fieldValidators = new ArrayList<>();
   private final List<InputFieldDecorator> fieldDecorators = new ArrayList<>();
   private boolean active = true;
   private boolean disableInactiveInputs = true;
   private boolean fixedSize = true;
   private KongaCompoundEdit compoundEdit;

   public InputPanel(LayoutManager var1, boolean var2) {
      this.ui = new InputPanel.Ui(var1, var2);
   }

   public InputPanel(LayoutManager var1) {
      this.ui = new InputPanel.Ui(var1);
   }

   public InputPanel(boolean var1) {
      this.ui = new InputPanel.Ui(var1);
   }

   public InputPanel() {
      this.ui = new InputPanel.Ui();
   }

   public JPanel getUi() {
      return this.ui;
   }

   public void setLayout(LayoutManager var1) {
      this.ui.setLayout(var1);
   }

   public void add(Component var1) {
      this.ui.add(var1);
   }

   public void add(Component var1, Object var2) {
      this.ui.add(var1, var2);
   }

   public void add(InputPanel var1) {
      this.ui.add(var1.getUi());
   }

   public void add(InputPanel var1, Object var2) {
      this.ui.add(var1.getUi(), var2);
   }

   public void setBorder(Border var1) {
      this.ui.setBorder(var1);
   }

   public void setOpaque(boolean var1) {
      this.ui.setOpaque(var1);
   }

   public Dimension getPreferredSize() {
      return this.ui.getPreferredSize();
   }

   public Dimension getMaximumSize() {
      return this.ui.getMaximumSize();
   }

   public final void addPropertyChangeListener(PropertyChangeListener var1) {
      this.ui.addPropertyChangeListener(var1);
   }

   public final void addPropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.ui.addPropertyChangeListener(var1, var2);
   }

   public final void removePropertyChangeListener(PropertyChangeListener var1) {
      this.ui.removePropertyChangeListener(var1);
   }

   public final void removePropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.ui.removePropertyChangeListener(var1, var2);
   }

   protected final void firePropertyChange(String var1, Object var2, Object var3) {
      this.ui.firePropertyChange(var1, var2, var3);
   }

   public void addInputField(InputField var1) {
      this.addInputField(var1, null, null);
   }

   public void addInputField(InputField var1, InputFieldValidator var2) {
      this.addInputField(var1, var2, null);
   }

   public void addInputField(InputField var1, InputFieldValidator var2, InputFieldDecorator var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("field can't be null.");
      }

      this.inputFields.add(var1);
      this.fieldValidators.add(var2);
      this.fieldDecorators.add(var3);
      var1.addInputChangeListener(this);
      var1.setSendInputChangeEvents(this.isSendingInputChangeEvents());
      var1.addUndoableEditListener(this);
      var1.setSendUndoEvents(this.isSendingUndoEvents());
      if (var2 != null && var3 != null) {
         new InputPanel.FocusLostHandler(var1, var2, var3);
         if (var2.isFieldRequired(var1)) {
            var3.decorateField(var1, var2.isFieldValid(var1));
         }
      }
   }

   public final boolean removeInputField(InputField var1) {
      int var2 = this.inputFields.indexOf(var1);
      if (var2 < 0) {
         return false;
      }

      var1.removeInputChangeListener(this);
      var1.removeUndoableEditListener(this);
      this.inputFields.remove(var2);
      this.fieldValidators.remove(var2);
      this.fieldDecorators.remove(var2);
      return true;
   }

   public void addInputPanel(InputPanelSite var1) {
      this.configureAddedChangeSource(var1);
      this.inputPanels.add(var1);
   }

   public void addChangeSource(ChangeSource var1) {
      this.configureAddedChangeSource(var1);
      this.otherChangeSources.add(var1);
   }

   private void configureAddedChangeSource(ChangeSource var1) {
      var1.addInputChangeListener(this);
      var1.addUndoableEditListener(this);
      var1.setSendInputChangeEvents(this.isSendingInputChangeEvents());
      var1.setSendUndoEvents(this.isSendingUndoEvents());
   }

   public void removeInputPanel(InputPanelSite var1) {
      if (var1 != null) {
         if (this.inputPanels.remove(var1)) {
            this.removeChangeSourceImpl(var1);
         }
      }
   }

   public void removeChangeSource(ChangeSource var1) {
      if (var1 != null) {
         if (this.otherChangeSources.remove(var1)) {
            this.removeChangeSourceImpl(var1);
         }
      }
   }

   private void removeChangeSourceImpl(ChangeSource var1) {
      var1.removeInputChangeListener(this);
      var1.removeUndoableEditListener(this);
   }

   @Override
   public void setActive(boolean var1) {
      if (var1 != this.active) {
         for (InputPanelSite var3 : this.inputPanels) {
            var3.setActive(var1);
         }

         for (int var5 = 0; var5 < this.inputFields.size(); var5++) {
            InputFieldValidator var6 = this.fieldValidators.get(var5);
            if (var6 != null) {
               InputFieldDecorator var4 = this.fieldDecorators.get(var5);
               if (var4 != null) {
                  var4.setActive(var1);
               }
            }

            if (this.disableInactiveInputs) {
               InputField var7 = this.inputFields.get(var5);
               if (var7.getLabel() != null) {
                  var7.getLabel().setEnabled(var1);
               }

               var7.setEnabled(var1);
            }
         }

         this.decorateFields();
         this.active = var1;
      }
   }

   public boolean isActive() {
      return this.active;
   }

   public void setDisableFieldsWhenInactive(boolean var1) {
      this.disableInactiveInputs = var1;
   }

   public boolean areFieldsDisabledWhenInactive() {
      return this.disableInactiveInputs;
   }

   @Override
   public void validateInput() throws InputPanelException {
      if (this.active) {
         ArrayList var1 = null;
         int var2 = 0;

         for (int var3 = this.inputFields.size(); var2 < var3; var2++) {
            InputFieldValidator var4 = this.fieldValidators.get(var2);
            boolean var5 = true;
            if (var4 != null) {
               InputField var6 = this.inputFields.get(var2);
               if (var6.isActive()) {
                  try {
                     var4.validateField(var6);
                  } catch (InputFieldException var10) {
                     if (var1 == null) {
                        var1 = new ArrayList();
                     }

                     var1.add(var10);
                     var5 = false;
                  }

                  InputFieldDecorator var7 = this.fieldDecorators.get(var2);
                  if (var7 != null) {
                     var7.decorateField(var6, var5);
                  }
               }
            }
         }

         for (InputPanelSite var12 : this.inputPanels) {
            try {
               var12.validateInput();
            } catch (InputPanelException var9) {
               if (var1 == null) {
                  var1 = new ArrayList();
               }

               for (InputFieldException var8 : var9.getFieldExceptions()) {
                  var1.add(var8);
               }
            }
         }

         if (var1 != null) {
            throw new InputPanelException(var1);
         }
      }
   }

   @Override
   public void decorateFields() {
      for (InputPanelSite var2 : this.inputPanels) {
         var2.decorateFields();
      }

      for (int var5 = 0; var5 < this.inputFields.size(); var5++) {
         InputFieldValidator var6 = this.fieldValidators.get(var5);
         if (var6 != null) {
            InputFieldDecorator var3 = this.fieldDecorators.get(var5);
            if (var3 != null) {
               InputField var4 = this.inputFields.get(var5);
               var3.decorateField(var4, var6.isFieldValid(var4));
            }
         }
      }
   }

   @Override
   public boolean isInputValid() {
      if (!this.active) {
         return true;
      }

      int var1 = 0;

      for (int var2 = this.inputFields.size(); var1 < var2; var1++) {
         InputFieldValidator var3 = this.fieldValidators.get(var1);
         if (var3 != null && !var3.isFieldValid(this.inputFields.get(var1))) {
            return false;
         }
      }

      for (InputPanelSite var5 : this.inputPanels) {
         if (!var5.isInputValid()) {
            return false;
         }
      }

      return true;
   }

   public void setFixedSize(boolean var1) {
      this.fixedSize = var1;
   }

   public boolean isFixedSize() {
      return this.fixedSize;
   }

   @Override
   public final boolean isSendingInputChangeEvents() {
      return this.changeManager.isSendingInputChangeEvents();
   }

   @Override
   public final void setSendInputChangeEvents(boolean var1) {
      if (var1 != this.isSendingInputChangeEvents()) {
         this.changeManager.setSendInputChangeEvents(var1);

         for (InputPanelSite var3 : this.inputPanels) {
            var3.setSendInputChangeEvents(var1);
         }

         for (InputField var6 : this.inputFields) {
            var6.setSendInputChangeEvents(var1);
         }

         for (ChangeSource var7 : this.otherChangeSources) {
            var7.setSendInputChangeEvents(var1);
         }
      }
   }

   @Override
   public final void addInputChangeListener(InputChangeListener var1) {
      this.changeManager.addInputChangeListener(var1);
   }

   @Override
   public final void removeInputChangeListener(InputChangeListener var1) {
      this.changeManager.removeInputChangeListener(var1);
   }

   @Override
   public void inputChanged(InputChangeEvent var1) {
      this.changeManager.sendChangeEvent(var1);
   }

   protected final void fireInputChanged(Object var1, String var2) {
      this.fireInputChanged(var1, var2, null, null);
   }

   protected final void fireInputChanged(Object var1, String var2, Object var3, Object var4) {
      if (this.isSendingInputChangeEvents()) {
         InputChangeEvent var5 = new InputChangeEvent(this, var1, var2, var3, var4);
         this.changeManager.sendChangeEvent(var5);
      }
   }

   @Override
   public void addUndoableEditListener(UndoableEditListener var1) {
      this.changeManager.addUndoableEditListener(var1);
   }

   @Override
   public void removeUndoableEditListener(UndoableEditListener var1) {
      this.changeManager.removeUndoableEditListener(var1);
   }

   protected final void fireUndoableEditEvent(UndoableEditEvent var1) {
      this.changeManager.sendUndoableEdit(var1);
   }

   @Override
   public void undoableEditHappened(UndoableEditEvent var1) {
      if (this.compoundEdit == null) {
         this.changeManager.sendUndoableEdit(var1);
      } else {
         this.compoundEdit.addEdit(var1.getEdit());
      }
   }

   public final void startCompoundEdit(@Nullable String var1) {
      if (this.compoundEdit == null) {
         this.compoundEdit = new KongaCompoundEdit(var1);
      }
   }

   public final void endCompoundEdit() {
      if (this.compoundEdit != null && !this.compoundEdit.isEmpty()) {
         this.compoundEdit.end();
         this.changeManager.sendUndoableEdit(new UndoableEditEvent(this, this.compoundEdit));
         this.compoundEdit = null;
      }
   }

   @Override
   public boolean isSendingUndoEvents() {
      return this.changeManager.isSendingUndoEvents();
   }

   @Override
   public void setSendUndoEvents(boolean var1) {
      if (var1 != this.isSendingUndoEvents()) {
         this.changeManager.setSendUndoEvents(var1);

         for (InputPanelSite var3 : this.inputPanels) {
            var3.setSendUndoEvents(var1);
         }

         for (InputField var6 : this.inputFields) {
            var6.setSendUndoEvents(var1);
         }

         for (ChangeSource var7 : this.otherChangeSources) {
            var7.setSendUndoEvents(var1);
         }
      }
   }

   @Override
   public void dispose() {
      for (InputPanelSite var2 : this.inputPanels) {
         var2.dispose();
      }

      for (InputField var4 : this.inputFields) {
         var4.dispose();
      }

      this.inputPanels.clear();
      this.inputFields.clear();
      this.otherChangeSources.clear();
      this.ui.removeAll();
   }

   public void wrap(UiProvider var1) {
      this.ui.removeAll();
      InvisiblePanel.convert(this.ui);
      var1.decorate(new UiCanvas() {
         @Override
         public void display(Component var1) {
            InputPanel.this.ui.add(var1);
         }
      });
   }

   @Override
   public void visitAllFields(InputFieldVisitor var1) {
      for (InputField var3 : this.inputFields) {
         var1.visit(var3);
      }

      for (InputPanelSite var5 : this.inputPanels) {
         var5.visitAllFields(var1);
      }
   }

   public void disable() {
      this.visitAllFields(new InputFieldVisitor() {
         @Override
         public void visit(InputField var1) {
            var1.setEnabled(false);
         }
      });
   }

   public void updateSilently(Runnable var1) {
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

   private static class FocusLostHandler implements FocusListener {
      private final InputFieldValidator validator;
      private final InputFieldDecorator decorator;
      private final InputField field;

      public FocusLostHandler(InputField var1, InputFieldValidator var2, InputFieldDecorator var3) {
         this.field = var1;
         this.validator = var2;
         this.decorator = var3;
         var1.getInputComponent().addFocusListener(this);
      }

      @Override
      public void focusGained(FocusEvent var1) {
      }

      @Override
      public void focusLost(FocusEvent var1) {
         this.decorator.decorateField(this.field, this.validator.isFieldValid(this.field));
      }
   }

   private class Ui extends JPanel {
      public Ui() {
      }

      public Ui(boolean var2) {
         super(var2);
      }

      public Ui(LayoutManager var2, boolean var3) {
         super(var2, var3);
      }

      public Ui(LayoutManager var2) {
         super(var2);
      }

      @Override
      public Dimension getMaximumSize() {
         return InputPanel.this.fixedSize ? this.getPreferredSize() : super.getMaximumSize();
      }

      @Override
      public Dimension getMinimumSize() {
         return InputPanel.this.fixedSize ? this.getPreferredSize() : super.getMinimumSize();
      }

      @Override
      protected void firePropertyChange(String var1, Object var2, Object var3) {
         super.firePropertyChange(var1, var2, var3);
      }
   }
}
