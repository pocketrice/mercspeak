package no.mincci.mercspeak;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class ClassSelCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("selclass").then(
                ClientCommandManager.argument("class", new MercenaryArgumentType())
                    .executes(ClassSelCommand::selectClass)
        ));
    }

    private static int selectClass(CommandContext<FabricClientCommandSource> context) {
        Mercenary merc = context.getArgument("mercenary", Mercenary.class);
        MercspeakClient.currentMerc = merc;
        context.getSource().sendFeedback(Component.literal("Client mercenary set to %s".formatted(merc)));
        return 1;
    }
}
