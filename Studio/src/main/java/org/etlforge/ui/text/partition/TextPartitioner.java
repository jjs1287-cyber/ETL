package org.etlforge.ui.text.partition;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.etlforge.util.AbstractInterval;
import org.etlforge.util.Interval;
import org.etlforge.util.IntervalFactory;

public class TextPartitioner {
   private final TokenScanner scanner;
   private TreeMap<Integer, TextPartitioner.Partition> tokens;
   private Document document;
   private final List<PartitionHandler> handlers;

   public TextPartitioner(TokenScanner var1) {
      Preconditions.checkNotNull(var1, "scanner");
      this.scanner = var1;
      this.handlers = new CopyOnWriteArrayList<>();
   }

   public final void setDocument(Document var1) {
      this.document = var1;
      this.refresh();
   }

   protected final Document getDocument() {
      return this.document;
   }

   public final void handleInsert(int var1, String var2) {
      Interval var3 = IntervalFactory.fromStartAndLength(var1, var2.length());
      TextPartitioner.Partition var4 = this.getStartPointForScan(var3);
      int var5 = this.calculateEndOfDamagedRegionOnInsert(var1, var2);
      int var6 = var2.length();
      this.calculateDamage(var4, var5, var6);
   }

   private TextPartitioner.Partition getStartPointForScan(Interval var1) {
      int var2 = var1.startsAt();
      return var2 == 0 ? null : this.calculateStartOfDamagedRegion(var2);
   }

   protected TextPartitioner.Partition calculateStartOfDamagedRegion(int var1) {
      return null;
   }

   private void calculateDamage(TextPartitioner.Partition var1, int var2, int var3) {
      List var4 = this.getUndamagedTokensHead(var1);
      List var5 = this.getUndamagedTokensTail(var2);
      this.shiftTail(var3, var5);
      int var6 = var2;
      if (var3 > 0) {
         var6 += var3;
      }

      List var7 = this.getDamagedTokens(var1, var6);
      this.removeOverlapFromTail(var7, var5);
      this.storeNewTokens(var4, var7, var5);
      this.notifyHandlers(var7);
   }

   private List<TextPartitioner.Partition> getUndamagedTokensHead(TextPartitioner.Partition var1) {
      ArrayList var2 = new ArrayList();
      if (var1 != null) {
         int var3 = var1.offset;

         for (Entry var5 : this.tokens.entrySet()) {
            if ((Integer)var5.getKey() >= var3) {
               break;
            }

            var2.add(var5.getValue());
         }
      }

      return var2;
   }

   protected int calculateEndOfDamagedRegionOnInsert(int var1, String var2) {
      return this.document.getLength();
   }

   private List<TextPartitioner.Partition> getUndamagedTokensTail(int var1) {
      NavigableMap var2 = this.tokens.tailMap(var1, true);
      return Lists.newArrayList(var2.values());
   }

   private void shiftTail(int var1, List<TextPartitioner.Partition> var2) {
      if (var1 != 0) {
         for (TextPartitioner.Partition var4 : var2) {
            var4.shift(var1);
         }
      }
   }

   private List<TextPartitioner.Partition> getDamagedTokens(TextPartitioner.Partition var1, int var2) {
      try {
         int var3 = var1 == null ? 0 : var1.offset;
         ArrayList var4 = new ArrayList();
         this.scanner.setText(this.document.getText(0, this.document.getLength()));
         this.scanner.setPosition(var3);
         Token var5 = null;

         while ((var5 = this.scanner.next()) != null) {
            TextPartitioner.Partition var6 = new TextPartitioner.Partition(var5);
            var4.add(var6);
            if (var6.endsAt >= var2) {
               break;
            }
         }

         return var4;
      } catch (BadLocationException var7) {
         throw new AssertionError();
      }
   }

   private void removeOverlapFromTail(List<TextPartitioner.Partition> var1, List<TextPartitioner.Partition> var2) {
      if (!var1.isEmpty() && !var2.isEmpty()) {
         TextPartitioner.Partition var3 = (TextPartitioner.Partition)var1.get(var1.size() - 1);
         Iterator var4 = var2.iterator();

         while (var4.hasNext()) {
            TextPartitioner.Partition var5 = (TextPartitioner.Partition)var4.next();
            if (var5.offset > var3.endsAt) {
               break;
            }

            var4.remove();
         }
      }
   }

