package org.iotmapper.server.common.media

import org.springframework.http.MediaType
import java.nio.charset.Charset

private const val APPLICATION_TYPE = "application"
private const val JSON_SUBTYPE = "json"
private const val PROBLEM_JSON_SUBTYPE = "problem+json"
private const val SIREN_SUBTYPE = "vnd.siren+json"
private const val JSON_HOME_SUBTYPE = "json-home"

private val CHARSET = Charset.forName("UTF-8")

val JSON_MEDIA_TYPE = MediaType(APPLICATION_TYPE, JSON_SUBTYPE, CHARSET)
val PROBLEM_JSON_MEDIA_TYPE = MediaType(APPLICATION_TYPE, PROBLEM_JSON_SUBTYPE, CHARSET)
val SIREN_MEDIA_TYPE = MediaType(APPLICATION_TYPE, SIREN_SUBTYPE, CHARSET)
val JSON_HOME_MEDIA_TYPE = MediaType(APPLICATION_TYPE, JSON_HOME_SUBTYPE, CHARSET)

const val JSON_MEDIA_TYPE_VALUE = "$APPLICATION_TYPE/$JSON_SUBTYPE;charset=UTF-8"
const val PROBLEM_JSON_MEDIA_TYPE_VALUE = "$APPLICATION_TYPE/$PROBLEM_JSON_SUBTYPE;charset=UTF-8"
const val SIREN_MEDIA_TYPE_VALUE = "$APPLICATION_TYPE/$SIREN_SUBTYPE;charset=UTF-8"
const val JSON_HOME_MEDIA_TYPE_VALUE = "$APPLICATION_TYPE/$JSON_HOME_SUBTYPE;charset=UTF-8"