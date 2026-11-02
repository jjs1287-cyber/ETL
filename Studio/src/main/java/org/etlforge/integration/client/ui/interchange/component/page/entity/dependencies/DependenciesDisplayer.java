package org.etlforge.integration.client.ui.interchange.component.page.entity.dependencies;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityTypeAndNameSorter;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.list.IntegrationEntityListDecorator;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.property.Binding;
import org.etlforge.ui.property.ListBinding;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.label.HtmlLabel;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.list.BoldTextListDecorator;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.util.Property;

final class DependenciesDisplayer implements Disposable, UiProvider {
   private final KongaList list = this.createList();
   private final Binding binding;

   public DependenciesDisplayer(EntityDependenciesModel var1) {
      this.binding = this.createBinding(var1);
      this.binding.syncUi();
   }

   private KongaList createList() {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      var1.setPrototypeValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
      var1.setLabel(KongaLabel.withMnemonic(Strings.get("EntityPage.Dependencies.Displayer.UsedBy")));
      return var1.newList();
   }

   private Binding createBinding(EntityDependenciesModel var1) {
      Property var2 = var1.getDependants();
      ListBinding var3 = new ListBinding(var2, this.list);
      var3.setListOrder(new EntityTypeAndNameSorter());
      var3.setListItemFunction(new DependenciesDisplayer.EntityListFunction());
      return var3;
   }

   @Override
   public void dispose() {
      this.binding.dispose();
   }

   public void setOpenPageAction(SelectionAction var1) {
      this.list.setDefaultAction(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      new BorderLayoutBuilder(0, 10).north(this.layoutCaption()).center(this.list).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.list.requestFocus();
   }

   private UiProvider layoutCaption() {
      KongaLabel var1 = TextStyles.InfoTextHeader.makeLabel(Strings.get("EntityPage.Dependencies.Displayer.Title"));
      var1.setIcon(ClientIcons.DEPENDENT_ITEMS_16);
      String var2 = Strings.get("EntityPage.Dependencies.Displayer.Caption");
      HtmlLabel var3 = new HtmlLabel(var2, 300);
      return CaptionLayout.of(var1, var3).withBackground(javax.swing.UIManager.getColor("Panel.background")).withBorder(new BorderBuilder().line().empty(5));
   }

   private static class EntityListFunction implements ListBinding.ListFunction<IntegrationEntity> {
      private EntityType lastType;

      private EntityListFunction() {
      }

      @Override
      public void prepare() {
         this.lastType = null;
      }

      public Object toListItem(IntegrationEntity var1) {
         IntegrationEntityListDecorator var2 = new IntegrationEntityListDecorator(var1);
         EntityType var3 = var1.getEntityType();
         if (var3 != this.lastType) {
            this.lastType = var3;
            return this.appendHeader(var2, var3);
         } else {
            return var2;
         }
      }

      private Object appendHeader(Object var1, EntityType var2) {
         Object var3 = this.getHeader(var2);
         return Arrays.asList(var3, var1);
      }

      private Object getHeader(EntityType var1) {
         Font var2 = TextStyles.DefaultListText.getFont();
         String var3 = var1.getPlural() + ":";
         return new BoldTextListDecorator(var3, var2);
      }
   }
}

