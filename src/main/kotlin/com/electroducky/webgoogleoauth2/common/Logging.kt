package com.electroducky.webgoogleoauth2.common

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified R : Any> R.logger(): Logger = LoggerFactory.getLogger(this::class.java)