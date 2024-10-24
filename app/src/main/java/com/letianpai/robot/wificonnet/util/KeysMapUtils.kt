package com.letianpai.robot.wificonnet.util

import android.content.Context

/**
 * 语音命令执行单元
 *
 * @author liujunbin
 */
class KeysMapUtils private constructor(context: Context) {
    private var mContext: Context? = null
    @JvmField
    val keysList: ArrayList<String> = ArrayList()
    @JvmField
    val smallCharacterList: ArrayList<String> = ArrayList()
    @JvmField
    val numList: ArrayList<String> = ArrayList()
    @JvmField
    val specialKeyList: ArrayList<String> = ArrayList()
    val numericPairingCodeKeyList: ArrayList<String> = ArrayList()

    init {
        init(context)
    }

    private fun init(context: Context) {
        this.mContext = context
        initKeysList()
        initSmailCharacterKeysList()
        initNumList()
        initSpecialKeyList()
        initPairingCodeList()
    }

    private fun initNumList() {
        numList.add("1")
        numList.add("2")
        numList.add("3")
        numList.add("4")
        numList.add("5")
        numList.add("6")
        numList.add("7")
        numList.add("8")
        numList.add("9")
        numList.add("0")
        numList.add("*")
        numList.add("#")
    }

    private fun initPairingCodeList() {
        numericPairingCodeKeyList.add("1")
        numericPairingCodeKeyList.add("2")
        numericPairingCodeKeyList.add("3")
        numericPairingCodeKeyList.add("4")
        numericPairingCodeKeyList.add("5")
        numericPairingCodeKeyList.add("6")
        numericPairingCodeKeyList.add("7")
        numericPairingCodeKeyList.add("8")
        numericPairingCodeKeyList.add("9")
        numericPairingCodeKeyList.add("0")
        numericPairingCodeKeyList.add("")
        numericPairingCodeKeyList.add("")
    }

    private fun initKeysList() {
        keysList.add("A")
        keysList.add("B")
        keysList.add("C")
        keysList.add("D")
        keysList.add("E")
        keysList.add("F")
        keysList.add("G")
        keysList.add("H")
        keysList.add("I")
        keysList.add("J")
        keysList.add("K")
        keysList.add("L")
        keysList.add("M")
        keysList.add("N")
        keysList.add("O")
        keysList.add("P")
        keysList.add("Q")
        keysList.add("R")
        keysList.add("S")
        keysList.add("T")
        keysList.add("U")
        keysList.add("V")
        keysList.add("W")
        keysList.add("X")
        keysList.add("Y")
        keysList.add("Z")
        keysList.add("")
        keysList.add("")
    }

    private fun initSmailCharacterKeysList() {
        smallCharacterList.add("a")
        smallCharacterList.add("b")
        smallCharacterList.add("c")
        smallCharacterList.add("d")
        smallCharacterList.add("e")
        smallCharacterList.add("f")
        smallCharacterList.add("g")
        smallCharacterList.add("h")
        smallCharacterList.add("i")
        smallCharacterList.add("j")
        smallCharacterList.add("k")
        smallCharacterList.add("l")
        smallCharacterList.add("m")
        smallCharacterList.add("n")
        smallCharacterList.add("o")
        smallCharacterList.add("p")
        smallCharacterList.add("q")
        smallCharacterList.add("r")
        smallCharacterList.add("s")
        smallCharacterList.add("t")
        smallCharacterList.add("u")
        smallCharacterList.add("v")
        smallCharacterList.add("w")
        smallCharacterList.add("x")
        smallCharacterList.add("y")
        smallCharacterList.add("z")
        smallCharacterList.add("")
        smallCharacterList.add("")
    }

