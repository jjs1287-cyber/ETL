package com.etlforge.integration.client.ui.sfdc.soql;

import java.awt.EventQueue;
import java.util.List;
import javax.swing.text.JTextComponent;
import org.etlforge.collections.Collections2;
import org.etlforge.ui.text.DocumentUtils;

public abstract class AbstractSoqlInputInserter {
   private final JTextComponent textArea;
   private final String fieldListMarker;

   public AbstractSoqlInputInserter(JTextComponent var1, String var2) {
      this.textArea = var1;
      this.fieldListMarker = var2;
   }

   public final void insertObjectName(final String var1) {
      this.textArea.requestFocusInWindow();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            String var1x = "SELECT " + AbstractSoqlInputInserter.this.fieldListMarker + " FROM " + var1;
            AbstractSoqlInputInserter.this.textArea.setText(var1x);
            int var2 = var1x.indexOf(AbstractSoqlInputInserter.this.fieldListMarker);
            AbstractSoqlInputInserter.this.textArea.setCaretPosition(var2);
            AbstractSoqlInputInserter.this.textArea.moveCaretPosition(var2 + AbstractSoqlInputInserter.this.fieldListMarker.length());
         }
      });
   }

   public final void insertFieldNames(final String var1, final List<String> var2) {
      this.textArea.requestFocusInWindow();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            if (this.isEmpty()) {
               AbstractSoqlInputInserter.this.insertSelectFieldFromObject(var1, var2);
            } else {
               AbstractSoqlInputInserter.this.insertField(var2);
            }
         }

         private boolean isEmpty() {
            return AbstractSoqlInputInserter.this.textArea.getDocument().getLength() == 0;
         }
      });
   }

   private void insertSelectFieldFromObject(String var1, List<String> var2) {
      String var3 = getFieldNameList(var2);
      String var4 = "SELECT " + var3 + " FROM " + var1;
      this.textArea.setText(var4);
      int var5 = 7 + var3.length();
      this.textArea.setCaretPosition(var5);
   }

   private static String getFieldNameList(List<String> var0) {
      return Collections2.join(var0, ", ");
   }

   private void insertField(List<String> var1) {
      String var2 = this.textArea.getText();
      boolean var3 = this.nothingSelected() && this.isFieldListMarkerPresent(var2);
      String var4 = getFieldNameList(var1);
      String var5 = "";
      int var6 = var3 ? this.indexOfFieldListMarker(var2) : this.textArea.getSelectionStart();
      int var7 = this.textArea.getSelectionEnd();
      if (this.isCommaRequired(var6 - 1)) {
         var5 = var5 + ", ";
      }

      var5 = var5 + var4;
      if (!var3 && this.isCommaRequired(var7)) {
         var5 = var5 + ", ";
      }

      if (var3) {
         this.replaceFieldListMarker(var5, var6);
      } else {
         this.textArea.replaceSelection(var5);
      }
   }

   private boolean nothingSelected() {
      return this.textArea.getSelectionStart() == this.textArea.getSelectionEnd();
   }

   private boolean isFieldListMarkerPresent(String var1) {
      return var1.contains(this.fieldListMarker);
   }

   private int indexOfFieldListMarker(String var1) {
      return var1.indexOf(this.fieldListMarker);
   }

   private boolean isCommaRequired(int var1) {
      if (var1 >= 0 && var1 < this.textArea.getDocument().getLength() - 1) {
         char var2 = this.getCharAt(var1);
         return var2 != ',' && !Character.isWhitespace(var2);
      } else {
         return false;
      }
   }

   private char getCharAt(int var1) {
      String var2 = DocumentUtils.getText(this.textArea.getDocument(), var1, 1);
      return var2.length() > 0 ? var2.charAt(0) : '\u0000';
   }

   private void replaceFieldListMarker(String var1, int var2) {
      this.textArea.setCaretPosition(var2);
      this.textArea.moveCaretPosition(var2 + this.fieldListMarker.length());
      this.textArea.replaceSelection(var1);
   }
}
