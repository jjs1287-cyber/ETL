package org.etlforge.application.ui.widget;

import java.awt.Component;
import org.etlforge.ui.widget.split.KongaSplitPane;
import org.etlforge.ui.widget.split.SplitPaneFactory;

class SplitPaneFactoryImpl implements SplitPaneFactory {
   @Override
   public KongaSplitPane newSplitPane() {
      return new SplitPaneFactoryImpl.SplitPaneImpl();
   }

   @Override
   public KongaSplitPane newSplitPane(int var1, boolean var2, Component var3, Component var4) {
      return new SplitPaneFactoryImpl.SplitPaneImpl(var1, var2, var3, var4);
   }

   @Override
   public KongaSplitPane newSplitPane(int var1, boolean var2) {
      return new SplitPaneFactoryImpl.SplitPaneImpl(var1, var2);
   }

   @Override
   public KongaSplitPane newSplitPane(int var1, Component var2, Component var3) {
      return new SplitPaneFactoryImpl.SplitPaneImpl(var1, var2, var3);
   }

   @Override
   public KongaSplitPane newSplitPane(int var1) {
      return new SplitPaneFactoryImpl.SplitPaneImpl(var1);
   }

   private static class SplitPaneImpl extends KongaSplitPane {
      public SplitPaneImpl() {
      }

      public SplitPaneImpl(int var1, boolean var2, Component var3, Component var4) {
         super(var1, var2, var3, var4);
      }

      public SplitPaneImpl(int var1, boolean var2) {
         super(var1, var2);
      }

      public SplitPaneImpl(int var1, Component var2, Component var3) {
         super(var1, var2, var3);
      }

      public SplitPaneImpl(int var1) {
         super(var1);
      }

      @Override
      public boolean isAnimationEnabled() {
         return AnimationPreference.INSTANCE.get();
      }
   }
}
