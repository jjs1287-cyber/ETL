package org.etlforge.integration.client.ui.interchange.entity.document.page.table;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.etlforge.integration.client.ui.interchange.entity.document.page.ValidationEditor;
import org.etlforge.integration.data.structure.text.validation.FieldValidation;
import org.etlforge.ui.event.DoubleClick;
import org.etlforge.ui.widget.table.KongaTable;

public final class ValidationTableService {
   private final ValidationEditor editor;

   public ValidationTableService(ValidationEditor var1) {
      this.editor = var1;
   }

   public void install(final KongaTable var1) {
      final ValidationTableService.RendererAndEditor var2 = new ValidationTableService.RendererAndEditor(this.editor);
      var1.setCellRendererFor(FieldTableColumns.VALIDATION, var2);
      var1.setCellEditorFor(FieldTableColumns.VALIDATION, var2);
      DoubleClick.on(var1).inColumn(FieldTableColumns.VALIDATION).toInvoke(var2);
      var1.addMouseListener(new MouseAdapter() {
         private boolean isButtonColumnEditor;

         @Override
         public void mousePressed(MouseEvent var1x) {
            if (var1.isEditing() && var1.getCellEditor() == var2) {
               this.isButtonColumnEditor = true;
            }
         }

         @Override
         public void mouseReleased(MouseEvent var1x) {
            if (this.isButtonColumnEditor && var1.isEditing()) {
               var1.getCellEditor().stopCellEditing();
            }

            this.isButtonColumnEditor = false;
         }
      });
   }

   private static class CellUi {
      private final JButton button = new JButton("...");
      private final JLabel label = new JLabel();
      private final JPanel panel;

      public CellUi(final boolean var1) {
         this.panel = new JPanel() {
            @Override
            public void addNotify() {
               super.addNotify();
               if (var1) {
                  CellUi.this.button.requestFocusInWindow();
               }
            }

            @Override
            protected boolean processKeyBinding(KeyStroke var1x, KeyEvent var2, int var3, boolean var4) {
               if (!var1) {
                  return super.processKeyBinding(var1x, var2, var3, var4);
               }

               InputMap var5 = CellUi.this.button.getInputMap(var3);
               ActionMap var6 = CellUi.this.button.getActionMap();
               Object var7 = var5.get(var1x);
               Action var8 = var7 == null ? null : var6.get(var7);
               return var8 != null ? SwingUtilities.notifyAction(var8, var1x, var2, CellUi.this.button, var2.getModifiers()) : false;
            }
         };
         this.panel.add(this.label);
         this.panel.add(this.button);
         this.panel.setLayout(new ValidationTableService.RendererLayout(this.label, this.button));
      }

      public void update(JTable var1, boolean var2, String var3, String var4) {
         this.label.setText(var3);
         this.label.setForeground(var2 ? var1.getSelectionForeground() : var1.getForeground());
         this.panel.setToolTipText(var4);
         this.panel.setBackground(var2 ? var1.getSelectionBackground() : var1.getBackground());
      }

      public void addActionListener(ActionListener var1) {
         this.button.addActionListener(var1);
      }

      public JPanel get() {
         return this.panel;
      }
   }

   private static class RendererAndEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer, ActionListener {
      private final ValidationEditor validationEditor;
      private final DescriptionBuilder descriptionBuilder;
      private final DescriptionBuilder tooltipBuilder;
      private final ValidationTableService.CellUi renderer = new ValidationTableService.CellUi(false);
      private final ValidationTableService.CellUi editor = new ValidationTableService.CellUi(true);
      private Object editorValue;
      private int row;

      public RendererAndEditor(ValidationEditor var1) {
         this.validationEditor = var1;
         this.descriptionBuilder = this.createDescriptionBuilder("; ");
         this.tooltipBuilder = this.createDescriptionBuilder("\n");
         this.editor.addActionListener(this);
      }

