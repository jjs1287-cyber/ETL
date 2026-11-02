package org.etlforge.integration.client.ui.script.builder;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.text.JTextComponent;
import org.etlforge.integration.client.ui.script.builder.dataelement.ScriptDataElementsModel;
import org.etlforge.integration.client.ui.script.partition.ScriptPartitioner;
import org.etlforge.integration.client.ui.script.partition.Tokens;
import org.etlforge.integration.client.ui.script.prefs.AutoAssistDoubleQuotesPreference;
import org.etlforge.integration.client.ui.script.prefs.AutoAssistParenthesisPreference;
import org.etlforge.integration.client.ui.script.prefs.AutoAssistSingleQuotesPreference;
import org.etlforge.integration.client.ui.script.text.DataElementHovererDisplayer;
import org.etlforge.integration.client.ui.script.text.FunctionHelpHovererDisplayer;
import org.etlforge.integration.client.ui.script.text.MatchingCharactersSupport;
import org.etlforge.integration.client.ui.script.text.ScriptCompletionContext;
import org.etlforge.integration.client.ui.script.text.ScriptDocument;
import org.etlforge.integration.client.ui.script.text.ScriptMouseHoverer;
import org.etlforge.integration.client.ui.script.text.TokenSelector;
import org.etlforge.integration.data.script.ExpressionParser;
import org.etlforge.integration.data.script.Function;
import org.etlforge.integration.data.script.FunctionInserter;
import org.etlforge.integration.data.script.Transform;
import org.etlforge.integration.dataelement.GlobalDE;
import org.etlforge.integration.dataelement.ui.DeTemplateTransferSupport;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.text.DocumentUtils;
import org.etlforge.ui.text.contentassist.CompletionContext;
import org.etlforge.ui.text.contentassist.TokenListCompletionContext;
import org.etlforge.ui.text.partition.TextPartitioner;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.CustomPaintable;
import org.etlforge.ui.widget.text.KongaTextPane;
import org.etlforge.ui.widget.text.LineMarker;
import org.etlforge.ui.widget.text.LineNumberBar;
import org.etlforge.ui.widget.text.TextComponentUtils;
import org.etlforge.util.Interval;

public final class ExpressionArea implements UiProvider, CustomPaintable {
   static final String DISPLAY_ROW_NUMBER = "displayRowNumber";
   private final ScriptPartitioner partitioner;
   private final ExpressionAreaTextPane textPane;
   private final JScrollPane scrollPane;
   private LineNumberBar rowNumberBar;
   private final PropertyChangeSupport propChangeSupport;
   private final ScriptMouseHoverer mouseHoverer;
   private final ScriptCompletionContext completionContext;
   private final TokenListCompletionContext localDataElementCompletion;
   private int tabSize;
   private FunctionInserterLookup functionInserterLookup;

   public ExpressionArea(ExpressionAreaTextPane var1, ScriptPartitioner var2, ScriptCompletionContext var3) {
      this.textPane = var1;
      this.partitioner = var2;
      this.completionContext = var3;
      this.localDataElementCompletion = createLocalVariableCompletionContext();
      var3.addAdditionalContext(this.localDataElementCompletion);
      this.scrollPane = new JScrollPane(var1);
      var2.setDocument(this.getDocument());
      var1.setSelectWordAction(new SelectTokenAction(var1, var2));
      var1.addMouseListener(new ExpressionArea.MouseClickHandler(var1, var2));
      this.tabSize = 8;
      this.registerDebugActions();
      this.propChangeSupport = new PropertyChangeSupport(this);
      DeTemplateTransferSupport.installDropHandler(var1, "$%s");
      this.mouseHoverer = this.installMouseHoverer();
      this.functionInserterLookup = FunctionInserterLookup.DEFAULT;
      this.installTransferHandler();
      this.installMatchingCharacterSupport();
   }

   private static TokenListCompletionContext createLocalVariableCompletionContext() {
      TokenListCompletionContext var0 = new TokenListCompletionContext(new String[0]);
      var0.setCaseSensitive(false);
      var0.setUseWordBoundaries(true);
      var0.setIgnoredWordBoundaries("$", ".", "_");
      return var0;
   }

