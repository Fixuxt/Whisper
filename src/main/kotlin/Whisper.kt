package com.fixuxt.mirai.whisper

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.events.GroupTempMessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.buildMessageChain
import net.mamoe.mirai.utils.info

object Whisper : KotlinPlugin(
    JvmPluginDescription(
        id = "com.fixuxt.mirai.whisper",
        name = "Whisper",
        version = "1.0.0",
    ) {
        author("fixuxt")
        info("""通过群发私聊消息来实现群聊功能""")
    }
) {
    override fun onEnable() {
        logger.info { "Whisper Plugin loaded" }

        val globalEventChannel = globalEventChannel()
        globalEventChannel.subscribeAlways<GroupTempMessageEvent> {
            sender.group.members
                .filter { member -> member.id != sender.id && member.id != bot.id }
                .forEach { member ->
                    member.sendMessage(buildMessageChain {
                        +PlainText("[${sender.group.name}]${sender.nick}:\n")
                        +it.message
                    })
                }
        }
    }
}