package com.sm.kotlincoroutinesplayground

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sm.kotlincoroutinesplayground.ui.theme.KotlinCoroutinesPlaygroundTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinCoroutinesPlaygroundTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KotlinCoroutinesPlaygroundTheme {
        MyScreen()
    }
}

@Composable
fun MyScreen(modifier: Modifier = Modifier) {

    val viewModel: MyScreenViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState()
    val firstProcessBlock  = viewModel.firstProcessBlock.collectAsState()
    val secondProcessBlock = viewModel.secondProcessBlock.collectAsState()
    val thirdProcessBlock  = viewModel.thirdProcessBlock.collectAsState()

    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(), onClick = {viewModel.startConcurrentCorountine()}, shape = RoundedCornerShape(5.dp)) {
            Text("Execute async block")
        }

        AnimatedVisibility(visible = uiState.value.started, enter = fadeIn(animationSpec = tween(1000))) {
            Text(text = "Starting ViewModelScope with builder Lunch { ", fontSize = 16.sp, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
        }

        firstProcessBlock.value?.let {
            ProcessBlockItem(processBlock = it)
        }

        secondProcessBlock.value?.let {
            ProcessBlockItem(processBlock = it)
        }

        thirdProcessBlock.value?.let {
            ProcessBlockItem(processBlock = it)
        }

        AnimatedVisibility(visible = uiState.value.started, enter = fadeIn(animationSpec = tween(1000))) {
            Text(text = "}", fontSize = 16.sp, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
        }

    }
}


@Composable
fun ProcessBlockItem(modifier: Modifier = Modifier, processBlock: ProcessBlock) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        var visible by remember { mutableStateOf(false) }


        LaunchedEffect(processBlock.state)  {
            visible = true
        }

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
            exit = fadeOut()
        ) {
            Text(modifier = Modifier.padding(horizontal = 10.dp), fontSize = 12.sp, text = processBlock.description)
        }

        if(processBlock.state == ProcessBlockState.LOADING){
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
                exit = fadeOut()
            ){ CircularProgressIndicator(modifier = Modifier.size(20.dp)) }
        } else {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
                exit = fadeOut()
            ){ Text(fontSize = 12.sp, text = processBlock.result, fontWeight = FontWeight.Bold) }
        }

    }
}