   private ScriptMouseHoverer installMouseHoverer() {
      ScriptMouseHoverer var1 = new ScriptMouseHoverer(this.textPane, this.partitioner);
      var1.setDisplayerForToken(Tokens.FUNCTION, new FunctionHelpHovererDisplayer());
      var1.install();
      return var1;
   }

   private void installMatchingCharacterSupport() {
      MatchingCharactersSupport var1 = new MatchingCharactersSupport(this.partitioner);
      if (AutoAssistParenthesisPreference.isEnabled()) {
         var1.installParenthesisAction(this.textPane);
      }

      if (AutoAssistDoubleQuotesPreference.isEnabled()) {
         var1.installDoubleQuoteAction(this.textPane);
      }

      if (AutoAssistSingleQuotesPreference.isEnabled()) {
         var1.installSingleQuoteAction(this.textPane);
      }
   }

   private void installTransferHandler() {
      TransferHandler var1 = this.textPane.getTransferHandler();
      ExpressionAreaTransferHandler var2 = new ExpressionAreaTransferHandler(var1);
      this.textPane.setTransferHandler(var2);
   }

   void installScriptScanner(ExpressionScanner var1) {
      var1.install(this.textPane);
      var1.addResultListener(new Receiver<ExpressionParser.Result>() {
         public void handle(ExpressionParser.Result var1) {
            if(ExpressionArea.this.getText().contains("<javascript>")&&!var1.getStatus().isInvalid())ExpressionArea.this.partitioner.refresh();
            ImmutableSet var2 = var1.getLocalDataElements();
            if (var2 != null) {
               ExpressionArea.this.localDataElementCompletion.setTokens(var2);
            }
         }
      });
   }

   public void setFunctionInserterLookup(FunctionInserterLookup var1) {
      this.functionInserterLookup = var1;
      this.completionContext.setFunctionInserterLookup(var1);
   }

   public FunctionInserterLookup getFunctionInserterLookup() {
      return this.functionInserterLookup;
   }

   public ScriptPartitioner getPartitioner() {
      return this.partitioner;
   }

   public ScriptDocument getDocument() {
      return this.textPane.getFormulaBuilderDocument();
   }

   public KongaTextPane getTextPane() {
      return this.textPane;
   }

   public boolean isEditable() {
      return this.textPane.isEditable();
   }

   public int getCaretPosition() {
      return this.textPane.getCaretPosition();
   }

   public void setCaretPosition(int var1) {
      this.textPane.setCaretPosition(var1);
   }

   public void setTabSize(int var1) {
      this.tabSize = var1;
      this.textPane.setTabSize(var1);
   }

   public int getTabSize() {
      return this.tabSize;
   }

   public int getCurrentRow() {
      return this.getRowNumber(this.getCaretPosition());
   }

   public int getRowNumber(int var1) {
      return DocumentUtils.getRowNumber(this.textPane, var1);
   }

   public int getCurrentColumn() {
      return this.getColumnNumber(this.getCaretPosition());
   }

   public int getColumnNumber(int var1) {
      return DocumentUtils.getColumnNumber(this.textPane, var1);
   }

   public void addToContextMenu(JMenuItem var1) {
      this.textPane.addToContextMenu(var1);
   }

   public void installShortCut(Object var1, KeyStroke var2, Action var3) {
      if (var2 == null) {
         var2 = (KeyStroke)var3.getValue("AcceleratorKey");
         Preconditions.checkArgument(var2 != null, "No keystroke specified");
      }

      this.textPane.getInputMap().put(var2, var1);
      this.textPane.getActionMap().put(var1, var3);
   }

   public boolean supportsInstanceFunctions() {
      return this.textPane.supportsInstanceFunctions();
   }

