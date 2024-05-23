package com.letianpai.robot.wificonnet.parser;

public class VersionData {

    private String version;
    private String rom_version;
    private String mcu_version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRom_version() {
        return rom_version;
    }

    public void setRom_version(String rom_version) {
        this.rom_version = rom_version;
    }

    public String getMcu_version() {
        return mcu_version;
    }

    public void setMcu_version(String mcu_version) {
        this.mcu_version = mcu_version;
    }

    @Override
    public String toString() {
        return "{" +
                "version:'" + version + '\'' +
                ", rom_version:'" + rom_version + '\'' +
                ", mcu_version:'" + mcu_version + '\'' +
                '}';
    }
}
