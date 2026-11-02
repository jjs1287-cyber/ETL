package org.etlforge.ui.widget.button;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.widget.DefaultComponentAction;

public abstract class KongaButtonPanel extends JPanel {
   public static final KongaButtonPanel.Button OK = KongaButtonPanel.Button.OK;
   public static final KongaButtonPanel.Button CANCEL = KongaButtonPanel.Button.CANCEL;
   public static final KongaButtonPanel.Button APPLY = KongaButtonPanel.Button.APPLY;
   public static final KongaButtonPanel.Button YES = KongaButtonPanel.Button.YES;
   public static final KongaButtonPanel.Button NO = KongaButtonPanel.Button.NO;
   public static final KongaButtonPanel.Button CLOSE = KongaButtonPanel.Button.CLOSE;
   public static final KongaButtonPanel.Button RESET = KongaButtonPanel.Button.RESET;
   public static final KongaButtonPanel.Button START = KongaButtonPanel.Button.START;
   public static final KongaButtonPanel.Button STOP = KongaButtonPanel.Button.STOP;
   private final Map<KongaButtonPanel.Button, JButton> mapButtons = Maps.newHashMap();
   private Dimension buttonSize;

   public KongaButtonPanel(KongaButtonPanel.Button[] var1, int var2) {
      this(var1, 2, -1, -1, var2);
   }

   public KongaButtonPanel(KongaButtonPanel.Button[] var1, int var2, int var3, int var4, int var5) {
      this.checkArguments(var1, var2, var5);
      this.initUI(var1, var2, var3, var4, var5);
   }

   private void initUI(KongaButtonPanel.Button[] var1, int var2, int var3, int var4, int var5) {
      JButton[] var6 = new JButton[var1.length];

      for (int var7 = 0; var7 < var1.length; var7++) {
         JButton var8 = var1[var7].create(this);
         var6[var7] = var8;
         this.mapButtons.put(var1[var7], var8);
      }

      this.setButtonsSizes(var6);
      this.setLayout(new BoxLayout(this, var2));
      this.addMargin(var2, var3);

      for (int var9 = 0; var9 < var6.length; var9++) {
         if (var9 > 0) {
            this.addStruts(var2, var5);
         }

         this.add(var6[var9]);
      }

      this.addMargin(var2, var4);
   }

   private void addMargin(int var1, int var2) {
      if (var2 > 0) {
         this.addStruts(var1, var2);
      } else if (var2 < 0) {
         this.addGlue(var1);
      }
   }

   private void addGlue(int var1) {
      this.add(var1 == 2 ? Box.createHorizontalGlue() : Box.createVerticalGlue());
   }

   private void addStruts(int var1, int var2) {
      this.add(var1 == 2 ? Box.createHorizontalStrut(var2) : Box.createVerticalStrut(var2));
   }

   private void setButtonsSizes(JButton[] var1) {
      FontMetrics var2 = this.getFontMetrics(var1[0].getFont());
      int var3 = 0;

      for (int var4 = 0; var4 < var1.length; var4++) {
         var3 = Math.max(var3, var2.stringWidth(var1[var4].getText()));
      }

      var3 += 40;
      JButton var7 = new JButton("X");
      int var5 = var7.getPreferredSize().height;
      this.buttonSize = new Dimension(var3, var5);
   }

   public final JButton getButton(KongaButtonPanel.Button var1) {
      return this.mapButtons.get(var1);
   }

   protected void onOK() {
   }

   protected void onCancel() {
   }

   protected void onApply() {
   }

   protected void onYes() {
   }

   protected void onNo() {
   }

   protected void onClose() {
   }

   protected void onReset() {
   }

   protected void onStart() {
   }

   protected void onStop() {
   }

