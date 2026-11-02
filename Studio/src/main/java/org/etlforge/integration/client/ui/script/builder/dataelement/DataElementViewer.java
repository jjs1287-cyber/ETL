package org.etlforge.integration.client.ui.script.builder.dataelement;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.TableCellRenderer;
import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.debug.client.de.DataElement;
import org.etlforge.integration.debug.client.de.DataElementScope;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.table.KongaRowTable;
import org.etlforge.ui.widget.table.KongaTable;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TableStringEditor;
import org.etlforge.ui.widget.table.renderer.TableRendererPreparer;

public final class DataElementViewer implements UiProvider {
   private final ScriptDataElementsModel dataElements;
   private DataElementTableModel tableModel;
   private final KongaRowTable table;
   private final PropertyChangeListener modelListener;
   private boolean sourceDataElementValuesEditable;

   public DataElementViewer(ScriptDataElementsModel var1) {
      this.dataElements = var1;
      this.tableModel = new DataElementTableModel(var1.getDataElements());
      this.table = this.createTable();
      this.modelListener = new DataElementViewer.DebugModelListener();
      this.sourceDataElementValuesEditable = true;
      var1.addPropertyChangeListener("allDataElements", this.modelListener);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.table.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   public void dispose() {
      this.dataElements.removePropertyChangeListener("allDataElements", this.modelListener);
   }

   public void setSourceDataElementValuesEditable(boolean var1) {
      this.sourceDataElementValuesEditable = var1;
   }

   public static JComponent getDefaultCaption() {
      BoxBuilder var0 = BoxBuilder.horizontal();
      JLabel var1 = new JLabel("= global", DataElementIcons.GLOBAL, 10);
      JLabel var2 = new JLabel("= source", DataElementIcons.SOURCE, 10);
      var0.addAllWithSpace(10, "Data Elements", var1, var2);
      return var0.container();
   }

   private void updateTable() {
      this.tableModel = new DataElementTableModel(this.dataElements.getDataElements());
      this.applyNewModel();
   }

   private void applyNewModel() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            DataElementViewer.this.table.setModel(DataElementViewer.this.tableModel);
         }
      });
   }

   private KongaRowTable createTable() {
      KongaTable var1 = new KongaTable(this.tableModel) {
         @Override
         public String getToolTipText(MouseEvent var1) {
            Point var2 = var1.getPoint();
            TableColumnMetaData var3 = this.columnDescriptorAtPoint(var2);
            if (var3 == DataElementTableModel.NAME) {
               int var4 = this.rowAtPoint(var2);
               if (var4 >= 0) {
                  var4 = this.convertRowIndexToModel(var4);
                  return DataElementViewer.this.tableModel.getRowObjectAt(var4).getName();
               }
            }

            return super.getToolTipText(var1);
         }

         @Override
         protected boolean processKeyBinding(KeyStroke var1, KeyEvent var2, int var3, boolean var4) {
            return KeyUtils.F8.equals(var1) ? false : super.processKeyBinding(var1, var2, var3, var4);
         }

         @Override
         public boolean isCellEditable(int var1, int var2) {
            TableColumnMetaData var3 = DataElementViewer.this.tableModel.getColumnDescriptor(var2);
            if (var3 == DataElementTableModel.NAME) {
               return false;
            }

            DataElement var4 = DataElementViewer.this.tableModel.getRowObjectAt(var1);
            return var4.getScope() == DataElementScope.SOURCE ? DataElementViewer.this.sourceDataElementValuesEditable : true;
         }
      };
      var1.setAutoCreateRowSorter(true);
      TableStyles.wide().makeOver(var1);
      var1.setShowVerticalLines(true);
      this.installEditors(var1);
      var1.setCellRendererFor(DataElementTableModel.NAME, new NameTableRenderer());
      var1.setRendererPreparer(new DataElementViewer.RendererPreparerImpl(var1.getFont()));
      this.setNumberOfVisibleRows(var1, 10);
      return new KongaRowTable(var1);
   }

   private void setNumberOfVisibleRows(KongaTable var1, int var2) {
      Dimension var3 = var1.getPreferredScrollableViewportSize();
      var3.height = var1.getRowHeight() * var2;
      var1.setPreferredScrollableViewportSize(var3);
   }

   private void installEditors(KongaTable var1) {
      TableStringEditor var2 = new TableStringEditor();
      var2.setTextStyle(TextStyles.DefaultMonoSpaced);
      var2.setClickCountToStart(1);
      var1.setCellEditorAt(1, var2);
      var1.setTerminateEditOnFocusLost(true);
   }

   private class DebugModelListener implements PropertyChangeListener {
      private DebugModelListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         DataElementViewer.this.updateTable();
      }
   }

   private static class RendererPreparerImpl implements TableRendererPreparer {
      private final Color disabled = new Color(225, 225, 225);
      private final Font normalFont;
      private final Font valueFont;

      public RendererPreparerImpl(Font var1) {
         this.normalFont = var1;
         this.valueFont = TextStyles.DefaultMonoSpaced.getFont();
      }

      @Override
      public boolean doDefaultPreparation() {
         return true;
      }

      @Override
      public Component prepare(JTable var1, TableCellRenderer var2, Component var3, int var4, int var5) {
         boolean var6 = true;
         var3.setForeground(var1.getForeground());
         TableColumnMetaData var7 = ((KongaTable)var1).getColumnDescriptor(var5);
         if (var7 == DataElementTableModel.VALUE) {
            DataElementTableModel var8 = (DataElementTableModel)var1.getModel();
            var6 = !var8.isNull(var4);
            var3.setFont(this.valueFont);
         } else {
            var3.setFont(this.normalFont);
         }

         var3.setEnabled(var6);
         var3.setBackground(var6 ? var1.getBackground() : this.disabled);
         return var3;
      }
   }
}