      private DescriptionBuilder createDescriptionBuilder(String var1) {
         DescriptionBuilder var2 = new DescriptionBuilder(var1);
         var2.setEntityLookup(this.validationEditor.getEntityLookup());
         return var2;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               RendererAndEditor.this.editorValue = RendererAndEditor.this.validationEditor.edit(RendererAndEditor.this.row);
               if (RendererAndEditor.this.editorValue == null) {
                  RendererAndEditor.this.fireEditingCanceled();
               } else {
                  RendererAndEditor.this.fireEditingStopped();
               }
            }
         });
      }

      @Override
      public Object getCellEditorValue() {
         return this.editorValue;
      }

      @Override
      public Component getTableCellEditorComponent(JTable var1, Object var2, boolean var3, int var4, int var5) {
         this.editorValue = var2;
         this.row = var4;
         return this.doLayout(this.editor, var1, var2, var3);
      }

      @Override
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         return this.doLayout(this.renderer, var1, var2, var3);
      }

      private JPanel doLayout(ValidationTableService.CellUi var1, JTable var2, Object var3, boolean var4) {
         String var5 = this.getLabelText(var3);
         String var6 = this.getToolTip(var3);
         var1.update(var2, var4, var5, var6);
         return var1.get();
      }

      private String getLabelText(Object var1) {
         return var1 instanceof FieldValidation ? this.describe((FieldValidation)var1) : "None";
      }

      private String describe(FieldValidation var1) {
         return var1.hasEffect() ? this.descriptionBuilder.describe(var1) : "Enabled, but has no effect.";
      }

      private String getToolTip(Object var1) {
         if (var1 instanceof FieldValidation) {
            FieldValidation var2 = (FieldValidation)var1;
            if (var2.hasEffect()) {
               String var3 = this.tooltipBuilder.describe(var2).replace("\n", "<br>");
               return "<html>" + var3 + "</html>";
            }
         }

         return null;
      }

      @Override
      public boolean isCellEditable(EventObject var1) {
         return var1 instanceof MouseEvent ? ((MouseEvent)var1).getClickCount() >= 1 : true;
      }
   }

   private static class RendererLayout implements LayoutManager {
      private final JLabel label;
      private final JButton button;

      public RendererLayout(JLabel var1, JButton var2) {
         this.label = var1;
         this.button = var2;
      }

      @Override
      public Dimension preferredLayoutSize(Container var1) {
         synchronized (var1.getTreeLock()) {
            Dimension var3 = this.label.getPreferredSize();
            Dimension var4 = this.button.getPreferredSize();
            return new Dimension(var3.width + 5 + var4.width, Math.max(var3.height, var4.height));
         }
      }

      @Override
      public Dimension minimumLayoutSize(Container var1) {
         synchronized (var1.getTreeLock()) {
            return this.button.getPreferredSize();
         }
      }

      @Override
      public void layoutContainer(Container var1) {
         synchronized (var1.getTreeLock()) {
            Insets var3 = var1.getInsets();
            int var4 = var1.getWidth() - var3.left - var3.right;
            int var5 = var1.getHeight() - var3.top - var3.bottom;
            Dimension var6 = this.button.getPreferredSize();
            int var7 = var4 - var6.width;
            int var8 = Math.min(var5, var6.height);
            int var9 = (var5 - var8) / 2;
            this.button.setBounds(var7, var9, var6.width, var8);
            Dimension var10 = this.label.getPreferredSize();
            int var11 = var3.left;
            int var12 = Math.min(var10.width, var7 - var11 - 5);
            int var13 = Math.min(var5, var10.height);
            int var14 = (var5 - var13) / 2;
            this.label.setBounds(var11, var14, var12, var13);
         }
      }

      @Override
      public void addLayoutComponent(String var1, Component var2) {
      }

      @Override
      public void removeLayoutComponent(Component var1) {
      }
   }
}
