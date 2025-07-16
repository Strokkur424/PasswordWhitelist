package net.strokkur.passwordwhitelist;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.strokkur.passwordwhitelist.commands.PasswordWhitelistCommandBrigadier;

@SuppressWarnings("UnstableApiUsage")
public class PasswordWhitelistBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(
            event -> PasswordWhitelistCommandBrigadier.register(event.registrar())
        ));
    }
}
