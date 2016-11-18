package com.palechip.hudpixelmod.util

import com.palechip.hudpixelmod.HudPixelMod
import com.palechip.hudpixelmod.extended.HudPixelExtendedEventHandler
import com.palechip.hudpixelmod.extended.util.IEventHandler
import com.palechip.hudpixelmod.extended.util.LoggerHelper.logInfo
import com.palechip.hudpixelmod.extended.util.LoggerHelper.logWarn
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.Minecraft.getMinecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.entity.RenderPlayer
import net.minecraft.client.renderer.entity.layers.LayerRenderer
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.MathHelper
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.opengl.GL11
import java.awt.image.BufferedImage
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors.newSingleThreadExecutor
import java.util.concurrent.Future
import javax.imageio.ImageIO.read

@SideOnly(Side.CLIENT)
object ContributorFancinessHandler : LayerRenderer<EntityPlayer> {

    init {
        val skinMap = Minecraft.getMinecraft().renderManager.skinMap
        var render: RenderPlayer?
        render = skinMap["default"]
        render?.addLayer(this)
        render = skinMap["slim"]
        render?.addLayer(this)
    }

    override fun doRenderLayer(player: EntityPlayer, limbSwing: Float, limbSwingAmount: Float, partialTicks: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scale: Float) {
        val name = player.name
        val yaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * partialTicks
        val yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks
        val pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks
        GlStateManager.pushMatrix()
        GlStateManager.rotate(yawOffset, 0f, -1f, 0f)
        GlStateManager.rotate(yaw - 270, 0f, 1f, 0f)
        GlStateManager.rotate(pitch, 0f, 0f, 1f)
        firstStart()
        if (!displayYourOwn) {
            stacks.remove(Minecraft.getMinecraft().thePlayer.name)
        }
        if (stacks.keys.map({ s ->
            s?.toLowerCase()
        }).contains(name.toLowerCase()))
            renderStack(player, stacks[name.toLowerCase()])
        GlStateManager.popMatrix()
    }

    override fun shouldCombineTextures(): Boolean {
        return false
    }

    private class ThreadContributorListLoader : Thread() {
        init {
            name = "Contributor Fanciness Thread"
            isDaemon = true
            start()
        }

        override fun run() {
            try {
                val url = URL("https://raw.githubusercontent.com/Eladkay/static/master/HudPixelSupporters")
                val props = Properties()
                InputStreamReader(url.openStream()).use { reader ->
                    props.load(reader)
                    load(props)
                }
            } catch (e: IOException) {
                HudPixelMod.logger!!.info("Could not load contributors list. Either you're offline or github is down. Nothing to worry about, carry on~")
            }

        }
    }

    class LoadImgur(private val thing: String, private val callback: IImgurCallback) : IEventHandler {

        var resourceLocation: ResourceLocation? = null
        var origin: String? = null
        private var image: BufferedImage? = null
        private var imageLoaded = false
        private var imageSetup = false

        init {
            HudPixelExtendedEventHandler.registerIEvent(this)
            loadFromURL()
        }

        private fun setupImage() {
            imageSetup = true
            if (image == null) {
                callback.onImgurCallback(null)
                HudPixelExtendedEventHandler.unregisterIEvent(this)
                return
            }
            val texture = DynamicTexture(image!!)
            resourceLocation = getMinecraft().textureManager.getDynamicTextureLocation(thing, texture)
            logInfo("[LoadContributors]: Loaded info for $thing @ http://i.imgur.com/$thing.png")
            callback.onImgurCallback(this.resourceLocation)
            HudPixelExtendedEventHandler.unregisterIEvent(this)
        }

        private fun loadFromURL() {

            object : Thread() {
                override fun run() {

                    val service: ExecutorService

                    service = newSingleThreadExecutor()
                    var pair: Pair<BufferedImage, String>? = null
                    val future: Future<Pair<BufferedImage, String>>
                    val failed: Boolean
                    try {
                        future = service.submit(CallURL())
                        pair = future.get()
                    } catch (ex: InterruptedException) {
                        logWarn("[LoadContributors]: Something went wrong while loading the contributor info for" + thing)
                        ex.printStackTrace()
                    } catch (ex: ExecutionException) {
                        logWarn("[LoadContributors]: Something went wrong while loading the contributor info for" + thing)
                        ex.printStackTrace()
                        try {
                            image = read(URL("http://i.imgur.com/$thing.png"))
                            imageLoaded = true
                        } catch (e: MalformedURLException) {
                            failed = true
                            logWarn("[LoadContributors]: Couldn't load contributor info for $thing @ http://i.imgur.com/$thing.png")
                        } catch (e: IOException) {
                            failed = true
                            logWarn("[LoadContributors]: Couldn't read contributor info for $thing @ http://i.imgur.com/$thing.png")
                        }

                    }

                    image = pair!!.component1()
                    origin = pair.component2()
                    logInfo("[LoadContributors]: Info loaded for " + thing)


                    imageLoaded = true

                    service.shutdownNow()
                }
            }.start()
        }

