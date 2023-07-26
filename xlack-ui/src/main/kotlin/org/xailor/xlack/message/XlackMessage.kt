package org.xailor.xlack.message

import com.slack.api.model.Attachment
import com.slack.api.model.block.LayoutBlock
import com.slack.api.model.kotlin_extension.block.dsl.LayoutBlockDsl
import com.slack.api.model.kotlin_extension.block.withBlocks
import org.xailor.xlack.AttachmentsBuilder

interface XlackMessage {
  val text: String?
  val attachments: List<Attachment>?
  val blocks: List<LayoutBlock>?
  val enableMarkdown: Boolean

  open class ConcreteXlackMessage(
    override val text: String? = null,
    override val attachments: List<Attachment>? = null,
    override val blocks: List<LayoutBlock>? = null,
    override val enableMarkdown: Boolean = true,
  ) : XlackMessage

  class Builder {
    private var text: String? = null
    private var attachments: List<Attachment>? = null
    private var blocks: List<LayoutBlock>? = null
    private var enableMarkdown: Boolean = false

    fun plaintext(text: String) = apply {
      this.enableMarkdown = false
      this.text = text
    }

    fun markdown(markdown: String) = apply {
      this.enableMarkdown = true
      this.text = markdown
    }

    fun attachments(builder: AttachmentsBuilder.() -> Unit) = apply {
      this.attachments = AttachmentsBuilder().also(builder).build()
    }

    fun blocks(builder: LayoutBlockDsl.() -> Unit) = apply {
      this.blocks = withBlocks(builder)
    }

    fun build(): XlackMessage {
      return ConcreteXlackMessage(text, attachments, blocks, enableMarkdown)
    }
  }
}
