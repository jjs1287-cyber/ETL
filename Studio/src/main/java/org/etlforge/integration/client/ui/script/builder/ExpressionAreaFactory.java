package org.etlforge.integration.client.ui.script.builder;

import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.text.editor.GotoLineNumberAction;
import org.etlforge.application.ui.widget.AutoCompleterFactory;
import org.etlforge.integration.client.ui.script.builder.dataelement.DataElementsMenu;
import org.etlforge.integration.client.ui.script.partition.ScriptPartitioner;
import org.etlforge.integration.client.ui.script.prefs.TabSizePreference;
import org.etlforge.integration.client.ui.script.text.ScriptCompletionContext;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.action.ToggleAction;
import org.etlforge.ui.text.TabInAction;
import org.etlforge.ui.text.TabOutAction;
import org.etlforge.ui.text.ToggleCommentAction;
import org.etlforge.ui.text.contentassist.AutoCompleter;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.menu.KongaMenuItem;
import org.etlforge.ui.widget.menu.ToggleActionCheckBoxMenuItem;
import org.etlforge.util.preference.KongaPreferences;
import org.etlforge.util.preference.PreferencesKey;

public final class ExpressionAreaFactory {
   private static final PreferencesKey FONT_SIZE_KEY = new PreferencesKey(ExpressionAreaFactory.class, "font_size");
   private boolean withInstanceFunctions = true;
   private Action testAction;
   private Action gotoLineAction;
   private ToggleAction showLineNumberAction;
   private Action toggleCommentAction;
   private IntegrationProject project;
   private FunctionArgumentSuggestions functionArgumentSuggestions;
   private FunctionInserterLookup functionInserterLookup;

   public ExpressionArea newExpressionArea() {
      ExpressionAreaTextPane var1 = new ExpressionAreaTextPane(this.withInstanceFunctions);
      ScriptPartitioner var2 = new ScriptPartitioner();
      ScriptCompletionContext var3 = this.createAutoCompletion(var1, var2);
      ExpressionArea var4 = new ExpressionArea(var1, var2, var3);
      var4.setFunctionInserterLookup(this.functionInserterLookup);
      this.customize(var4);
      this.installTestActionShortCut(var4);
      this.gotoLineAction = this.installGoToLineAction(var4);
      this.showLineNumberAction = this.installShowLineNumberAction(var4);
      this.toggleCommentAction = this.installToggleCommentAction(var4);
      this.installTabIndentActions(var4);
      this.installExpressionAreaAddOns(var4);
      installOperatorWindowAction(var4);
      this.addContextMenuItems(var4);
      return var4;
   }

   private ScriptCompletionContext createAutoCompletion(ExpressionAreaTextPane var1, ScriptPartitioner var2) {
      ScriptCompletionContext var3 = new ScriptCompletionContext(var1, var2, this.functionArgumentSuggestions, this.withInstanceFunctions);
      var3.setFunctionInserterLookup(this.functionInserterLookup);
      if (this.project != null) {
         var3.setGlobalDataElements(this.project.getGlobalDataElements());
      }

      AutoCompleterFactory var4 = AutoCompleterFactory.codeEditorFactory();
      var4.setGrabFocus(false);
      AutoCompleter var5 = var4.createAutoCompleter(var3);
      var5.install(var1);
      return var3;
   }

   public void setFunctionArgumentSuggestions(FunctionArgumentSuggestions var1) {
      this.functionArgumentSuggestions = var1;
   }

   public void setFunctionInserterLookup(FunctionInserterLookup var1) {
      this.functionInserterLookup = var1;
   }

   public void setProject(IntegrationProject var1) {
      this.project = var1;
   }

   private void installTabIndentActions(ExpressionArea var1) {
      TabOutAction.install(var1.getTextPane(), var1.getTabSize());
      TabInAction.install(var1.getTextPane());
   }

   private void installExpressionAreaAddOns(ExpressionArea var1) {
      ScriptBuilderAddOns var2 = new ScriptBuilderAddOns(var1);
      var2.installCharacterMatching();
      var2.installWhitespaceDecorator();
   }

   private void installTestActionShortCut(ExpressionArea var1) {
      if (this.testAction != null) {
         KeyStroke var2 = (KeyStroke)this.testAction.getValue("AcceleratorKey");
         if (var2 != null) {
            var1.installShortCut("test", var2, this.testAction);
         }
      }
   }

   private Action installGoToLineAction(ExpressionArea var1) {
      GotoLineNumberAction var2 = new GotoLineNumberAction(var1.getTextPane());
      KeyStroke var3 = KeyUtils.getCtrlDown(76);
      var2.putValue("AcceleratorKey", var3);
      var1.installShortCut("gotoLine", var3, var2);
      return var2;
   }

   private ToggleAction installShowLineNumberAction(ExpressionArea var1) {
      ShowLineNumbersAction var2 = new ShowLineNumbersAction(var1);
      KeyStroke var3 = KeyUtils.getCtrlShiftDown(76);
      var2.putValue("AcceleratorKey", var3);
      var1.installShortCut("showLineNumbers", var3, var2);
      return var2;
   }

   private Action installToggleCommentAction(ExpressionArea var1) {
      ToggleCommentAction var2 = new ToggleCommentAction(var1.getTextPane());
      KeyStroke var3 = KeyUtils.getCtrlDown(47);
      var2.putValue("AcceleratorKey", var3);
      var1.installShortCut("toggleComment", var3, var2);
      return var2;
   }

