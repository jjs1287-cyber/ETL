package org.etlforge.integration.client.ui.interchange.entity.script.action;

import java.awt.EventQueue;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.entity.search.ProjectItemOrder;
import org.etlforge.integration.client.project.CurrentProjectEvent;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.project.CurrentProjectServiceListener;
import org.etlforge.integration.client.ui.entity.EntityPageDisplayer;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewer;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewerManager;
import org.etlforge.integration.client.ui.interchange.entity.script.page.ScriptPage;
import org.etlforge.integration.client.ui.interchange.entity.script.search.PersistingScriptReplaceStrategy;
import org.etlforge.integration.client.ui.interchange.entity.script.search.ScriptReplaceStrategy;
import org.etlforge.integration.client.ui.interchange.entity.script.search.ScriptSearchAndReplaceTarget;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderUi;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.entity.script.CachedPathsExpressionTranslator;
import org.etlforge.integration.data.entity.script.ExpressionTranslator;
import org.etlforge.integration.data.entity.script.ProjectGuidPathCache;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.script.FunctionRegistry;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.find.SearchAndReplaceTarget;
import org.etlforge.ui.util.find.SearchAndReplaceTextDialog;
import org.etlforge.ui.util.find.SearchTarget;
import org.etlforge.ui.util.find.TextSearchModel;
import org.etlforge.ui.util.find.TextSearchObserver;
import org.etlforge.util.Interval;

public final class SearchAndReplaceInScriptsAction extends AbstractSingleActiveObjectAction<RootFolder> {
   public static final String ID = "action.ic.SearchAndReplaceInScripts";
   private static final boolean IN_PAGE_CHANGES_SUPPORTED = false;
   private final ApplicationWindow appWin;
   private final CurrentProjectService projectManager;
   private final ProjectContentViewerManager contentViewers;
   private final ProjectPersistor persistor;
   private final EntityPageDisplayer pageDisplayer;
   private TextSearchModel parameterModel;
   private SearchAndReplaceTextDialog dialog;
   private SearchAndReplaceInScriptsAction.NewProjectListener newProjectListener;

   public SearchAndReplaceInScriptsAction(
      ApplicationWindow var1, CurrentProjectService var2, ProjectContentViewerManager var3, ProjectPersistor var4, EntityPageDisplayer var5
   ) {
      super("action.ic.SearchAndReplaceInScripts", var1.getActiveObjectService(), "Search and Replace in Scripts...", null, RootFolder.class);
      this.appWin = var1;
      this.projectManager = var2;
      this.contentViewers = var3;
      this.persistor = var4;
      this.pageDisplayer = var5;
   }

   protected boolean isEnabledFor(RootFolder var1) {
      return var1.getItemType() == EntityType.Script;
   }

