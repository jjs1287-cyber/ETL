package org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.DocumentContentChangeListener;
import org.etlforge.ui.widget.text.KongaTextField;

abstract class AbstractWsdlSelector implements WsdlSelector, UiProvider {
   protected static final Insets FIELD_INSETS = new Insets(5, 0, 5, 5);
   private JComponent ui;
   private final PropertyChangeSupport propChangeSupport = new PropertyChangeSupport(this);
   private final List<ActionListener> actionListeners = new CopyOnWriteArrayList<>();

   protected AbstractWsdlSelector() {
   }

   @Override
   public final void addChangeListener(PropertyChangeListener var1) {
      this.propChangeSupport.addPropertyChangeListener("IWsdlDefiner#InputChanged", var1);
   }

   @Override
   public final void removeChangeListener(PropertyChangeListener var1) {
      this.propChangeSupport.removePropertyChangeListener("IWsdlDefiner#InputChanged", var1);
   }

   @Override
   public void addActionListener(ActionListener var1) {
      this.actionListeners.add(var1);
   }

   @Override
   public void removeActionListener(ActionListener var1) {
      this.actionListeners.remove(var1);
   }

   @Override
   public UiProvider getUi() {
      return this;
   }

   @Override
   public final void decorate(UiCanvas var1) {
      var1.display(this.ui);
   }

   protected final void setDisplayer(JComponent var1) {
      this.ui = var1;
   }

   protected final void fireInputChanged() {
      this.propChangeSupport.firePropertyChange("IWsdlDefiner#InputChanged", "0", "1");
   }

   protected final void fireActionEvent() {
      ActionEvent var1 = null;

      for (ActionListener var3 : this.actionListeners) {
         if (var1 == null) {
            var1 = new ActionEvent(this, 1001, "active_select");
         }

         var3.actionPerformed(var1);
      }
   }

   protected static GridBagConstraints getConstraints() {
      GridBagConstraints var0 = new GridBagConstraints();
      var0.anchor = 18;
      var0.fill = 1;
      var0.gridx = 0;
      return var0;
   }

   protected class WsdlInputField extends KongaTextField {
      public WsdlInputField() {
         super(30);
         this.addListeners();
      }

      @Override
      public Dimension getMaximumSize() {
         return this.getPreferredSize();
      }

      @Override
      public Dimension getMinimumSize() {
         return this.getPreferredSize();
      }

      private void addListeners() {
         this.getDocument().addDocumentListener(new DocumentContentChangeListener() {
            @Override
            public void contentChanged(DocumentEvent var1) {
               AbstractWsdlSelector.this.fireInputChanged();
            }
         });
         this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent var1) {
               AbstractWsdlSelector.this.fireActionEvent();
            }
         });
      }
   }
}