   public void setTestAction(Action var1) {
      this.testAction = var1;
   }

   public void setUseInstanceFunctions(boolean var1) {
      this.withInstanceFunctions = var1;
   }

   private void customize(ExpressionArea var1) {
      var1.getTextPane().setWrapping(true);
      var1.setTabSize(TabSizePreference.get());
      setFont(var1.getTextPane());
      installChangeFontActions(var1);
   }

   private static void setFont(JTextComponent var0) {
      Font var1 = TextStyles.LargeMonoSpaced.getFont();
      KongaPreferences var2 = KongaPreferences.getPreferences(FONT_SIZE_KEY);
      int var3 = var2.getInt(FONT_SIZE_KEY.keyName(), -1);
      if (var3 > 0) {
         var1 = var1.deriveFont((float)var3);
      }

      var0.setFont(var1);
   }

   private void addContextMenuItems(ExpressionArea var1) {
      if (this.testAction != null) {
         var1.addToContextMenu(new KongaMenuItem(this.testAction));
      }

      FunctionMenu var2 = new FunctionMenu(var1, var1.supportsInstanceFunctions());
      var1.addToContextMenu(var2.getMenu());
      if (this.project != null) {
         DataElementsMenu var3 = new DataElementsMenu(this.project.getGlobalDataElements(), var1);
         var1.addToContextMenu(var3.getMenu());
      }

      var1.addToContextMenu(null);
      var1.addToContextMenu(new ToggleActionCheckBoxMenuItem(this.showLineNumberAction));
      var1.addToContextMenu(new KongaMenuItem(this.gotoLineAction));
      var1.addToContextMenu(new KongaMenuItem(this.toggleCommentAction));
      var1.addToContextMenu(null);
   }

   private static void installChangeFontActions(ExpressionArea var0) {
      KeyStroke[] var1 = new KeyStroke[]{KeyUtils.getCtrlDown(107)};
      ExpressionAreaFactory.ChangeFontAction.install(var0, 1, "increase_font_size", var1);
      KeyStroke[] var2 = new KeyStroke[]{KeyUtils.getCtrlDown(109)};
      ExpressionAreaFactory.ChangeFontAction.install(var0, -1, "decrease_font_size", var2);
   }

   private static void installOperatorWindowAction(ExpressionArea var0) {
      ExpressionAreaFactory.ShowOperatorWindowAction var1 = new ExpressionAreaFactory.ShowOperatorWindowAction(var0);
      var0.installShortCut("show-operator-window", null, var1);
   }

   private static final class ChangeFontAction extends AbstractAction {
      private final ExpressionArea textComponent;
      private final int sizeDelta;

      private ChangeFontAction(ExpressionArea var1, int var2) {
         this.textComponent = var1;
         this.sizeDelta = var2;
      }

      public static void install(ExpressionArea var0, int var1, Object var2, KeyStroke[] var3) {
         ExpressionAreaFactory.ChangeFontAction var4 = new ExpressionAreaFactory.ChangeFontAction(var0, var1);

         for (KeyStroke var8 : var3) {
            var0.installShortCut(var2, var8, var4);
         }
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         int var2 = this.installNewFont();
         this.updateUserPreferences(var2);
      }

      private int installNewFont() {
         Font var1 = this.textComponent.getTextPane().getFont();
         int var2 = var1.getSize() + this.sizeDelta;
         Font var3 = var1.deriveFont((float)var2);
         this.textComponent.getTextPane().setFont(var3);
         this.redoSyntaxHighlighting();
         return var2;
      }

      private void redoSyntaxHighlighting() {
         this.textComponent.getPartitioner().refresh();
      }

      private void updateUserPreferences(int var1) {
         KongaPreferences var2 = KongaPreferences.getPreferences(ExpressionAreaFactory.FONT_SIZE_KEY);
         var2.putInt(ExpressionAreaFactory.FONT_SIZE_KEY.keyName(), var1);
      }
   }

   private static final class ShowOperatorWindowAction extends AbstractAction {
      private final ExpressionArea target;

      public ShowOperatorWindowAction(ExpressionArea var1) {
         this.target = var1;
         this.putValue("AcceleratorKey", KeyUtils.getCtrlShiftDown(79));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         try {
            Point var2 = this.getLocationForWindow();
            OperatorWindow var3 = this.createWindow();
            var3.show(var2.x, var2.y);
         } catch (BadLocationException var4) {
            ErrorLog.log(ExpressionAreaFactory.ShowOperatorWindowAction.class, null, var4);
         }
      }

      private Point getLocationForWindow() throws BadLocationException {
         int var1 = this.target.getCaretPosition();
         Point var2 = this.target.getTextPane().modelToView(var1).getLocation();
         SwingUtilities.convertPointToScreen(var2, this.target.getTextPane());
         return var2;
      }

      private OperatorWindow createWindow() {
         OperatorWindow var1 = new OperatorWindow(this.target.getTextPane());
         var1.setCallback(new Receiver<String>() {
            public void handle(String var1) {
               ShowOperatorWindowAction.this.target.insertText(var1);
            }
         });
         return var1;
      }
   }
}
