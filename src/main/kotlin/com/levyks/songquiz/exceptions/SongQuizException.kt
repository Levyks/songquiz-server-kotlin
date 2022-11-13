package com.levyks.songquiz.exceptions

import com.levyks.songquiz.enums.ExceptionCode

class SongQuizException(
    val code: ExceptionCode,
    val data: Map<String, Any>? = null
): Exception()
