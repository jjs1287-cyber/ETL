package org.etlforge.application.ui.window.toolbar;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import java.util.List;
import javax.swing.JToolBar;
import org.etlforge.system.ArgCheck;

public abstract class ToolBar {
   private final String id;
   private final ListMultimap<ToolBarExtensionPoint, ToolBarEntry> extensions = ArrayListMultimap.create();

   protected ToolBar(String var1) {
      this.id = ArgCheck.checkNotEmpty(var1, "id");
   }

   public final String getID() {
      return this.id;
   }

   public final void addExtension(ToolBarExtensionPoint var1, List<? extends ToolBarEntry> var2) {
      this.extensions.putAll(var1, var2);
   }

   public final void removeExtension(ToolBarExtensionPoint var1, List<? extends ToolBarEntry> var2) {
      if (var2 != null) {
         this.extensions.remove(var1, var2);
      }
   }

   protected abstract List<ToolBarEntry> getEntries();

   public final JToolBar createJToolBar() {
      JToolBar var1 = new JToolBar();
      var1.setRollover(true);
      this.populate(var1);
      return var1;
   }

   public final void populate(JToolBar var1) {
      var1.removeAll();

      for (ToolBarEntry var4 : this.getEntries()) {
         var4.addToToolBar(var1);
      }
   }

   protected final void addExtensionsToEntryList(List<ToolBarEntry> var1, ToolBarExtensionPoint var2) {
      var1.addAll(this.extensions.get(var2));
   }
}
