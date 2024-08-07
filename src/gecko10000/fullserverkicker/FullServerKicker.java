package gecko10000.fullserverkicker;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class FullServerKicker extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);
    }


}
