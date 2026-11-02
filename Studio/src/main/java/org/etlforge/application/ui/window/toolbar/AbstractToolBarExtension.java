package org.etlforge.application.ui.window.toolbar;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ListMultimap;
import java.util.List;
import javax.swing.JToolBar;
import org.etlforge.system.ArgCheck;

public abstract class AbstractToolBarExtension {
   private final String toolBarId;
   private final ListMultimap<ToolBarExtensionPoint, ToolBarEntry> extensionButtons = ArrayListMultimap.create();

   protected AbstractToolBarExtension(String var1) {
      this.toolBarId = ArgCheck.checkNotEmpty(var1, "toolBarId");
   }

   public final String getId() {
      return this.toolBarId;
   }

   public final ImmutableList<ToolBarEntry> getEntries(ToolBarExtensionPoint var1) {
      List var2 = this.extensionButtons.get(var1);
      if (var2.isEmpty()) {
         var2 = this.createExtension(var1);
         if (var2 != null) {
            this.extensionButtons.putAll(var1, var2);
         }
      }

      return var2 == null ? ImmutableList.of() : ImmutableList.copyOf(var2);
   }

   protected abstract List<ToolBarEntry> createExtension(ToolBarExtensionPoint var1);

   public final void remove(ToolBarManager var1, ToolBarExtensionPoint var2) {
      ToolBar var3 = var1.getToolBar(this.toolBarId);
      if (var3 != null) {
         var3.removeExtension(var2, this.extensionButtons.get(var2));
         JToolBar var4 = var1.getJToolBar(this.toolBarId);
         if (var4 != null) {
            var3.populate(var4);
         }
      }
   }

   public final void removeAll(ToolBarManager var1) {
      ToolBar var2 = var1.getToolBar(this.toolBarId);
      if (var2 != null) {
         for (ToolBarExtensionPoint var6 : ToolBarExtensionPoint.values()) {
            var2.removeExtension(var6, this.extensionButtons.get(var6));
         }

         JToolBar var7 = var1.getJToolBar(this.toolBarId);
         if (var7 != null) {
            var2.populate(var7);
         }
      }
   }
}
