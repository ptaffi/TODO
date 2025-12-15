// package util;

// import com.formdev.flatlaf.FlatDarkLaf;
// import com.formdev.flatlaf.FlatLightLaf;
// import javax.swing.*;
// import java.awt.*;

// public class ThemeManager {

//     public enum Theme { DARK, BLACK, SEPIA }
//     private static final Color BUTTON_MAIN = new Color(188, 143, 187);
//     private static final Color BUTTON_TEXT = Color.WHITE;
//     private static final Color FOCUS_COLOR = new Color(218, 164, 214);

//     public static void apply(Theme theme) {
//         try {
//             switch (theme) {
//                 case DARK -> {
//                     UIManager.setLookAndFeel(new FlatDarkLaf());
//                     applyCommonOverrides();
//                 }
//                 case BLACK -> {
//                     UIManager.setLookAndFeel(new FlatDarkLaf());
//                     applyBlackColors();
//                     applyCommonOverrides();
//                 }
//                 case SEPIA -> {
//                     UIManager.setLookAndFeel(new FlatLightLaf());
//                     applySepiaColors();
//                     applyCommonOverrides();
//                 }
//             }

//             for (Window w : Window.getWindows()) {
//                 SwingUtilities.updateComponentTreeUI(w);
//                 w.invalidate();
//                 w.validate();
//                 w.repaint();
//             }

//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

//     // Các override dùng chung cho mọi theme
//     private static void applyCommonOverrides() {
//         // --- Button global ---
//         UIManager.put("Button.background", BUTTON_MAIN);
//         UIManager.put("Button.foreground", BUTTON_TEXT);

//         // hover/pressed (FlatLaf có key này)
//         UIManager.put("Button.hoverBackground", BUTTON_MAIN.darker());
//         UIManager.put("Button.pressedBackground", BUTTON_MAIN.darker().darker());

//         // --- đổi màu viền focus (cái viền xanh khi click vào input) ---
//         UIManager.put("Component.focusColor", FOCUS_COLOR);
//         UIManager.put("TextComponent.focusColor", FOCUS_COLOR);

//         // một số bản FlatLaf ăn key này cho TextField
//         UIManager.put("TextField.focusedBorderColor", FOCUS_COLOR);
//         UIManager.put("PasswordField.focusedBorderColor", FOCUS_COLOR);

//         // nếu muốn viền thường cũng theo màu nhẹ
//         UIManager.put("Component.borderColor", new Color(210, 210, 210));
//     }

//     private static void applyBlackColors() {
//         Color bg = new Color(10, 10, 10);
//         Color bg2 = new Color(18, 18, 18);
//         Color fg = new Color(230, 230, 230);

//         UIManager.put("control", bg);
//         UIManager.put("Panel.background", bg);
//         UIManager.put("ScrollPane.background", bg);

//         UIManager.put("Label.foreground", fg);

//         UIManager.put("TextField.background", bg2);
//         UIManager.put("TextField.foreground", fg);
//         UIManager.put("TextArea.background", bg2);
//         UIManager.put("TextArea.foreground", fg);

//         UIManager.put("List.background", bg2);
//         UIManager.put("List.foreground", fg);

//         UIManager.put("Table.background", bg2);
//         UIManager.put("Table.foreground", fg);

//         // đừng set Button.background ở đây nếu muốn dùng BUTTON_MAIN chung
//         UIManager.put("Button.foreground", fg);
//     }

//     private static void applySepiaColors() {
//         Color bg = new Color(245, 233, 214);
//         Color bg2 = new Color(236, 221, 196);
//         Color fg = new Color(60, 45, 30);
//         Color field = new Color(250, 242, 230);
//         Color sel = new Color(210, 180, 140);

//         UIManager.put("control", bg);
//         UIManager.put("Panel.background", bg);
//         UIManager.put("ScrollPane.background", bg);

//         UIManager.put("Label.foreground", fg);

//         UIManager.put("TextField.background", field);
//         UIManager.put("TextField.foreground", fg);
//         UIManager.put("TextArea.background", field);
//         UIManager.put("TextArea.foreground", fg);

//         UIManager.put("List.background", bg2);
//         UIManager.put("List.foreground", fg);
//         UIManager.put("List.selectionBackground", sel);
//         UIManager.put("List.selectionForeground", fg);

//         UIManager.put("Table.background", bg2);
//         UIManager.put("Table.foreground", fg);
//         UIManager.put("Table.selectionBackground", sel);
//         UIManager.put("Table.selectionForeground", fg);

//         UIManager.put("Button.foreground", fg);
//     }

//     public static void setDarkTheme()  { apply(Theme.DARK); }
//     public static void setBlackTheme() { apply(Theme.BLACK); }
//     public static void setSepiaTheme() { apply(Theme.SEPIA); }
// }
