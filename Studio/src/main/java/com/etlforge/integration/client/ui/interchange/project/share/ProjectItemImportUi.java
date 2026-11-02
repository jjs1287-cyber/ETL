package com.etlforge.integration.client.ui.interchange.project.share;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.overlay.KongaLayeredPane;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.ui.widget.tree.event.KongaTreeAdapter;
import org.etlforge.ui.widget.tree.event.KongaTreeEvent;

public final class ProjectItemImportUi implements UiProvider {
   private final ProjectItemImporter controller;
   private final ShareProjectDataModel model;
   private final ProjectItemImportUi.TopPanel topPanel;
   private final ProjectTrees trees;
   private final ExportEntityAction exportAction;
   private final KongaLayeredPane ui;
   private final JLabel note = new JLabel("Note, this will reference the imported component, not copy.  After import, use 'Copy Deep' if copy is desired.");

   public ProjectItemImportUi(ApplicationWindow var1, ProjectItemImporter var2, ShareProjectDataModel var3) {
      this.controller = var2;
      this.model = var3;
      this.topPanel = new ProjectItemImportUi.TopPanel(var3);
      this.exportAction = new ExportEntityAction(var1, var3, var2.getWorker());
      this.exportAction.setExportProjectSupported(false);
      this.trees = this.createProjectTrees();
      this.ui = new KongaLayeredPane();
      this.bindNoteLabelToAction();
   }

   private ProjectTrees createProjectTrees() {
      ProjectTrees var1 = new ProjectTrees(this.model, new ProjectItemImportUi.TargetTreeDropHandlerImpl());
      var1.setExportAction(this.exportAction);
      var1.setSourceProjectDraggable(false);
      var1.addSourceTreeListener(new ProjectItemImportUi.SourceTreeSelectionListener());
      return var1;
   }

   private void bindNoteLabelToAction() {
      this.exportAction.addPropertyChangeListener(new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            if ("enabled".equals(var1.getPropertyName())) {
               ProjectItemImportUi.this.note.setEnabled(ProjectItemImportUi.this.exportAction.isEnabled());
            }
         }
      });
      this.note.setEnabled(this.exportAction.isEnabled());
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 10);
      var2.north(this.topPanel.layout());
      this.layoutTrees(var2);
      this.layoutButton(var2);
      this.ui.setBaseComponent(var2.container());
      var1.display(this.ui);
   }

   private void layoutTrees(BorderLayoutBuilder var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 3);
      String var3 = "Use drag and drop or the button below to import items from the source project into the target project";
      var2.north(var3).center(this.trees.layout());
      var1.center(var2);
   }

   private void layoutButton(BorderLayoutBuilder var1) {
      BoxBuilder var2 = Horizontal.box();
      var2.addAllWithSpace(10, this.exportAction, this.note);
      var1.south(var2);
   }

   @Override
   public void requestFocus() {
      this.topPanel.browseButton.requestFocusInWindow();
   }

   public void setProjectBrowser(ActionListener var1) {
      this.topPanel.browseButton.addActionListener(var1);
   }

   public WaitService getWaitService() {
      return this.ui;
   }

   private class SourceTreeSelectionListener extends KongaTreeAdapter {
      private SourceTreeSelectionListener() {
      }

      @Override
      public void selectionChanged(KongaTreeEvent var1) {
         if (!var1.getSourceNodes().isEmpty() && ProjectItemImportUi.this.model.getSourceProject() == null) {
            ProjectItemImportUi.this.controller.selectSourceProject();
         }
      }
   }

   private class TargetTreeDropHandlerImpl implements ProjectTrees.TargetTreeDropHandler {
      private TargetTreeDropHandlerImpl() {
      }

      @Override
      public boolean isValidParent(IntegrationEntity var1, IntegrationEntity var2) {
         EntityType var3 = var2.getEntityType();
         return var3 != EntityType.RootFolder && var3 != EntityType.Project ? this.isValidParentImpl(var1, var2) : false;
      }

      private boolean isValidParentImpl(IntegrationEntity var1, IntegrationEntity var2) {
         return var1 instanceof Folder ? ((Folder)var1).isAllowedChild(var2) : var1 instanceof IntegrationProject;
      }

      @Override
      public boolean handleDrop(IntegrationEntity var1, IntegrationEntity var2) {
         if (this.isValidParent(var2, var1)) {
            ProjectItemImportUi.this.controller.importItem(var1, var2);
            return true;
         } else {
            return false;
         }
      }
   }

   private class TopPanel implements PropertyChangeListener {
      private final JTextField sourceProjectField = this.createField("[Select a project to import from...]");
      private final JTextField targetProjectField;
      private final JButton browseButton;

      public TopPanel(ShareProjectDataModel var2) {
         this.targetProjectField = this.createField(var2.getTargetProject().getProject().getName());
         this.browseButton = new KongaButton("Select...");
         this.browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent var1) {
               ProjectItemImportUi.this.controller.selectSourceProject();
            }
         });
         var2.addPropertyChangeListener("sourceProject", this);
         this.prepareEmptySourceField();
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               TopPanel.this.sourceProjectField.setFont(TopPanel.this.targetProjectField.getFont());
               TopPanel.this.sourceProjectField.setText(ProjectItemImportUi.this.model.getSourceProject().getProject().getName());
            }
         });
      }

      public UiProvider layout() {
         NColumnPanel var1 = new NColumnPanel(3);
         var1.setWeights(1, 1.0, 0.0).setFill(1, NColumnPanel.HORIZONTAL);
         var1.addRow(this.getLabel("Import from:"), this.sourceProjectField, this.browseButton);
         var1.addRow(this.getLabel("Destination project:"), this.targetProjectField, null);
         return var1;
      }

      private KongaTextField createField(String var1) {
         KongaTextField var2 = new KongaTextField(var1);
         var2.setEditable(false);
         var2.setBackground(javax.swing.UIManager.getColor("Panel.background"));
         return var2;
      }

      private JLabel getLabel(String var1) {
         return new JLabel(var1, EntityIcons.forType(EntityType.Project).getDefaultIcon(EntityIcons.Size.SMALL), 10);
      }

      private void prepareEmptySourceField() {
         this.sourceProjectField.addMouseListener(new ProjectItemImportUi.TopPanel.SourceFieldActivator());
         this.sourceProjectField.setFont(TextStyles.DefaultItalic.getFont());
      }

      private class SourceFieldActivator extends MouseAdapter {
         private SourceFieldActivator() {
         }

         @Override
         public void mouseClicked(MouseEvent var1) {
            if (ProjectItemImportUi.this.model.getSourceProject() == null) {
               ProjectItemImportUi.this.controller.selectSourceProject();
            }
         }
      }
   }
}