        override fun onClientTick() {
            if (imageLoaded && !imageSetup) setupImage()
        }

        override fun onChatReceived(e: ClientChatReceivedEvent) {

        }

        override fun onRender() {

        }

        override fun handleMouseInput(i: Int, mX: Int, mY: Int) {

        }

        override fun onMouseClick(mX: Int, mY: Int) {

        }

        /**
         * Helper class to get the image via url request and filereader
         */
        internal inner class CallURL : Callable<Pair<BufferedImage, String>> {

            @Throws(Exception::class)
            override fun call(): Pair<BufferedImage, String> {
                return Pair(read(URL("http://i.imgur.com/$thing.png")), "http://i.imgur.com/$thing.png")
            }
        }

    }
    @Volatile private var stacks: MutableMap<String, Option<ItemStack, LoadImgur>> = HashMap()
    @Volatile private var startedLoading = false

    //https://www.youtube.com/watch?v=41aGCrXM20E
    //@ConfigPropertyBoolean(category = CCategory.HUDPIXEL, id = "displayYourOwn", comment = "Should display your own Contributor Render? (If you don't know what that is, ignore it)", def = true)
    var displayYourOwn = true



    private fun firstStart() {
        if (!startedLoading) {
            ThreadContributorListLoader()
            startedLoading = true
        }
    }

    private fun load(props: Properties) {
        stacks = HashMap<String, Option<ItemStack, LoadImgur>>()
        println("Fanciness time!")
        for (key in props.stringPropertyNames()) {
            val value = props.getProperty(key)
            println("Loading fanciness for $key: $value")
            try {
                val i = Integer.parseInt(value)
                stacks.put(key.toLowerCase(), Option<ItemStack, LoadImgur>(ItemStack(Item.getItemById(i)), null))
            } catch (e: NumberFormatException) {
                if (Item.getByNameOrId("minecraft:" + value) != null) {
                    stacks.put(key.toLowerCase(), Option<ItemStack, LoadImgur>(ItemStack(Item.getByNameOrId("minecraft:" + value)), null))
                } else if (Block.getBlockFromName("minecraft:" + value) != null) {
                    stacks.put(key.toLowerCase(), Option<ItemStack, LoadImgur>(ItemStack(Block.getBlockFromName("minecraft:" + value)!!), null))
                } else {
                    stacks.put(key.toLowerCase(), Option<ItemStack, LoadImgur>(null, LoadImgur(value, IImgurCallback{  st -> })))
                }
            }

        }
    }

    private fun renderStack(player: EntityPlayer, `object`: Any?) {
        if (`object` is ItemStack) {
            GlStateManager.pushMatrix()
            translateToHeadLevel(player)
            getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
            GlStateManager.rotate(180f, 0f, 0f, 1f)
            GlStateManager.translate(0.0, -0.85, 0.0)
            GlStateManager.rotate(-90f, 0f, 1f, 0f)
            if (`object`.item is ItemBlock)
                GlStateManager.scale(1f, 1f, 1f)
            else
                GlStateManager.scale(0.5, 0.5, 0.5)
            getMinecraft().renderItem.renderItem(`object`, ItemCameraTransforms.TransformType.NONE)
            GlStateManager.popMatrix()
        } else if (`object` is LoadImgur) {
            GlStateManager.pushMatrix()
            translateToHeadLevel(player)
            GlStateManager.rotate(180f, 0f, 0f, 1f)
            GlStateManager.translate(0.0, -0.85, 0.0)
            GlStateManager.rotate(-90f, 0f, 1f, 0f)
            GlStateManager.rotate(90f, 0f, 0f, 1f)
            GlStateManager.scale(1f, 1f, 1f)
            if (`object`.resourceLocation == null) return
            getMinecraft().renderEngine.bindTexture(`object`.resourceLocation!!)
            GlStateManager.enableTexture2D()
            val width = 1.0
            val height = 1.0
            val left = width / 2
            val bottom = height / 4

            val tessellator = Tessellator.getInstance()
            val vb = tessellator.buffer

            vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
            vb.pos(height - bottom, -left, 0.0).tex(0.0, 0.0).endVertex()
            vb.pos(height - bottom, width - left, 0.0).tex(1.0, 0.0).endVertex()
            vb.pos(-bottom, width - left, 0.0).tex(1.0, 1.0).endVertex()
            vb.pos(-bottom, -left, 0.0).tex(0.0, 1.0).endVertex()
            tessellator.draw()
            GlStateManager.disableTexture2D()
            GlStateManager.popMatrix()
        }
    }

    private fun translateToHeadLevel(player: EntityPlayer) {
        GlStateManager.translate(0f, -player.defaultEyeHeight, 0f)
        if (player.isSneaking)
            GlStateManager.translate(0.25f * MathHelper.sin(player.rotationPitch * Math.PI.toFloat() / 180), 0.25f * MathHelper.cos(player.rotationPitch * Math.PI.toFloat() / 180), 0f)
    }
}