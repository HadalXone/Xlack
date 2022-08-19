package org.arxing.xlack

import com.slack.api.model.Action

class ActionsBuilder {
  private val actions = mutableListOf<Action>()

  fun action(builder: Action.ActionBuilder.() -> Unit) = apply {
    val action = Action.builder()
      .build()
    actions.add(action)
  }

  fun build(): List<Action> = actions.toList()
}
