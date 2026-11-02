package org.etlforge.integration.client.ui.script.builder;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.script.GuidPathCache;
import org.etlforge.integration.data.entity.script.GuidPathCacheListener;
import org.etlforge.integration.data.entity.script.ProjectGuidPathCache;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.script.ExpressionSource;
import org.etlforge.integration.data.script.FunctionRegistry;
import org.etlforge.integration.data.script.ScriptStatus;
import org.etlforge.system.Disposable;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public final class ScriptBuilderModel extends AbstractPropertyBasedModel implements GuidPathCache, Disposable {
   public static final String STATUS_PROPERTY = "Status";
   public static final String MODE_PROPERTY = "Mode";
   public static final String SOURCE_PROPERTY = "Source";
   private final IntegrationProject project;
   private final Property<ScriptBuilderMode> mode;
   private final Property<ScriptStatus> status;
   private final Property<ExpressionSource> source;
   private final ProjectGuidPathCache guidPathCache;
   private boolean mustBeSingleValue;

   public ScriptBuilderModel(IntegrationProject var1) {
      this.project = Preconditions.checkNotNull(var1);
      this.guidPathCache = FunctionRegistry.getRegistry().buildProjectBuidPathCache(var1);
      this.mode = this.newProperty("Mode", ScriptBuilderMode.DEVELOPER);
      this.mode.setAllowsNull(false);
      this.status = this.newProperty("Status");
      this.status.set(ScriptStatus.NONE);
      this.source = this.newProperty("Source");
      this.mustBeSingleValue = false;
   }

   @Override
   public void dispose() {
      this.guidPathCache.dispose();
   }

   public IntegrationProject getProject() {
      return this.project;
   }

   public ScriptBuilderMode getMode() {
      return this.mode.get();
   }

   public void setMode(ScriptBuilderMode var1) {
      this.mode.set(var1);
   }

   public ExpressionSource getSource() {
      return this.source.get();
   }

   public void setSource(ExpressionSource var1) {
      this.source.set(var1);
   }

   public void setStatus(ScriptStatus var1) {
      this.status.set(var1);
   }

   public ScriptStatus getStatus() {
      return this.status.get();
   }

   @Override
   public String getGuid(String var1) {
      return this.guidPathCache.getGuid(var1);
   }

   @Override
   public String getPathFromGuid(String var1) {
      return this.guidPathCache.getPathFromGuid(var1);
   }

   @Override
   public void addGuidPathCacheListener(GuidPathCacheListener var1) {
      this.guidPathCache.addGuidPathCacheListener(var1);
   }

   @Override
   public void removeGuidPathCacheListener(GuidPathCacheListener var1) {
      this.guidPathCache.removeGuidPathCacheListener(var1);
   }

   public boolean mustBeSingleValue() {
      return this.mustBeSingleValue;
   }

   public void setMustBeSingleValue(boolean var1) {
      this.mustBeSingleValue = var1;
   }
}
