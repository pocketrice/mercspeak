package no.mincci.mercspeak;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ModBinds {
    private ModBinds() {}

    protected static final KeyMappingLatch<VCmd> VC_LATCH = new KeyMappingLatch<>();
    protected static final KeyMappingLatch<VNum> VN_LATCH = new KeyMappingLatch<>();
    protected static final KeyMappingLatch<VNum> VNA_LATCH = new KeyMappingLatch<>();
    protected static final Map<VCmd, String[]> VL_MAP = new HashMap<>(8 + 8 + 8);

    private static final KeyMapping.Category CATEGORY_VMENU = KeyMapping.Category.register(Mercspeak.resolveIdPath("voice_menu"));
    private static final KeyMapping.Category CATEGORY_VMISC = KeyMapping.Category.register(Mercspeak.resolveIdPath("voice_misc"));

    private static final KeyMapping BIND_VCMD_A = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_cmd_a", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, CATEGORY_VMENU));
    private static final KeyMapping BIND_VCMD_B = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_cmd_b", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_X, CATEGORY_VMENU));
    private static final KeyMapping BIND_VCMD_C = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.voice_cmd_c", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, CATEGORY_VMENU));

    protected static final KeyMapping BIND_VNUM_1 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.selector_1", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_1, CATEGORY_VMENU));
    protected static final KeyMapping BIND_VNUM_2 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.selector_2", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_2, CATEGORY_VMENU));
    protected static final KeyMapping BIND_VNUM_3 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.selector_3", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_3, CATEGORY_VMENU));
    protected static final KeyMapping BIND_VNUM_4 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.selector_4", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_4, CATEGORY_VMENU));
    protected static final KeyMapping BIND_VNUM_5 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.selector_5", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_5, CATEGORY_VMENU));
    protected static final KeyMapping BIND_VNUM_6 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.selector_6", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_6, CATEGORY_VMENU));
    protected static final KeyMapping BIND_VNUM_7 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.selector_7", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_7, CATEGORY_VMENU));
    protected static final KeyMapping BIND_VNUM_8 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.selector_8", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_8, CATEGORY_VMENU));
    protected static final KeyMapping BIND_VNUM_9 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.selector_9", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_9, CATEGORY_VMENU));
    protected static final KeyMapping BIND_VNUM_0 = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.selector_0", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_0, CATEGORY_VMENU));

    protected static final KeyMapping BIND_KILL = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.killbind", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_EQUAL, CATEGORY_VMISC));
    protected static final KeyMapping BIND_EXPLODE = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.explodebind", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_MINUS, CATEGORY_VMISC));
    protected static final KeyMapping BIND_TAUNT = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.taunt", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, CATEGORY_VMISC));
    protected static final KeyMapping BIND_CLASS_SEL = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.mercspeak.class_select", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_COMMA, CATEGORY_VMISC));

    public static void initialize() {
        Mercspeak.LOGGER.info("Registering {} keybinds!", Mercspeak.MOD_ID);

        String[] vNumsetA = Arrays.stream(VType.category(VCmd.VCMD_A))
                .map(vt -> String.format("text.mercspeak.%s", vt.name().toLowerCase()))
                .toArray(String[]::new);
        String[] vNumsetB = Arrays.stream(VType.category(VCmd.VCMD_B))
                .map(vt -> String.format("text.mercspeak.%s", vt.name().toLowerCase()))
                .toArray(String[]::new);
        String[] vNumsetC = Arrays.stream(VType.category(VCmd.VCMD_C))
                .map(vt -> String.format("text.mercspeak.%s", vt.name().toLowerCase()))
                .toArray(String[]::new);

        VL_MAP.put(VCmd.VCMD_A, vNumsetA);
        VL_MAP.put(VCmd.VCMD_B, vNumsetB);
        VL_MAP.put(VCmd.VCMD_C, vNumsetC);

        VC_LATCH.register(VCmd.VCMD_A, BIND_VCMD_A);
        VC_LATCH.register(VCmd.VCMD_B, BIND_VCMD_B);
        VC_LATCH.register(VCmd.VCMD_C, BIND_VCMD_C);

        VN_LATCH.register(VNum.VNUM_1, BIND_VNUM_1);
        VN_LATCH.register(VNum.VNUM_2, BIND_VNUM_2);
        VN_LATCH.register(VNum.VNUM_3, BIND_VNUM_3);
        VN_LATCH.register(VNum.VNUM_4, BIND_VNUM_4);
        VN_LATCH.register(VNum.VNUM_5, BIND_VNUM_5);
        VN_LATCH.register(VNum.VNUM_6, BIND_VNUM_6);
        VN_LATCH.register(VNum.VNUM_7, BIND_VNUM_7);
        VN_LATCH.register(VNum.VNUM_8, BIND_VNUM_8);

        VNA_LATCH.copy(VN_LATCH);
        VNA_LATCH.register(VNum.VNUM_9, BIND_VNUM_9);
        VNA_LATCH.register(VNum.VNUM_0,  BIND_VNUM_0);
    }
}
