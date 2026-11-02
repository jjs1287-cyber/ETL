package org.etlforge.application.ui.window.toolbar;

import com.google.common.collect.Lists;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.WindowActionDictionary;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.application.ui.window.editor.EditorServiceAdapter;
import org.etlforge.application.ui.window.editor.EditorServiceEvent;
import org.etlforge.application.ui.window.editor.service.BackwardForwardStack;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.button.KongaButton;

public final class NavigationToolBar extends ToolBar {
   private final NavigationToolBar.ShowEditorStackAction showEditorStackAction;
   private final List<? extends ApplicationAction> actions;
   private final ApplicationWindow appWin;

   public NavigationToolBar(ApplicationWindow var1, List<? extends ApplicationAction> var2) {
      super("toolbar.Navigation");
      this.appWin = var1;
      this.actions = var2;
      this.showEditorStackAction = new NavigationToolBar.ShowEditorStackAction();
      var1.getEditorService().addEditorServiceListener(new EditorServiceAdapter() {
         @Override
         public void editorOpened(EditorServiceEvent var1) {
            NavigationToolBar.this.updateShowEditorStackAction();
         }

         @Override
         public void editorClosed(EditorServiceEvent var1) {
            NavigationToolBar.this.updateShowEditorStackAction();
         }
      });
      this.updateShowEditorStackAction();
   }

   private void updateShowEditorStackAction() {
      EditorService var1 = this.appWin.getEditorService();
      int var2 = var1.getNumberOfEditors();
      this.showEditorStackAction.setEnabled(var2 >= 2);
   }

   @Override
   protected List<ToolBarEntry> getEntries() {
      ArrayList var1 = Lists.newArrayList();

      for (ApplicationAction var3 : this.actions) {
         var1.add(ToolBarEntry.of(var3, WindowActionDictionary.DICTIONARY));
      }

      var1.add(this.createShowEditorStackButton());
      return var1;
   }

   private ToolBarEntry createShowEditorStackButton() {
      final KongaButton var1 = new KongaButton(this.showEditorStackAction);
      ActionKeyBinding.install(var1, 2, KeyUtils.getAltDown(40), new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1x) {
            var1.doClick();
         }
      });
      return ToolBarEntry.of(var1);
   }

   private static class EditorSwitchAction extends AbstractAction {
      private final BackwardForwardStack stack;
      private final int position;

      public EditorSwitchAction(BackwardForwardStack var1, Editor var2, int var3) {
         super(var2.getTitle(), var2.getSmallIcon());
         this.stack = var1;
         this.position = var3;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.stack.moveToPosition(this.position);
      }
   }

   private class ShowEditorStackAction extends AbstractAction {
      public ShowEditorStackAction() {
         super(null, ApplicationResources.ICONS.getIcon("ArrowDown.16"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         Object var2 = var1.getSource();
         if (var2 instanceof Component) {
            final Component var3 = (Component)var2;
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  ShowEditorStackAction.this.openEditorList(var3);
               }
            });
         }
      }

      private void openEditorList(Component var1) {
         JPopupMenu var2 = this.createEditorList();
         if (var2 != null) {
            var2.show(var1, 16, 16);
         }
      }

      private JPopupMenu createEditorList() {
         EditorService var1 = NavigationToolBar.this.appWin.getEditorService();
         BackwardForwardStack var2 = var1.getBackwardForwardStack();
         if (var2.size() == 0) {
            return null;
         }

         JPopupMenu var3 = new JPopupMenu();
         int var4 = var2.getPosition();
         int var5 = Math.max(0, var4 - 9);
         int var6 = Math.min(var2.size(), var4 + 10);

         for (int var7 = var5; var7 < var6; var7++) {
            Editor var8 = var2.getEditorAt(var7);
            JCheckBoxMenuItem var9 = new JCheckBoxMenuItem(new NavigationToolBar.EditorSwitchAction(var2, var8, var7));
            if (var7 == var4) {
               var9.setSelected(true);
            }

            var3.add(var9);
         }

         return var3;
      }
   }
}
