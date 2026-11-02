package com.etlforge.integration.client.ui.script.fcn.db.sql;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.List;
import javax.swing.Action;
import javax.swing.JScrollPane;
import javax.swing.TransferHandler;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.database.ui.sqlbuilder.NameGenerator;
import org.etlforge.integration.client.database.ui.sqlbuilder.NameGenerators;
import org.etlforge.integration.client.database.ui.sqlbuilder.SqlInputArea;
import org.etlforge.integration.client.ui.script.builder.ExpressionAreaTextPane;
import org.etlforge.integration.client.ui.script.partition.ScriptSyntaxHighlighter;
import org.etlforge.integration.client.ui.script.prefs.TabSizePreference;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.dataelement.DataElementTemplate;
import org.etlforge.integration.dataelement.GlobalDE;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.action.ActionKeyBinding;

public final class SqlStringArea implements SqlInputArea {
   private final ExpressionAreaTextPane textPane = this.createTextPane();
   private NameGenerator nameGenerator = NameGenerators.SIMPLE;
   private Template template = Template.SELECT;

   private ExpressionAreaTextPane createTextPane() {
      ExpressionAreaTextPane var1 = new ExpressionAreaTextPane(true);
      var1.disableTabs();
      ScriptSyntaxHighlighter.installWithoutTransTags(var1);
      var1.setWrapping(true);
      var1.setTabSize(TabSizePreference.get());
      Font var2 = TextStyles.LargeMonoSpaced.getFont();
      var1.setFont(var2);
      ((AbstractDocument)var1.getDocument()).setDocumentFilter(new SqlStringArea.QuoteInserter());
      this.installDropSupport(var1);
      return var1;
   }

   private void installDropSupport(ExpressionAreaTextPane var1) {
      TransferHandler var2 = var1.getTransferHandler();
      SqlStringAreaTransferHandler var3 = new SqlStringAreaTransferHandler(this, var2);
      var1.setTransferHandler(var3);
   }

   public void setTemplate(Template var1) {
      Preconditions.checkNotNull(var1, "template");
      this.template = var1;
   }

   @Override
   public void setNameGenerator(NameGenerator var1) {
      this.nameGenerator = var1;
   }

   public void setSql(String var1) {
      this.textPane.setText(var1.trim());
   }

   public String getSql() {
      return this.textPane.getText().trim();
   }

   public boolean isEmpty() {
      return this.textPane.getDocument().getLength() == 0;
   }

   public void installActionShortCut(Action var1) {
      ActionKeyBinding.install(var1, this.textPane);
   }

   public Supplier<String> asStringSupplier() {
      return this.textPane.asTrimmingStringSupplier();
   }

   public void addChangeListener(DocumentListener var1) {
      this.textPane.getDocument().addDocumentListener(var1);
   }

   public void removeChangeListener(DocumentListener var1) {
      this.textPane.getDocument().removeDocumentListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(new JScrollPane(this.textPane));
   }

   @Override
   public void requestFocus() {
      this.textPane.requestFocusInWindow();
   }

   @Override
   public void insertTable(DatabaseObject var1) {
      this.checkCaretLocationBeforeInsertingStringLiteral();
      Inserter var2 = this.createInserter();
      InsertionData var3 = var2.getStringToInsertForTable(var1);
      this.replaceSelection(var3);
   }

   private void checkCaretLocationBeforeInsertingStringLiteral() {
      int var1 = this.getSelectionStart();
      int var2 = this.getSelectionEnd();
      if (var1 == var2) {
         this.moveCaretBackIntoStringLiteralIfPossible();
      }
   }

   private void moveCaretBackIntoStringLiteralIfPossible() {
      String var1 = this.textPane.getText();
      int var2 = this.textPane.getCaretPosition();
      if (!StringDetector.isInString(var1, var2)) {
         while (var2 > 0) {
            if (StringDetector.isInString(var1, --var2)) {
               break;
            }

            char var3 = var1.charAt(var2);
            if (!Character.isWhitespace(var3)) {
               return;
            }
         }

         this.textPane.setCaretPosition(var2);
      }
   }

   private Inserter createInserter() {
      Inserter var1 = new Inserter(this.nameGenerator, this.textPane.getText(), this.getSelectionStart(), this.getSelectionEnd());
      var1.setTemplate(this.template);
      return var1;
   }

   private int getSelectionStart() {
      return this.textPane.getSelectionStart();
   }

   private int getSelectionEnd() {
      return this.textPane.getSelectionEnd();
   }

