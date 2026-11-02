package org.etlforge.integration.client.ui.entity.dependencies.graph;

import com.google.common.base.Function;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.ProjectDependencies;
import org.etlforge.system.Disposable;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.graph2d.DefaultEdgeUiFactory2D;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.GraphController2D;
import org.etlforge.ui.graph2d.NodeUiFactory2D;
import org.etlforge.ui.graph2d.layout.GraphLayout2D;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.util.Fonts;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

public final class DependencyGraph implements UiProvider, Disposable {
   private final Graph2D graph;
   private DependencyGraphModel model;
   private final ProjectDependencies dependencies;
   private boolean includeCaption;

   public DependencyGraph(IntegrationEntity var1, ProjectDependencies var2) {
      this(var1, var2, null);
   }

   public DependencyGraph(IntegrationEntity var1, ProjectDependencies var2, Function<IntegrationEntity, Icon> var3) {
      this.model = new DependencyGraphModel(var1, var2);
      this.graph = new Graph2D(this.model, new DependencyGraph.NodeUiFactoryImpl(var3), new DefaultEdgeUiFactory2D()) {
         @Override
         protected GraphLayout2D createLayout() {
            return new DependencyGraphLayout(this, (DependencyGraphModel)this.getModel());
         }
      };
      this.graph.setMinimumSize(new Dimension(400, 200));
      this.dependencies = var2;
      new DependencyGraph.Controller(this.graph);
   }

   public DependencyGraphModel getModel() {
      return this.model;
   }

   public DependencyGraph withCaption() {
      this.includeCaption = true;
      return this;
   }

   @Override
   public void decorate(UiCanvas var1) {
      if (this.includeCaption) {
         CaptionLayout.of(this.layoutCaption(), this.graph).decorate(var1);
      } else {
         this.graph.decorate(var1);
      }
   }

   private UiProvider layoutCaption() {
      return Vertical.grid(3)
         .of("Double-click on the centered item to open it.", "Double-click on a dependency to view the dependencies for that item.")
         .withBorder(Empty.border(0, 2, 0, 0));
   }

   @Override
   public void requestFocus() {
      this.graph.requestFocus();
   }

   @Override
   public void dispose() {
      this.graph.dispose();
   }

   public void showDependenciesFor(IntegrationEntity var1) {
      this.model = new DependencyGraphModel(var1, this.dependencies);
      this.graph.setModel(this.model);
   }

   public void addPropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.graph.addPropertyChangeListener(var1, var2);
   }

   public void removePropertyChangeListener(String var1, PropertyChangeListener var2) {
      this.graph.removePropertyChangeListener(var1, var2);
   }

   private class Controller extends GraphController2D {
      public Controller(Graph2D var2) {
         super(var2);
         this.setMovingNodesSupported(false);
      }

      @Override
      protected void handleNodeDoubleClick(Object var1) {
         if (var1 instanceof IntegrationEntity) {
            this.handleDoubleClick((IntegrationEntity)var1);
         }
      }

      private void handleDoubleClick(IntegrationEntity var1) {
         if (var1 == DependencyGraph.this.model.getEntity()) {
            UiCompanion.of(var1).openPage();
         } else {
            DependencyGraph.this.showDependenciesFor(var1);
         }
      }

      @Override
      protected void handlePopupMenuRequest(Object var1, Point var2) {
         if (var1 instanceof IntegrationEntity) {
            this.handlePopupMenuRequest((IntegrationEntity)var1, var2);
         }
      }

      private void handlePopupMenuRequest(IntegrationEntity var1, Point var2) {
         KongaPopupMenu var3 = new KongaPopupMenu();
         JMenuItem var4 = var3.add(DependencyGraph.this.new ShowDependenciesAction(var1));
         UiCompanion var5 = UiCompanion.of(var1);
         if (var5.canOpenPage()) {
            Fonts.convertToBold(var4);
            var3.add(DependencyGraph.this.new OpenEditorAction(var5));
         }

         DependencyGraph.this.graph.showContextMenu(var3, var2);
      }
   }

   private static class NodeUiFactoryImpl implements NodeUiFactory2D {
      private final Function<IntegrationEntity, Icon> iconLookup;

      public NodeUiFactoryImpl(Function<IntegrationEntity, Icon> var1) {
         this.iconLookup = var1;
      }

      @Override
      public GraphNodeUi2D createNodeUi(Graph2D var1, Object var2) {
         DependencyGraphModel var3 = (DependencyGraphModel)var1.getModel();
         IntegrationEntity var4 = (IntegrationEntity)var2;
         EntityNodeUi var5 = new EntityNodeUi(var4, var4 == var3.getEntity());
         this.checkIcon(var4, var5);
         return var5;
      }

      private void checkIcon(IntegrationEntity var1, EntityNodeUi var2) {
         if (this.iconLookup != null) {
            Icon var3 = this.iconLookup.apply(var1);
            if (var3 != null) {
               var2.setIcon(var3);
            }
         }
      }
   }

   private class OpenEditorAction extends AbstractAction {
      private final UiCompanion uic;

      public OpenEditorAction(UiCompanion var2) {
         super("Open Editor...");
         this.uic = var2;
         this.putValue("ShortDescription", "Open an editor for the selected item");
         this.setEnabled(var2.canOpenPage());
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               OpenEditorAction.this.uic.openPage();
            }
         });
      }
   }

   private class ShowDependenciesAction extends AbstractAction {
      private final IntegrationEntity entity;

      public ShowDependenciesAction(IntegrationEntity var2) {
         super("Show Dependencies");
         this.entity = var2;
         this.putValue("ShortDescription", "Show the dependencies for the selected item");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         DependencyGraph.this.showDependenciesFor(this.entity);
      }
   }
}
