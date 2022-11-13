package com.levyks.exceptions

import com.levyks.enums.ExceptionCode

class SongQuizException(
    val code: ExceptionCode,
    val data: Map<String, Any>? = null
): Exception()
