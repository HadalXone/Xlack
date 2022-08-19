package org.arxing.xlack.message

import com.slack.api.model.Field

fun AttachmentMessage(
  text: String,
  color: String,
  title: String? = null,
  fields: List<Pair<String, String>>? = null,
) : XlackMessage {
  return xlackMessage {
    attachments {
      attachment {
        title(title)
        text(text)
        color(color)
        fields(
          fields?.map { (name, value) ->
            Field.builder()
              .title(name)
              .value(value)
              .valueShortEnough(value.length <= 15)
              .build()
          }
        )
      }
    }
  }
}