   public void setDisplayRowNumbers(boolean var1) {
      boolean var2 = this.rowNumberBar != null;
      if (var1 != var2) {
         if (this.rowNumberBar == null) {
            this.rowNumberBar = LineNumberBar.install(this.textPane, this.scrollPane);
         } else if (this.rowNumberBar != null) {
            this.rowNumberBar.uninstall();
            this.rowNumberBar = null;
         }

         this.propChangeSupport.firePropertyChange("displayRowNumber", var2, var1);
      }
   }

   public boolean isDisplayingRowNumbers() {
      return this.rowNumberBar != null;
   }

   public void addLineMarker(LineMarker var1) {
      if (this.rowNumberBar == null) {
         this.setDisplayRowNumbers(true);
      }

      this.rowNumberBar.addMarker(var1);
   }

   public void removeLineMarker(LineMarker var1) {
      if (this.rowNumberBar != null) {
         this.rowNumberBar.removeMarker(var1);
      }
   }

   public void ensureRowIsVisible(int var1) {
      this.textPane.scrollToRow(var1);
   }

   public void moveCaretToRow(int var1) {
      this.setCaretPosition(DocumentUtils.getRowStart(this.getDocument(), var1));
   }

   public TextInputField2 asInputField(String var1) {
      return new TextInputField2(var1, this.textPane) {
         @Override
         public boolean isInputValid(String var1) {
            return true;
         }
      };
   }

   void addAdditionalCompletionContext(CompletionContext var1) {
      this.completionContext.addAdditionalContext(var1);
   }

