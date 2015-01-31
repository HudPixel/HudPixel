package com.palechip.hudpixelmod;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import com.palechip.hudpixelmod.api.interaction.Queue;
import com.palechip.hudpixelmod.detectors.GameDetector;
import com.palechip.hudpixelmod.detectors.GameStartStopDetector;
import com.palechip.hudpixelmod.detectors.HypixelNetworkDetector;
import com.palechip.hudpixelmod.games.Game;
import com.palechip.hudpixelmod.util.ScoreboardReader;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

@Mod(modid = HudPixelMod.MODID, version = HudPixelMod.VERSION, name = HudPixelMod.NAME, guiFactory = "com.palechip.hudpixelmod.HudPixelGuiFactory")
public class HudPixelMod
{
    public static final String MODID = "hudpixel";
    public static final String NAME = "HudPixel Reloaded";
    public static final String VERSION = "2.3.1";
    public static final boolean IS_DEBUGGING = false;
    public static final String HUDPIXEL_CHAT_PREFIX = "[" + EnumChatFormatting.RED + "HudPixel" + EnumChatFormatting.RESET + "] ";

    private static HudPixelMod instance;

    public Logger LOGGER;
    public HudPixelConfig CONFIG;
    private HudPixelUpdateNotifier  updater;
    private boolean isUpdateMessageQueued;
    public HudPixelRenderer renderer;
    private Queue apiQueue;

    private HypixelNetworkDetector hypixelDetector;
    public GameDetector gameDetector;
    private GameStartStopDetector gameStartStopDetector;

    // key related vars
    public static final String KEY_CATEGORY = "HudPixel Mod";
    private KeyBinding hideHUDKey;
    private KeyBinding openConfigGui;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        try {
            instance = this;
            // check for updates
            this.updater = new HudPixelUpdateNotifier();
            // Initialize the logger
            this.LOGGER = LogManager.getLogger("HudPixel");
            // load the configuration file
            this.CONFIG = new HudPixelConfig(event.getSuggestedConfigurationFile());
            this.CONFIG.syncConfig();
            // Load the API key
            this.apiQueue = new Queue();
        } catch(Exception e) {
            this.logWarn("An exception occured in preInit(). Stacktrace below.");
            e.printStackTrace();
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // register this class as an event handler
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);

        // initialize stuff
        this.hypixelDetector = new HypixelNetworkDetector();
        this.gameDetector = new GameDetector();
        this.gameStartStopDetector = new GameStartStopDetector(this.gameDetector);
        this.renderer = new HudPixelRenderer(this.updater);

