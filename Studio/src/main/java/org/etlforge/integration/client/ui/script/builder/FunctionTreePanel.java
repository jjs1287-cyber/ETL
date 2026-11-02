package org.etlforge.integration.client.ui.script.builder;

import com.google.common.base.Preconditions;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.etlforge.application.ui.widget.tree.TreePanel;
import org.etlforge.integration.client.ui.script.Icons;
import org.etlforge.integration.client.ui.script.builder.layout.LayoutType;
import org.etlforge.integration.client.ui.script.prefs.LayoutTypePreference;
import org.etlforge.integration.data.script.Function;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.tree.SimpleTreeFilterField;

public class FunctionTreePanel extends TreePanel implements ScriptBuilderSection {
   private final FunctionTree tree;
   private final Action helpAction;
   private Receiver<InsertionToken> receiver = NoReceiver.create();

   public FunctionTreePanel(FunctionTree var1) {
      super(createTitle(), var1.getTree());
      this.tree = var1;
      this.helpAction = this.createShowFunctionHelpAction();
      if (!isTabBased()) {
         TreeFilters.installFilter(this);
      }

      this.installHelpActionShortCut();
   }

   private static String createTitle() {
      return isTabBased() ? null : Strings.get("FunctionTree.Title");
   }

   private static boolean isTabBased() {
      return LayoutTypePreference.get() == LayoutType.TABS;
   }

   private Action createShowFunctionHelpAction() {
      return this.tree.getFunctionAction(new Receiver<Function>() {
         public void handle(Function var1) {
            FunctionHelpDialog.showFunctionHelp(var1, SwingUtilities.getWindowAncestor(FunctionTreePanel.this.tree.getTree()));
         }
      }, Icons.HELP_ICON, Strings.get("FunctionTree.Help.Tooltip"), false);
   }

   private void installHelpActionShortCut() {
      KeyBindingInstaller var1 = new KeyBindingInstaller(this.tree.getTree());
      var1.addBinding(KeyUtils.HELP, "help", this.helpAction);
      var1.install();
   }

   @Override
   public void setReceiver(Receiver<InsertionToken> var1) {
      this.receiver = Preconditions.checkNotNull(var1);
   }

   @Override
   public void setMode(ScriptBuilderMode var1) {
      this.tree.setMode(var1);
   }

   @Override
   public ScriptBuilderSection.Title getTitleDecoration() {
      return new ScriptBuilderSection.Title("&Functions", Icons.FORMULA_ICON);
   }

   @Override
   public void collectArgumentSuggestions(FunctionArgumentSuggestions var1) {
   }

   @Override
   public void dispose() {
   }

   @Override
   protected JButton[] getToolBarButtons() {
      return new JButton[]{
         KongaButton.forToolbar(this.createInsertFunctionAction()),
         KongaButton.forToolbar(this.tree.getExpansionToggleAction()),
         null,
         KongaButton.forToolbar(this.helpAction)
      };
   }

   private SelectionAction createInsertFunctionAction() {
      Icon var1 = Icons.getPreferredTransferIcon();
      return this.tree.getFunctionAction(new Receiver<Function>() {
         public void handle(Function var1) {
            FunctionTreePanel.this.receiver.handle(new InsertionToken.Function(var1));
         }
      }, var1, Strings.get("FunctionTree.Transfer.Tooltip"), true);
   }

   @Override
   protected JComponent[] getAdditionalTitlePanelComponents() {
      if (isTabBased()) {
         SimpleTreeFilterField var1 = TreeFilters.createSearchField(this.getTree());
         return new JComponent[]{InvisiblePanel.of(var1)};
      } else {
         return new JComponent[0];
      }
   }
}
