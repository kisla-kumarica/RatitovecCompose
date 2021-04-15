package com.example.ratitoveccompose

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ratitoveccompose.data.Pohod
import com.example.ratitoveccompose.data.PohodDAO
import com.example.ratitoveccompose.data.PohodiViewModel
import com.example.ratitoveccompose.data.PohodiViewModelFactory
import com.example.ratitoveccompose.ui.theme.RatitovecComposeTheme
import com.google.android.material.internal.ContextUtils
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RatitovecComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Base()
                }
            }
        }
    }
}

@Composable
fun Base() {
    val viewModel: PohodiViewModel = viewModel(factory = PohodiViewModelFactory(LocalContext.current))
    Column {
        TopAppBar(
            title = {
                Text(text = stringResource(R.string.app_name))
            },
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu Btn")
                }
            },
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
            elevation = 2.dp
        )
        Text(text = "Hello World!", modifier = Modifier.align(Alignment.CenterHorizontally))
        Button(onClick = { viewModel.Insert(Pohod(Date().time)) },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)) {
            Text(text = "Dodaj")
        }
        PohodiList()

    }
}

@Composable
fun PohodiList()
{
    val viewModel: PohodiViewModel = viewModel(factory = PohodiViewModelFactory(LocalContext.current))
    val pohodi by viewModel.pohodi.observeAsState()
    if (pohodi != null)
    {
        val scrollState = rememberLazyListState()
        LazyColumn(modifier = Modifier.fillMaxSize(), state = scrollState) {
            items(pohodi!!) {data ->
                PohodItem(pohod = data, modifier = Modifier
                    .height(56.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(onLongPress = {
                            viewModel.Remove(data)
                            Log.d("REMOVED", "REMOVED")
                        })
                    }
                )
                }

        }
    }
}

@Composable
fun PohodItem(pohod: Pohod, modifier: Modifier)
{
    val format = SimpleDateFormat("yyyy-MM-dd")
    Column(modifier = modifier) {
        Divider()
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxHeight())
        {
            Icon(painterResource(id = R.drawable.ic_paper),
                contentDescription = null,
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier.size(32.dp))
            Text(format.format(Date(pohod.Datum)), modifier = Modifier.padding(8.dp,0.dp,0.dp,0.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RatitovecComposeTheme {
        Base()
    }
}