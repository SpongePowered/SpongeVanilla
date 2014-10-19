import org.granitemc.granite.api.ServerConfig;
import org.granitemc.granite.api.config.CfgFile;

import java.io.File;

public class GraniteServerConfig implements ServerConfig {
    CfgFile file;

    @Override
    public File getPluginDataDirectory() {
        return new File(file.getString("plugin_"));
    }

    @Override
    public File getPluginDirectory() {
        return null;
    }
}
