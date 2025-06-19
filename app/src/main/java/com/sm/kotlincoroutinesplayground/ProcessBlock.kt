package com.sm.kotlincoroutinesplayground

data class ProcessBlock(
    val id: String,
    val description: String,
    var state: ProcessBlockState = ProcessBlockState.FINISHED,
    var result: String
)