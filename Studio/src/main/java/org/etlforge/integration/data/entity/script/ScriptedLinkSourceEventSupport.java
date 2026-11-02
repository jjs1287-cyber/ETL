package org.etlforge.integration.data.entity.script;

import com.google.common.collect.Lists;
import java.util.List;

public final class ScriptedLinkSourceEventSupport {
   private final ScriptedLinkSource source;
   private final List<ScriptedLinkSourceListener> listeners = Lists.newCopyOnWriteArrayList();

   public ScriptedLinkSourceEventSupport(ScriptedLinkSource var1) {
      this.source = var1;
   }

   public void addListener(ScriptedLinkSourceListener var1) {
      this.listeners.add(var1);
   }

   public void removeListener(ScriptedLinkSourceListener var1) {
      this.listeners.remove(var1);
   }

   public void notifyListeners() {
      for (ScriptedLinkSourceListener var2 : this.listeners) {
         var2.scriptedLinksMayHaveChanged(this.source);
      }
   }
}
