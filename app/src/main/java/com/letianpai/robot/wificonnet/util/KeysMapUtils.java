package com.letianpai.robot.wificonnet.util;

import android.content.Context;

import java.util.ArrayList;

/**
 * 语音命令执行单元
 *
 * @author liujunbin
 */
public class KeysMapUtils {

    private static KeysMapUtils instance;
    private Context mContext;
    private ArrayList<String> keysList = new ArrayList<>();
    private ArrayList<String> smallCharacterList = new ArrayList<>();
    private ArrayList<String> numList = new ArrayList<>();
    private ArrayList<String> specialKeyList = new ArrayList<>();
    private ArrayList<String> numberCodeKeyList = new ArrayList<>();

    private KeysMapUtils(Context context) {
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        initKeysList();
        initSmailCharacterKeysList();
        initNumList();
        initSpecialKeyList();
        initPairingCodeList();
    }

    private void initNumList() {
        numList.add("1");
        numList.add("2");
        numList.add("3");
        numList.add("4");
        numList.add("5");
        numList.add("6");
        numList.add("7");
        numList.add("8");
        numList.add("9");
        numList.add("0");
        numList.add("*");
        numList.add("#");
    }
    private void initPairingCodeList() {
        numberCodeKeyList.add("1");
        numberCodeKeyList.add("2");
        numberCodeKeyList.add("3");
        numberCodeKeyList.add("4");
        numberCodeKeyList.add("5");
        numberCodeKeyList.add("6");
        numberCodeKeyList.add("7");
        numberCodeKeyList.add("8");
        numberCodeKeyList.add("9");
        numberCodeKeyList.add("0");
        numberCodeKeyList.add("");
        numberCodeKeyList.add("");
    }

    private void initKeysList() {
        keysList.add("A");
        keysList.add("B");
        keysList.add("C");
        keysList.add("D");
        keysList.add("E");
        keysList.add("F");
        keysList.add("G");
        keysList.add("H");
        keysList.add("I");
        keysList.add("J");
        keysList.add("K");
        keysList.add("L");
        keysList.add("M");
        keysList.add("N");
        keysList.add("O");
        keysList.add("P");
        keysList.add("Q");
        keysList.add("R");
        keysList.add("S");
        keysList.add("T");
        keysList.add("U");
        keysList.add("V");
        keysList.add("W");
        keysList.add("X");
        keysList.add("Y");
        keysList.add("Z");
        keysList.add("");
        keysList.add("");
    }

