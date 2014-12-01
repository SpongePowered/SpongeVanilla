package org.granitemc.granite.api.config;

/*
 * License (MIT)
 *
 * Copyright (c) 2014. Granite Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
