package org.etlforge.integration.client.ui.interchange.component.tree;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import org.etlforge.application.Application;
import org.etlforge.application.ui.action.popup.PopupMenuManager;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.widget.PopupMenuActionPopulators;
import org.etlforge.application.ui.widget.SimpleTabPane;
import org.etlforge.application.ui.window.event.ActiveObjectProvider;
import org.etlforge.application.ui.window.event.ActiveObjectProviderHelper;
import org.etlforge.application.ui.window.event.ActiveObjectProviderListener;
import org.etlforge.integration.client.ui.entity.CompoundDeployDirtyIndicator;
import org.etlforge.integration.client.ui.entity.EntityPageDisplayer;
import org.etlforge.integration.client.ui.entity.tree.TreeValidationService;
import org.etlforge.integration.client.ui.entity.validation.CompoundValidationStatusIndicator;
import org.etlforge.integration.client.ui.entity.validation.ValidationStatusIndicator;
import org.etlforge.integration.client.ui.interchange.ExpandProjectContentAction;
import org.etlforge.integration.client.ui.interchange.core.action.popup.PopupMenuSites;
import org.etlforge.integration.client.ui.project.ProjectRenamer;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.validation.ValidationModel;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.integration.uidelegate.project.ProjectIcons;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.event.MouseActionListener;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.border.NoTopEtchedBorder;
import org.etlforge.ui.util.border.RoundedBorder;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.button.PopupMenuButton;

final class ProjectSection implements UiProvider, ActiveObjectProvider, ValidationStatusIndicator {
   private static final Color BACKGROUND = Colors.LIGHT_BLUE_BACKGROUND;
   private final MultiTreeContentViewer owner;
   private final Iterable<MultiTreeSection> categories;
   private final ActiveObjectProviderHelper activeObjectHelper;
   private final ProjectSection.ProjectLabel label;
   private final PopupMenuManager popupMenuManager;
   private final ProjectRenamer projectRenamer;
   private IntegrationProject project;
   private boolean indicatingValidationStatus;
   private TreeValidationService validationService;
   private final PopupMenuButton dropDownButton;
   private final ProjectSection.ExpandCollapseAllToggleAction expansionToggleAction;

   public ProjectSection(MultiTreeContentViewer var1, Iterable<MultiTreeSection> var2, PopupMenuManager var3, ProjectRenamer var4, EntityPageDisplayer var5) {
      this.owner = var1;
      this.categories = var2;
      this.popupMenuManager = var3;
      this.projectRenamer = var4;
      this.activeObjectHelper = new ActiveObjectProviderHelper(this);
      this.label = this.createLabel(var5);
      this.expansionToggleAction = new ProjectSection.ExpandCollapseAllToggleAction();
      this.dropDownButton = this.createDropDownButton();
      this.setProject(null);
   }

   private ProjectSection.ProjectLabel createLabel(EntityPageDisplayer var1) {
      ProjectSection.ProjectLabel var2 = new ProjectSection.ProjectLabel(var1);
      var2.setBackground(BACKGROUND);
      var2.setSelectedForeground(org.etlforge.application.ui.look.Colors.TEXT_FOREGROUND);
      var2.setHighlightedBackground(BACKGROUND);
      var2.setBorder(Empty.border(2));
      var2.setSelectedBorder(new RoundedBorder(2, 2, 1, Color.GRAY));
      this.bindLabelActions(var2);
      return var2;
   }

   private void bindLabelActions(ProjectSection.ProjectLabel var1) {
      ActionKeyBinding.install(new ProjectSection.RenameAction(), var1);
      ActionKeyBinding.install(new ProjectSection.KeyDownAction(), var1);
      ActionKeyBinding.install(new ProjectSection.ShowContextMenuAction(), var1);
   }

   private PopupMenuButton createDropDownButton() {
      PopupMenuButton var1 = new PopupMenuButton(
         "", ApplicationResources.ICONS.getIcon("ArrowDown.16"), this.createDropDownActions(), PopupMenuActionPopulators.DEFAULT
      );
      var1.setToolTipText(ApplicationResources.STRINGS.getString("Options"));
      ButtonUtils.configureButtonForToolBar(var1);
      return var1;
   }

