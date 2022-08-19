package org.arxing.xlack

import com.slack.api.model.Attachment
import com.slack.api.model.Attachment.AttachmentBuilder

class AttachmentsBuilder {
  private val attachments = mutableListOf<Attachment>()

  fun attachment(builder: AttachmentBuilder.() -> Unit) = apply {
    attachments.add(Attachment.builder().also(builder).build())
  }

  fun build(): List<Attachment> = attachments.toList()
}