   protected void runOnObject(RootFolder var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            SearchAndReplaceInScriptsAction.this.openDialog();
         }
      });
   }

   private void openDialog() {
      SearchAndReplaceTarget var1 = this.createTarget();
      if (this.dialog != null) {
         this.dialog.setTarget(var1);
         this.dialog.toFront();
      } else {
         TextSearchModel var2 = this.getParameterModel();
         this.dialog = new SearchAndReplaceTextDialog(var2, var1);
         this.dialog.addObserver(new SearchAndReplaceInScriptsAction.MatchDisplayer());
         this.dialog.addCloseListener(new SearchAndReplaceInScriptsAction.DialogCloseListener());
         this.addNewProjectListener();
         this.dialog.show(this.appWin.getFrame());
      }
   }

   private SearchAndReplaceTarget createTarget() {
      ProjectItemOrder var1 = this.getScriptsToSearch();
      ExpressionTranslator var2 = this.getExpressionTranslator();
      ScriptReplaceStrategy var3 = this.getReplaceStrategy(var2);
      return new ScriptSearchAndReplaceTarget(var1, var2, var3);
   }

   private ProjectItemOrder<Script> getScriptsToSearch() {
      ProjectContentViewer var1 = this.contentViewers.getViewer();
      return var1.getItemOrder(EntityType.Script, Script.class);
   }

   private ExpressionTranslator getExpressionTranslator() {
      IntegrationProject var1 = this.projectManager.getProject();
      ProjectGuidPathCache var2 = FunctionRegistry.getRegistry().buildProjectBuidPathCache(var1);
      return new CachedPathsExpressionTranslator(var2);
   }

   private ScriptReplaceStrategy getReplaceStrategy(ExpressionTranslator var1) {
      return new PersistingScriptReplaceStrategy(this.persistor, var1);
   }

   private TextSearchModel getParameterModel() {
      if (this.parameterModel == null) {
         this.parameterModel = new TextSearchModel();
      }

      return this.parameterModel;
   }

   private void addNewProjectListener() {
      this.newProjectListener = new SearchAndReplaceInScriptsAction.NewProjectListener();
      this.projectManager.addListener(this.newProjectListener);
   }

   private class DialogCloseListener implements Receiver<Void> {
      private DialogCloseListener() {
      }

      public void handle(Void var1) {
         SearchAndReplaceInScriptsAction.this.dialog = null;
         this.removeProjectListener();
      }

      private void removeProjectListener() {
         SearchAndReplaceInScriptsAction.this.projectManager.removeListener(SearchAndReplaceInScriptsAction.this.newProjectListener);
         SearchAndReplaceInScriptsAction.this.newProjectListener = null;
      }
   }

   private class MatchDisplayer extends TextSearchObserver.Adapter {
      private MatchDisplayer() {
      }

      @Override
      public void newMatch(SearchTarget.Match var1) {
         this.displayMatch((ScriptSearchAndReplaceTarget.ScriptMatch)var1);
      }

      @Override
      public void searchWrappedAround(SearchTarget.Match var1) {
         this.displayMatch((ScriptSearchAndReplaceTarget.ScriptMatch)var1);
      }

      private void displayMatch(ScriptSearchAndReplaceTarget.ScriptMatch var1) {
         Script var2 = var1.getScript();
         int var3 = this.getDelay(var2);
         Editor var4 = SearchAndReplaceInScriptsAction.this.pageDisplayer.openPage(var2);
         if (var4 instanceof ScriptPage) {
            this.highlightText((ScriptPage)var4, var1.getTextLocation(), var3);
         }
      }

      private int getDelay(Script var1) {
         Editor var2 = SearchAndReplaceInScriptsAction.this.pageDisplayer.findOpenPage(var1);
         return var2 == null ? 250 : 50;
      }

      private void highlightText(final ScriptPage var1, final Interval var2, int var3) {
         DelayedJob.createAndSchedule(var3, new Runnable() {
            @Override
            public void run() {
               SearchAndReplaceInScriptsAction.this.appWin.getFrame().requestFocus();
               var1.highlightText(var2);
            }
         });
      }
   }

   private class NewProjectListener extends CurrentProjectServiceListener.Adapter {
      private NewProjectListener() {
      }

      @Override
      public void projectOpened(CurrentProjectEvent var1) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               if (SearchAndReplaceInScriptsAction.this.dialog != null) {
                  SearchAndReplaceTarget var1x = SearchAndReplaceInScriptsAction.this.createTarget();
                  SearchAndReplaceInScriptsAction.this.dialog.setTarget(var1x);
               }
            }
         });
      }

      @Override
      public void projectClosed(CurrentProjectEvent var1) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               if (SearchAndReplaceInScriptsAction.this.dialog != null) {
                  SearchAndReplaceInScriptsAction.this.dialog.setTarget(null);
               }
            }
         });
      }
   }

   private class OpenPageReplaceStrategy implements ScriptReplaceStrategy {
      @Override
      public boolean handleReplace(Script var1, String var2) {
         ScriptPage var3 = this.findOpenPage(var1);
         if (var3 == null) {
            return false;
         }

         this.replaceDisplayedExpression(var2, var3);
         return true;
      }

      private ScriptPage findOpenPage(Script var1) {
         Editor var2 = SearchAndReplaceInScriptsAction.this.pageDisplayer.findOpenPage(var1);
         return var2 instanceof ScriptPage ? (ScriptPage)var2 : null;
      }

      private void replaceDisplayedExpression(String var1, ScriptPage var2) {
         ScriptBuilderUi var3 = var2.getScriptBuilder();
         var3.replaceDisplayedExpression(var1);
      }
   }
}
