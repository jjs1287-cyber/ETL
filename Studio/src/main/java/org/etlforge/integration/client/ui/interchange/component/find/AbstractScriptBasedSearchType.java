package org.etlforge.integration.client.ui.interchange.component.find;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import java.lang.reflect.Constructor;
import java.util.Collection;
import org.etlforge.integration.client.ui.entity.search.AbstractSearchType;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.script.CachedPathsExpressionTranslator;
import org.etlforge.integration.data.entity.script.ExpressionTranslator;
import org.etlforge.integration.data.entity.script.GuidPathCache;
import org.etlforge.integration.data.entity.script.GuidPathCacheListener;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.script.FunctionRegistry;
import org.etlforge.integration.data.script.ScriptConstants;

abstract class AbstractScriptBasedSearchType extends AbstractSearchType {
   private ExpressionTranslator translator;

   public AbstractScriptBasedSearchType(String var1, EntityType var2) {
      super(var1, EntityFilters.allOfType(var2), true);
   }

   @Override
   public final void setProject(IntegrationProject var1) {
      this.translator = getTranslator(var1);
   }

   private static ExpressionTranslator getTranslator(IntegrationProject var0) {
      if (var0 == null) {
         return null;
      }

      AbstractScriptBasedSearchType.GuidPathCacheImpl var1 = new AbstractScriptBasedSearchType.GuidPathCacheImpl(var0);
      return new CachedPathsExpressionTranslator(var1);
   }

   @Override
   public final Iterable<String> getStringsToSearch(IntegrationEntity var1) {
      Collection var2 = this.getRawStrings(var1);
      return this.translator == null ? var2 : Iterables.transform(var2, new AbstractScriptBasedSearchType.TranslatingFunction(this.translator));
   }

   protected abstract Collection<String> getRawStrings(IntegrationEntity var1);

   private static class GuidPathCacheImpl implements GuidPathCache {
      private final FunctionRegistry registry = FunctionRegistry.getRegistry();
      private final IntegrationEntityLookup itemLookup;

      public GuidPathCacheImpl(IntegrationProject var1) {
         this.itemLookup = var1.getItemLookup();
      }

      @Override
      public String getPathFromGuid(String var1) {
         int var2 = var1.indexOf(46);
         if (var2 == -1) {
            return null;
         }

         String var3 = var1.substring(0, var2);
         Class var4 = this.registry.getProjectItemIdClassForPrefix(var3).orNull();
         if (var4 == null) {
            return null;
         }

         String var5 = var1.substring(var2 + 1);
         IntegrationEntity var6 = this.findEntity(var5, var4);
         return var6 == null ? null : ScriptConstants.getProjectItemPath(var6);
      }

      private IntegrationEntity findEntity(String var1, Class<? extends IntegrationEntityId> var2) {
         try {
            Constructor var3 = var2.getConstructor(String.class);
            IntegrationEntityId var4 = (IntegrationEntityId)var3.newInstance(var1);
            return this.itemLookup.getEntity(var4);
         } catch (Exception var6) {
            var6.printStackTrace();
            return null;
         }
      }

      @Override
      public String getGuid(String var1) {
         throw new UnsupportedOperationException();
      }

      @Override
      public void addGuidPathCacheListener(GuidPathCacheListener var1) {
         throw new UnsupportedOperationException();
      }

      @Override
      public void removeGuidPathCacheListener(GuidPathCacheListener var1) {
         throw new UnsupportedOperationException();
      }
   }

   private static final class TranslatingFunction implements Function<String, String> {
      private final ExpressionTranslator translator;

      public TranslatingFunction(ExpressionTranslator var1) {
         this.translator = var1;
      }

      public String apply(String var1) {
         return this.translator.toDisplay(var1);
      }
   }
}
