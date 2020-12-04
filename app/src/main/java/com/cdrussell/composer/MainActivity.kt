package com.cdrussell.composer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cdrussell.composer.ui.ComposerTheme
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposerTheme {
                MainActivityRoot()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityRoot(model: MainActivityViewModel = viewModel()) {
    Column(modifier = Modifier.fillMaxHeight()) {
        Column(modifier = Modifier.weight(1f).padding(4.dp)) {
            val password: String by model.viewState.observeAsState("ABC")
            MainActivityContent(password)
        }
    }
}

@Composable
fun MainActivityContent(password: String) {
    HeroImage()

    HeadingGreeting("Password Composer")
    Divider()

    Password(password)

    GeneratePasswordButton()

    Divider()
    Spacer(Modifier.padding(10.dp))
    HeadingGreeting("History")
    PreviousPasswordList(listOf("correct", "horse", "battery", "staple"), onPasswordClicked = {
        Log.i("MainActivity", "Password clicked: $it")
    })
}

@Composable
fun PreviousPasswordList(passwords: List<String>, onPasswordClicked: (String) -> Unit) {
    LazyColumnFor(passwords) { password ->
        PasswordListItem(password, onPasswordClicked)
    }
}

@Composable
fun PasswordListItem(password: String, onPasswordClicked: (String) -> Unit) {
    Text(
        password,
        Modifier.clickable(onClick = { onPasswordClicked(password) })
            .fillMaxWidth()
            .padding(6.dp),
        style = MaterialTheme.typography.body1.copy(fontSize = TextUnit.Companion.Sp(20))
    )
}

@Composable
fun HeroImage() {
    Image(
        imageResource(R.drawable.header),
        modifier = Modifier
            .preferredHeight(180.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(4.dp)),
        contentScale = ContentScale.Crop

    )
}

@Composable
fun HeadingGreeting(value: String) {
    Text(
        text = value,
        style = MaterialTheme.typography.h6
    )
}

@Composable
fun Password(value: String) {
    Spacer(Modifier.padding(20.dp))
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = value,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h4
    )
    Spacer(Modifier.padding(20.dp))
}

@Preview
@Composable
fun GeneratePasswordButton(viewModel: MainActivityViewModel = viewModel()) {

    Button(onClick = { viewModel.generatePassword() }) {
        Text("Generate password")
    }
}

class MainActivityViewModel: ViewModel() {

    private val _viewState = MutableLiveData("")
    val viewState: LiveData<String> = _viewState

    fun generatePassword(): String {
        val pw = "" + Random.nextInt(1_000_000, 99_999_999)
        _viewState.value = (pw)
        return pw
    }

}