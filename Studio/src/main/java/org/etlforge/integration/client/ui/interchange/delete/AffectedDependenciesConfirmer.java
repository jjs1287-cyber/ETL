package org.etlforge.integration.client.ui.interchange.delete;

import com.google.common.base.Supplier;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListModel;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityTypeAndNameSorter;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.uidelegate.entity.list.IntegrationEntityListDecorator;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.graphics.IconRow;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.decorator.DefaultListDecorRenderer;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.list.BoldTextListDecorator;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;

public final class AffectedDependenciesConfirmer {
   private final Dependencies dependencies;

   public AffectedDependenciesConfirmer(Dependencies var1) {
      this.dependencies = var1;
   }

   public boolean isGreenLighted(EntityFilter var1) {
      return this.dependencies.isEmpty(var1) ? true : UiThreadSupplier.call(new Supplier<Boolean>() {
         public Boolean get() {
            AffectedDependenciesConfirmer.Question var1x = AffectedDependenciesConfirmer.this.new Question();
            Answer var2 = var1x.ask();
            return var2.isYes();
         }
      });
   }

   private static class EntityListItem extends IntegrationEntityListDecorator {
      private static final Icon DELETE_ICON = CommonIcons.DELETE_16;
      private static final Icon EMPTY_ICON = Empty.icon(DELETE_ICON.getIconWidth(), DELETE_ICON.getIconHeight());
      private final boolean willBeDeleted;

      public EntityListItem(IntegrationEntity var1, boolean var2) {
         super(var1);
         this.willBeDeleted = var2;
      }

      @Override
      protected Icon getIcon() {
         Icon var1 = this.willBeDeleted ? DELETE_ICON : EMPTY_ICON;
         Icon var2 = super.getIcon();
         IconRow var3 = new IconRow(var1, var2);
         var3.setIconGap(5);
         return var3;
      }

      @Override
      public Color getTextColor() {
         return this.willBeDeleted ? Color.RED : super.getTextColor();
      }
   }

   private static class ListRenderer extends DefaultListDecorRenderer {
      private ListRenderer() {
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         if (var2 instanceof AffectedDependenciesConfirmer.EntityListItem) {
            this.setBorder(Empty.border(0, 5, 0, 0));
         }

         return this;
      }
   }

   private class Question implements UiProvider {
      private final BorderLayoutBuilder layout = new BorderLayoutBuilder(0, 5);
      private boolean atLeastOneDependantWillBeDeleted;

      public Question() {
         KongaList var2 = this.createList();
         this.layout.center(var2);
         this.addCaption();
         this.addQuestion();
      }

      private KongaList createList() {
         ListModel var1 = this.getListModel();
         KongaListFactory var2 = ComponentFactories.newKongaListFactory();
         var2.setCellRenderer(new AffectedDependenciesConfirmer.ListRenderer());
         var2.setPrototypeValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
         return var2.newList(var1);
      }

      private ListModel getListModel() {
         DefaultListModel var1 = new DefaultListModel();
         Font var2 = TextStyles.DefaultListText.getFont();
         EntityType var3 = null;
         boolean var4 = true;

         for (IntegrationEntity var6 : this.getEntitiesToDisplay()) {
            if (var6.getEntityType() != var3) {
               if (!var4) {
                  var1.addElement(" ");
               }

               var3 = var6.getEntityType();
               String var7 = var3.getPlural() + ":";
               var1.addElement(new BoldTextListDecorator(var7, var2));
            }

            boolean var8 = AffectedDependenciesConfirmer.this.dependencies.willBeDeleted(var6);
            var1.addElement(new AffectedDependenciesConfirmer.EntityListItem(var6, var8));
            if (var8) {
               this.atLeastOneDependantWillBeDeleted = true;
            }

            var4 = false;
         }

         return var1;
      }

      private Collection<IntegrationEntity> getEntitiesToDisplay() {
         List var1 = AffectedDependenciesConfirmer.this.dependencies.getDependantObjects();
         Collections.sort(var1, new EntityTypeAndNameSorter());
         return new LinkedHashSet<>(var1);
      }

      private void addCaption() {
         JLabel var1 = new JLabel("The following items will be affected, since they use one or more of the deleted items.");
         if (this.atLeastOneDependantWillBeDeleted) {
            KongaLabel var2 = TextStyles.DefaultBold.makeLabel("Items in RED will also be deleted.");
            var2.setForeground(Color.RED);
            OneColumnPanel var3 = new OneColumnPanel().withInsets(0, 0, 5, 0).of(var1, var2);
            this.layout.north(var3);
         } else {
            this.layout.north(var1);
         }
      }

      private void addQuestion() {
         this.layout.south("Do you want to continue?");
      }

      @Override
      public void decorate(UiCanvas var1) {
         this.layout.decorate(var1);
      }

      @Override
      public void requestFocus() {
      }

      public Answer ask() {
         return Ask.yesOrNo(this, "Dependent Objects");
      }
   }
}