   public void insertFunction(Function var1) {
      if (this.isEditable()) {
         if (!org.etlforge.integration.data.script.JavaScriptFunctions.isJavaScript(var1) && org.etlforge.integration.data.script.JavaScriptFunctions.isInside(this.textPane.getText(),this.textPane.getCaretPosition())) {
            Function js=org.etlforge.integration.data.script.JavaScriptFunctions.find(var1.getName());
            if(js!=null)var1=js;
            else {
               for(int[] block:org.etlforge.integration.data.script.NativeJavaScript.inspect(this.textPane.getText()))if(block[0]==1&&this.textPane.getCaretPosition()>=block[2]&&this.textPane.getCaretPosition()<=block[3]){this.textPane.setCaretPosition(block[4]);break;}
               this.insertText("<trans>\n"+var1.getInsertValue()+"\n</trans>");return;
            }
         }
         if (org.etlforge.integration.data.script.JavaScriptFunctions.isJavaScript(var1)) {
            String source=this.textPane.getText();int caret=this.textPane.getCaretPosition();
            String value=var1.getInsertValue();
            if (!org.etlforge.integration.data.script.JavaScriptFunctions.isInside(source,caret)) {
               int trans=source.lastIndexOf("<trans>",Math.max(0,caret-1));
               int end=source.lastIndexOf("</trans>",Math.max(0,caret-1));
               if(trans>end){int close=source.indexOf("</trans>",caret);if(close>=0)this.textPane.setCaretPosition(close+8);}
               value="<javascript>\n"+value+";\n</javascript>";
            }
            this.insertText(value);return;
         }
         final FunctionInserter var2 = this.functionInserterLookup.getInserter(var1);
         this.requestFocus();
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               var2.insertFunction(ExpressionArea.this.textPane);
            }
         });
      }
   }

   public String getText() {
      return this.textPane.getText();
   }

   void replaceDisplayedExpression(String var1) {
      this.requestFocus();
      int var2 = this.getCaretPosition();
      this.setText(var1);
      var2 = Math.min(var2, this.getDocument().getLength());
      this.setCaretPosition(var2);
   }

   public void setText(String var1) {
      this.partitioner.setDocument(null);
      this.getDocument().setPartitioner(null);
      this.textPane.setText(var1);
      this.partitioner.setDocument(this.getDocument());
      this.getDocument().discardAllPendingEdits();
   }

   public void insertGlobalDataElement(String var1) {
      String var2 = GlobalDE.forScript(var1);
      this.insertText(var2);
   }

   public void insertText(final String var1) {
      if (this.textPane.isEditable()) {
         this.requestFocus();
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ExpressionArea.this.textPane.replaceSelection(var1);
            }
         });
      }
   }

   public void highlightText(Interval var1) {
      int var2 = var1.startsAt();
      if (var2 >= 0) {
         int var3 = var2 + var1.length();
         if (var3 <= this.getDocument().getLength()) {
            this.requestFocus();
            this.textPane.setCaretPosition(var2);
            this.textPane.moveCaretPosition(var3);
         }
      }
   }

   public boolean hasTransTag() {
      return this.textPane.hasTransTag();
   }

   Action getFindAction() {
      return this.textPane.getFindAction();
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.scrollPane);
   }

   @Override
   public void requestFocus() {
      this.textPane.requestFocusInWindow();
   }

   @Override
   public void setPainter(ComponentPainter var1) {
      this.textPane.setPainter(var1);
   }

   public ComponentPainter getPainter() {
      return this.textPane.getPainter();
   }

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      this.propChangeSupport.addPropertyChangeListener(var1);
   }

   public void removePropertyChangeListener(PropertyChangeListener var1) {
      this.propChangeSupport.removePropertyChangeListener(var1);
   }

   public void addPropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.propChangeSupport.addPropertyChangeListener(var1, var2);
   }

   public void removePropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.propChangeSupport.removePropertyChangeListener(var1, var2);
   }

   public void setScriptDataElements(ScriptDataElementsModel var1) {
      DataElementHovererDisplayer var2 = new DataElementHovererDisplayer(var1);
      this.mouseHoverer.setDisplayerForToken(Tokens.SOURCE_PATH, var2);
      this.mouseHoverer.setDisplayerForToken(Tokens.GLOBAL_DATA_ELEMENT, var2);
   }

   private void registerDebugActions() {
      this.installShortCut("print-partitions", KeyUtils.getCtrlDown(61), new ExpressionArea.PrintTokensAction());
      this.installShortCut("print-revpolish", KeyUtils.getCtrlDown(91), new ExpressionArea.PrintReversePolishNotationAction());
   }

   private static final class MouseClickHandler extends MouseAdapter {
      private final JTextComponent component;
      private final TokenSelector tokenSelector;

      public MouseClickHandler(JTextComponent var1, ScriptPartitioner var2) {
         this.component = var1;
         this.tokenSelector = new TokenSelector(var1, var2);
         this.tokenSelector.setSelectCompleteToken(true);
      }

      @Override
      public void mouseClicked(MouseEvent var1) {
         switch (var1.getClickCount()) {
            case 1:
               this.checkLineSelect(var1);
               break;
            case 3:
               this.selectCompleteToken(var1);
               var1.consume();
         }
      }

      private void checkLineSelect(MouseEvent var1) {
         if (var1.isControlDown()) {
            int var2 = this.component.viewToModel(var1.getPoint());
            if (TextComponentUtils.isStartOfLine(this.component, var2)) {
               TextComponentUtils.selectLine(this.component, var2);
               var1.consume();
            }
         }
      }

      private void selectCompleteToken(MouseEvent var1) {
         int var2 = this.component.viewToModel(var1.getPoint());
         this.tokenSelector.selectToken(var2);
      }
   }

   private final class PrintReversePolishNotationAction extends AbstractAction {
      private PrintReversePolishNotationAction() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         String var2 = ExpressionArea.this.getText();
         Transform var3 = new Transform();
         List var4 = var3.getRpnTokenVector(var2);
         if (var4 == null) {
            System.err.println("Syntax error");
         } else {
            System.out.println("Reverse Polish Notation:");

            for (Object __etlItem433 : var4) {
               Transform.Token var6 = (Transform.Token) __etlItem433;
               System.out.println(var6);
            }
         }
      }
   }

   private final class PrintTokensAction extends AbstractAction {
      private PrintTokensAction() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         for (TextPartitioner.Partition var2 = ExpressionArea.this.partitioner.getFirst(); var2 != null; var2 = var2.getNext()) {
            System.out.println(var2.token.getClass().getSimpleName() + ": " + DocumentUtils.getText(ExpressionArea.this.getDocument(), var2));
         }
      }
   }
}
