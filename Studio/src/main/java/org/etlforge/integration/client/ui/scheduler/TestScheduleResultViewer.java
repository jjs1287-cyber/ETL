package org.etlforge.integration.client.ui.scheduler;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.client.scheduler.ScheduledOperation;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.renderer.RendererUtils;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;

public class TestScheduleResultViewer implements UiProvider {
   private final KongaList timestampList;
   private final JComponent caption;

   public TestScheduleResultViewer(Schedule var1, Date[] var2) {
      this(createCaption(var1), var2);
   }

   public TestScheduleResultViewer(ScheduledOperation var1, Date[] var2) {
      this(createCaption(var1.getOperation()), var2);
   }

   private static JComponent createCaption(IntegrationEntity var0) {
      JLabel var1 = new JLabel();
      RendererUtils.decorateRenderer(var1, var0, EntityIcons.Size.MEDIUM);
      return Horizontal.box().addAllWithSpace(5, var1, "will next run:").container();
   }

   private TestScheduleResultViewer(JComponent var1, Date[] var2) {
      this.caption = var1;
      this.timestampList = createList(var2);
   }

   private static KongaList createList(Date[] var0) {
      KongaListFactory var1 = createListFactory();
      List var2 = createListObjects(var0);
      return var1.newList(var2);
   }

   private static KongaListFactory createListFactory() {
      KongaListFactory var0 = ComponentFactories.newKongaListFactory();
      var0.setStyle(TextStyles.DefaultMonoSpaced);
      var0.setVisibleRowCount(10);
      return var0;
   }

   private static List<Object> createListObjects(Date[] var0) {
      ArrayList var1 = Lists.newArrayList();

      for (Date var5 : var0) {
         String var6 = formatDate(var5);
         var1.add(var6);
      }

      return var1;
   }

   private static String formatDate(Date var0) {
      String var1 = String.format("%tB %te, %tY", var0, var0, var0);
      return String.format("%-10tA%-20s@ %tH:%tM:%tS", var0, var1, var0, var0, var0);
   }

   @Override
   public void decorate(UiCanvas var1) {
      CaptionLayout.of(this.caption, this.timestampList).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.timestampList.requestFocus();
   }
}
