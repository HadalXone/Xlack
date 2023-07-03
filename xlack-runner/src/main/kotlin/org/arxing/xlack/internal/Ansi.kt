package org.arxing.xlack.internal

/**
 * See Also: [wiki](https://en.wikipedia.org/wiki/ANSI_escape_code#Colors)
 */
@Suppress("unused")
enum class AnsiColor(foreground: Int, background: Int) {
  Black(30, 40),
  Red(31, 41),
  Green(32, 42),
  Yellow(33, 43),
  Blue(34, 44),
  Magenta(35, 45),
  Cyan(36, 46),
  White(37, 47),
  Gray(90, 100),
  BrightRed(91, 101),
  BrightGreen(92, 102),
  BrightYellow(93, 103),
  BrightBlue(94, 104),
  BrightMagenta(95, 105),
  BrightCyan(96, 106),
  BrightWhite(97, 107), ;

  private val foregroundCode: String = "\u001B[${foreground}m"
  private val backgroundCode: String = "\u001B[${background}m"
  private val resetCode: String = "\u001B[0m"

  private fun wrap(foreground: Boolean, text: String): String {
    val results = "\u001B\\[\\d+m.*?\u001B\\[0m".toRegex().findAll(text).toList()
    val renderCode = if (foreground) foregroundCode else backgroundCode
    return if (results.isNotEmpty()) {
      val value = results.first().value
      val startIdx = text.indexOf(value)
      val endIdx = startIdx + value.length
      val left = text.slice(0 until startIdx)
      val right = text.slice(endIdx until text.length)
      val center = text.slice(startIdx until startIdx + value.length)
      renderCode + left + resetCode + center + wrap(foreground, right)
    } else {
      renderCode + text + resetCode
    }
  }

  fun foreground(text: String): String = wrap(true, text)

  fun background(text: String): String = wrap(false, text)
}

fun String.fg(ansiColor: AnsiColor): String = ansiColor.foreground(this)

fun String.bg(ansiColor: AnsiColor): String = ansiColor.background(this)
