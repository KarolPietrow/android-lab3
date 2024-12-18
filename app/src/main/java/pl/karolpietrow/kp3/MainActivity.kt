package pl.karolpietrow.kp3

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import pl.karolpietrow.kp3.ui.theme.KP3Theme
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.system.exitProcess
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices





class MainActivity : ComponentActivity() {

    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            // Sprawdzamy, które uprawnienia zostały przyznane
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Uprawnienia do dokładnej lokalizacji przyznane
                    getLocation()
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Uprawnienia do przybliżonej lokalizacji przyznane
                    getLocation()
                }
                else -> {
                    // Brak uprawnień
                    Toast.makeText(this, "Nie zezwolono na lokalizację", Toast.LENGTH_SHORT).show()
                }
            }
        }

        setContent {
            KP3Theme {
                var inputText1 by remember { mutableStateOf("") }
                var inputText2 by remember { mutableStateOf("") }
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp)
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "Wybór intencji",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    TextField(
                        value = inputText1,
                        onValueChange = { inputText1 = it },
                        label = { Text("Pole wprowadzania 1") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                    TextField(
                        value = inputText2,
                        onValueChange = { inputText2 = it },
                        label = { Text("Pole wprowadzania 2") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                    Spacer(modifier = Modifier.height(50.dp))
                    Button(onClick = {
                        sendSMS(inputText1, inputText2)
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Aktywność 1A (Wyślij SMS)")
                    }
                    Text(
                        text = "Pole 1 - numer telefonu; Pole 2 - treść SMS",
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Button(onClick = {
                        if (inputText1 != "aktualna") {
                            openMap(inputText1)
                        } else {
                            getLocation()
                        }
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Aktywność 2A (Otwórz Mapy i wyszukaj)")
                    }
                    Text(
                        text = "Pole 1 - zapytanie do wyszukiwania",
                        fontSize = 15.sp
                    )
                    Text(
                        text = "\"aktualna\" - zwróć lokalizację użytkownika",
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Button(onClick = {
                        selectPdfFile()
                        // "Uruchomienie aplikacji do tworzenia dokumentów" zinterpretowałem jako wybranie dokumentu PDF,
                        // a następnie uruchomienie wybranego pliku w wybranej aplikacji do PDFów.
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Aktywność 3A (Wybierz plik PDF i otwórz)")
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Button(onClick = {
                        sendEmail(inputText1, inputText2)
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Aktywność 4A (Wyślij E-mail)")
                    }
                    Text(
                        text = "Pole 1 - adres e-mail; Pole 2 - temat wiadomości",
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Button(onClick = {
                        callNumber(inputText2)
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Aktywność 5A (Zadzwoń na numer)")
                    }
                    Text(
                        text = "Pole 2 - numer telefonu",
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Button(onClick = {
                        openMusic()
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Aktywność 6A (Otwórz odtwarzacz muzyki)")
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Button(onClick = {
                        if (inputText1=="") {
                            inputText1="Nie podano tekstu dla linii 1"
                        }
                        if (inputText2=="") {
                            inputText2="Nie podano tekstu dla linii 2"
                        }
                        openSecondActivity(inputText1, inputText2 )
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Aktywność 7A (Otwórz aktywność B)")
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Button(onClick = {
                        addCalendarEvent(inputText1, inputText2)
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Aktywność 8A (Utwórz event w kalendarzu)")
                    }
                    Text(
                        text = "Pole 1 - data+godz w formacie \"yyyy-MM-dd HH:mm\"",
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Pole 2 - nazwa wydarzenia",
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Button(onClick = {
                        exitProcess(0)
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Aktywność 9A (Zamknij aplikację)")
                    }
                }
            }
        }
    }

    private fun sendSMS(number: String, message: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:$number")
            putExtra("sms_body", message)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "Błąd: Brak aplikacji do wysyłania SMS!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun openMap(location: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("geo:0,0?q=$location")
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "Błąd: Brak aplikacji do wyświetlania map!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun openMap(latitude:Double, longitude:Double) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("geo:0,0?q=$latitude,$longitude")
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "Błąd: Brak aplikacji do wyświetlania map!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun getLocation() {
        // Sprawdzenie uprawnień do lokalizacji
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Jeśli nie ma uprawnień, prosimy o nie
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            return
        }

        // Uzyskiwanie ostatniej znanej lokalizacji
        fusedLocationClient.lastLocation
            .addOnSuccessListener(this) { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    // Wyświetlanie lokalizacji w Toast
                    Toast.makeText(
                        this,
                        "Lokalizacja: Lat: $latitude, Lon: $longitude",
                        Toast.LENGTH_LONG
                    ).show()
                    openMap(latitude, longitude)
                } else {
                    Toast.makeText(this, "Nie udało się pobrać lokalizacji", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private val selectPdfLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                openPdfFile(uri)
            } else {
                Toast.makeText(this, "Błąd: Nie wybrano pliku!", Toast.LENGTH_SHORT).show()
            }
        }

    private fun selectPdfFile() {
        selectPdfLauncher.launch(arrayOf("application/pdf")) // Filtrujemy tylko pliki PDF
    }

    private fun openPdfFile(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION // przyznanie uprawnienia do odczytu URI
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "Błąd: Brak aplikacji do otwarcia PDF!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendEmail(address: String, subject: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$address")
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "Błąd: Brak aplikacji do wysłanai E-maila!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun callNumber(number: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$number")
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "Błąd: Brak aplikacji do dzwonienia!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openMusic() {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_APP_MUSIC)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "Błąd: Brak aplikacji do odtwarzania muzyki!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addCalendarEvent(dateString: String, name: String) {
        if (dateString.isBlank()) {
            Toast.makeText(this, "Błąd: Nie podano daty!", Toast.LENGTH_SHORT).show()
            return
        }
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        try {
            val localDateTime = LocalDateTime.parse(dateString, formatter)
            val convertedDate = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            val intent = Intent(Intent.ACTION_INSERT).apply {
                data = Uri.parse("content://com.android.calendar/events")
                putExtra("title", name)
                putExtra("beginTime", convertedDate)
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "Błąd: Brak aplikacji kalendarza!", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Błąd: Niepoprawny format daty! Użyj 'yyyy-MM-dd HH:mm'", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openSecondActivity(string1: String, string2: String) {
        val intent = Intent(this@MainActivity, SecondActivity::class.java).apply {
            putExtra("STRING_KEY_1", string1)
            putExtra("STRING_KEY_2", string2)
        }
        startActivity(intent)
    }
}


class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val string1 = intent.getStringExtra("STRING_KEY_1") ?: "No text provided"
        val string2 = intent.getStringExtra("STRING_KEY_2") ?: "No text provided"
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Przekazane dane z aktywnośći A:",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = string1,
                    fontSize = 20.sp
                )
                Text(
                    text = string2,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(100.dp))
                Button(onClick = {
                    finish()
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Zakończ aktywność B")
                }
                Text(
                    text = "Używa finish() do zakończenia aktywności B, w efekcie powracając do aktywności A",
                    fontSize = 15.sp
                )
                Button(onClick = {
                    val intent = Intent(this@SecondActivity, MainActivity::class.java).apply {}
                    startActivity(intent)

                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Uruchom aktywność A")
                }
                Text(
                    text = "Używa startActivity(intent) do uruchomienia aktywności A",
                    fontSize = 15.sp
                )
            }
        }
    }
}
