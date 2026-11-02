package org.etlforge.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DisposableCollection implements Disposable {
   private final List<Disposable> elements = new ArrayList<>();

   public DisposableCollection() {
   }

   public DisposableCollection(Disposable... var1) {
      this();
      this.add(var1);
   }

   public DisposableCollection(Iterable<? extends Disposable> var1) {
      this();
      this.add(var1);
   }

   public void add(Disposable... var1) {
      Collections.addAll(this.elements, var1);
   }

   public void add(Iterable<? extends Disposable> var1) {
      for (Disposable var3 : var1) {
         this.elements.add(var3);
      }
   }

   @Override
   public void dispose() {
      Disposables.disposeAll(this.elements);
   }
}