    private void initSmailCharacterKeysList() {
        smallCharacterList.add("a");
        smallCharacterList.add("b");
        smallCharacterList.add("c");
        smallCharacterList.add("d");
        smallCharacterList.add("e");
        smallCharacterList.add("f");
        smallCharacterList.add("g");
        smallCharacterList.add("h");
        smallCharacterList.add("i");
        smallCharacterList.add("j");
        smallCharacterList.add("k");
        smallCharacterList.add("l");
        smallCharacterList.add("m");
        smallCharacterList.add("n");
        smallCharacterList.add("o");
        smallCharacterList.add("p");
        smallCharacterList.add("q");
        smallCharacterList.add("r");
        smallCharacterList.add("s");
        smallCharacterList.add("t");
        smallCharacterList.add("u");
        smallCharacterList.add("v");
        smallCharacterList.add("w");
        smallCharacterList.add("x");
        smallCharacterList.add("y");
        smallCharacterList.add("z");
        smallCharacterList.add("");
        smallCharacterList.add("");
    }
    // -/:;()$&@“”.,?!’[]{}#%^*+=_\|~<>€£¥•.,?!’
    //specialKeyList
    private void initSpecialKeyList() {
        specialKeyList.add("-");
        specialKeyList.add("/");
        specialKeyList.add(":");
        specialKeyList.add(";");
        specialKeyList.add("(");
        specialKeyList.add(")");
        specialKeyList.add("$");
        specialKeyList.add("&");
        specialKeyList.add("@");
        specialKeyList.add("\"");
        specialKeyList.add(".");
        specialKeyList.add(",");
        specialKeyList.add("?");
        specialKeyList.add(".");
        specialKeyList.add("!");
        specialKeyList.add("'");
        specialKeyList.add("[");
        specialKeyList.add("]");
        specialKeyList.add("{");
        specialKeyList.add("}");
        specialKeyList.add("#");
        specialKeyList.add("%");
        specialKeyList.add("^");
        specialKeyList.add("*");
        specialKeyList.add("+");
        specialKeyList.add("=");
        specialKeyList.add("≠");
        specialKeyList.add("_");
        specialKeyList.add("\\");
        specialKeyList.add("|");
        specialKeyList.add("~");
        specialKeyList.add("<");
        specialKeyList.add(">");
        specialKeyList.add("≥");
        specialKeyList.add("≤");
        specialKeyList.add("≈");
        specialKeyList.add("≡");
        specialKeyList.add("€");
        specialKeyList.add("£");
        specialKeyList.add("¥");
        specialKeyList.add("₩");
        specialKeyList.add("₹");
        specialKeyList.add("₽");
        specialKeyList.add("฿");
        specialKeyList.add("₪");
        specialKeyList.add("₺");
        specialKeyList.add("•");

        specialKeyList.add("`");

        specialKeyList.add("α");
        specialKeyList.add("β");
        specialKeyList.add("γ");
        specialKeyList.add("δ");
        specialKeyList.add("ε");
        specialKeyList.add("ζ");
        specialKeyList.add("η");
        specialKeyList.add("θ");
        specialKeyList.add("ι");
        specialKeyList.add("κ");
        specialKeyList.add("λ");
        specialKeyList.add("μ");
        specialKeyList.add("ν");
        specialKeyList.add("ξ");
        specialKeyList.add("ο");
        specialKeyList.add("π");
        specialKeyList.add("ρ");
        specialKeyList.add("σ");
        specialKeyList.add("τ");
        specialKeyList.add("υ");
        specialKeyList.add("φ");
        specialKeyList.add("χ");
        specialKeyList.add("ψ");
        specialKeyList.add("ω");

        specialKeyList.add("→");
        specialKeyList.add("Σ");
        specialKeyList.add("∏");
        specialKeyList.add("∫");
        specialKeyList.add("∵");
        specialKeyList.add("∴");
        specialKeyList.add("∃");
        specialKeyList.add("∅");
        specialKeyList.add("∩");
        specialKeyList.add("∪");
        specialKeyList.add("⊆");
        specialKeyList.add("⊂");
        specialKeyList.add("⊄");
        specialKeyList.add("∈");
        specialKeyList.add("∉");
        specialKeyList.add("√");
        specialKeyList.add("∛");
//        specialKeyList.add("^");
        specialKeyList.add("∞");
        specialKeyList.add("°");
        specialKeyList.add("¬");
        specialKeyList.add("∞");
        specialKeyList.add("∧");
        specialKeyList.add("∨");
        specialKeyList.add("¬");
        specialKeyList.add("⊕");
        specialKeyList.add("⊙");
        specialKeyList.add("⊥");
        specialKeyList.add("||");
        specialKeyList.add("∼");
        specialKeyList.add("±");
        specialKeyList.add("《");
        specialKeyList.add("》");
        specialKeyList.add("〈");
        specialKeyList.add("〉");
        specialKeyList.add("「");
        specialKeyList.add("」");

        specialKeyList.add("á");
        specialKeyList.add("é");
        specialKeyList.add("í");
        specialKeyList.add("ó");
        specialKeyList.add("ú");
        specialKeyList.add("");
        specialKeyList.add("");
        specialKeyList.add("");


//        specialKeyList.add("");
//        specialKeyList.add("-/:;()$&@“”.,?!’[]{}#%^*+=_\\|~<>€£¥•.,?!");

    }
    public static KeysMapUtils getInstance(Context context) {
        synchronized (KeysMapUtils.class) {
            if (instance == null) {
                instance = new KeysMapUtils(context.getApplicationContext());
            }
            return instance;
        }
    }

    public ArrayList<String> getSmallCharacterList() {
        return smallCharacterList;
    }

    public ArrayList<String> getKeysList() {
        return keysList;
    }

    public ArrayList<String> getNumList() {
        return numList;
    }

    public ArrayList<String> getSpecialKeyList() {
        return specialKeyList;
    }

    public ArrayList<String> getNumericPairingCodeKeyList() {
        return numberCodeKeyList;
    }
}
