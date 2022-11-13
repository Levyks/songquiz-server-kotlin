package com.levyks.helpers

import com.fasterxml.jackson.databind.exc.MismatchedInputException


fun getMismatchedInputExceptionFilteredMessage(error: MismatchedInputException): String? {
    val path = error.path[0]
    val from = path.from::class.java

    var message = error.message
    message = message?.split("\n")?.get(0)
    message = message?.replace(from.packageName + ".", "")
    message = message?.replace("com\\.levyks\\..*?\\.".toRegex(), "")
    return message
}