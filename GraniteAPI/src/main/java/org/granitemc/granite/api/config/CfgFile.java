package org.granitemc.granite.api.config;

import java.io.File;
import java.io.IOException;

public class CfgFile extends CfgObject {
    File file;

    public CfgFile(File file) {
        load(file);
    }

    public void load(File file) {
        this.file = file;
        loadOnce(file);
    }

    public void loadOnce(File file) {
        try {
            file.getAbsoluteFile().getParentFile().mkdirs();
            if (!file.exists()) file.createNewFile();
            this.value = ((CfgObject) CfgValue.read(file)).value;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() throws IOException {
        write(file);
    }

    public void save(File file) throws IOException {
        write(file);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CfgFile cfgFile = (CfgFile) o;

        if (file != null ? !file.equals(cfgFile.file) : cfgFile.file != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (file != null ? file.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CfgFile{" +
                "file=" + file +
                '}';
    }
}
