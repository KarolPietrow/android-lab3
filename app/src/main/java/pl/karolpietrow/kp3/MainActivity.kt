package pl.karolpietrow.kp3

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.karolpietrow.kp3.ui.theme.KP3Theme
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KP3Theme {
                var inputText1 by remember { mutableStateOf("") }
                var inputText2 by remember { mutableStateOf("") }
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "Wybór intencji",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    TextField(
                        value = inputText1,
                        onValueChange = {
                            inputText1 = it
                        },
                        label = { Text("Pole wprowadzania 1") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = inputText2,
                        onValueChange = {
                            inputText2 = it
                        },
                        label = { Text("Pole wprowadzania 2") },
                        modifier = Modifier.fillMaxWidth()
                    )
//                  Spacer(modifier = Modifier.height(50.dp))
                    Button(onClick = {
//                  openSecondActivity(inputText)
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Open Second Activity")
                    }
                    Button(onClick = {
                        openMap(inputText1)
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Aktywność 1A (Wyślij SMS)")
                    }
                    Button(onClick = {
                        openMap(inputText1)
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Aktywność 2A (Wyszukaj w Mapach)")
                    }
                    Button(onClick = {
                        exitProcess(0)
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Aktywność 9A (Zamknij aplikację)")
                    }
                }
            }
        }
    }
//    private fun sendSMS(number:Int, message:String) {
//        val intent
//    }

    private fun openMap(location: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("geo:0,0?q=$location")
        }
        startActivity(intent)
    }
}




//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    KP3Theme {
//
//    }
//}