   private void replaceSelection(final InsertionData var1) {
      this.textPane.requestFocusInWindow();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            String var1x = var1.text;
            int var2 = SqlStringArea.this.textPane.getSelectionStart();
            int var3 = var2 + var1.selectionStart;
            int var4 = var2 + var1.selectionEnd;
            if (var2 == 0 && !var1x.startsWith("SqlEscape(")) {
               if (!var1x.startsWith("\"")) {
                  var1x = "\"" + var1x;
                  var3++;
                  var4++;
               }

               if (!SqlStringArea.endsWithStringQuote(var1x)) {
                  var1x = var1x + "\"";
               }
            }

            SqlStringArea.this.textPane.replaceSelection(var1x);
            SqlStringArea.this.textPane.setCaretPosition(var3);
            SqlStringArea.this.textPane.moveCaretPosition(var4);
         }
      });
   }

   private static boolean endsWithStringQuote(String var0) {
      return var0.endsWith("\"") && !var0.endsWith("\\\"");
   }

   @Override
   public void insertColumns(List<DatabaseColumn> var1) {
      this.checkCaretLocationBeforeInsertingStringLiteral();
      Inserter var2 = this.createInserter();
      InsertionData var3 = var2.getStringToInsertForColumns(var1);
      this.replaceSelection(var3);
   }

   public void insertGlobalDataElement(DataElementTemplate var1) {
      this.insertGlobalDataElement(var1.getName());
   }

   public void insertGlobalDataElement(String var1) {
      this.insertDataElementImpl(GlobalDE.forScript(var1));
   }

   private void insertDataElementImpl(String var1) {
      this.checkCaretLocationBeforeInsertingDataElement();
      Inserter var2 = this.createInserter();
      InsertionData var3 = var2.getStringToInsertForDataElement(var1);
      this.replaceSelection(var3);
   }

   private void checkCaretLocationBeforeInsertingDataElement() {
      int var1 = this.getSelectionStart();
      int var2 = this.getSelectionEnd();
      if (var1 == var2) {
         this.moveCaretOutOfStringLiteral();
      }
   }

   private void moveCaretOutOfStringLiteral() {
      String var1 = this.textPane.getText();
      int var2 = this.textPane.getCaretPosition();
      if (StringDetector.isInString(var1, var2) && !StringDetector.isInString(var1, var2 + 1)) {
         this.textPane.setCaretPosition(var2 + 1);
      }
   }

   public void insertSourceDataElement(String var1) {
      this.insertDataElementImpl(var1);
   }

   public void insertReservedWord(String var1) {
      this.checkCaretLocationBeforeInsertingStringLiteral();
      Inserter var2 = this.createInserter();
      InsertionData var3 = var2.getStringToInsertForReservedWord(var1);
      this.replaceSelection(var3);
   }

   private class QuoteInserter extends DocumentFilter {
      private QuoteInserter() {
      }

      @Override
      public void remove(FilterBypass var1, int var2, int var3) throws BadLocationException {
         this.setFilter(null);
         SqlStringArea.this.textPane.getDocument().remove(var2, var3);
         this.setFilter(this);
      }

      private void setFilter(DocumentFilter var1) {
         ((AbstractDocument)SqlStringArea.this.textPane.getDocument()).setDocumentFilter(var1);
      }

      @Override
      public void insertString(FilterBypass var1, int var2, String var3, AttributeSet var4) throws BadLocationException {
         this.setFilter(null);
         SqlStringArea.this.textPane.getDocument().insertString(var2, var3, var4);
         this.setFilter(this);
      }

      @Override
      public void replace(FilterBypass var1, int var2, int var3, String var4, AttributeSet var5) throws BadLocationException {
         this.setFilter(null);
         AbstractDocument var6 = (AbstractDocument)SqlStringArea.this.textPane.getDocument();
         if (var2 == 0 && var3 >= var6.getLength()) {
            boolean var7 = false;
            if (var4.equals("\"")) {
               var4 = "\"\"";
               var7 = true;
            } else if (this.needsSurroundingQuotes(var4)) {
               var4 = "\"" + var4 + "\"";
               var7 = true;
            }

            if (var7) {
               this.moveCaret(var4);
            }
         }

         var6.replace(var2, var3, var4, var5);
         this.setFilter(this);
      }

      private boolean needsSurroundingQuotes(String var1) {
         return var1.startsWith("\"\"") || var1.startsWith("SqlEscape(") ? false : !var1.startsWith("\"") || !SqlStringArea.endsWithStringQuote(var1);
      }

      private void moveCaret(String var1) {
         final int var2 = var1.length() - 1;
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               SqlStringArea.this.textPane.setCaretPosition(var2);
            }
         });
      }
   }
}