        // Initialize key bindings
        this.hideHUDKey = new KeyBinding("Hide HUD", Keyboard.KEY_F9, KEY_CATEGORY);
        this.openConfigGui = new KeyBinding("Open Config", Keyboard.KEY_P, KEY_CATEGORY);
        ClientRegistry.registerKeyBinding(this.hideHUDKey);
        ClientRegistry.registerKeyBinding(this.openConfigGui);
    }

    @SubscribeEvent
    public void onGuiShow(GuiOpenEvent event) {
        try {
            // check if the player is on Hypixel Network
            // this can only change when a new gui is opened
            this.hypixelDetector.check();

            // pass the event to the GameDetector
            this.gameDetector.onGuiShow(event.gui);
        } catch(Exception e) {
            this.logWarn("An exception occured in onGuiShow(). Stacktrace below.");
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        try {
            // this one reads the normal chat messages
            if(event.type == 0) {
                // pass the event to the GameDetector
                this.gameDetector.onChatMessage(event.message.getUnformattedText(), event.message.getFormattedText());

                // pass the chat messages to the current game
                if(this.gameDetector.getCurrentGame() != null && this.gameDetector.getCurrentGame().hasGameStarted()) {
                    this.gameDetector.getCurrentGame().onChatMessage(event.message.getUnformattedText(), event.message.getFormattedText());
                }

                // check for start and stop
                this.gameStartStopDetector.onChatMessage(event.message.getUnformattedText(), event.message.getFormattedText());

                // pass the message to the api connection
                this.apiQueue.onChatMessage(event.message.getUnformattedText());

                // and the booster display needs it as well
                this.renderer.boosterDisplay.onChatMessage(event.message.getUnformattedText(), event.message.getFormattedText());
            } 
            // this one are the messages on the status bar
            else {
                // not used right now
            }
        } catch(Exception e) {
            this.logWarn("An exception occured in onChatMessage(). Stacktrace below.");
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
        try {
            // make sure the Scoreboard reader updates when necessary
            ScoreboardReader.resetCache();
            
            // update the resolution and the result display, this renders nothing
            this.renderer.onClientTick();
            
            // pass the event to the GameDetector
            this.gameDetector.onClientTick();

            if(this.gameDetector.getCurrentGame() != null) {
                // tick the current game
                if(this.gameDetector.getCurrentGame().hasGameStarted()) {
                    this.gameDetector.getCurrentGame().onTickUpdate();
                }
                // update render strings
                this.gameDetector.getCurrentGame().updateRenderStrings();
            }

            this.apiQueue.onClientTick();
            
            this.renderer.boosterDisplay.onClientTick();

            // check if the update message can be displayed
            if(this.isUpdateMessageQueued) {
                this.updateFound();
            }
        } catch(Exception e) {
            this.logWarn("An exception occured in onClientTick(). Stacktrace below.");
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onRenderTick(RenderTickEvent event) {
        try {
            // render the game
            this.renderer.onRenderTick();
        } catch(Exception e) {
            this.logWarn("An exception occured in onRenderTick(). Stacktrace below.");
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        // check all listened keys
        if(this.hideHUDKey.isPressed()) {
            this.renderer.isHUDShown = !this.renderer.isHUDShown;
        }
        if(this.openConfigGui.isPressed()) {
            // open the config screen
            FMLClientHandler.instance().getClient().displayGuiScreen(new HudPixelConfigGui(null));
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if(eventArgs.modID.equals(MODID)){
            this.CONFIG.syncConfig();
            // reload stuff that uses the config values for immediate effect
            this.renderer.loadRenderingProperties(updater);
            Game.loadGames();
        }
    }
    
    @SubscribeEvent
    public void onInitGui(InitGuiEvent event) {
        // pass the event to the booster display
        // used to inject the tip-all button into GuiChat
        this.renderer.boosterDisplay.onInitGui(event);
    }
    
    @SubscribeEvent
    public void onGuiActionPerformed(ActionPerformedEvent event) {
        // notify all classes which have registered a button using on init gui
        this.renderer.boosterDisplay.onGuiActionPerformed(event);
    }
    
    public void updateFound() {
        try {
            if(hypixelDetector.isHypixelNetwork && FMLClientHandler.instance().getClientPlayerEntity() != null) {
                FMLClientHandler.instance().getClientPlayerEntity().addChatMessage(new ChatComponentText(HUDPIXEL_CHAT_PREFIX + EnumChatFormatting.DARK_PURPLE + "Update available: " + EnumChatFormatting.GREEN + this.updater.newVersion));
                FMLClientHandler.instance().getClientPlayerEntity().addChatMessage(new ChatComponentText(HUDPIXEL_CHAT_PREFIX + EnumChatFormatting.DARK_PURPLE + "Download here: " + EnumChatFormatting.YELLOW + this.updater.downloadLink));
                this.isUpdateMessageQueued = false;
                // also update rendering vars to make sure it notices the update
                this.renderer.loadRenderingProperties(this.updater);
                
            } else {
                // make this being called from onTick()
                this.isUpdateMessageQueued = true;
            }
        } catch(Exception e) {
            this.logWarn("An exception occured in updateFound(). Stacktrace below.");
            e.printStackTrace();
        }
    }

    public static HudPixelMod instance() {
        return instance;
    }

    public void logDebug(String s) {
        if(IS_DEBUGGING) {
            this.LOGGER.info("[DEBUG] "  + s);
        }
    }

    public void logInfo(String s) {
        this.LOGGER.info(s);
    }

    public void logWarn(String s) {
        this.LOGGER.warn(s);
    }

    public void logError(String s) {
        this.LOGGER.error(s); 	
    }
}
