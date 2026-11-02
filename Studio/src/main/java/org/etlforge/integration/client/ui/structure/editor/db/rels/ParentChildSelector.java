package org.etlforge.integration.client.ui.structure.editor.db.rels;

import com.google.common.base.Predicate;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import org.etlforge.integration.data.structure.database.TableRelationship;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.DatabaseTable;
import org.etlforge.ui.Enablable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.widget.combobox.FilteredComboNavigationAction;
import org.etlforge.ui.widget.combobox.KongaComboBox;

final class ParentChildSelector implements UiProvider {
   private final JoinChecker validator;
   private final JComboBox parentSelector;
   private final JComboBox childSelector;
   private Enablable enabledStateTracker;

   public ParentChildSelector(DatabaseObject[] var1, JoinChecker var2) {
      this.validator = var2;
      this.parentSelector = new KongaComboBox(var1);
      this.parentSelector.setRenderer(new ParentChildSelector.ParentRenderer());
      this.childSelector = new KongaComboBox(var1);
      this.childSelector.setRenderer(new ParentChildSelector.ChildRenderer());
      this.parentSelector.addActionListener(new ParentChildSelector.ParentListener());
      this.childSelector.addActionListener(new ParentChildSelector.ChildListener());
      this.parentSelector.setSelectedIndex(-1);
      this.childSelector.setSelectedIndex(-1);
      FilteredComboNavigationAction.installAll(this.childSelector, new ParentChildSelector.ChildNavigationFilter());
   }

   @Override
   public void decorate(UiCanvas var1) {
      Grid var2 = Grid.oneRow(20);
      var2.addAll(this.parentSelector, this.childSelector);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.parentSelector.requestFocusInWindow();
   }

   public void setEnabledStateTracker(Enablable var1) {
      this.enabledStateTracker = var1;
   }

   public void repaint() {
      this.parentSelector.repaint();
      this.childSelector.repaint();
   }

   public TableRelationship getSelectedRelationship() {
      DatabaseObject var1 = this.getSelectedParent();
      DatabaseObject var2 = this.getSelectedChild();
      return this.isValidRelation(var1, var2) ? new TableRelationship(var1, var2) : null;
   }

   private boolean isValidRelation(DatabaseObject var1, DatabaseObject var2) {
      return var1 != null && var2 != null && this.validator.isValidJoin(var1, var2);
   }

   private DatabaseObject getSelectedParent() {
      return (DatabaseObject)this.parentSelector.getSelectedItem();
   }

   private DatabaseObject getSelectedChild() {
      return (DatabaseObject)this.childSelector.getSelectedItem();
   }

   private void updateEnabledStateTracker() {
      if (this.enabledStateTracker != null) {
         this.enabledStateTracker.setEnabled(this.hasSelection(this.parentSelector) && this.hasSelection(this.childSelector));
      }
   }

   private boolean hasSelection(JComboBox var1) {
      return var1.getSelectedIndex() >= 0;
   }

   private class ChildListener implements ActionListener {
      private ChildListener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.validateSelection();
         ParentChildSelector.this.updateEnabledStateTracker();
      }

      private void validateSelection() {
         DatabaseObject var1 = ParentChildSelector.this.getSelectedChild();
         if (var1 != null) {
            DatabaseObject var2 = ParentChildSelector.this.getSelectedParent();
            boolean var3 = ParentChildSelector.this.validator.isValidJoin(var2, var1);
            if (!var3) {
               ParentChildSelector.this.childSelector.setSelectedIndex(-1);
            }
         }
      }
   }

   private class ChildNavigationFilter implements Predicate<Integer> {
      private ChildNavigationFilter() {
      }

      public boolean apply(Integer var1) {
         Object var2 = ParentChildSelector.this.childSelector.getItemAt(var1);
         return var2 instanceof DatabaseTable
            ? ParentChildSelector.this.validator.canBeJoinedToParent(ParentChildSelector.this.getSelectedParent(), (DatabaseTable)var2)
            : false;
      }
   }

   private class ChildRenderer extends DefaultListCellRenderer {
      private ChildRenderer() {
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         if (var2 == null) {
            this.setText("Select Child Table");
         } else {
            DatabaseObject var6 = (DatabaseObject)var2;
            this.setEnabled(this.isEnabledFor(var6));
         }

         return this;
      }

      private boolean isEnabledFor(DatabaseObject var1) {
         return ParentChildSelector.this.validator.canBeJoinedToParent(ParentChildSelector.this.getSelectedParent(), var1);
      }
   }

   private class ParentListener implements ActionListener {
      private ParentListener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ParentChildSelector.this.updateEnabledStateTracker();
         ParentChildSelector.this.childSelector.repaint();
      }
   }

   private static class ParentRenderer extends DefaultListCellRenderer {
      private ParentRenderer() {
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         if (var2 == null) {
            this.setText("Select Parent Table");
         }

         return this;
      }
   }
}
