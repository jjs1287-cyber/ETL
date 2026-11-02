package org.etlforge.application.ui.viewer.error.log;

import java.util.Date;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.errorlog.ErrorLogEntry;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.ui.util.decorator.DateCell;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TypedAbstractKongaTableModel;

public final class ErrorLogTableModel extends TypedAbstractKongaTableModel<ErrorLogEntry> {
   public static final TableColumnMetaData MESSAGE = new TableColumnMetaData("Message", ErrorLogTableModel.MessageCapsule.class, false, 300);
   public static final TableColumnMetaData TIMESTAMP = new TableColumnMetaData("Timestamp", DateCell.class, false, 150);
   public static final TableColumnMetaData SOURCE = new TableColumnMetaData("Reported From", ErrorLogTableModel.SourceDataCapsule.class, false, 200);
   public static final TableColumnMetaData LEVEL = new TableColumnMetaData("Level", ErrorLevel.class, false, 100);
   public static final TableColumnMetaData CAUSE = new TableColumnMetaData("Cause", ErrorLogTableModel.CauseDataCapsule.class, false, 200);

   public ErrorLogTableModel(Iterable<ErrorLogEntry> var1) {
      super(ErrorLogEntry.class, new TableColumnMetaData[]{MESSAGE, TIMESTAMP, SOURCE, LEVEL, CAUSE}, var1);
   }

   protected Object[] getColumnDataForTypedObject(ErrorLogEntry var1) {
      return new Object[]{
         new ErrorLogTableModel.MessageCapsule(var1),
         new DateCell(new Date(var1.timestamp()), DateCell.Mode.DETAILED),
         new ErrorLogTableModel.SourceDataCapsule(var1),
         var1.level(),
         new ErrorLogTableModel.CauseDataCapsule(var1)
      };
   }

   private static final class CauseDataCapsule implements Comparable<ErrorLogTableModel.CauseDataCapsule>, ListDecorator {
      private final Throwable cause;
      private final String tooltip;

      public CauseDataCapsule(ErrorLogEntry var1) {
         this.cause = var1.cause();
         this.tooltip = this.cause != null ? this.cause.getClass().getName() : "";
      }

      @Override
      public void decorate(ListDecor var1, boolean var2) {
         var1.setText(this.toString());
         var1.setToolTip(this.tooltip);
         var1.setIcon(null);
      }

      public int compareTo(ErrorLogTableModel.CauseDataCapsule var1) {
         return this.toString().compareToIgnoreCase(var1.toString());
      }

      @Override
      public String toString() {
         return this.cause == null ? "" : this.cause.getClass().getSimpleName();
      }
   }

   private static final class MessageCapsule implements Comparable<ErrorLogTableModel.MessageCapsule>, ListDecorator {
      private final String message;

      public MessageCapsule(ErrorLogEntry var1) {
         String var2 = var1.message();
         this.message = var2 != null ? var2 : "";
      }

      public int compareTo(ErrorLogTableModel.MessageCapsule var1) {
         return this.message.compareToIgnoreCase(var1.message);
      }

      @Override
      public void decorate(ListDecor var1, boolean var2) {
         var1.setText(this.message);
         var1.setToolTip(this.message);
         var1.setIcon(CommonIcons.ERROR_16);
      }
   }

   private static final class SourceDataCapsule implements Comparable<ErrorLogTableModel.SourceDataCapsule>, ListDecorator {
      private final String stringRep;
      private final String tooltip;

      public SourceDataCapsule(ErrorLogEntry var1) {
         Object var2 = var1.source();
         if (var2 == null) {
            this.stringRep = "[Unknown]";
            this.tooltip = this.stringRep;
         } else if (var2 instanceof Class) {
            this.stringRep = ((Class)var2).getSimpleName();
            this.tooltip = ((Class)var2).getName();
         } else {
            this.stringRep = var2.toString();
            this.tooltip = this.stringRep;
         }
      }

      @Override
      public void decorate(ListDecor var1, boolean var2) {
         var1.setText(this.stringRep);
         var1.setToolTip(this.tooltip);
         var1.setIcon(null);
      }

      public int compareTo(ErrorLogTableModel.SourceDataCapsule var1) {
         return this.stringRep.compareToIgnoreCase(var1.stringRep);
      }

      @Override
      public String toString() {
         return this.stringRep;
      }
   }
}
