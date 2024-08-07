package gecko10000.fullserverkicker;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Modules.Afk.AfkInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.Map;
import java.util.UUID;

public class JoinListener implements Listener {

    private final FullServerKicker plugin;

    public JoinListener(FullServerKicker plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncJoin(AsyncPlayerPreLoginEvent event) {
        if (Bukkit.getOnlinePlayers().size() < Bukkit.getMaxPlayers()) return;
        UUID toKick = null;
        long oldestAfkTime = Long.MAX_VALUE;
        for (Map.Entry<UUID, AfkInfo> entry : CMI.getInstance().getAfkManager().getAfkPlayersMap().entrySet()) {
            UUID uuid = entry.getKey();
            Player player = Bukkit.getPlayer(uuid);
            if (player == null || player.hasPermission("cmi.command.afk.kickbypass")) {
                continue;
            }
            long afkTime = entry.getValue().getAfkFrom();
            if (afkTime < oldestAfkTime) {
                oldestAfkTime = afkTime;
                toKick = entry.getKey();
            }
        }
        if (toKick == null) {
            return;
        }
        Player player = Bukkit.getPlayer(toKick);
        if (player == null) return;
        Component kickMessage = plugin.getConfig().getComponent("kick-message", MiniMessage.miniMessage());
        Bukkit.getScheduler().runTask(plugin, () -> player.kick(kickMessage));
    }

}
