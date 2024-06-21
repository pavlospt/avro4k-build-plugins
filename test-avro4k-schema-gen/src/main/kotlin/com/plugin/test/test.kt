package com.plugin.test

import kotlinx.serialization.Serializable

@Serializable
data class Foo(val foo: String, val bar: String)

@Serializable
data class Bar(val qux: String, val zzzz: String)
