package org.etlforge.integration.client.ui.script.builder;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.help.DocumentedResource;
import org.etlforge.application.ui.widget.tree.ExpandCollapseToggleAction;
import org.etlforge.application.ui.widget.tree.TreePanel;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.ui.script.Icons;
import org.etlforge.integration.client.ui.script.builder.docs.ProjectItemTreeHelp;
import org.etlforge.integration.client.ui.script.builder.layout.LayoutType;
import org.etlforge.integration.client.ui.script.prefs.LayoutTypePreference;
import org.etlforge.integration.client.ui.script.text.FunctionProjectItems;
import org.etlforge.integration.client.ui.script.text.ProjectItemArgumentSuggestion;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.script.Function;
import org.etlforge.integration.uidelegate.project.ProjectIcons;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.SimpleTreeFilterField;

public final class ProjectItemTreePanel extends TreePanel implements ScriptBuilderSection, FunctionProjectItems {
   private final ProjectItemTree tree;
   private Receiver<InsertionToken> receiver = NoReceiver.create();
   private final ProjectItemTreePanel.ShowHelpAction helpAction = new ProjectItemTreePanel.ShowHelpAction();
   private final SelectionAction insertItemAction;
   private final ExpandCollapseToggleAction expansionToggle;

   public ProjectItemTreePanel(ProjectItemTree var1) {
      super(createTitle(), var1.getTree());
      this.tree = var1;
      this.insertItemAction = this.createInsertItemAction();
      this.expansionToggle = var1.getExpansionToggleAction();
      if (!isTabBased()) {
         TreeFilters.installFilter(this);
      }

      this.installHelpActionShortCut();
      this.listenToTreeSelections();
   }

   private static String createTitle() {
      return isTabBased() ? null : Strings.get("ProjectItemTree.Title");
   }

   private static boolean isTabBased() {
      return LayoutTypePreference.get() == LayoutType.TABS;
   }

   private SelectionAction createInsertItemAction() {
      Icon var1 = Icons.getPreferredTransferIcon();
      return this.tree.getEntityAction(new Receiver<IntegrationEntity>() {
         public void handle(IntegrationEntity var1) {
            ProjectItemTreePanel.this.receiver.handle(new InsertionToken.ProjectItem(var1));
         }
      }, var1, Strings.get("ProjectItemTree.Transfer.Tooltip"), true);
   }

   private void installHelpActionShortCut() {
      KeyBindingInstaller var1 = new KeyBindingInstaller(this.tree.getTree());
      var1.addBinding(KeyUtils.HELP, "help", this.helpAction);
      var1.install();
   }

   private void listenToTreeSelections() {
      this.tree.getTree().addTreeSelectionListener(new TreeSelectionListener() {
         @Override
         public void valueChanged(TreeSelectionEvent var1) {
            ProjectItemTreePanel.this.updateHelpAction();
         }
      });
   }

   @Override
   public ScriptBuilderSection.Title getTitleDecoration() {
      return new ScriptBuilderSection.Title("&Project Items", ProjectIcons.PROJECT_16);
   }

   @Override
   public void collectArgumentSuggestions(FunctionArgumentSuggestions var1) {
      var1.addSuggestion(new ProjectItemArgumentSuggestion(this));
   }

   @Override
   public void setReceiver(Receiver<InsertionToken> var1) {
      this.receiver = Preconditions.checkNotNull(var1);
   }

   @Override
   public void setMode(ScriptBuilderMode var1) {
   }

   @Override
   public List<IntegrationEntity> getApplicableProjectItemArguments(final Function var1) {
      return KongaListUtils.filter(this.tree.getProject().getAllEntities(), new EntityFilter() {
         @Override
         public boolean apply(IntegrationEntity var1x) {
            return var1.acceptsProjectItemAsInput(var1x);
         }
      });
   }

   @Override
   public void dispose() {
      this.tree.dispose();
      this.expansionToggle.dispose();
   }

   @Override
   protected JButton[] getToolBarButtons() {
      return new JButton[]{buttonFor(this.insertItemAction), buttonFor(this.expansionToggle), null, buttonFor(this.helpAction)};
   }

   private static JButton buttonFor(Action var0) {
      return KongaButton.forToolbar(var0);
   }

   private void updateHelpAction() {
      DocumentedResource var1 = null;
      List var2 = this.tree.getTree().getSelectedNodes();
      if (var2.size() == 1) {
         KongaTreeNode var3 = (KongaTreeNode)var2.get(0);
         if (var3 instanceof DocumentedResource) {
            var1 = (DocumentedResource)var3;
         }
      }

      this.helpAction.setSelectedResource(var1);
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

   private class ShowHelpAction extends AbstractAction {
      private DocumentedResource selectedResource;

      public ShowHelpAction() {
         super("", Icons.HELP_ICON);
         this.setEnabled(false);
      }

      public void setSelectedResource(DocumentedResource var1) {
         this.selectedResource = var1;
         this.setEnabled(var1 != null);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         String var2 = ProjectItemTreeHelp.lookupTitle(this.selectedResource);
         String var3 = ProjectItemTreeHelp.lookupDocs(this.selectedResource);
         JLabel var4 = new JLabel(var3);
         Alert.message(null, var4, var2, Alert.Type.INFORMATION);
         ProjectItemTreePanel.this.tree.getTree().requestFocusInWindow();
      }
   }
}
