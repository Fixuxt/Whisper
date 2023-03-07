package com.fixuxt.mirai.whisper

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info

object Whisper : KotlinPlugin(
    JvmPluginDescription(
        id = "com.fixuxt.mirai.whisper",
        name = "Whisper",
        version = "0.1.0",
    ) {
        author("fixuxt")
        info("""通过群发私聊消息来实现群聊功能""")
    }
) {
    override fun onEnable() {
        logger.info { "Plugin loaded" }
    }
}