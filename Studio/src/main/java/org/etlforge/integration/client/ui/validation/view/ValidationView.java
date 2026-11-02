package org.etlforge.integration.client.ui.validation.view;

import com.google.common.collect.Lists;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.window.AbstractWindowSectionContent;
import org.etlforge.application.ui.window.ActiveObjectProviderResource;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.DisposableWindowElementResource;
import org.etlforge.application.ui.window.WindowElementState;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.event.ActiveObjectProvider;
import org.etlforge.application.ui.window.event.ActiveObjectProviderHelper;
import org.etlforge.application.ui.window.event.ActiveObjectProviderListener;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.validation.resources.ValidationUiResources;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.validation.ValidationModel;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;

public final class ValidationView extends AbstractWindowSectionContent implements ActiveObjectProvider {
   public static final String CONTENT_ID = ValidationView.class.getName();
   private final ValidationDisplayer displayer;
   private final ActiveObjectProviderHelper activeObjectHelper = new ActiveObjectProviderHelper(this);

   public ValidationView(WindowSection var1, ValidationModel var2) {
      super(CONTENT_ID, var1);
      this.displayer = new ValidationDisplayer(var2);
      this.addResource(new DisposableWindowElementResource(this.displayer));
   }

   @Override
   public String getTitle() {
      return ValidationUiResources.getString("View.Title");
   }

   @Override
   public Icon getIcon() {
      return ClientIcons.VALIDATE_16;
   }

   @Override
   public String getProviderId() {
      return CONTENT_ID;
   }

   @Override
   public boolean isActive() {
      return this.getState().contains(WindowElementState.ACTIVE);
   }

   @Override
   protected void activated() {
      super.activated();
      this.fireActiveObjectChange();
   }

   public void setValidationModel(ValidationModel var1) {
      this.displayer.setModel(var1);
   }

   @Override
   public void decorate(JComponent var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.north(this.getTitlePanel()).center(this.displayer);
      BorderLayoutBuilder.from(var1).center(var2.container());
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.displayer.addSelectionListener(new SelectionListener() {
         @Override
         public void selectionChanged(SelectionChangedEvent var1) {
            ValidationView.this.fireActiveObjectChange();
         }
      });
      this.addResource(new ActiveObjectProviderResource(var1.getActiveObjectService(), this));
   }

   @Override
   protected void populateToolBar(JToolBar var1) {
      this.addExpandButton(var1);
      this.addCollapseButton(var1);
      super.populateToolBar(var1);
   }

   private void addExpandButton(JToolBar var1) {
      JButton var2 = createToolBarButton(ApplicationResources.ICONS.getIcon("ExpandTree.16"), ValidationUiResources.getString("View.ToolTip.ExpandTree"));
      var2.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            ValidationView.this.displayer.expandTree();
         }
      });
      var1.add(var2);
   }

   private void addCollapseButton(JToolBar var1) {
      JButton var2 = createToolBarButton(ApplicationResources.ICONS.getIcon("CollapseTree.16"), ValidationUiResources.getString("View.ToolTip.CollapseTree"));
      var2.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            ValidationView.this.displayer.collapseTree();
         }
      });
      var1.add(var2);
   }

   @Override
   public void addActiveObjectListener(ActiveObjectProviderListener var1) {
      this.activeObjectHelper.add(var1);
   }

   @Override
   public List<?> getActiveObjects() {
      IntegrationEntity var1 = this.displayer.getSelectedEntity();
      return var1 != null ? Lists.newArrayList(var1) : new ArrayList();
   }

   @Override
   public void removeActiveObjectListener(ActiveObjectProviderListener var1) {
      this.activeObjectHelper.remove(var1);
   }

   private void fireActiveObjectChange() {
      this.activeObjectHelper.notifyListeners(this.getActiveObjects());
   }
}