    // -/:;()$&@“”.,?!’[]{}#%^*+=_\|~<>€£¥•.,?!’
    //specialKeyList
    private fun initSpecialKeyList() {
        specialKeyList.add("-")
        specialKeyList.add("/")
        specialKeyList.add(":")
        specialKeyList.add(";")
        specialKeyList.add("(")
        specialKeyList.add(")")
        specialKeyList.add("$")
        specialKeyList.add("&")
        specialKeyList.add("@")
        specialKeyList.add("\"")
        specialKeyList.add(".")
        specialKeyList.add(",")
        specialKeyList.add("?")
        specialKeyList.add(".")
        specialKeyList.add("!")
        specialKeyList.add("'")
        specialKeyList.add("[")
        specialKeyList.add("]")
        specialKeyList.add("{")
        specialKeyList.add("}")
        specialKeyList.add("#")
        specialKeyList.add("%")
        specialKeyList.add("^")
        specialKeyList.add("*")
        specialKeyList.add("+")
        specialKeyList.add("=")
        specialKeyList.add("≠")
        specialKeyList.add("_")
        specialKeyList.add("\\")
        specialKeyList.add("|")
        specialKeyList.add("~")
        specialKeyList.add("<")
        specialKeyList.add(">")
        specialKeyList.add("≥")
        specialKeyList.add("≤")
        specialKeyList.add("≈")
        specialKeyList.add("≡")
        specialKeyList.add("€")
        specialKeyList.add("£")
        specialKeyList.add("¥")
        specialKeyList.add("₩")
        specialKeyList.add("₹")
        specialKeyList.add("₽")
        specialKeyList.add("฿")
        specialKeyList.add("₪")
        specialKeyList.add("₺")
        specialKeyList.add("•")

        specialKeyList.add("`")

        specialKeyList.add("α")
        specialKeyList.add("β")
        specialKeyList.add("γ")
        specialKeyList.add("δ")
        specialKeyList.add("ε")
        specialKeyList.add("ζ")
        specialKeyList.add("η")
        specialKeyList.add("θ")
        specialKeyList.add("ι")
        specialKeyList.add("κ")
        specialKeyList.add("λ")
        specialKeyList.add("μ")
        specialKeyList.add("ν")
        specialKeyList.add("ξ")
        specialKeyList.add("ο")
        specialKeyList.add("π")
        specialKeyList.add("ρ")
        specialKeyList.add("σ")
        specialKeyList.add("τ")
        specialKeyList.add("υ")
        specialKeyList.add("φ")
        specialKeyList.add("χ")
        specialKeyList.add("ψ")
        specialKeyList.add("ω")

        specialKeyList.add("→")
        specialKeyList.add("Σ")
        specialKeyList.add("∏")
        specialKeyList.add("∫")
        specialKeyList.add("∵")
        specialKeyList.add("∴")
        specialKeyList.add("∃")
        specialKeyList.add("∅")
        specialKeyList.add("∩")
        specialKeyList.add("∪")
        specialKeyList.add("⊆")
        specialKeyList.add("⊂")
        specialKeyList.add("⊄")
        specialKeyList.add("∈")
        specialKeyList.add("∉")
        specialKeyList.add("√")
        specialKeyList.add("∛")
        //        specialKeyList.add("^");
        specialKeyList.add("∞")
        specialKeyList.add("°")
        specialKeyList.add("¬")
        specialKeyList.add("∞")
        specialKeyList.add("∧")
        specialKeyList.add("∨")
        specialKeyList.add("¬")
        specialKeyList.add("⊕")
        specialKeyList.add("⊙")
        specialKeyList.add("⊥")
        specialKeyList.add("||")
        specialKeyList.add("∼")
        specialKeyList.add("±")
        specialKeyList.add("《")
        specialKeyList.add("》")
        specialKeyList.add("〈")
        specialKeyList.add("〉")
        specialKeyList.add("「")
        specialKeyList.add("」")

        specialKeyList.add("á")
        specialKeyList.add("é")
        specialKeyList.add("í")
        specialKeyList.add("ó")
        specialKeyList.add("ú")
        specialKeyList.add("")
        specialKeyList.add("")
        specialKeyList.add("")


        //        specialKeyList.add("");
//        specialKeyList.add("-/:;()$&@“”.,?!’[]{}#%^*+=_\\|~<>€£¥•.,?!");
    }

    companion object {
        private var instance: KeysMapUtils? = null
        @JvmStatic
        fun getInstance(context: Context): KeysMapUtils? {
            synchronized(KeysMapUtils::class.java) {
                if (instance == null) {
                    instance = KeysMapUtils(context.applicationContext)
                }
                return instance
            }
        }
    }
}
