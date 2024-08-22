package com.malism.dot.utils

import io.ktor.server.application.Application

fun Application.info(msg: String) = environment.log.info(msg)
fun Application.info(msg: String, t: Throwable) = environment.log.info(msg, t)
fun Application.info(format: String, vararg args: Any) = environment.log.info(format, *args)

fun Application.debug(msg: String) = environment.log.debug(msg)
fun Application.debug(msg: String, t: Throwable) = environment.log.debug(msg, t)
fun Application.debug(format: String, vararg args: Any) = environment.log.debug(format, *args)

fun Application.warn(msg: String) = environment.log.warn(msg)
fun Application.warn(msg: String, t: Throwable) = environment.log.warn(msg, t)
fun Application.warn(format: String, vararg args: Any) = environment.log.warn(format, *args)

fun Application.error(msg: String) = environment.log.error(msg)
fun Application.error(msg: String, t: Throwable) = environment.log.error(msg, t)
fun Application.error(format: String, vararg args: Any) = environment.log.error(format, *args)