package com.sm.kotlincoroutinesplayground

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import java.util.UUID

class MyScreenViewModel : ViewModel() {
    private val _UIState: MutableStateFlow<MyScreenUIState> = MutableStateFlow(MyScreenUIState())
    private val _FirstProcessBlock: MutableStateFlow<ProcessBlock?> = MutableStateFlow(null)
    private val _SecondProcessBlock: MutableStateFlow<ProcessBlock?> = MutableStateFlow(null)
    private val _ThirdProcessBlock: MutableStateFlow<ProcessBlock?> = MutableStateFlow(null)
    val uiState = _UIState.asStateFlow()
    val firstProcessBlock = _FirstProcessBlock.asStateFlow()
    val secondProcessBlock = _SecondProcessBlock.asStateFlow()
    val thirdProcessBlock = _ThirdProcessBlock.asStateFlow()

    fun startConcurrentCorountine() {

        clearAllProcessBlock()

        viewModelScope.launch {

            delay(1000)

            _UIState.update { it.copy(started = true) }

            delay(1000)

            _FirstProcessBlock.update {
                ProcessBlock(
                    id = UUID.randomUUID().toString(),
                    description = "Firs process block -> ",
                    result = "DONE"
                )
            }
            delay(1000)
            _SecondProcessBlock.update {
                ProcessBlock(
                    id = UUID.randomUUID().toString(),
                    description = "Second ASYNC process block -> ",
                    state = ProcessBlockState.LOADING,
                    result = "DONE"
                )
            }

            async {
                delay(3000)
                _SecondProcessBlock.update {
                    it?.copy(
                       state = ProcessBlockState.FINISHED
                    )
                }
            }

            delay(1000)
            _ThirdProcessBlock.update {
                ProcessBlock(
                    id = UUID.randomUUID().toString(),
                    description = "Third process block -> ",
                    result = "DONE"
                )
            }


        }

    }

    private fun clearAllProcessBlock() {
        _UIState.update { it.copy(started = false) }
        _FirstProcessBlock.value = null
        _SecondProcessBlock.value = null
        _ThirdProcessBlock.value = null
    }

    private fun loadProcessBlockList(): List<ProcessBlock> {
        return listOf(
            ProcessBlock(
                id = UUID.randomUUID().toString(),
                description = "Starting Concurrent Coroutines -> ",
                result = "DONE"
            ),
            ProcessBlock(
                id = UUID.randomUUID().toString(),
                description = "First process dalayed -> ",
                result = "DONE"
            ),
            ProcessBlock(
                id = UUID.randomUUID().toString(),
                description = "Last process not delayed -> ",
                result = "DONE"
            )
        )
    }

}