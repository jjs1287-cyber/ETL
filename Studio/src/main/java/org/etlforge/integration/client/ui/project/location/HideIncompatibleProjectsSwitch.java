package org.etlforge.integration.client.ui.project.location;

import org.etlforge.integration.client.project.CompatibleVersionFilter;
import org.etlforge.integration.client.ui.project.resources.ProjectUiResources;
import org.etlforge.system.EmptyKeyValue;
import org.etlforge.system.KeyValue;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.util.preference.BooleanKeyValue;
import org.etlforge.util.version.Version;

public final class HideIncompatibleProjectsSwitch implements UiProvider {
   public static final boolean ENABLED = false;
   private static final String MEMENTO_KEY = "HideIncompatibleProjects";
   private final ProjectLocationsTable table;
   private final Version currentVersion;
   private final KongaCheckBox checkBox;
   private final KeyValue<String, Boolean> memento;

   public HideIncompatibleProjectsSwitch(ProjectLocationsTable var1, Version var2) {
      this(var1, var2, new EmptyKeyValue<>());
   }

   public HideIncompatibleProjectsSwitch(ProjectLocationsTable var1, Version var2, Class<?> var3) {
      this(var1, var2, new BooleanKeyValue(var3));
   }

   private HideIncompatibleProjectsSwitch(ProjectLocationsTable var1, Version var2, KeyValue<String, Boolean> var3) {
      this.table = var1;
      this.currentVersion = var2;
      this.memento = var3;
      boolean var4 = var3.get("HideIncompatibleProjects", true);
      this.checkBox = new KongaCheckBox(ProjectUiResources.getString("ProjectLocationsTable.HideIncompatible"), var4);
      this.checkBox.addSelectionChangeListener(new HideIncompatibleProjectsSwitch.Listener());
      this.setHideIncompatibleProjects(var4);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.checkBox);
   }

   @Override
   public void requestFocus() {
      this.checkBox.requestFocusInWindow();
   }

   private void setHideIncompatibleProjects(boolean var1) {
      if (var1) {
         CompatibleVersionFilter var2 = new CompatibleVersionFilter(this.currentVersion);
         this.table.setFilter(var2);
      } else {
         this.table.removeFilter();
      }
   }

   private class Listener implements Receiver<Boolean> {
      private Listener() {
      }

      public void handle(Boolean var1) {
         HideIncompatibleProjectsSwitch.this.memento.set("HideIncompatibleProjects", var1);
         HideIncompatibleProjectsSwitch.this.setHideIncompatibleProjects(var1);
      }
   }
}
