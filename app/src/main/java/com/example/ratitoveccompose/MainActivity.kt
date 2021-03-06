package com.example.ratitoveccompose

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.Orientation
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.preference.PreferenceManager
import com.example.ratitoveccompose.data.*
import com.example.ratitoveccompose.ui.theme.RatitovecComposeTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.material.internal.ContextUtils
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    @ExperimentalPagerApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).init()
        val prefs = (application as MyApplication).ThemePreferences

        setContent {
            val darkMode = prefs.DarkTheme.collectAsState(initial = false)
            //TODO observe dark mode
            Log.d("Dark mode", darkMode.value.toString())
            RatitovecComposeTheme(darkTheme = darkMode.value) {
                Surface(color = MaterialTheme.colors.background) {
                    Activity()
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun Activity()
{
    // Remember a SystemUiController
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight
    val context = LocalContext.current
    val primary = MaterialTheme.colors.primary
    val isLight = MaterialTheme.colors.isLight


    SideEffect {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
      /*  systemUiController.setSystemBarsColor(
            color = primary,
            darkIcons = useDarkIcons
        )*/
        systemUiController.setStatusBarColor(primary, isLight)
        // setStatusBarsColor() and setNavigationBarsColor() also exist
    }
    Column {
        TopAppBar(
            title = {
                Text(text = stringResource(R.string.app_name), textAlign = TextAlign.Center)
            },
            actions = {
                IconButton(onClick = { context.startActivity(Intent(context,SettingsActivity::class.java))}) {
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
        TabBase()

    }
}

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun TabBase()
{
    val coroutineScope = rememberCoroutineScope()
    val Tabs = listOf("Vpisani", "Nevpisani", "Vsi")
    val pagerState = rememberPagerState(pageCount = 3)
    Log.d("CURRENTPAGE", pagerState.currentPage.toString())
    Column {
        TabRow(selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            }) {
            Tabs.forEachIndexed { i, el ->
                Tab(
                    selected = i == pagerState.currentPage,
                    onClick = {
                        coroutineScope.launch {  pagerState.scrollToPage(i) }
                    },
                    enabled = true
                )
                {
                    Text(el, modifier = Modifier.padding(8.dp))
                }
            }
        }
        HorizontalPager(state = pagerState, offscreenLimit = 3) { page ->
            Log.d("PAGE", page.toString())
            Base(pagerState.currentPage)
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun Base(type: Int) {
    val context = LocalContext.current
    val viewModel: PohodiViewModel = viewModel(factory = PohodiViewModelFactory(LocalContext.current))
    Column {
        Button(onClick = {
                    viewModel.Insert(Pohod(Date().time, if(type==2) 1 else type))
                            Toast.makeText(context, "Bravo, le tako naprej!", Toast.LENGTH_LONG).show()
                         },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)) {
            Text(text = "Dodaj")
        }
        PohodiList(type)

    }
}

@ExperimentalAnimationApi
@Composable
fun PohodiList(type: Int)
{
    val viewModel: PohodiViewModel = viewModel(factory = PohodiViewModelFactory(LocalContext.current))
    val context = LocalContext.current
    viewModel.setFilter(type)
    val pohodi by viewModel.pohodi.observeAsState()
    Text(text = pohodi?.size.toString(), textAlign = TextAlign.Center, fontSize = 40.sp, modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp))
    if (pohodi != null)
    {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(pohodi!!) {data ->
                var visible by remember { mutableStateOf(true) }
                AnimatedVisibility(visible = visible) {
                    PohodItem(pohod = data, modifier = Modifier
                        .height(56.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(onLongPress = {
                                viewModel.Remove(data)
                                visible = false
                                Toast
                                    .makeText(context, "clicked index", Toast.LENGTH_LONG)
                                    .show()
                            })
                        }
                    )
                }
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
   //     Base(0)
    }
}