   private void checkArguments(KongaButtonPanel.Button[] var1, int var2, int var3) {
      Preconditions.checkNotNull(var1, "buttons");
      Preconditions.checkArgument(var1.length > 0, "buttons cannot have zero length");
      Preconditions.checkArgument(
         var2 == 3 || var2 == 2, "Invalid orientation (" + var2 + "). Valid values are: " + 3 + " (BoxLayout.PAGE_AXIS) and " + 2 + " (BoxLayout.LINE_AXIS)."
      );
      Preconditions.checkArgument(var3 >= 0 || var1.length == 1, "space can't be < 0 (space = " + var3);
   }

   public abstract static class Button {
      private final String text;
      private static final KongaButtonPanel.Button OK = new KongaButtonPanel.Button(UiResourceBundle.getString("OK")) {
         @Override
         protected ActionListener getButtonListener(final KongaButtonPanel var1) {
            return new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent var1x) {
                  var1.onOK();
               }
            };
         }
      };
      private static final KongaButtonPanel.Button CANCEL = new KongaButtonPanel.Button(UiResourceBundle.getString("Cancel")) {
         @Override
         protected ActionListener getButtonListener(final KongaButtonPanel var1) {
            return new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent var1x) {
                  var1.onCancel();
               }
            };
         }
      };
      private static final KongaButtonPanel.Button APPLY = new KongaButtonPanel.Button(UiResourceBundle.getString("Apply")) {
         @Override
         protected ActionListener getButtonListener(final KongaButtonPanel var1) {
            return new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent var1x) {
                  var1.onApply();
               }
            };
         }
      };
      private static final KongaButtonPanel.Button YES = new KongaButtonPanel.Button(UiResourceBundle.getString("Yes")) {
         @Override
         protected ActionListener getButtonListener(final KongaButtonPanel var1) {
            return new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent var1x) {
                  var1.onYes();
               }
            };
         }
      };
      private static final KongaButtonPanel.Button NO = new KongaButtonPanel.Button(UiResourceBundle.getString("No")) {
         @Override
         protected ActionListener getButtonListener(final KongaButtonPanel var1) {
            return new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent var1x) {
                  var1.onNo();
               }
            };
         }
      };
      private static final KongaButtonPanel.Button CLOSE = new KongaButtonPanel.Button(UiResourceBundle.getString("Close")) {
         @Override
         protected ActionListener getButtonListener(final KongaButtonPanel var1) {
            return new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent var1x) {
                  var1.onClose();
               }
            };
         }
      };
      private static final KongaButtonPanel.Button RESET = new KongaButtonPanel.Button(UiResourceBundle.getString("Reset")) {
         @Override
         protected ActionListener getButtonListener(final KongaButtonPanel var1) {
            return new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent var1x) {
                  var1.onReset();
               }
            };
         }
      };
      private static final KongaButtonPanel.Button START = new KongaButtonPanel.Button(UiResourceBundle.getString("Start")) {
         @Override
         protected ActionListener getButtonListener(final KongaButtonPanel var1) {
            return new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent var1x) {
                  var1.onStart();
               }
            };
         }
      };
      private static final KongaButtonPanel.Button STOP = new KongaButtonPanel.Button(UiResourceBundle.getString("Stop")) {
         @Override
         protected ActionListener getButtonListener(final KongaButtonPanel var1) {
            return new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent var1x) {
                  var1.onStop();
               }
            };
         }
      };

      private Button(String var1) {
         this.text = var1;
      }

      private final JButton create(final KongaButtonPanel var1) {
         JButton var2 = new JButton(this.text) {
            @Override
            public Dimension getPreferredSize() {
               return var1.buttonSize;
            }
         };
         var2.addActionListener(this.getButtonListener(var1));
         this.installKeyBindings(var2);
         return var2;
      }

      private void installKeyBindings(final JButton var1) {
         DefaultComponentAction.bind(var1, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent var1x) {
               var1.doClick();
            }
         });
      }

      protected abstract ActionListener getButtonListener(KongaButtonPanel var1);
   }
}
