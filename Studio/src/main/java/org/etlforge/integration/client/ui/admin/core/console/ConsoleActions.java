package org.etlforge.integration.client.ui.admin.core.console;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.Action;

public final class ConsoleActions implements Iterable<Action> {
   private final List<Action> factories;

   public ConsoleActions(List<Action> var1) {
      this.factories = Collections.unmodifiableList(var1);
   }

   @Override
   public Iterator<Action> iterator() {
      return this.factories.iterator();
   }
}
