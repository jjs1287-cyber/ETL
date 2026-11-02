package org.etlforge.integration.client.ui.interchange.entity.document.page.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableCellRenderer;
import org.etlforge.application.ui.look.PanelStyle;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.system.SystemUtils;
import org.etlforge.ui.event.MouseActionListener;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TableStringEditor;

public class DocDefTableUi extends KongaTable {
   private static final boolean USE_ROW_SELECTOR = false;
   private final DocDefTable table;
   private RowMouseSelector rowSelector;

   public DocDefTableUi(DocDefTable var1) {
      super(var1.getModel());
      this.table = var1;
      this.customize();
   }

   protected void customize() {
      this.setupSelectionModel();
      this.customizeHeaders();
      this.setupEditors();
      this.customizeColumnRenderers();
      this.setupKeyBindings();
      this.setupDoubleClickListener();
      this.setTerminateEditOnFocusLost(true);
      this.setFont(TextStyles.InfoText.getFont());
      this.getColumnModel().getColumn(0).setMaxWidth(40);
      this.table.registerKeyBindings(this);
      TableStyles.narrow().makeOver(this);
   }

   private void setupKeyBindings() {
      this.getInputMap(1).put(KeyUtils.ENTER, "edit_fields");
      this.getActionMap().put("edit_fields", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            int[] var2 = DocDefTableUi.this.getSelectedRows();
            if (var2.length == 1) {
               DocDefTableUi.this.table.onDoubleClick(var2[0]);
            }
         }
      });
   }

   private void setupSelectionModel() {
      this.setSelectionMode(2);
   }


   private void customizeHeaders() {
      this.getTableHeader().setReorderingAllowed(false);
      this.getTableHeader().setResizingAllowed(true);
      if (!SystemUtils.IS_OS_WINDOWS_VISTA) {
         this.customizeHeaderRenderers();
      }
   }

   private void customizeHeaderRenderers() {
      DocDefTableUi.HeaderRenderer var1 = new DocDefTableUi.HeaderRenderer();

      for (int var2 = 0; var2 < this.getColumnCount(); var2++) {
         this.getColumnModel().getColumn(var2).setHeaderRenderer(var1);
      }
   }

   private void setupEditors() {
      this.table.setEditors(this);
      TableStringEditor var1 = new TableStringEditor();
      this.setDefaultEditor(String.class, var1);
      this.setClickCountToStartEdit(1);
   }

   private void customizeColumnRenderers() {
      this.setCellRendererAt(0, new TableRenderers.IndexRenderer());
   }

   private void setupDoubleClickListener() {
      this.addMouseListener(new MouseActionListener() {
         @Override
         protected void onDoubleClick(MouseEvent var1) {
            DocDefTableUi.this.table.onDoubleClick(DocDefTableUi.this.rowAtPoint(var1.getPoint()));
         }
      });
   }

   @Override
   public boolean isCellSelected(int var1, int var2) {
      return super.isCellSelected(var1, var2);
   }

   @Override
   protected void onPopupTriggered(int var1, int var2) {
      JPopupMenu var3 = this.table.getPopupMenu();
      if (var3 != null) {
         this.showPopupMenu(var3);
      }
   }

   @Override
   protected void mapKeyStrokes() {
      InputMap var1 = this.getInputMap();
      ActionMap var2 = this.getActionMap();
      KeyStroke var3 = KeyStroke.getKeyStroke(127, 0);
      var1.put(var3, "DELETE_SELECTED_ROWS");
      var2.put("DELETE_SELECTED_ROWS", this.getDeleteAction());
   }

   private Action getDeleteAction() {
      return new KeyStrokeDeleteAction(this, new KeyStrokeDeleteAction.TableProvider() {
         @Override
         public DocDefTable getTable() {
            return DocDefTableUi.this.table;
         }
      });
   }

   @Override
   public int getRowHeight() {
      return (int)(super.getRowHeight() * 1.25);
   }

   @Override
   public Dimension getPreferredScrollableViewportSize() {
      return new Dimension(super.getPreferredScrollableViewportSize().width, (this.getRowHeight() + this.getRowMargin()) * this.table.getVisibleRows());
   }

   @Override
   protected final boolean processKeyBinding(KeyStroke var1, KeyEvent var2, int var3, boolean var4) {
      return !var1.equals(KeyUtils.UNDO) && !var1.equals(KeyUtils.REDO) ? super.processKeyBinding(var1, var2, var3, var4) : false;
   }

   private static class HeaderRenderer extends DefaultTableCellRenderer {
      private final PanelStyle style = new PanelStyle(
         new Color(51, 102, 153), new BorderBuilder().bevel(1, Color.BLACK, new Color(150, 150, 150, 150)).empty(4)
      );

      private HeaderRenderer() {
      }

      @Override
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         JLabel var7 = (JLabel)super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         this.style.makeOver(var7);
         TextStyles.EntityPageTableHeaderText.makeOver(var7);
         return var7;
      }
   }
}
