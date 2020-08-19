package com.ort.lastwolf.fw

import java.time.LocalDate
import java.time.LocalDateTime


class LastwolfDateUtil private constructor() {
    companion object {
        fun currentLocalDateTime(): LocalDateTime {
            return LocalDateTime.now()
        }

        fun currentLocalDate(): LocalDate {
            return LocalDate.now()
        }
    }
}