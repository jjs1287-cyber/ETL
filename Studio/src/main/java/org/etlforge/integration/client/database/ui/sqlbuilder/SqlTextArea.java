package org.etlforge.integration.client.database.ui.sqlbuilder;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.awt.datatransfer.DataFlavor;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.event.DocumentListener;
import org.etlforge.integration.client.database.ui.dnd.TransferableDatabaseColumns;
import org.etlforge.integration.client.database.ui.dnd.TransferableDatabaseObjects;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.dataelement.DataElementTemplate;
import org.etlforge.integration.dataelement.GlobalDE;
import org.etlforge.integration.dataelement.ui.DeTemplateTransferSupport;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.dnd.DelegatingTransferHandler;
import org.etlforge.ui.widget.text.KongaTextArea;

public final class SqlTextArea implements SqlInputArea {
   private final KongaTextArea textArea = new KongaTextArea(5, 40);
   private NameGenerator nameGenerator = NameGenerators.SIMPLE;

   public SqlTextArea() {
      this.textArea.setTabSize(4);
      this.textArea.setTransferHandler(new SqlTextArea.TransferHandlerImpl(this, this.textArea.getTransferHandler()));
      DeTemplateTransferSupport.installDropHandler(this.textArea, "$%s");
   }

   @Override
   public void setNameGenerator(NameGenerator var1) {
      this.nameGenerator = var1;
   }

   public void setSql(String var1) {
      this.textArea.setText(var1 == null ? "" : var1.trim());
   }

   public String getSql() {
      return this.textArea.getText().trim();
   }

   public void installActionShortCut(Action var1) {
      ActionKeyBinding.install(var1, this.textArea);
   }

   public Supplier<String> asStringSupplier() {
      return this.textArea.asTrimmingStringSupplier();
   }

   public void addChangeListener(DocumentListener var1) {
      this.textArea.getDocument().addDocumentListener(var1);
   }

   public void removeChangeListener(DocumentListener var1) {
      this.textArea.getDocument().removeDocumentListener(var1);
   }

   public void replaceSelection(final String var1) {
      this.textArea.requestFocusInWindow();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            SqlTextArea.this.textArea.replaceSelection(var1);
         }
      });
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.textArea.asUiProvider().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.textArea.requestFocusInWindow();
   }

   @Override
   public void insertTable(DatabaseObject var1) {
      String var2 = this.nameGenerator.getTableName(var1);
      String var3 = this.isEmpty() ? "SELECT * FROM " + var2 : var2;
      this.replaceSelection(var3);
   }

   private boolean isEmpty() {
      return this.textArea.getDocument().getLength() == 0;
   }

   @Override
   public void insertColumns(List<DatabaseColumn> var1) {
      DatabaseObject var2 = null;
      StringBuilder var3 = new StringBuilder();

      for (DatabaseColumn var5 : var1) {
         if (var2 == null) {
            var2 = var5.getDatabaseObject();
         }

         if (var3.length() > 0) {
            var3.append(", ");
         }

         var3.append(this.nameGenerator.getColumnName(var5));
      }

      assert var2 != null;
      String var6 = this.isEmpty() ? "SELECT " + var3 + " FROM " + this.nameGenerator.getTableName(var2) : var3.toString();
      this.replaceSelection(var6);
   }

   public void insertDataElement(DataElementTemplate var1) {
      String var2 = GlobalDE.forScript(var1.getName());
      this.replaceSelection(var2);
   }

   private static class TransferHandlerImpl extends DelegatingTransferHandler {
      private final SqlTextArea textArea;

      public TransferHandlerImpl(SqlTextArea var1, TransferHandler var2) {
         super(var2);
         this.textArea = var1;
      }

      @Override
      public boolean canImport(TransferSupport var1) {
         return var1.isDataFlavorSupported(TransferableDatabaseObjects.FLAVOR)
            || var1.isDataFlavorSupported(TransferableDatabaseColumns.FLAVOR)
            || this.delegate.canImport(var1);
      }

      @Override
      public boolean importData(TransferSupport var1) {
         if (var1.isDataFlavorSupported(TransferableDatabaseObjects.FLAVOR)) {
            List var2 = this.getDraggedItems(var1, TransferableDatabaseObjects.FLAVOR, DatabaseObject.class);
            if (var2.size() == 1) {
               this.textArea.insertTable((DatabaseObject)var2.get(0));
               return true;
            }
         } else if (var1.isDataFlavorSupported(TransferableDatabaseColumns.FLAVOR)) {
            List var3 = this.getDraggedItems(var1, TransferableDatabaseColumns.FLAVOR, DatabaseColumn.class);
            if (!var3.isEmpty()) {
               this.textArea.insertColumns(var3);
               return true;
            }

            return false;
         }

         return this.delegate.importData(var1);
      }

      private <T> List<T> getDraggedItems(TransferSupport var1, DataFlavor var2, Class<T> var3) {
         try {
            Object var4 = var1.getTransferable().getTransferData(var2);
            if (var4 instanceof List) {
               List var5 = (List)var4;
               ArrayList var6 = Lists.newArrayList();

               for (Object var8 : var5) {
                  if (var3.isInstance(var8)) {
                     var6.add(var3.cast(var8));
                  }
               }

               return var6;
            }
         } catch (Exception var9) {
            var9.printStackTrace();
         }

         return null;
      }
   }
}
