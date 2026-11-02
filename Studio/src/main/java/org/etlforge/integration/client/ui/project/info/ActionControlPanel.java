package org.etlforge.integration.client.ui.project.info;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.integration.client.ui.project.info.resources.ProjectInfoUiResources;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.system.EnumWithIdLookup;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.CardPanel;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.CaptionTitlePanel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class ActionControlPanel implements UiProvider {
   private final Action downloadAction;
   private final Multimap<ProjectConsoleAction.Category, Action> consoleActions;

   public ActionControlPanel(Action var1, Iterable<ProjectConsoleAction> var2) {
      this.downloadAction = var1;
      this.consoleActions = ArrayListMultimap.create();

      for (ProjectConsoleAction var4 : var2) {
         try {
            ProjectConsoleAction.Category var5 = EnumWithIdLookup.lookup(ProjectConsoleAction.Category.class, var4.getCategory());
            this.consoleActions.put(var5, var4.getAction());
         } catch (RuntimeException var6) {
            ErrorLog.log(ActionControlPanel.class, "Invalid action category in the Project Console configuration: " + var4.getCategory(), var6);
         }
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      int var2 = this.consoleActions.keySet().size();
      if (var2 == 0) {
         new ActionControlPanel.NoCategoryLayout().decorate(var1);
      } else if (var2 == 1) {
         ProjectConsoleAction.Category var3 = Lists.newArrayList(this.consoleActions.keySet()).get(0);
         new ActionControlPanel.OneCategoryLayout(var3).decorate(var1);
      } else {
         new ActionControlPanel.MultiCategoryToggleLayout().decorate(var1);
      }
   }

   @Override
   public void requestFocus() {
   }

   private class MultiCategoryToggleLayout implements ActionListener {
      private final List<JToggleButton> activators = Lists.newArrayList();
      private final CardPanel cards = new CardPanel();

      public MultiCategoryToggleLayout() {
         ArrayList var2 = Lists.newArrayList(ActionControlPanel.this.consoleActions.keySet());
         Collections.sort(var2);

         for (Object __etlItem77 : var2) {
            ProjectConsoleAction.Category var4 = (ProjectConsoleAction.Category) __etlItem77;
            JToggleButton var5 = this.createActivator(var4);
            this.cards.add(this.createCategoryCard(var4), var4.getId());
            this.activators.add(var5);
         }

         ButtonUtils.makeMutuallyExclusive(this.activators);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.cards.show(var1.getActionCommand());
      }

      public void decorate(UiCanvas var1) {
         this.activators.get(0).doClick();
         JComponent var2 = this.doLayout();
         var1.display(var2);
      }

      private JComponent doLayout() {
         OneColumnPanel var1 = new OneColumnPanel();
         var1.withInsets(0, 0, 10, 0);
         var1.setWeightX(1.0);
         var1.add(InvisiblePanel.of(ActionControlPanel.this.downloadAction));
         JLabel var2 = this.caption("ProjectConsole.Activator.Caption");
         var1.add(new CaptionTitlePanel(var2, 20));
         Grid var3 = Grid.oneRow(10).of(this.activators).withBorder(Empty.border(0, 0, 8, 0));
         var1.add(var3);
         var1.add(this.caption("ProjectConsole.Actions"));
         var1.add(this.cards);
         return var1.invisible();
      }

      private JLabel caption(String var1) {
         return WizardStyleSheet.SMALL_CAPTION_STYLE.makeLabel(ProjectInfoUiResources.getString(var1));
      }

      private JToggleButton createActivator(ProjectConsoleAction.Category var1) {
         JToggleButton var2 = new JToggleButton(this.getActivatorText(var1), this.getIcon(var1));
         var2.setHorizontalTextPosition(0);
         var2.setVerticalTextPosition(3);
         TextStyles.DefaultTextSmall.makeOver(var2);
         var2.setActionCommand(var1.getId());
         var2.addActionListener(this);
         return var2;
      }

      private JComponent createCategoryCard(ProjectConsoleAction.Category var1) {
         Grid var2 = Grid.oneColumn(10);

         for (Action var4 : ActionControlPanel.this.consoleActions.get(var1)) {
            KongaButton var5 = new KongaButton(var4);
            var5.setHorizontalAlignment(10);
            var5.setUseMediumActionIcon(true);
            var2.add(var5);
         }

         return InvisiblePanel.of(var2);
      }

      private String getActivatorText(ProjectConsoleAction.Category var1) {
         return ProjectInfoUiResources.getString("ProjectConsole.Activator." + var1.name());
      }

      private Icon getIcon(ProjectConsoleAction.Category var1) {
         EntityType var2;
         switch (var1) {
            case PROJECT:
               var2 = EntityType.Project;
               break;
            case OPERATION:
               var2 = EntityType.Operation;
               break;
            case SCHEDULE:
               var2 = EntityType.Schedule;
               break;
            default:
               throw new RuntimeException("Unexpected category: " + var1);
         }

         return EntityIcons.forType(var2).getIcon(EntityIcons.Category.VALID, EntityIcons.Size.LARGE);
      }
   }

   private class NoCategoryLayout {
      private final JComponent ui = InvisiblePanel.of(ActionControlPanel.this.downloadAction);

      public NoCategoryLayout() {
      }

      public void decorate(UiCanvas var1) {
         var1.display(this.ui);
      }
   }

   private class OneCategoryLayout {
      private final Grid grid = Grid.oneColumn(10);

      public OneCategoryLayout(ProjectConsoleAction.Category var2) {
         this.grid.add(ActionControlPanel.this.downloadAction);

         for (Action var4 : ActionControlPanel.this.consoleActions.get(var2)) {
            this.grid.addAll(var4);
         }

         this.grid.deeplyNonOpaque();
      }

      public void decorate(UiCanvas var1) {
         var1.display(InvisiblePanel.of(this.grid));
      }
   }
}
