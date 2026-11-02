package org.etlforge.application.ui.groovy;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Segment;
import javax.swing.text.DefaultEditorKit.DefaultKeyTypedAction;
import javax.swing.text.DefaultEditorKit.InsertBreakAction;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.AutoCompleterFactory;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.text.DocumentUtils;
import org.etlforge.ui.text.contentassist.AutoCompleter;
import org.etlforge.ui.text.contentassist.CompletionContext;
import org.etlforge.ui.widget.text.KongaTextArea;

final class ScriptArea implements UiProvider {
   private final KongaTextArea textArea = this.createTextArea();

   public ScriptArea() {
      this.installAutoCompletion();
      this.installAutoIndent();
   }

   private KongaTextArea createTextArea() {
      KongaTextArea var1 = new KongaTextArea(20, 100);
      var1.setLineWrap(false);
      TextStyles.DefaultMonoSpaced.makeOver(var1);
      var1.setLabel("&Script:");
      var1.setTabSize(2);
      return var1;
   }

   private void installAutoCompletion() {
      CompletionContext var1 = PackageNameCompletionContextBuilder.buildForEditor();
      AutoCompleterFactory var2 = AutoCompleterFactory.codeEditorFactory();
      var2.setGrabFocus(false);
      AutoCompleter var3 = var2.createAutoCompleter(var1);
      var3.install(this.textArea);
   }

   private void installAutoIndent() {
      IndentLevelCalculator var1 = new IndentLevelCalculator();
      this.textArea.getActionMap().put("insert-break", new ScriptArea.NewLineAction(var1));
      this.textArea.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke('}'), new ScriptArea.ClosingBlockAction(var1));
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.textArea.asUiProvider().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.textArea.requestFocusInWindow();
   }

   public String getScript() {
      return this.textArea.getText().trim();
   }

   public void insertImport(String var1) {
      String var2 = "import " + var1;
      if (!this.containsImport(var2)) {
         this.textArea.insert(var2 + "\n", 0);
         this.requestFocus();
      }
   }

   private boolean containsImport(String var1) {
      for (String var5 : this.getScript().split("\\n")) {
         if (var5.trim().equals(var1)) {
            return true;
         }

         if (!var5.startsWith("import ")) {
            break;
         }
      }

      return false;
   }

   public void setExecuteAction(Action var1) {
      ActionKeyBinding.install(var1, this.textArea);
   }

   public void setEnabled(boolean var1) {
      this.textArea.setEnabled(var1);
   }

   private final class ClosingBlockAction extends DefaultKeyTypedAction {
      private final IndentLevelCalculator calculator;

      public ClosingBlockAction(IndentLevelCalculator var2) {
         this.calculator = var2;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         super.actionPerformed(var1);
         if ("}".equals(var1.getActionCommand())) {
            this.indent();
         }
      }

      private void indent() {
         try {
            int var1 = ScriptArea.this.textArea.getCaretPosition();
            int var2 = DocumentUtils.getRowNumber(ScriptArea.this.textArea, var1);
            int var3 = DocumentUtils.getRowStart(ScriptArea.this.textArea, var2);
            if (this.isPreceededByWhitespaceOnly(var1, var3)) {
               int var4 = this.calculator.getIndentAtPosition(ScriptArea.this.textArea.getDocument(), var1);
               if (var4 >= 0) {
                  this.replaceStartOfLineWithLevelsOfIndent(var1, var3, var4);
               }
            }
         } catch (BadLocationException var5) {
         }
      }

      private boolean isPreceededByWhitespaceOnly(int var1, int var2) throws BadLocationException {
         Segment var3 = new Segment();
         ((AbstractDocument)ScriptArea.this.textArea.getDocument()).getText(var2, var1 - var2 - 1, var3);

         for (int var4 = var3.offset; var4 < var3.getEndIndex(); var4++) {
            char var5 = var3.array[var4];
            if (var5 != ' ' && var5 != '\t') {
               return false;
            }
         }

         return true;
      }

      private void replaceStartOfLineWithLevelsOfIndent(int var1, int var2, int var3) {
         StringBuilder var4 = new StringBuilder();

         for (int var5 = 0; var5 < var3; var5++) {
            var4.append("\t");
         }

         ScriptArea.this.textArea.replaceRange(var4.toString(), var2, var1 - 1);
      }
   }

   private final class NewLineAction extends InsertBreakAction {
      private final IndentLevelCalculator calculator;

      public NewLineAction(IndentLevelCalculator var2) {
         this.calculator = var2;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         super.actionPerformed(var1);
         this.indent();
      }

      private void indent() {
         int var1 = ScriptArea.this.textArea.getCaretPosition();
         int var2 = this.calculator.getIndentAtPosition(ScriptArea.this.textArea.getDocument(), var1);
         if (var2 >= 0) {
            StringBuilder var3 = new StringBuilder();

            for (int var4 = 0; var4 < var2; var4++) {
               var3.append("\t");
            }

            ScriptArea.this.textArea.insert(var3.toString(), var1);
         }
      }
   }
}