   public final void handleRemoval(int var1, int var2, String var3) {
      Interval var4 = IntervalFactory.fromStartAndLength(var1, var2);
      TextPartitioner.Partition var5 = this.getStartPointForScan(var4);
      int var6 = this.calculateEndOfDamagedRegionOnRemoval(var1, var2, var3);
      this.calculateDamage(var5, var6, -var2);
   }

   protected int calculateEndOfDamagedRegionOnRemoval(int var1, int var2, String var3) {
      return this.document.getLength();
   }

   public final void handleReplace(int var1, int var2, String var3) {
      if (var2 == 0) {
         this.handleInsert(var1, var3);
      } else {
         Interval var4 = IntervalFactory.fromStartAndLength(var1, Math.max(var2, var3.length()));
         this.calculateDamage(var4);
      }
   }

   public final void refresh() {
      if (this.document != null) {
         this.calculateDamage(IntervalFactory.fromStartAndLength(0, this.document.getLength()));
      }
   }

   public final void calculateDamage(Interval var1) {
      if (this.document != null) {
         TextPartitioner.Partition var2 = this.getStartPointForScan(var1);
         List var3 = this.getUndamagedTokensHead(var2);
         List var4 = this.getDamagedTokens(var2);
         this.storeNewTokens(var3, var4, null);
         this.notifyHandlers(var4);
      }
   }

   public final void addPartitionHandler(PartitionHandler var1) {
      this.handlers.add(var1);
   }

   public final void removePartitionHandler(PartitionHandler var1) {
      this.handlers.remove(var1);
   }

   public final TextPartitioner.Partition getPartitionAt(int var1) {
      Entry var2 = this.tokens.floorEntry(var1);
      return var2 != null ? (TextPartitioner.Partition)var2.getValue() : null;
   }

   public final TextPartitioner.Partition getFirst() {
      return this.tokens.isEmpty() ? null : this.tokens.firstEntry().getValue();
   }

   public final TextPartitioner.Partition getLast() {
      return this.tokens.isEmpty() ? null : this.tokens.lastEntry().getValue();
   }

   private List<TextPartitioner.Partition> getDamagedTokens(TextPartitioner.Partition var1) {
      return this.getDamagedTokens(var1, this.document.getLength());
   }

   private void storeNewTokens(List<TextPartitioner.Partition> var1, List<TextPartitioner.Partition> var2, List<TextPartitioner.Partition> var3) {
      this.tokens = new TreeMap<>();
      this.storeTokensInMap(var1);
      this.storeTokensInMap(var2);
      if (var3 != null) {
         this.storeTokensInMap(var3);
      }

      TextPartitioner.Partition var4 = null;

      for (TextPartitioner.Partition var6 : this.tokens.values()) {
         var6.previous = var4;
         if (var4 != null) {
            var4.next = var6;
         }

         var4 = var6;
      }
   }

   private void storeTokensInMap(Collection<TextPartitioner.Partition> var1) {
      for (TextPartitioner.Partition var3 : var1) {
         this.tokens.put(var3.offset, var3);
      }
   }

   private void notifyHandlers(List<TextPartitioner.Partition> var1) {
      for (PartitionHandler var3 : this.handlers) {
         var3.handle(var1);
      }
   }

   public static final class Partition extends AbstractInterval {
      public int offset;
      public int endsAt;
      public final Token token;
      private TextPartitioner.Partition previous;
      private TextPartitioner.Partition next;

      public Partition(Token var1) {
         this.offset = var1.offset();
         this.endsAt = this.offset + var1.lexeme().length();
         this.token = var1;
      }

      @Override
      public int length() {
         return this.endsAt - this.offset;
      }

      @Override
      public int startsAt() {
         return this.offset;
      }

      public TextPartitioner.Partition getPrevious() {
         return this.previous;
      }

      public TextPartitioner.Partition getNext() {
         return this.next;
      }

      public void shift(int var1) {
         this.offset += var1;
         this.endsAt += var1;
      }
   }
}
