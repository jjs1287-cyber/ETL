package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.search;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.resources.TreeStrings;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.RadioButtonGroupPanel;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.OneRowPanel;
import org.etlforge.ui.text.ConditionalTextEnabler;
import org.etlforge.ui.text.NonEmptyTextEnablerCondition;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.util.find.SearchDirectionPanel;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextField;

public final class NodeSearchUi implements UiProvider {
   private final TreeComponent tree;
   private final JTextField searchField;
   private final String title;
   private final Receiver<NodeSearchResult> resultCallback;
   private final RadioButtonGroupPanel<NodeSearchType> nodeTypeSelector;
   private final SearchDirectionPanel directionPanel;
   private final Action findNextAction;
   private NodeSearchResult currentResult;
   private NodeSearchParameters currentSearchParams;
   private ConditionalTextEnabler actionEnabler;

   public NodeSearchUi(TreeComponent var1, String var2, Receiver<NodeSearchResult> var3) {
      Preconditions.checkNotNull(var1, "tree");
      Preconditions.checkNotNull(var3, "resultCallback");
      this.tree = var1;
      this.title = var2;
      this.resultCallback = var3;
      this.searchField = new KongaTextField();
      this.nodeTypeSelector = this.createNodeTypeSelector();
      this.directionPanel = SearchDirectionPanel.newHorizontalPanel();
      this.findNextAction = new NodeSearchUi.FindNextAction();
      this.searchField.addActionListener(this.findNextAction);
      this.currentSearchParams = this.getSearchParameters();
      this.actionEnabler = this.installActionEnabler(this.searchField);
   }

   public String getTitle() {
      return this.title;
   }

   void shareModel(NodeSearchUi var1) {
      if (var1 != this) {
         this.searchField.setDocument(var1.searchField.getDocument());
         this.nodeTypeSelector.shareModel(var1.nodeTypeSelector);
         this.directionPanel.shareModel(var1.directionPanel);
         this.actionEnabler.uninstall();
         this.actionEnabler = this.installActionEnabler(var1.searchField);
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      new NodeSearchUi.Layout().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.searchField.requestFocusInWindow();
   }

   public void clearPreviousResult() {
      this.currentResult = null;
   }

   private RadioButtonGroupPanel<NodeSearchType> createNodeTypeSelector() {
      RadioButtonGroupPanel var1 = RadioButtonGroupPanel.forEnum(NodeSearchType.class);
      var1.setSelectedValue(NodeSearchType.DATA_ELEMENTS_AND_FOLDERS);
      return var1;
   }

   private NodeSearchParameters getSearchParameters() {
      return new NodeSearchParameters(this.searchField.getText(), this.nodeTypeSelector.getSelection(), this.directionPanel.getSearchDirection());
   }

   private ConditionalTextEnabler installActionEnabler(JTextField var1) {
      ConditionalTextEnabler var2 = ConditionalTextEnabler.install(var1);
      NonEmptyTextEnablerCondition var3 = new NonEmptyTextEnablerCondition();
      var2.addDependency(this.findNextAction, var3);
      return var2;
   }

   private class FindNextAction extends AbstractAction {
      public FindNextAction() {
         super(TreeStrings.get("Search.Next"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         NodeSearchParameters var2 = NodeSearchUi.this.getSearchParameters();
         if (NodeSearchUi.this.currentResult == null || !var2.equals(NodeSearchUi.this.currentSearchParams)) {
            NodeSearchUi.this.currentSearchParams = var2;
            NodeSearchUi.this.currentResult = new NodeSearchResult(var2, NodeSearchUi.this.tree);
            NodeSearchUi.this.resultCallback.handle(NodeSearchUi.this.currentResult);
         }

         Node var3 = NodeSearchUi.this.currentResult.nextNode();
         if (var3 != null) {
            NodeSearchUi.this.tree.highlightSearchResult(var3);
         }
      }
   }

   private class Layout {
      private final OneRowPanel row = new OneRowPanel();

      public Layout() {
         this.layoutSearchUi();
         this.layoutButtons();
      }

      public void decorate(UiCanvas var1) {
         BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 15);
         if (NodeSearchUi.this.title != null && NodeSearchUi.this.title.length() > 0) {
            KongaLabel var3 = TextStyles.AdminHintBold.makeLabel(NodeSearchUi.this.title);
            OneColumnPanel var4 = new OneColumnPanel();
            var4.withInsets(0, 0, 15, 0);
            var4.add(var3);
            var4.fillOut(this.row);
            var2.north(var4);
         } else {
            var2.north(this.row);
         }

         var2.center(new JLabel(" "));
         var2.decorate(var1);
      }

      private void layoutSearchUi() {
         this.row.withInsets(0, 0, 0, 10);
         this.row.setFill(OneRowPanel.HORIZONTAL);
         this.row.setWeightX(1.0);
         OneColumnPanel var1 = new OneColumnPanel();
         var1.withInsets(0, 0, 10, 0);
         this.addSearchField(var1);
         this.addOptionsUi(var1);
         this.row.add(var1);
      }

      private void addSearchField(OneColumnPanel var1) {
         KongaLabel var2 = new KongaLabel(ApplicationResources.MNEMONIC_FACTORY.fromString(TreeStrings.get("Search.FieldLabel")));
         var2.setLabelFor(NodeSearchUi.this.searchField);
         BoxBuilder var3 = BoxBuilder.horizontal();
         var3.addAllWithSpace(10, var2, NodeSearchUi.this.searchField);
         var1.setWeightX(1.0);
         var1.add(var3);
      }

      private void addOptionsUi(OneColumnPanel var1) {
         OneRowPanel var2 = new OneRowPanel().withInsets(0, 0, 0, 10).of(this.getNodeTypeUi(), NodeSearchUi.this.directionPanel);
         var1.noFill(var2);
      }

      private JComponent getNodeTypeUi() {
         UiProvider var1 = NodeSearchUi.this.nodeTypeSelector.getUi(null, RadioButtonGroupPanel.Layout.ONE_COLUMN);
         JPanel var2 = InvisiblePanel.of(var1);
         var2.setBorder(new BorderBuilder().titled(TreeStrings.get("Search.NodeType.Title")).empty(5));
         return var2;
      }

      private void layoutButtons() {
         this.row.withInsets(0, 0, 0, 0);
         this.row.setFill(OneRowPanel.HORIZONTAL);
         this.row.setWeightX(0.0);
         OneColumnPanel var1 = new OneColumnPanel();
         var1.withInsets(0, 0, 10, 0);
         var1.add(new KongaButton(NodeSearchUi.this.findNextAction));
         this.row.add(var1);
      }
   }
}
