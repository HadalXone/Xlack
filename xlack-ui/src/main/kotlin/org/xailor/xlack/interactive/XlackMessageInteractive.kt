package org.xailor.xlack.interactive

import com.slack.api.Slack
import com.slack.api.methods.response.chat.ChatDeleteResponse
import com.slack.api.methods.response.chat.ChatPostEphemeralResponse
import com.slack.api.methods.response.chat.ChatPostMessageResponse
import com.slack.api.methods.response.chat.ChatUpdateResponse
import com.slack.api.model.Message
import org.xailor.xlack.XlackUiTools
import org.xailor.xlack.message.XlackMessage
import org.xailor.xlack.message.xlackMessage

interface XlackMessageInteractive {
  val slack: Slack get() = XlackUiTools.slack
  val defaultToken: String get() = XlackUiTools.botUserToken

  fun XlackMessage.post(
    channel: String,
    token: String = defaultToken,
    metadata: Message.Metadata? = null,
    replyBroadcast: Boolean = false,
    threadTs: String? = null,
  ): ChatPostMessageResponse {
    return slack.methods().chatPostMessage {
      it.token(token)
        .channel(channel)
        .metadata(metadata)
        .mrkdwn(enableMarkdown)
        .replyBroadcast(replyBroadcast)
        .threadTs(threadTs)
        .attachments(attachments)
        .blocks(blocks)
        .text(text)
    }
  }

  fun XlackMessage.postEphemeral(
    channel: String,
    userId: String,
    token: String = defaultToken,
    threadTs: String? = null,
  ): ChatPostEphemeralResponse {
    return slack.methods().chatPostEphemeral {
      it.token(token)
        .channel(channel)
        .text(text)
        .user(userId)
        .attachments(attachments)
        .blocks(blocks)
        .threadTs(threadTs)
    }
  }

  fun XlackMessage.postDirectMessage(
    userId: String,
    token: String = defaultToken,
  ): ChatPostMessageResponse {
    val conversation = slack.methods().conversationsOpen {
      it.token(token).users(listOf(userId))
    }
    return post(channel = conversation.channel.id, token = token)
  }

  fun deleteMessage(
    channel: String,
    messageTs: String,
    token: String = defaultToken,
  ): ChatDeleteResponse {
    return slack.methods().chatDelete {
      it.token(token)
        .channel(channel)
        .ts(messageTs)
    }
  }

  fun XlackMessage.update(
    channel: String,
    messageTs: String,
    token: String = defaultToken,
  ): ChatUpdateResponse {
    return slack.methods().chatUpdate {
      it.token(token)
        .channel(channel)
        .ts(messageTs)
        .text(text)
        .blocks(blocks)
        .attachments(attachments)
    }
  }

  fun getMessage(
    channel: String,
    messageTs: String,
    token: String = defaultToken,
  ): Message? {
    return slack.methods().conversationsHistory {
      it.token(token)
        .channel(channel)
        .latest(messageTs)
        .inclusive(true)
        .limit(1)
    }
      .messages
      ?.singleOrNull()
  }

  fun findMessages(
    channel: String,
    token: String = defaultToken,
    filter: (Message) -> Boolean,
  ): List<Message> {
    return slack.methods().conversationsHistory {
      it.token(token)
        .channel(channel)
    }
      .messages
      ?.filter(filter)
      .orEmpty()
  }

  fun String.toPlaintextMessage(): XlackMessage {
    return xlackMessage {
      plaintext(this@toPlaintextMessage)
    }
  }

  fun String.toMarkdownMessage(): XlackMessage {
    return xlackMessage {
      markdown(this@toMarkdownMessage)
    }
  }
}
