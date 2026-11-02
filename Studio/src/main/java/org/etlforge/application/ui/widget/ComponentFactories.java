package org.etlforge.application.ui.widget;

import javax.swing.JList;
import org.etlforge.application.ui.look.LookAndFeelCustomizer;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.util.decorator.DefaultListDecorRenderer;
import org.etlforge.ui.widget.list.DefaultKongaListFactory;
import org.etlforge.ui.widget.list.KongaListBase;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListFactory;
import org.etlforge.ui.widget.list.StandardListFactory;
import org.etlforge.ui.widget.split.SplitPaneFactory;

public final class ComponentFactories {
   private static final ListFactory LIST_FACTORY = new StandardListFactory(getListCustomizer());
   private static final ListFactory MONO_SPACED_LIST_FACTORY = new StandardListFactory(getMonoSpacedListCustomizer());
   private static final TableFactory TABLE_FACTORY = new TableFactoryImpl();
   private static final SplitPaneFactory SPLITPANE_FACTORY = new SplitPaneFactoryImpl();

   public static ListFactory listFactory() {
      return LIST_FACTORY;
   }

   public static ListFactory monoSpacedListFactory() {
      return MONO_SPACED_LIST_FACTORY;
   }

   public static TableFactory tableFactory() {
      return TABLE_FACTORY;
   }

   public static KongaListFactory newKongaListFactory() {
      return new ComponentFactories.KongaListFactoryImpl();
   }

   public static SplitPaneFactory splitPaneFactory() {
      return SPLITPANE_FACTORY;
   }

   private static org.etlforge.ui.widget.ComponentCustomizer<KongaListBase> getListCustomizer() {
      return new org.etlforge.ui.widget.ComponentCustomizer<KongaListBase>() {
         public KongaListBase customize(KongaListBase var1) {
            LookAndFeelCustomizer.makeOver(var1);
            return var1;
         }
      };
   }

   private static org.etlforge.ui.widget.ComponentCustomizer<KongaListBase> getMonoSpacedListCustomizer() {
      return new org.etlforge.ui.widget.ComponentCustomizer<KongaListBase>() {
         public KongaListBase customize(KongaListBase var1) {
            TextStyles.DefaultMonoSpaced.makeOver(var1);
            return var1;
         }
      };
   }

   private ComponentFactories() {
   }

   private static final class KongaListFactoryImpl extends DefaultKongaListFactory {
      public KongaListFactoryImpl() {
         this.setCellRenderer(new DefaultListDecorRenderer());
         this.setVisibleRowCount(10);
      }

      @Override
      protected JList createEmptyList() {
         return ComponentFactories.LIST_FACTORY.newList();
      }
   }
}
