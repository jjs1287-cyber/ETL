package org.etlforge.ui.util.option;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.util.Orientation;
import org.etlforge.util.event.ListenerManager;
import org.etlforge.util.event.ListenerManagerFactory;

abstract class AbstractOptionsPanel<T extends Option> implements UiProvider {
   private final T[] options;
   private final Set<T> defaultOptions;
   private List<OptionSelector<T>> selectors;
   private final Orientation orientation;
   private JComponent giveInitialFocusTo;
   private final ListenerManager<SelectedOptionListener> selectionListeners;
   private Set<T> selectedProgrammatically;

   protected AbstractOptionsPanel(T[] var1, Collection<T> var2, Orientation var3) {
      Preconditions.checkArgument(var1 != null && var1.length > 0, "options cannot be null or empty");
      Preconditions.checkNotNull(var3, "orientation");
      this.options = (T[])var1;
      this.defaultOptions = var2 == null ? new HashSet<>() : new HashSet<>(var2);
      this.orientation = var3;
      this.selectionListeners = ListenerManagerFactory.create(SelectedOptionListener.class);
   }

   @Override
   public final void decorate(UiCanvas var1) {
      this.createDisplayer().decorate(var1);
   }

   @Override
   public final void requestFocus() {
      this.giveInitialFocusTo.requestFocusInWindow();
   }

   public final void setEnabled(boolean var1) {
      for (OptionSelector var3 : this.selectors) {
         var3.setEnabled(var1);
      }
   }

   public final T[] getAllOptions() {
      return (T[])((Option[])this.options.clone());
   }

   public final List<T> getSelectedOptions() {
      if (this.selectors == null) {
         return Lists.newArrayList(this.defaultOptions);
      }

      ArrayList var1 = new ArrayList();

      for (OptionSelector var3 : this.selectors) {
         if (var3.isSelected()) {
            var1.add(var3.getOption());
         }
      }

      return var1;
   }

   public final void setSelectedOption(T var1) {
      if (this.selectors != null) {
         for (OptionSelector var3 : this.selectors) {
            var3.setSelected(var3.getOption().equals(var1));
         }
      } else {
         this.selectedProgrammatically = Sets.newHashSet((T[])(new Option[]{var1}));
      }
   }

   protected final void setSelectedOptions(Iterable<T> var1) {
      HashSet var2 = Sets.newHashSet(var1);
      if (this.selectors != null) {
         for (OptionSelector var4 : this.selectors) {
            var4.setSelected(var2.contains(var4.getOption()));
         }
      } else {
         this.selectedProgrammatically = var2;
      }
   }

   private UiProvider createDisplayer() {
      BoxBuilder var1 = this.orientation == Orientation.HORIZONTAL ? BoxBuilder.horizontal() : BoxBuilder.vertical();
      this.createSelectors(var1, 5);
      return var1.deeplyNonOpaque();
   }

   private void createSelectors(BoxBuilder var1, int var2) {
      this.selectors = new ArrayList<>();

      for (Option var6 : this.options) {
         boolean var7 = this.defaultOptions.contains(var6);
         JToggleButton var8 = this.createSelectorButton((T)var6, var7);
         this.selectors.add(new ToggleButtonOptionSelector<>((T)var6, var8));
         var1.add(var8).strut(var2);
         if (this.giveInitialFocusTo == null) {
            this.giveInitialFocusTo = var8;
         }
      }

      if (this.selectedProgrammatically != null) {
         this.setSelectedOptions(this.selectedProgrammatically);
         this.selectedProgrammatically = null;
      }
   }

   protected abstract JToggleButton createSelectorButton(T var1, boolean var2);

   public final void addSelectionListener(SelectedOptionListener var1) {
      this.selectionListeners.add(var1);
   }

   public final void removeSelectionListener(SelectedOptionListener var1) {
      this.selectionListeners.remove(var1);
   }

   protected final void fireSelectionChanged() {
      if (!this.selectionListeners.isEmpty()) {
         List var1 = this.getSelectedOptions();
         Option[] var2 = (Option[])Array.newInstance(Option.class, var1.size());
         var1.toArray(var2);
         this.selectionListeners.call().selectionChanged(var2);
      }
   }

   protected final void fireSelectionChanged(T var1) {
      if (!this.selectionListeners.isEmpty()) {
         Option[] var2 = new Option[]{var1};
         this.selectionListeners.call().selectionChanged(var2);
      }
   }
}
