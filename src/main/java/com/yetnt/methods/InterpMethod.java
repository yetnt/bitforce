package com.yetnt.methods;

import javax.swing.*;
import java.util.ArrayList;

public class InterpMethod {
    private JRadioButton radioButton;
    private String name;
    private VAConsumer interpreter;

    public InterpMethod(String name) {
        this.name = name;
        radioButton = new JRadioButton(name);
        radioButton.setToolTipText(name);
    }

    protected InterpMethod setInterpreter(VAConsumer interpreter) {
        this.interpreter = interpreter;
        return this;
    }

    public JRadioButton getRadioButton() {
        return radioButton;
    }

    public String interpret(byte[] bytes, Endianess endianess) {
        return interpreter.consume(bytes, endianess);
    }

    public static ArrayList<InterpMethod> getMethods() {
        ArrayList<InterpMethod> methods = new ArrayList<>();
        methods.add(new uint16());
        methods.add(new int16());
        methods.add(new float32());
        methods.add(new utf8());
        return methods;
    }

    public static class int16 extends InterpMethod {

            public int16() {
                super("int16");

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

        public uint16() {
            super("uint16");

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
        public utf8() {
            super("UTF-8");
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
        public float32() {
            super("float32");
            this.setInterpreter(
                    (stuff, endianess) -> {
                        if (stuff.length != 4) {
                            return "N/A (requires 4 bytes)";
                        }
                        int intBits;
                        if (endianess == Endianess.BIG) {
                            intBits = ((stuff[0] & 0xFF) << 24) |
                                    ((stuff[1] & 0xFF) << 16) |
                                    ((stuff[2] & 0xFF) << 8) |
                                    (stuff[3] & 0xFF);
                        } else { // LITTLE
                            intBits = ((stuff[3] & 0xFF) << 24) |
                                    ((stuff[2] & 0xFF) << 16) |
                                    ((stuff[1] & 0xFF) << 8) |
                                    (stuff[0] & 0xFF);
                        }
                        return String.valueOf(Float.intBitsToFloat(intBits));
                    }
            );
        }

    }
}
