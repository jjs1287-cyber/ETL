package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.search;

import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.MappingSearchAndReplaceUi;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.MappingSearchMatchDisplayer;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.resources.TreeStrings;
import org.etlforge.integration.data.entity.script.CachedPathsExpressionTranslator;
import org.etlforge.integration.data.entity.script.ProjectGuidPathCache;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.script.FunctionRegistry;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.tabbedpane.KongaTabbedPane;

public final class TreeMapperSearchUi implements UiProvider, Disposable {
   private final TreeMapperSearchUi.Tabs tabs = new TreeMapperSearchUi.Tabs();
   private final MappingSearchAndReplaceUi mapSearch;
   private final NodeSearchUi sourceSearch;
   private final NodeSearchUi targetSearch;
   private final ProjectGuidPathCache pathLookup;

   public TreeMapperSearchUi(TreeMapper var1) {
      IntegrationProject var2 = var1.getTreeMapperContainer().getProject();
      this.pathLookup = FunctionRegistry.getRegistry().buildProjectBuidPathCache(var2);
      this.sourceSearch = var1.m_sourceTreeComponent.getNodeSearchUi();
      this.tabs.addContent(TreeStrings.get("Search.Tab.SourceTree"), this.sourceSearch);
      this.targetSearch = var1.m_targetTreeComponent.getNodeSearchUi();
      this.tabs.addContent(TreeStrings.get("Search.Tab.TargetTree"), this.targetSearch);
      CachedPathsExpressionTranslator var3 = new CachedPathsExpressionTranslator(this.pathLookup);
      MappingSearchMatchDisplayer var4 = new MappingSearchMatchDisplayer(var1, var3);
      this.mapSearch = new MappingSearchAndReplaceUi(var1, var3, var4);
      this.tabs.addContent(TreeStrings.get("Search.Tab.Mappings"), this.mapSearch);
      this.sourceSearch.shareModel(this.targetSearch);
   }

   public void reset() {
      this.mapSearch.reset();
      this.sourceSearch.clearPreviousResult();
      this.targetSearch.clearPreviousResult();
   }

   @Override
   public void dispose() {
      this.pathLookup.dispose();
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.tabs.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tabs.requestFocus();
   }

   private static class Tabs implements UiProvider {
      private final KongaTabbedPane pane = new KongaTabbedPane();
      private final Map<String, UiProvider> tabs;

      public Tabs() {
         this.pane.setInterpretTitlesAsMnemonicStrings(true);
         this.tabs = Maps.newHashMap();
      }

      public void addContent(String var1, UiProvider var2) {
         this.tabs.put(var1, var2);
         BorderLayoutBuilder var3 = new BorderLayoutBuilder().center(var2).withEmptyBorder(10);
         this.pane.addTab(var1, var3);
      }

      @Override
      public void decorate(UiCanvas var1) {
         this.pane.updatePreferredSize();
         this.pane.decorate(var1);
      }

      @Override
      public void requestFocus() {
         int var1 = this.pane.getSelectedIndex();
         UiProvider var2 = this.tabs.get(this.pane.getTitleAt(var1));
         if (var2 != null) {
            var2.requestFocus();
         }
      }
   }
}
