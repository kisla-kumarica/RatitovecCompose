package com.example.ratitoveccompose

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
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
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.preference.PreferenceManager
import com.example.ratitoveccompose.data.*
import com.example.ratitoveccompose.ui.theme.RatitovecComposeTheme
import com.google.android.material.internal.ContextUtils
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val darkMode = ThemePreferences(this).DarkTheme.collectAsState(initial = false)
            //TODO observe dark mode
            if (darkMode.value)
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            else
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)

            RatitovecComposeTheme(darkTheme = darkMode.value) {
                Surface(color = MaterialTheme.colors.background) {
                    Column {
                        TopAppBar(
                            title = {
                                Text(text = stringResource(R.string.app_name))
                            },
                           actions = {
                                IconButton(onClick = { startActivity(Intent(applicationContext,SettingsActivity::class.java))}) {
                                    Icon(
                                        imageVector = Icons.Filled.Settings,
                                        contentDescription = "Menu Btn"
                                    )
                                }
                            },
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = MaterialTheme.colors.onPrimary,
                            elevation = 2.dp
                        )
                        val Tabs = listOf("Vpisani", "Nevpisani", "Vsi")
                        var index by remember { mutableStateOf(0) }
                        Column {
                            TabRow(selectedTabIndex = index) {
                                Tabs.forEachIndexed { i, el ->
                                    Tab(
                                        selected = i == index,
                                        onClick = { index = i },
                                        enabled = true,
                                //        selectedContentColor = MaterialTheme.colors.onPrimary,
                                //        unselectedContentColor = MaterialTheme.colors.onPrimary
                                    )
                                    {
                                        Text(el, modifier = Modifier.padding(8.dp))
                                        //tab = Tabs.values()[index]
                                    }
                                }
                            }
                            Base(index)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Base(type: Int) {
    val viewModel: PohodiViewModel = viewModel(factory = PohodiViewModelFactory(LocalContext.current))
    Column {
        Button(onClick = { viewModel.Insert(Pohod(Date().time, if(type==2) 1 else type)) },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)) {
            Text(text = "Dodaj")
        }
        PohodiList(type)

    }
}

@Composable
fun PohodiList(type: Int)
{
    val viewModel: PohodiViewModel = viewModel(factory = PohodiViewModelFactory(LocalContext.current))
    val context = LocalContext.current;
    viewModel.setFilter(type)
    val pohodi by viewModel.pohodi.observeAsState()
    Text(text = pohodi?.size.toString(), textAlign = TextAlign.Center, fontSize = 40.sp, modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp))
    if (pohodi != null)
    {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(pohodi!!) {data ->
                PohodItem(pohod = data, modifier = Modifier
                    .height(56.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(onLongPress = {
                            viewModel.Remove(data)
                            Toast
                                .makeText(context, "clicked index", Toast.LENGTH_LONG)
                                .show();
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
            Icon(painterResource(id = if(pohod.Type == 0) R.drawable.ic_paper else R.drawable.ic_assignment_late),
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
        Base(0)
    }
}