   private Action[] createDropDownActions() {
      CompoundDeployDirtyIndicator var1 = new CompoundDeployDirtyIndicator(this.categories);
      var1.setPreference(DeployDirtyIndicatorPreference.INSTANCE);
      return new Action[]{this.createValidationIndicationSwitch(), var1.getSwitch(), null, new ExpandProjectContentAction(this.owner)};
   }

   private Action createValidationIndicationSwitch() {
      ArrayList var1 = Lists.newArrayList();
      var1.add(this);

      for (MultiTreeSection var3 : this.categories) {
         var1.add(var3);
      }

      CompoundValidationStatusIndicator var4 = new CompoundValidationStatusIndicator(var1);
      var4.setPreference(InvalidItemsIndicatorPreference.INSTANCE);
      return var4.getSwitch();
   }

   public void installLabelActions(Action... var1) {
      for (Action var5 : var1) {
         ActionKeyBinding.install(var5, this.label);
      }
   }

   public void setProject(final IntegrationProject var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            if (ProjectSection.this.isActive() && var1 == null) {
               ProjectSection.this.activeObjectHelper.notifyListeners((Object)null);
               ProjectSection.this.label.setSelected(false);
            }

            ProjectSection.this.project = var1;
            ProjectSection.this.label.update();
            ProjectSection.this.label.updateValidationIndicator();
            ProjectSection.this.dropDownButton.setEnabled(var1 != null);
            ProjectSection.this.expansionToggleAction.setEnabled(var1 != null);
         }
      });
   }

   public void setExpansionMode(ExpansionMode var1) {
      this.expansionToggleAction.setMode(var1);
   }

   public void setValidationService(TreeValidationService var1) {
      this.validationService = var1;
   }

   public void renameProject(IntegrationProject var1) {
      this.projectRenamer.renameProject(var1);
   }

   @Override
   public boolean isIndicatingValidationStatus() {
      return this.indicatingValidationStatus;
   }

   @Override
   public void setIndicateValidationStatus(boolean var1) {
      if (var1 != this.indicatingValidationStatus) {
         this.indicatingValidationStatus = var1;
         this.updateValidationIndicator();
      }
   }

   public void updateValidationIndicator() {
      this.label.updateValidationIndicator();
   }

   public void inactivate() {
      this.label.setSelected(false);
   }

   public void setSelected(boolean var1) {
      this.owner.inactivateAllSections();
      this.label.setSelected(var1);
      if (var1) {
         this.label.requestFocusInWindow();
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = BoxBuilder.horizontal().nonOpaque();
      var2.addAllWithSpace(5, this.dropDownButton, KongaButton.forToolbar(this.expansionToggleAction));
      Border var3 = createBorder();
      BorderLayoutBuilder var4 = new BorderLayoutBuilder().withBackground(BACKGROUND).withBorder(var3);
      var4.center(this.label).east(var2).decorate(var1);
   }

   private static Border createBorder() {
      Border var0 = SimpleTabPane.SUPPORTED ? new NoTopEtchedBorder() : new EtchedBorder();
      return new CompoundBorder(var0, Empty.border(3, 5, 3, 5));
   }

   @Override
   public void requestFocus() {
      this.label.requestFocus();
   }

   @Override
   public List<?> getActiveObjects() {
      return this.activeObjectHelper.getActiveObjects();
   }

   @Override
   public String getProviderId() {
      return "interchange.multitree.project";
   }

   @Override
   public boolean isActive() {
      return this.label.isSelected();
   }

   @Override
   public void addActiveObjectListener(ActiveObjectProviderListener var1) {
      this.activeObjectHelper.add(var1);
   }

   @Override
   public void removeActiveObjectListener(ActiveObjectProviderListener var1) {
      this.activeObjectHelper.remove(var1);
   }

   private void showContextMenu(Point var1) {
      if (this.project != null) {
         List var2 = Collections.singletonList(this.project);
         this.activeObjectHelper.notifyListeners(var2);
         JPopupMenu var3 = this.popupMenuManager.getPopupMenu(PopupMenuSites.OTHER, var2);
         if (var3 != null) {
            var3.show(this.label, var1.x, var1.y);
         }
      }
   }

   private class ExpandCollapseAllToggleAction extends AbstractAction {
      private ExpansionMode mode;

      private ExpandCollapseAllToggleAction() {
      }

      public void setMode(ExpansionMode var1) {
         this.mode = var1;
         this.putValue("SmallIcon", var1.getIcon());
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.mode.apply(ProjectSection.this.owner);
      }
   }

   private class KeyDownAction extends AbstractAction {
      public KeyDownAction() {
         this.putValue("AcceleratorKey", KeyUtils.DOWN);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ProjectSection.this.owner.giveFocusToFilterField();
      }
   }

   private class ProjectLabel extends SectionLabel {
      public ProjectLabel(EntityPageDisplayer var2) {
         super(null, ProjectIcons.PROJECT_16, EntityIcons.forType(EntityType.Project).getIcon(EntityIcons.Category.INVALID, EntityIcons.Size.SMALL));
         this.update();
         ToolTipManager.sharedInstance().registerComponent(this);
         this.addMouseListener(new ProjectSection.ProjectLabel.Listener(var2));
      }

      @Override
      public String getToolTipText() {
         return ToolTipStringFactory.getToolTip(ProjectSection.this.project);
      }

      @Override
      protected boolean showItemCount() {
         return false;
      }

      public void update() {
         String var1;
         TextStyle var2;
         if (ProjectSection.this.project == null) {
            var1 = "No project open";
            var2 = TextStyles.DefaultItalic;
         } else {
            var1 = "Project: " + ProjectSection.this.project.getName();
            var2 = TextStyles.DefaultText;
         }

         this.setText(var1);
         var2.makeOver(this);
      }

      public void updateValidationIndicator() {
         ValidationStatus var1 = ValidationStatus.VALID;
         if (ProjectSection.this.isIndicatingValidationStatus() && ProjectSection.this.validationService != null) {
            ValidationModel var2 = ProjectSection.this.validationService.getModel();
            var1 = var2 != null && ProjectSection.this.project != null ? var2.getStatus(ProjectSection.this.project, true) : ValidationStatus.VALID;
         }

         this.setValidationStatus(var1);
      }

      private class Listener extends MouseActionListener {
         private final EntityPageDisplayer pageDisplayer;

         public Listener(EntityPageDisplayer var2) {
            this.pageDisplayer = var2;
         }

         @Override
         protected void onSingleClick(MouseEvent var1) {
            this.makeSelected();
         }

         private void makeSelected() {
            ProjectSection.this.label.setSelected(true);
            ProjectSection.this.owner.inactivateAllSections();
            ProjectSection.this.activeObjectHelper.notifyListeners(ProjectSection.this.project);
         }

         @Override
         protected void onDoubleClick(MouseEvent var1) {
            if (ProjectSection.this.project == null) {
               this.openProject();
            } else {
               this.openProjectPage();
            }
         }

         private void openProject() {
            if (Application.containsBean("action.ic.Open")) {
               Action var1 = Application.getBean("action.ic.Open", Action.class);
               var1.actionPerformed(null);
            }
         }

         private void openProjectPage() {
            this.pageDisplayer.openPage(ProjectSection.this.project);
         }

         @Override
         protected void onPopupTrigger(MouseEvent var1) {
            this.makeSelected();
            ProjectSection.this.showContextMenu(var1.getPoint());
         }

         @Override
         public void mouseEntered(MouseEvent var1) {
            ProjectLabel.this.setHighlighted(true);
         }

         @Override
         public void mouseExited(MouseEvent var1) {
            ProjectLabel.this.setHighlighted(false);
         }
      }
   }

   private class RenameAction extends AbstractAction {
      public RenameAction() {
         this.putValue("AcceleratorKey", KeyUtils.F2);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (ProjectSection.this.project != null) {
            ProjectSection.this.projectRenamer.renameProject(ProjectSection.this.project);
         }
      }
   }

   private class ShowContextMenuAction extends AbstractAction {
      public ShowContextMenuAction() {
         this.putValue("AcceleratorKey", KeyUtils.CONTEXT_MENU);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ProjectSection.this.showContextMenu(new Point(60, 12));
      }
   }
}

