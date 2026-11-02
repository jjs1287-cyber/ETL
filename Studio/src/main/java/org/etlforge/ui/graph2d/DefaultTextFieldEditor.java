package org.etlforge.ui.graph2d;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTextField;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.text.KongaTextField;

public class DefaultTextFieldEditor extends AbstractCellEditor implements GraphNodeEditor {
   private final Object node;
   private JTextField field;
   private Point offset;
   private boolean canceled;
   private boolean processingStop;

   public DefaultTextFieldEditor(Object var1) {
      this.node = var1;
      this.offset = new Point();
   }

   @Override
   public JComponent getComponent(Graph2D var1) {
      if (this.field == null) {
         this.field = this.createTextField(this.node);
         this.prepareField(this.field);
      }

      return this.field;
   }

   @Override
   public Object getNode() {
      return this.node;
   }

   public String getCellEditorValue() {
      return this.field.getText();
   }

   @Override
   public void cancelCellEditing() {
      this.canceled = true;
      super.cancelCellEditing();
   }

   @Override
   public Point getOffset() {
      return new Point(this.offset);
   }

   public void setOffset(Point var1) {
      this.offset = new Point(var1);
   }

   protected JTextField createTextField(Object var1) {
      KongaTextField var2 = new KongaTextField();
      var2.setSelectAllWhenFocused(true);
      return var2;
   }

   protected void prepareField(JTextField var1) {
      var1.addFocusListener(new FocusAdapter() {
         @Override
         public void focusLost(FocusEvent var1) {
            DefaultTextFieldEditor.this.processStopEvent();
         }
      });
      var1.getInputMap().put(KeyUtils.ESCAPE, "cancel");
      var1.getActionMap().put("cancel", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            DefaultTextFieldEditor.this.cancelCellEditing();
         }
      });
      var1.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            DefaultTextFieldEditor.this.processStopEvent();
         }
      });
   }

   private void processStopEvent() {
      if (!this.canceled && !this.processingStop) {
         this.processingStop = true;

         try {
            this.stopCellEditing();
         } finally {
            this.processingStop = false;
         }
      }
   }
}
