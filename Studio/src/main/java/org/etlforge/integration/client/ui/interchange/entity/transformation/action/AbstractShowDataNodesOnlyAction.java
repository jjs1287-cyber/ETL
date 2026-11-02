package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.MappingTreeDataService;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.MappingTreeDataServiceEDTListener;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.MappingTreeDataServiceListener;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.ui.action.ToggleAction;

abstract class AbstractShowDataNodesOnlyAction extends AbstractApplicationAction implements ToggleAction {
   protected final TransformationPage page;
   private final MappingTreeDataServiceListener dataServiceListener;
   private PropertyChangeListener treeListener;
   private boolean internalChange;

   public AbstractShowDataNodesOnlyAction(String var1, TransformationPage var2) {
      super(var1, TransformationActionDictionary.DICTIONARY);
      this.page = var2;
      this.dataServiceListener = new MappingTreeDataServiceEDTListener(this.createDataServiceListener());
      this.putValue("ToggleAction#ON_OFF", Boolean.FALSE);
      this.addPageLoadingJob(var2);
   }

   private void addPageLoadingJob(TransformationPage var1) {
      var1.addPostPageLoadingJob(new Runnable() {
         @Override
         public void run() {
            AbstractShowDataNodesOnlyAction.this.listenToDataService();
            AbstractShowDataNodesOnlyAction.this.listenToTree();
            AbstractShowDataNodesOnlyAction.this.updateSelectedState();
            boolean var1x = AbstractShowDataNodesOnlyAction.this.isAvailable();
            AbstractShowDataNodesOnlyAction.this.setEnabled(var1x);
         }
      });
   }

   protected abstract MappingTreeDataServiceListener createDataServiceListener();

   private void listenToDataService() {
      MappingTreeDataService var1 = this.page.getTreeDataService();
      var1.addListener(this.dataServiceListener);
   }

   private void listenToTree() {
      this.treeListener = new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            assert "showDataNodesOnly".equals(var1.getPropertyName());
            if (!AbstractShowDataNodesOnlyAction.this.internalChange) {
               AbstractShowDataNodesOnlyAction.this.setSelected((Boolean)var1.getNewValue());
            }
         }
      };
      TreeComponent var1 = this.getTreeComponent(this.page.getTreeMapper());
      var1.addPropertyChangeListener("showDataNodesOnly", this.treeListener);
   }

   protected abstract TreeComponent getTreeComponent(TreeMapper var1);

   private void updateSelectedState() {
      TreeComponent var1 = this.getTreeComponent(this.page.getTreeMapper());
      boolean var2 = var1.isShowDataNodesOnly();
      this.setSelected(var2);
   }

   protected abstract boolean isAvailable();

   @Override
   public final void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            AbstractShowDataNodesOnlyAction.this.toggle();
         }
      });
   }

   private void toggle() {
      boolean var1 = this.isSelected();
      boolean var2 = !var1;
      TreeComponent var3 = this.getTreeComponent(this.page.getTreeMapper());
      this.internalChange = true;
      var3.setShowDataNodesOnly(var2);
      this.internalChange = false;
      this.setSelected(var2);
   }

   @Override
   public final void setEnabled(boolean var1) {
      super.setEnabled(var1 && this.isAvailable());
   }

   @Override
   public final boolean isSelected() {
      Object var1 = this.getValue("ToggleAction#ON_OFF");
      return var1 instanceof Boolean ? (Boolean)var1 : false;
   }

   @Override
   public final void setSelected(boolean var1) {
      this.putValue("ToggleAction#ON_OFF", var1);
   }

   @Override
   public final void dispose() {
      this.removeDataServiceListener();
      this.removeTreeListener();
   }

   private void removeDataServiceListener() {
      MappingTreeDataService var1 = this.page.getTreeDataService();
      var1.removeListener(this.dataServiceListener);
   }

   private void removeTreeListener() {
      if (this.treeListener != null) {
         TreeComponent var1 = this.getTreeComponent(this.page.getTreeMapper());
         var1.removePropertyChangeListener("showDataNodesOnly", this.treeListener);
      }
   }
}
