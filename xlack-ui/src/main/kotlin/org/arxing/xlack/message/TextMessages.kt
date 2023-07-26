package org.arxing.xlack.message

fun textMessage(
  text: String,
  enableMarkdown: Boolean = true,
  color: String? = null,
) = xlackMessage {
  if (color != null) {
    attachments {
      attachment {
        text(text)
        color(color)
      }
    }
  } else {
    if (enableMarkdown) markdown(text)
    else plaintext(text)
  }
}
