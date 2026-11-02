package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.structure.mapping.NodePath;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.widget.label.DottedTextLabel;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.util.Property;

public final class MappingSuggestionUi implements UiProvider {
   private final SuggestionsModel model;
   private final KongaTextField selectedNodeField;
   private final KongaList candidates;
   private final DottedTextLabel statusLabel;
   private Receiver<Node> callback;
   private DefaultListModel listModel;

   public MappingSuggestionUi(SuggestionsModel var1) {
      this.model = var1;
      this.selectedNodeField = this.createSelectedNodeField();
      this.candidates = this.createCandidatesList();
      this.statusLabel = new DottedTextLabel();
      this.callback = NoReceiver.create();
      var1.addNodesFoundListener(new MappingSuggestionUi.NodesFoundListener());
      this.installBindings();
   }

   private KongaTextField createSelectedNodeField() {
      NodePath var1 = new NodePath(this.model.getSelectedNode().m_deName);
      String var2 = var1.getLeafName();
      KongaTextField var3 = new KongaTextField(var2);
      var3.setEditable(false);
      return var3;
   }

   private KongaList createCandidatesList() {
      this.listModel = new DefaultListModel();
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      var1.setCellRenderer(new MappingSuggestionUi.ListRenderer());
      var1.setLabel(new JLabel("Suggested mappings:"));
      var1.setVisibleRowCount(8);
      KongaList var2 = var1.newList(this.listModel);
      var2.setDefaultAction(new MappingSuggestionUi.SelectNodeAction());
      return var2;
   }

   private void installBindings() {
      Property var1 = this.model.getProperty("working");
      MappingSuggestionUi.InProgressBinding var2 = new MappingSuggestionUi.InProgressBinding(var1);
      var2.syncUi();
   }

   public void setCallback(Receiver<Node> var1) {
      this.callback = var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      String var2 = (this.model.isSelectedNodeSource() ? "Source" : "Target") + " field: ";
      BoxBuilder var3 = Horizontal.box().of(var2, this.selectedNodeField);
      BorderLayoutBuilder var4 = new BorderLayoutBuilder(0, 5);
      var4.northToSouth(var3, this.candidates, this.statusLabel);
      var4.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.candidates.requestFocus();
   }

   private class InProgressBinding extends AbstractBinding<Boolean> {
      private boolean hasBeenRunning;

      public InProgressBinding(Property<Boolean> var2) {
         super(var2);
      }

      protected void updateUiWithValue(Boolean var1) {
         if (var1) {
            this.hasBeenRunning = true;
            MappingSuggestionUi.this.statusLabel.setText("Searching for matching nodes");
            MappingSuggestionUi.this.statusLabel.setUpdateAutomatically(125);
         } else {
            MappingSuggestionUi.this.statusLabel.stopAutomaticUpdate();
            String var2 = MappingSuggestionUi.this.model.isSelectedNodeSource() ? "target" : "source";
            String var3 = this.hasBeenRunning ? MappingSuggestionUi.this.model.getNumberOfTargetNodes() + " " + var2 + " node(s) found" : " ";
            MappingSuggestionUi.this.statusLabel.setText(var3);
         }
      }
   }

   private class ListRenderer extends DefaultListCellRenderer {
      private ListRenderer() {
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         if (var2 instanceof Node) {
            Node var6 = (Node)var2;
            this.setText(this.getDisplayString(var6));
            this.setToolTipText(var6.m_deName);
         } else {
            this.setToolTipText(null);
         }

         return this;
      }

      private String getDisplayString(Node var1) {
         Node var2 = MappingSuggestionUi.this.model.getCommonAncestor();
         String var3 = var2 == null ? var1.m_deName : this.getRelativePath(var1, var2);
         return StringUtils.substringBeforeLast(var3, "$");
      }

      private String getRelativePath(Node var1, Node var2) {
         int var3 = var2.m_deName.length();
         if (var2.m_deName.startsWith("[") && var2.m_deName.endsWith("]")) {
            var3 -= 2;
         }

         return var1.m_deName.substring(var3);
      }
   }

   private class NodesFoundListener implements SuggestionsModelListener {
      private NodesFoundListener() {
      }

      @Override
      public void nodesFound(List<Node> var1) {
         for (Node var3 : var1) {
            MappingSuggestionUi.this.listModel.addElement(var3);
         }
      }

      @Override
      public void cleared() {
         MappingSuggestionUi.this.listModel = new DefaultListModel();
         MappingSuggestionUi.this.candidates.setModel(MappingSuggestionUi.this.listModel);
      }
   }

   private class SelectNodeAction extends AbstractSingleObjectSelectionAction<Node> {
      public SelectNodeAction() {
         super(Node.class);
      }

      protected void run(Node var1, ActionEvent var2) {
         MappingSuggestionUi.this.callback.handle(var1);
      }
   }
}
