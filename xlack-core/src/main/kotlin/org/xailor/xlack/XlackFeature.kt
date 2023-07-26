package org.xailor.xlack

import com.slack.api.bolt.App

interface XlackFeature {

  fun configure(app: App)
}
