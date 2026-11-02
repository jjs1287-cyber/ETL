package org.etlforge.integration.client.ui.interchange.component.tree;

import com.google.common.base.Predicate;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.etlforge.application.ui.search.string.StringFilterField;
import org.etlforge.application.ui.search.string.StringSearchRule;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectPathFilter;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionFactory;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.util.preference.KongaPreferences;

public final class MultiTreeNameFilterField implements UiProvider {
   private static final StringSearchRule PATH = new ProjectPathRule();
   private final Receiver<MultiTreeNameFilter> owner;
   private final StringFilterField filterField;
   private IntegrationProject project;

   public MultiTreeNameFilterField(Receiver<MultiTreeNameFilter> var1) {
      this.owner = var1;
      this.filterField = this.createField();
   }

   private StringFilterField createField() {
      List var1 = Arrays.asList(StringSearchRule.CONTAINS_IGNORE_CASE, StringSearchRule.STARTS_WITH_IGNORE_CASE, PATH);
      StringFilterField var2 = new StringFilterField(var1);
      KongaPreferences var3 = KongaPreferences.getPreferences(this.getClass());
      var2.setPreferences(var3);
      var2.setCallback(new Receiver<Predicate<String>>() {
         public void handle(Predicate<String> var1) {
            MultiTreeNameFilterField.this.applyFilter();
         }
      });
      return var2;
   }

   public void setKeyUpHandler(ActionListener var1) {
      this.filterField.addKeyBinding(ActionFactory.forKey(KeyUtils.UP, var1));
   }

   public void setKeyDownHandler(ActionListener var1) {
      this.filterField.addKeyBinding(ActionFactory.forKey(KeyUtils.DOWN, var1));
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.filterField.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.filterField.requestFocus();
   }

   public void addFocusListener(FocusListener var1) {
      this.filterField.addFocusListener(var1);
   }

   public void setProject(final IntegrationProject var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            MultiTreeNameFilterField.this.project = var1;
            if (var1 == null) {
               MultiTreeNameFilterField.this.filterField.setEnabled(false);
               MultiTreeNameFilterField.this.filterField.clear();
            } else {
               MultiTreeNameFilterField.this.filterField.setEnabled(true);
               MultiTreeNameFilterField.this.applyFilter();
            }
         }
      });
   }

   private void applyFilter() {
      MultiTreeNameFilter var1 = this.getFilter();
      this.owner.handle(var1);
   }

   public MultiTreeNameFilter getFilter() {
      Collection var1 = this.collectItems();
      return new MultiTreeNameFilter(var1);
   }

   private Collection<IntegrationEntity> collectItems() {
      if (this.project == null) {
         return null;
      }

      String var1 = this.filterField.getFilterString();
      if (var1.isEmpty()) {
         return null;
      }

      if (this.filterField.getSelectedOption() == PATH) {
         String var2 = this.addPathSeparatorToTypeName(var1);
         ProjectPathFilter var3 = new ProjectPathFilter(var2);
         Set var4 = var3.apply(this.project);
         if (!var4.isEmpty()) {
            return var4;
         }
      }

      Predicate var5 = this.filterField.getFilter();
      EntityFilter var6 = EntityFilters.nameFilter(var5);
      return KongaListUtils.filter(this.project.getAllEntities(), var6);
   }

   private String addPathSeparatorToTypeName(String var1) {
      int var2 = var1.indexOf(47);
      if (var2 == -1) {
         for (EntityType var6 : EntityType.values()) {
            if (var1.equalsIgnoreCase(var6.getPlural())) {
               return var1 + "/";
            }
         }
      }

      return var1;
   }
}
