package org.etlforge.integration.client.ui.structure.editor.ldap.source;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.EnumSet;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.TransferHandler;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.widget.AutoCompleterFactory;
import org.etlforge.integration.client.ldap.ui.TransferableAttribute;
import org.etlforge.integration.ldap.structure.LdapStructureAttribute;
import org.etlforge.integration.ldap.structure.LdapStructureEntryNode;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.text.BracketMatcher;
import org.etlforge.ui.text.BracketType;
import org.etlforge.ui.text.contentassist.AutoCompleter;
import org.etlforge.ui.text.highlight.DefaultSyntaxHighlightDocument;
import org.etlforge.ui.text.partition.TextPartitioner;
import org.etlforge.ui.widget.DocumentContentChangeListener;
import org.etlforge.ui.widget.text.KongaTextPane;

final class FilterExpressionArea implements UiProvider {
   private final String defaultFilter;
   private final KongaTextPane textArea = new KongaTextPane("").disableTabs();
   private final FilterExpressionArea.StatusPanel statusPanel;

   public FilterExpressionArea(LdapStructureEntryNode var1) {
      this.installSyntaxHighlighting(var1);
      this.defaultFilter = SearchParamUtils.getDefaultFilter(var1);
      this.textArea.setText(this.defaultFilter);
      this.textArea.setPreferredSize(new Dimension(500, 300));
      this.textArea.setTransferHandler(new FilterExpressionArea.DropHandler());
      this.statusPanel = new FilterExpressionArea.StatusPanel();
      TextStyles.DefaultMonoSpaced.makeOver(this.textArea);
      this.installAutoCompletion(var1, this.textArea);
      this.installBracketMatching(this.textArea);
      this.textArea.getDocument().addDocumentListener(new FilterExpressionArea.ParenthesesChecker());
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 5);
      var2.north(PackageResources.SearchParamsDefiner.FILTER).center(this.textArea);
      this.statusPanel.layout(var2.container(), "South");
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.textArea.requestFocusInWindow();
   }

   public String getExpression() {
      return this.textArea.getText().trim();
   }

   public void setExpression(String var1) {
      this.textArea.setText(var1);
   }

   public void insertAttribute(LdapStructureAttribute var1) {
      this.insertAttributeName(var1.getName());
   }

   private void insertAttributeName(String var1) {
      try {
         this.textArea.getDocument().insertString(this.textArea.getCaretPosition(), var1, null);
      } catch (BadLocationException var3) {
         var3.printStackTrace();
      }

      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            FilterExpressionArea.this.requestFocus();
         }
      });
   }

   private void installAutoCompletion(LdapStructureEntryNode var1, JTextComponent var2) {
      AutoCompleterFactory var3 = AutoCompleterFactory.codeEditorFactory();
      var3.setGrabFocus(false);
      FilterCompletionContext var4 = new FilterCompletionContext(var1);
      AutoCompleter var5 = var3.createAutoCompleter(var4);
      var5.install(var2);
   }

   private void installBracketMatching(JTextComponent var1) {
      EnumSet var2 = EnumSet.of(BracketType.PARENTHESES);
      BracketMatcher var3 = new BracketMatcher(var1, var2);
      var3.setHighlightColors(Colors.CHARACTER_PAIR_MATCHING_HIGHLIGHT, Colors.CHARACTER_PAIR_MISMATCH_HIGHLIGHT);
   }

   private void installSyntaxHighlighting(LdapStructureEntryNode var1) {
      FilterExpressionTokens var2 = new FilterExpressionTokens(var1);
      FilterTokenScanner var3 = new FilterTokenScanner(var2);
      FilterTokenStyles var4 = new FilterTokenStyles(var2);
      DefaultSyntaxHighlightDocument var5 = new DefaultSyntaxHighlightDocument(var4);
      this.textArea.setDocument(var5);
      TextPartitioner var6 = new TextPartitioner(var3);
      var5.setPartitioner(var6);
   }

   private class DropHandler extends TransferHandler {
      private DropHandler() {
      }

      @Override
      public boolean canImport(JComponent var1, DataFlavor[] var2) {
         if (var2.length != 1) {
            return false;
         }

         DataFlavor var3 = var2[0];
         return TransferableAttribute.FLAVOR.equals(var3) || DataFlavor.stringFlavor.equals(var3);
      }

      @Override
      public boolean importData(JComponent var1, Transferable var2) {
         try {
            if (var2.isDataFlavorSupported(DataFlavor.stringFlavor)) {
               String var5 = (String)var2.getTransferData(DataFlavor.stringFlavor);
               FilterExpressionArea.this.insertAttributeName(var5);
               return true;
            } else if (var2.isDataFlavorSupported(TransferableAttribute.FLAVOR)) {
               LdapStructureAttribute var3 = (LdapStructureAttribute)var2.getTransferData(TransferableAttribute.FLAVOR);
               FilterExpressionArea.this.insertAttribute(var3);
               return true;
            } else {
               return false;
            }
         } catch (Exception var4) {
            var4.printStackTrace();
            return false;
         }
      }
   }

   private class ParenthesesChecker extends DocumentContentChangeListener implements Runnable {
      private DelayedJob timer;
      private long statusPanelToken = -1L;

      private ParenthesesChecker() {
      }

      @Override
      public void run() {
         this.timer = null;
         this.checkParantheses();
      }

      @Override
      public void contentChanged(DocumentEvent var1) {
         if (this.timer != null) {
            this.timer.cancel();
         }

         this.timer = DelayedJob.createAndSchedule(500, this);
      }

      private void checkParantheses() {
         String var1 = FilterExpressionArea.this.getExpression();
         int var2 = 0;

         for (char var6 : var1.toCharArray()) {
            if (var6 == '(') {
               var2++;
            } else if (var6 == ')') {
               var2--;
            }
         }

         if (var2 == 0) {
            FilterExpressionArea.this.statusPanel.clear(this.statusPanelToken);
         } else {
            this.statusPanelToken = FilterExpressionArea.this.statusPanel.error("The expression contains unbalanced parentheses");
         }
      }
   }

   private static class StatusPanel {
      private final JLabel label = new JLabel("");
      private long modifications;

      private StatusPanel() {
      }

      public long error(String var1) {
         assert EventQueue.isDispatchThread();
         this.label.setForeground(Colors.ERROR_TEXT_COLOR);
         return this.setStatus(CommonIcons.ERROR_16, var1, true);
      }

      public void clear(long var1) {
         if (var1 == this.modifications) {
            this.clear();
         }
      }

      public void layout(Container var1, Object var2) {
         var1.add(this.label, var2);
      }

      private long setStatus(Icon var1, String var2, boolean var3) {
         this.label.setIcon(var1);
         this.label.setText(var2);
         if (!var3) {
            new FilterExpressionArea.StatusPanel.StatusClearer(++this.modifications).schedule();
         }

         return this.modifications;
      }

      private void clear() {
         this.label.setIcon(null);
         this.label.setText("");
      }

      private class StatusClearer {
         private final long state;

         public StatusClearer(long var2) {
            this.state = var2;
         }

         public void schedule() {
            DelayedJob.createAndSchedule(5000, new Runnable() {
               @Override
               public void run() {
                  if (StatusPanel.this.modifications == StatusClearer.this.state) {
                     StatusPanel.this.clear();
                  }
               }
            });
         }
      }
   }
}
