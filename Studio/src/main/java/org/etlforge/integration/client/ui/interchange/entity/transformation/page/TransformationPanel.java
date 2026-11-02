package org.etlforge.integration.client.ui.interchange.entity.transformation.page;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.Action;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoableEdit;
import org.etlforge.integration.client.mapping.MappingStructureFactory;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaDisplayer;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.grammar.CheckingException;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.focus.StaticFocusTraversalPolicy;

public final class TransformationPanel extends IntegrationEntityPanel {
   private final TransformationPage page;
   private final InvalidMappingsIndicator invalidMappingsIndicator;
   private final TreeMapper treeMapper;
   private final TreeMapperEditFactory editFactory;

   public TransformationPanel(TransformationPage var1, MappingStructureFactory var2, FormulaDisplayer var3, Action var4) {
      super(var1.getWindow(), var1.getObject());
      this.page = var1;
      this.editFactory = new TreeMapperEditFactory(this);
      this.treeMapper = this.createTreeMapper(var1.getObject(), var2, var3);
      this.invalidMappingsIndicator = new InvalidMappingsIndicator(this.treeMapper);
      this.setSendUndoEvents(false);
      this.setSendInputChangeEvents(false);
   }

   @Override
   public void dispose() {
      this.treeMapper.dispose();
      super.dispose();
   }

   public TreeMapper getTreeMapper() {
      return this.treeMapper;
   }

   public TransformationPage getPage() {
      return this.page;
   }

   public Transformation getDisplayedObject() {
      return (Transformation)super.getDisplayedObject();
   }

   @Override
   protected void applyOtherPropertiesThanTheName() {
      this.treeMapper.saveMaps();
      this.treeMapper.setDirty(false);
      this.lookForMissingRequiredMappingsAndMappingConflicts();
   }

   private void lookForMissingRequiredMappingsAndMappingConflicts() {
      try {
         this.treeMapper.lookForMissingRequiredMappingsAndMappingConflicts();
      } catch (CheckingException var2) {
      }
   }

   public void treeMapperLoaded() {
      this.editFactory.setTreeMapper(this.treeMapper);
      this.lookForMissingRequiredMappingsAndMappingConflicts();
      this.applyFloatingWindowHack();
      this.setSendUndoEvents(true);
      this.setSendInputChangeEvents(true);
      this.page.pageIsLoaded();
   }

   public void undoableEditHappened() {
      if (this.isSendingUndoEvents()) {
         UndoableEdit var1 = this.editFactory.getEdit();
         if (var1 != null) {
            this.fireUndoableEditEvent(new UndoableEditEvent(this, var1));
         }
      }
   }

   public void inputChanged() {
      if (this.isSendingInputChangeEvents()) {
         this.fireInputChanged(this, "TreeMapper");
      }
   }

   public void startWait() {
      this.getUi().setCursor(Cursor.getPredefinedCursor(3));
   }

   public void stopWait() {
      this.getUi().setCursor(Cursor.getDefaultCursor());
   }

   private TreeMapper createTreeMapper(Transformation var1, MappingStructureFactory var2, FormulaDisplayer var3) {
      TreeMapperContainerImpl var4 = new TreeMapperContainerImpl(this, var2, var3);
      TreeMapper var5 = new TreeMapper(var1, var4);
      var5.setSize(700, 800);
      var5.initComponentSize();
      return var5;
   }

   @Override
   public void display() {
      this.layoutComponents();
      super.display();
   }

   private void layoutComponents() {
      this.setLayout(new BorderLayout());
      this.layoutNorthPanel();
      this.layoutTreeMapper();
      UiUtils.setOpaqueRecursivelySkipDefaults(this.getUi(), false);
      this.getUi()
         .setFocusTraversalPolicy(
            new StaticFocusTraversalPolicy(
               new Component[]{this.getNameField().getInputComponent(), this.treeMapper.m_sourceTreeComponent, this.treeMapper.m_targetTreeComponent}
            )
         );
      this.getUi().setFocusCycleRoot(true);
   }

   private void layoutNorthPanel() {
      Vertical.box()
         .leftAligned()
         .addAllWithSpace(10, this.getNameFieldLayout(), this.invalidMappingsIndicator)
         .deeplyNonOpaque()
         .addToParent(this.getUi(), "North");
   }

   private void layoutTreeMapper() {
      this.add(this.treeMapper, "Center");
   }

   private void applyFloatingWindowHack() {
      DelayedJob.createAndSchedule(250, new Runnable() {
         @Override
         public void run() {
            JInternalFrame var1 = (JInternalFrame)SwingUtilities.getAncestorOfClass(JInternalFrame.class, TransformationPanel.this.getUi());
            if (var1 != null) {
               Dimension var2 = var1.getSize();
               var2.width++;
               var1.setSize(var2);
               var1.revalidate();
               var1.repaint();
            }
         }
      });
   }
}
