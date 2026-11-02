package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings;

import com.google.common.base.Preconditions;
import java.util.List;
import javax.swing.JLabel;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.resources.TreeStrings;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.data.entity.script.ExpressionTranslator;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.find.ReplaceUiLayout;
import org.etlforge.ui.util.find.SearchTarget;
import org.etlforge.ui.util.find.TextSearchController;
import org.etlforge.ui.util.find.TextSearchModel;
import org.etlforge.ui.util.find.TextSearchObserver;
import org.etlforge.ui.util.find.TextSearchUi;

public final class MappingSearchAndReplaceUi implements UiProvider {
   private final TreeMapper treeMapper;
   private final MappingSearchAndReplaceTarget target;
   private final TextSearchUi searchParametersUi;
   private final TextSearchController controller;
   private int matchCount;
   private final MappingSearchMatchDisplayer matchDisplayer;
   private final MappingSearchAndReplaceUi.StatusPanel statusPanel;

   public MappingSearchAndReplaceUi(TreeMapper var1, ExpressionTranslator var2, MappingSearchMatchDisplayer var3) {
      Preconditions.checkNotNull(var1, "treeMapper");
      Preconditions.checkNotNull(var1, "translator");
      Preconditions.checkNotNull(var3, "matchDisplayer");
      this.treeMapper = var1;
      this.matchDisplayer = var3;
      this.target = new MappingSearchAndReplaceTarget(var1, var2);
      TextSearchModel var4 = new TextSearchModel();
      this.controller = new TextSearchController(var4);
      this.controller.addObserver(new MappingSearchAndReplaceUi.SearchListener());
      this.controller.setTarget(this.target);
      this.searchParametersUi = TextSearchUi.searchAndReplace(var4);
      this.searchParametersUi.setDefaultAction(this.controller.getFindAction());
      this.statusPanel = new MappingSearchAndReplaceUi.StatusPanel();
   }

   @Override
   public void decorate(UiCanvas var1) {
      MappingSearchAndReplaceUi.Layout var2 = new MappingSearchAndReplaceUi.Layout();
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.searchParametersUi.requestFocus();
   }

   public void reset() {
      this.target.reset();
      this.setStatus(null);
      this.matchDisplayer.clear();
   }

   private void displayNewMatch(final MappingSearchMatch var1) {
      assert var1 != null : "displayNewMatch() should be called only when a new match has been found";
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            MappingSearchAndReplaceUi.this.setStatus(null);
            MappingSearchAndReplaceUi.this.matchDisplayer.displayMatch(var1);
            MappingSearchAndReplaceUi.this.highlightMapping(var1);
         }
      });
   }

   private void highlightMapping(MappingSearchMatch var1) {
      NodeMapping var2 = var1.getMapping();
      this.treeMapper.getMappingManager().changeHighLighted(var2);
      Node var3 = this.treeMapper.findTargetNode(var2.getTargetPath());
      if (var3 != null) {
         this.treeMapper.m_targetTreeComponent.showNode(var3, true);
      }
   }

   private void clearMatch() {
      if (this.matchCount == 0) {
         this.setStatus(TreeStrings.get("Search.Mappings.NoMatch"));
         this.matchDisplayer.clear();
      } else {
         this.setStatus(TreeStrings.get("Search.Mappings.NoMoreMatch"));
      }
   }

   private void setStatus(String var1) {
      this.statusPanel.setStatus(var1);
   }

   private class Layout {
      private final BorderLayoutBuilder layout = new BorderLayoutBuilder();

      public Layout() {
         this.layout
            .northToSouth(
               new ReplaceUiLayout(MappingSearchAndReplaceUi.this.searchParametersUi, MappingSearchAndReplaceUi.this.controller),
               MappingSearchAndReplaceUi.this.matchDisplayer,
               MappingSearchAndReplaceUi.this.statusPanel.label
            );
      }

      public void decorate(UiCanvas var1) {
         this.layout.decorate(var1);
      }
   }

   private class SearchListener implements TextSearchObserver {
      private SearchListener() {
      }

      @Override
      public void newMatch(SearchTarget.Match var1) {
         MappingSearchAndReplaceUi.this.displayNewMatch((MappingSearchMatch)var1);
         MappingSearchAndReplaceUi.this.setStatus("");
      }

      @Override
      public void noMatch() {
         MappingSearchAndReplaceUi.this.clearMatch();
         MappingSearchAndReplaceUi.this.setStatus(TreeStrings.get("Search.Mappings.NoMatch"));
      }

      @Override
      public void searchWrappedAround(SearchTarget.Match var1) {
         MappingSearchAndReplaceUi.this.displayNewMatch((MappingSearchMatch)var1);
         MappingSearchAndReplaceUi.this.setStatus("Wrapped search");
      }

      @Override
      public void matchReplaced(SearchTarget.Match var1, String var2) {
         MappingSearchAndReplaceUi.this.displayNewMatch((MappingSearchMatch)var1);
         MappingSearchAndReplaceUi.this.setStatus("");
      }

      @Override
      public void matchesReplaced(List<? extends SearchTarget.Match> var1, String var2) {
         assert var1.size() > 0;
         String var3;
         if (var1.size() == 1) {
            var3 = "1 match replaced";
         } else {
            var3 = var1.size() + " matches replaced";
         }

         MappingSearchAndReplaceUi.this.setStatus(var3);
      }
   }

   private static class StatusPanel {
      private final JLabel label = new JLabel(" ");

      private StatusPanel() {
      }

      public void setStatus(String var1) {
         String var2 = var1;
         if (var1 == null || var1.length() == 0) {
            var2 = " ";
         }

         this.label.setText(var2);
      }
   }
}
