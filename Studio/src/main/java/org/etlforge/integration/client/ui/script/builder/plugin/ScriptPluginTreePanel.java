package org.etlforge.integration.client.ui.script.builder.plugin;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.widget.tree.TreePanel;
import org.etlforge.integration.client.plugin.ui.pipeline.PipelinePluginInfoDialogViewer;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.script.Icons;
import org.etlforge.integration.client.ui.script.builder.FunctionArgumentSuggestions;
import org.etlforge.integration.client.ui.script.builder.InsertionToken;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderMode;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderSection;
import org.etlforge.integration.client.ui.script.builder.layout.LayoutType;
import org.etlforge.integration.client.ui.script.prefs.LayoutTypePreference;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.widget.button.KongaButton;

public final class ScriptPluginTreePanel extends TreePanel implements ScriptBuilderSection {
   private final ScriptPluginTree tree;
   private Receiver<InsertionToken> receiver = NoReceiver.create();
   private final SelectionAction transferAction = new ScriptPluginTreePanel.TransferAction();
   private final SelectionAction viewManifestAction = new ScriptPluginTreePanel.ViewManifestAction();

   public ScriptPluginTreePanel(ScriptPluginTree var1) {
      super(createTitle(), var1.getTree());
      this.tree = var1;
      var1.setDefaultAction(this.transferAction);
      var1.addSelectionAction(this.viewManifestAction);
      this.setRootVisible(true);
   }

   private static String createTitle() {
      boolean var0 = LayoutTypePreference.get() == LayoutType.TABS;
      return var0 ? null : "Plugins";
   }

   @Override
   public ScriptBuilderSection.Title getTitleDecoration() {
      return new ScriptBuilderSection.Title("Pl&ugins", ClientIcons.PLUGIN_16);
   }

   @Override
   public void collectArgumentSuggestions(FunctionArgumentSuggestions var1) {
      var1.addSuggestion(new PluginArgumentSuggestion(this.tree.getProvider()));
   }

   @Override
   public void setReceiver(Receiver<InsertionToken> var1) {
      this.receiver = Preconditions.checkNotNull(var1);
   }

   @Override
   public void setMode(ScriptBuilderMode var1) {
   }

   @Override
   protected JButton[] getToolBarButtons() {
      return new JButton[]{
         KongaButton.forToolbar(this.transferAction), KongaButton.forToolbar(this.viewManifestAction), KongaButton.forToolbar(this.tree.getRefreshAction())
      };
   }

   @Override
   public void dispose() {
      this.tree.dispose();
   }

   private final class TransferAction extends AbstractSingleObjectSelectionAction<PipelinePluginManifest> {
      public TransferAction() {
         super(PipelinePluginManifest.class, null, Icons.getPreferredTransferIcon());
         this.putValue("ShortDescription", "Insert the selected plugin into the script");
      }

      protected void run(PipelinePluginManifest var1, ActionEvent var2) {
         ScriptPluginTreePanel.this.receiver.handle(new InsertionToken.Plugin(var1));
      }
   }

   private static final class ViewManifestAction extends AbstractSingleObjectSelectionAction<PipelinePluginManifest> {
      public ViewManifestAction() {
         super(PipelinePluginManifest.class);
         ActionDecorators.VIEW_DETAILS.decorate(this);
         this.putValue("ShortDescription", "View the plugin manifest for the selected plugin");
      }

      protected void run(final PipelinePluginManifest var1, ActionEvent var2) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ViewManifestAction.this.openManifestViewer(var1);
            }
         });
      }

      private void openManifestViewer(PipelinePluginManifest var1) {
         new PipelinePluginInfoDialogViewer().view(var1);
      }
   }
}
