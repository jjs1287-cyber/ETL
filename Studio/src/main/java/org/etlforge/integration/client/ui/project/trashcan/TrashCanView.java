package org.etlforge.integration.client.ui.project.trashcan;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.window.AbstractWindowSectionContent;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.integration.client.ui.project.resources.ProjectUiResources;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.KongaScrollPane;

public final class TrashCanView extends AbstractWindowSectionContent {
   public static final String CONTENT_ID = TrashCanView.class.getName();
   private final TrashCanViewUi ui;
   private JComponent displayer;

   public TrashCanView(WindowSection var1, TrashCanViewUi var2) {
      super(CONTENT_ID, var1);
      this.ui = var2;
   }

   @Override
   public String getTitle() {
      return ProjectUiResources.getString("Trashcan.View.Title");
   }

   @Override
   public Icon getIcon() {
      return ApplicationResources.getIcon("Trash.16");
   }

   @Override
   public void decorate(JComponent var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      final KongaScrollPane var3 = new KongaScrollPane();
      this.ui.decorate(new UiCanvas() {
         @Override
         public void display(Component var1) {
            var3.setViewportView(var1);
         }
      });
      var2.north(this.getTitlePanel()).center(var3);
      this.displayer = var2.container();
      BorderLayoutBuilder.from(var1).center(this.displayer);
   }

   @Override
   protected void populateToolBar(JToolBar var1) {
      var1.add(createToolBarButton(this.ui.getReviveAction()));
      super.populateToolBar(var1);
   }
}
