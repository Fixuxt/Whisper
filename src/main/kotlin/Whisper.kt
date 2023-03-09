package com.fixuxt.mirai.whisper

import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.GroupAwareCommandSender
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value
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
        WhisperConfig.reload()

        CommandManager.registerCommand(WhisperCommend)

        val globalEventChannel = globalEventChannel()
        globalEventChannel.subscribeAlways<GroupTempMessageEvent> {
            if (WhisperConfig.enabledGroups.contains(group.id)) {
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

        logger.info { "Whisper Plugin loaded" }
    }
}

object WhisperConfig : AutoSavePluginConfig("WhisperConfig") {
    val enabledGroups: MutableSet<Long> by value()
}

object WhisperCommend : CompositeCommand(Whisper, "whisper") {
    @SubCommand()
    suspend fun GroupAwareCommandSender.on() {
        WhisperConfig.enabledGroups.add(group.id)
        group.sendMessage("whisper已启动，私聊【${bot.nick}】即可全员转发")
    }

    @SubCommand()
    suspend fun GroupAwareCommandSender.off() {
        WhisperConfig.enabledGroups.remove(group.id)
        group.sendMessage("whisper已关闭")
    }

}