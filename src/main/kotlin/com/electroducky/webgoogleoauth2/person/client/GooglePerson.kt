package com.electroducky.webgoogleoauth2.person.client

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GooglePersonName(val displayName: String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GooglePerson(val names: List<GooglePersonName>)