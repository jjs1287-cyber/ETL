package org.etlforge.integration.client.ui.entity.dependencies.graph;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import org.etlforge.application.ui.window.AbstractWindowSectionContent;
import org.etlforge.application.ui.window.DisposableWindowElementResource;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.layout.BorderLayoutBuilder;

public final class DependencyGraphView extends AbstractWindowSectionContent {
   public static final String CONTENT_ID = DependencyGraphView.class.getName();
   private final DependencyGraph graph;
   private final BackAndForthNavigator navigator;

   public DependencyGraphView(WindowSection var1, DependencyGraph var2) {
      super(CONTENT_ID, var1);
      this.graph = var2;
      this.navigator = new BackAndForthNavigator(var2);
      this.addResource(new DisposableWindowElementResource(var2));
      this.addResource(new DisposableWindowElementResource(this.navigator));
   }

   public void showDependenciesFor(IntegrationEntity var1) {
      this.graph.showDependenciesFor(var1);
   }

   @Override
   public String getTitle() {
      return "Dependencies";
   }

   @Override
   public Icon getIcon() {
      return ClientIcons.DEPENDENT_ITEMS_16;
   }

   @Override
   public void decorate(JComponent var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder().north(this.getTitlePanel()).center(this.graph);
      JComponent var3 = var2.container();
      BorderLayoutBuilder.from(var1).center(var3);
   }

   @Override
   protected void populateToolBar(JToolBar var1) {
      var1.add(createToolBarButton(this.navigator.getBackAction()));
      var1.add(createToolBarButton(this.navigator.getForwardAction()));
      var1.addSeparator();
      super.populateToolBar(var1);
   }
}
