package com.palechip.hudpixelmod.api.interaction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import com.palechip.hudpixelmod.HudPixelMod;
import com.palechip.hudpixelmod.api.interaction.callbacks.ApiKeyLoadedCallback;

import cpw.mods.fml.client.FMLClientHandler;

public class ApiKeyHandler {
    private static ApiKeyHandler instance;

    // this gets set to true when the loading fails but is finished
    public boolean loadingFailed = false;

    private String apiKey;
    private ApiKeyLoadedCallback callback;
    private static String API_KEY_STORAGE_PATH;
    private static String API_KEY_STORAGE_FILE;
    private static String API_KEY_REQUEST_MESSAGE_1 = "No API key found. This key is necessary for some cool features.";
    private static String API_KEY_REQUEST_MESSAGE_2 = "Simply do " + EnumChatFormatting.RED + "/api" + EnumChatFormatting.RESET +" for creating a new one.";
    private static String API_KEY_REQUEST_MESSAGE_3 = "You can also add your key manually to config\\hypixel_api_key.txt.";
    private static String API_KEY_REQUEST_MESSAGE_4 = "If you don't want to use the API features, you can disable \"useAPI\" in the config";
    private static String EMPTY_FILE_CONTENT = "Replace this with the api key or do /api on Hypixel Network. This File gets reset when a key doesn't work.";
    private static String API_KEY_PATTERN = "[a-f0-9]{8}[-]([a-f0-9]{4}[-]){3}[a-f0-9]{12}";

    static {
        try {
            // in the config folder
            API_KEY_STORAGE_PATH = FMLClientHandler.instance().getClient().mcDataDir.getCanonicalPath() + File.separatorChar + "config" +File.separatorChar;
            // a file called hypixel_api_key.txt
            API_KEY_STORAGE_FILE = API_KEY_STORAGE_PATH + "hypixel_api_key.txt";
        } catch (IOException e) {
            HudPixelMod.instance().logError("Critical error when finding the api key file: ");
            e.printStackTrace();
        }
    }

    /**
     * Loads the api key.
     * @param callback The class which gets notified upon completion
     */
    public ApiKeyHandler(ApiKeyLoadedCallback callback) {
        instance = this;
        this.callback = callback;
        // load the api in a separate thread
        new Thread() {
            @Override
            public void run() {
                ApiKeyHandler.instance.loadAPIKey();
            }
        }.start();
    }

    public void onChatMessage(String textMessage) {
        if(textMessage.startsWith("Your new API key is ")) {
            // extract the key
            this.apiKey = textMessage.substring(textMessage.indexOf("is ") + 3);
            // let the callback know
            this.callback.ApiKeyLoaded(false, this.apiKey);
            // and save it
            new Thread() {
                @Override
                public void run() {
                    ApiKeyHandler.getInstance().saveAPIKey();
                };
            }.start();
            // tell the user
            FMLClientHandler.instance().getClientPlayerEntity().addChatMessage(new ChatComponentText(HudPixelMod.HUDPIXEL_CHAT_PREFIX + EnumChatFormatting.GREEN + "API key successfully detected and saved. The API is ready for usage."));
        }
    }

    /**
     * Asks the user to do /api
     */
    public void requestApiKey() {
        FMLClientHandler.instance().getClientPlayerEntity().addChatMessage(new ChatComponentText(HudPixelMod.HUDPIXEL_CHAT_PREFIX + API_KEY_REQUEST_MESSAGE_1));
        FMLClientHandler.instance().getClientPlayerEntity().addChatMessage(new ChatComponentText(HudPixelMod.HUDPIXEL_CHAT_PREFIX + API_KEY_REQUEST_MESSAGE_2));
        FMLClientHandler.instance().getClientPlayerEntity().addChatMessage(new ChatComponentText(HudPixelMod.HUDPIXEL_CHAT_PREFIX + API_KEY_REQUEST_MESSAGE_3));
        FMLClientHandler.instance().getClientPlayerEntity().addChatMessage(new ChatComponentText(HudPixelMod.HUDPIXEL_CHAT_PREFIX + API_KEY_REQUEST_MESSAGE_4));
    }

    /**
     * Gets called from a separate thread for loading the api key
     */
    private void loadAPIKey() {
        try {
            // make sure the path exists
            File path = new File(API_KEY_STORAGE_PATH);
            File file = new File(API_KEY_STORAGE_FILE);
            if(!path.exists()) {
                path.mkdirs();
            }

            // check if the file exists
            if(!file.exists()) {
                // there is no file so there can't be an api key
                // create it
                file.createNewFile();
                this.resetApiFile(file);
                this.loadingFailed = true;
                this.callback.ApiKeyLoaded(true, null);
                return;
            }
            // read the key
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String key = reader.readLine();
            reader.close();
            // make sure the content can be a valid key
            if(key == null || key.equals(EMPTY_FILE_CONTENT) || !this.isCorrectKeyFormat(key.replace(" ", ""))) {
                this.resetApiFile(file);
                this.loadingFailed = true;
                this.callback.ApiKeyLoaded(true, null);
                return;
            }
            this.apiKey = key.replace(" ", "");
            this.loadingFailed = false;
            this.callback.ApiKeyLoaded(false, this.apiKey);
        } catch (Exception e) {
            HudPixelMod.instance().logError("Critical error when reading the api key file: ");
            e.printStackTrace();
        }
    }

    /**
     * Gets called from a separate thread for loading the api key
     */
    private void saveAPIKey() {
        try {
            // we don't check whether the file exists since we already do it on startup
            // there is no way the user deletes the file after startup unintentionally
            PrintWriter writer = new PrintWriter(new File(API_KEY_STORAGE_FILE));
            writer.write(this.apiKey);
            writer.close();
        } catch (Exception e) {
            HudPixelMod.instance().logError("Critical error when storing the api key file: ");
            e.printStackTrace();
        }
    }

    /**
     * Empties the api key file and adds a message how to use it.
     */
    private void resetApiFile(File file) throws FileNotFoundException{
        // empty the file
        PrintWriter writer = new PrintWriter(file);
        // fill the file with the empty content
        writer.write(EMPTY_FILE_CONTENT);
        writer.flush();
        writer.close();
    }

    /**
     * Verifies that the key has the correct pattern.
     */
    private boolean isCorrectKeyFormat(String key) {
        return key.matches(API_KEY_PATTERN);
    }


    public static ApiKeyHandler getInstance() {
        return instance;
    }
}