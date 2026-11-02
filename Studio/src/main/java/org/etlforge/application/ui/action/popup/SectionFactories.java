package org.etlforge.application.ui.action.popup;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import java.util.List;

public final class SectionFactories implements Iterable<PopupMenuItemFactory> {
   private final ContextMenuSection section;
   private final ImmutableList<PopupMenuItemFactory> factories;

   public SectionFactories(ContextMenuSection var1, List<PopupMenuItemFactory> var2) {
      this.section = Preconditions.checkNotNull(var1, "section");
      this.factories = ImmutableList.copyOf(Preconditions.checkNotNull(var2, "factories"));
   }

   public ContextMenuSection getSection() {
      return this.section;
   }

   @Override
   public Iterator<PopupMenuItemFactory> iterator() {
      return this.factories.iterator();
   }
}
