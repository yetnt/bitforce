package com.yetnt.methods;

import com.yetnt.Main;

import javax.swing.*;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import java.util.ArrayList;

/**
 * The base class for interpreting byte arrays.
 * @implNote This as a superclass provides the {@link JRadioButton} and the {@link JMenuItem}
 * for interacting with this particular method within the UI. The {@link #getMethods()} defines
 * the {@code keycode} that the JMenuItem will expect. Each key code however has a base modifier of
 * {@link KeyEvent#SHIFT_DOWN_MASK} and can provide any other extra modifiers to prevent clashes.
 * @implSpec Only interpretation methods are allowed to use a keycode which contains
 * {@link KeyEvent#SHIFT_DOWN_MASK} and {@code (any letter)}.
 * @author Lehlogonolo Poole
 */
public class InterpMethod {
    private JRadioButton radioButton;
    private String name;
    private VAConsumer interpreter;
    private JMenuItem menuItem;
    /**
     * Constructs a new InterpMethod.
     * @param name The name of the interpretation method.
     * @param keystrokeKey The key code for the JMenuItem accelerator.
     * @param modifiers Additional modifiers for the JMenuItem accelerator,
     *                  which will be ORed with {@link KeyEvent#SHIFT_DOWN_MASK}.
     */
    public InterpMethod(String name, int keystrokeKey, int modifiers) {
        this.name = name;
        radioButton = new JRadioButton(name);
        radioButton.setToolTipText(name);
        menuItem = new JMenuItem(name);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                keystrokeKey,
                KeyEvent.SHIFT_DOWN_MASK | modifiers
        ));
    }

    protected InterpMethod setInterpreter(VAConsumer interpreter) {
        this.interpreter = interpreter;
        return this;
    }

    public JRadioButton getRadioButton() {
        return radioButton;
    }

    public JMenuItem  getMenuItem() {
        return menuItem;
    }

    public String interpret(byte[] bytes, Endianess endianess) {
        return interpreter.consume(bytes, endianess);
    }

    public static ArrayList<InterpMethod> getMethods() {
        ArrayList<InterpMethod> methods = new ArrayList<>();
        // SHIFT + V for view as is, for copy pasting
        methods.add(new asview(KeyEvent.VK_V, 0));
        // SHIFT + U for UTF-8
        methods.add(new utf8(KeyEvent.VK_U, 0));
        // SHIFT + I for int16
        methods.add(new int16(KeyEvent.VK_I, 0));
        // SHIFT + CTRL + I for uint16
        methods.add(new uint16(KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK));
        // SHIFT + F for float32
        methods.add(new float32(KeyEvent.VK_F, 0));
        // SHIFT + CTRL + F for float64
        methods.add(new float64(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
        return methods;
    }

    public String getName() {
        return name;
    }

    public static class asview extends InterpMethod {

        public asview(int keycode, int mod) {
            super("view", keycode, mod);

            this.setInterpreter((stuff, endianess) -> {
                StringBuilder sb = new StringBuilder();
                for (byte b : stuff) {
                    sb.append(
                            Main.window.getConverter().applyGrouping(
                                    Main.window.getConverter().view.call(
                                            b
                                    )
                            )
                    );
                    sb.append(" ");
                }

                return sb.toString().trim();
            });
        }
    }

    public static class int16 extends InterpMethod {

        public int16(int keycode, int mod) {
            super("int16", keycode, mod);

            this.setInterpreter((stuff, endianess) -> {
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < stuff.length; i += 2) {

                    if (i + 1 >= stuff.length) {
                        sb.append("? ");
                        break;
                    }

                    int b1 = stuff[i] & 0xFF;
                    int b2 = stuff[i + 1] & 0xFF;

                    int value = switch (endianess) {
                        case BIG -> (b1 << 8) | b2;
                        case LITTLE -> (b2 << 8) | b1;
                    };

                    sb.append((short) value).append(" ");
                }

                return sb.toString().trim();
            });
        }
    }

    public static class uint16 extends InterpMethod {

        public uint16(int keycode, int mod) {
            super("uint16", keycode, mod);

            this.setInterpreter((stuff, endianess) -> {
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < stuff.length; i += 2) {

                    // STRICT: must be exactly 2 bytes
                    if (i + 1 >= stuff.length) {
                        sb.append("? ");
                        continue;
                    }

                    int b1 = stuff[i] & 0xFF;
                    int b2 = stuff[i + 1] & 0xFF;

                    int value = switch (endianess) {
                        case BIG -> (b1 << 8) | b2;
                        case LITTLE -> (b2 << 8) | b1;
                    };

                    sb.append(value).append(" ");
                }

                return sb.toString().trim();
            });
        }
    }

    public static class utf8 extends InterpMethod {
        public utf8(int keycode, int mod) {
            super("UTF-8", keycode, mod);
            this.setInterpreter(
                    (stuff, endianess) -> {
                        // UTF-8 doesn't care about endianess for individual bytes, but for multibyte characters
                        // the byte order is fixed. We'll just decode the bytes as a UTF-8 string.
                        return new String(stuff, java.nio.charset.StandardCharsets.UTF_8);
                    }
            );
        }

    }

    public static class float32 extends InterpMethod {
        public float32(int keycode, int mod) {
            super("float32", keycode, mod);
            this.setInterpreter(
                    (stuff, endianess) -> {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < stuff.length; i += 4) {
                            if (i + 3 >= stuff.length) {
                                sb.append("? ");
                                break;
                            }
                            int intValue = getBits(stuff, endianess, i);
                            sb.append(intValue).append(" ");
                        }
                        return sb.toString().trim();
                    }
            );
        }

        private static int getBits(byte[] stuff, Endianess endianess, int i) {
            int intBits;
            if (endianess == Endianess.BIG) {
                intBits = ((stuff[i] & 0xFF) << 24) |
                        ((stuff[i + 1] & 0xFF) << 16) |
                        ((stuff[i + 2] & 0xFF) << 8) |
                        (stuff[i + 3] & 0xFF);
            } else { // LITTLE
                intBits = ((stuff[i + 3] & 0xFF) << 24) |
                        ((stuff[i + 2] & 0xFF) << 16) |
                        ((stuff[i + 1] & 0xFF) << 8) |
                        (stuff[i] & 0xFF);
            }
            return intBits;
        }
    }

    public static class float64 extends InterpMethod {
        public float64(int keycode, int mod) {
            super("float64", keycode, mod);
            this.setInterpreter(
                    (stuff, endianess) -> {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < stuff.length; i += 8) {
                            if (i + 7 >= stuff.length) {
                                sb.append("? ");
                                break;
                            }
                            long longBits;
                            if (endianess == Endianess.BIG) {
                                longBits = ((long)(stuff[i] & 0xFF) << 56) |
                                        ((long)(stuff[i + 1] & 0xFF) << 48) |
                                        ((long)(stuff[i + 2] & 0xFF) << 40) |
                                        ((long)(stuff[i + 3] & 0xFF) << 32) |
                                        ((long)(stuff[i + 4] & 0xFF) << 24) |
                                        ((long)(stuff[i + 5] & 0xFF) << 16) |
                                        ((long)(stuff[i + 6] & 0xFF) << 8) |
                                        ((long)(stuff[i + 7] & 0xFF));
                            } else { // LITTLE
                                longBits = ((long)(stuff[i + 7] & 0xFF) << 56) |
                                        ((long)(stuff[i + 6] & 0xFF) << 48) |
                                        ((long)(stuff[i + 5] & 0xFF) << 40) |
                                        ((long)(stuff[i + 4] & 0xFF) << 32) |
                                        ((long)(stuff[i + 3] & 0xFF) << 24) |
                                        ((long)(stuff[i + 2] & 0xFF) << 16) |
                                        ((long)(stuff[i + 1] & 0xFF) << 8) |
                                        ((long)(stuff[i] & 0xFF));
                            }
                            sb.append(Double.longBitsToDouble(longBits)).append(" ");
                        }
                        return sb.toString().trim();
                    }
            );
        }

    }
}
