package org.etlforge.ui.widget;

import com.google.common.base.Predicate;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.TableCellRenderer;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.style.TableStyle;

public class TableSelectorDialog<E> extends KongaDialog {
   private SelectorTable<E> table;
   private JLabel topLabel;
   private JLabel bottomLabel;

   public TableSelectorDialog(Collection<E> var1, Class<E> var2, String var3, Object var4, Object var5, boolean var6) {
      this(var1, var2, (Window)null, var3, var4, var5, var6);
   }

   public TableSelectorDialog(Collection<E> var1, Class<E> var2, Window var3, String var4, Object var5, Object var6, boolean var7) {
      super(var3, var4, var7 ? ModalityType.APPLICATION_MODAL : ModalityType.MODELESS);
      this.init(var1, var2, var5, var6);
   }

   public TableSelectorDialog(Collection<E> var1, Class<E> var2, JDialog var3, String var4, Object var5, Object var6, boolean var7) {
      super(var3, var4, null, var7);
      this.init(var1, var2, var5, var6);
   }

   public void setInitialSelectionState(boolean var1) {
      this.table.setInitialSelectionState(var1);
   }

   public void setSelection(Selection var1) {
      this.table.asSelectionSource().setSelection(var1);
   }

   public void setDisabledRows(Collection<E> var1) {
      this.table.setDisabledRows(var1);
   }

   public void setFilter(Predicate<? super E> var1, boolean var2) {
      this.table.setFilter(var1, var2);
   }

   public void setObjectCellRenderer(TableCellRenderer var1) {
      this.table.setObjectCellRenderer(var1);
   }

   public void setSelectionMode(SelectorTable.Mode var1) {
      this.table.setSelectionMode(var1);
   }

   public List<E> getSelectedObjects() {
      return this.wasOkPressed() ? this.table.getSelectedObjects() : new ArrayList<>();
   }

   public void setMessageFont(Font var1) {
      if (this.topLabel != null) {
         this.topLabel.setFont(var1);
      }

      if (this.bottomLabel != null) {
         this.bottomLabel.setFont(var1);
      }
   }

   public void setTableStyle(TableStyle var1) {
      this.table.setStyle(var1);
   }

   @Override
   protected void onDialogOpened() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            TableSelectorDialog.this.table.requestFocus();
         }
      });
   }

   private void init(Collection<E> var1, Class<E> var2, Object var3, Object var4) {
      BorderLayoutBuilder var5 = new BorderLayoutBuilder(0, 5);
      Component var6 = this.getTopMessageComponent(var3);
      var5.north(var6);
      var5.center(this.createTablePanel(var1, var2));
      JPanel var7 = this.getButtonPanel(OK_CANCEL, 10);
      var7.setBorder(Empty.border(10, 0, 10, 0));
      Component var8 = this.getBottomMessageComponent(var4);
      if (var8 == null) {
         var5.south(var7);
      } else {
         Grid var9 = new Grid(2, 1, 0, 10);
         var9.addAll(var8, var7);
         var5.south(var9);
      }

      var5.withBorder(Empty.border(10)).asContentPaneFor(this);
      this.pack();
   }

   private UiProvider createTablePanel(Collection<E> var1, Class<E> var2) {
      this.table = new SelectorTable<>(var1, var2);
      this.table.setIncludeBulkSelectionButtons(true);
      return this.table;
   }

   private Component getTopMessageComponent(Object var1) {
      if (var1 instanceof Component) {
         return (Component)var1;
      } else if (var1 != null) {
         this.topLabel = new JLabel(var1.toString());
         return this.topLabel;
      } else {
         return null;
      }
   }

   private Component getBottomMessageComponent(Object var1) {
      if (var1 instanceof Component) {
         return (Component)var1;
      } else if (var1 != null) {
         this.bottomLabel = new JLabel(var1.toString());
         return this.bottomLabel;
      } else {
         return null;
      }
   }
}
