package com.example.funfactsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Using default MaterialTheme
            MaterialTheme {
                // Surface provides a background for the app content
                Surface(color = MaterialTheme.colorScheme.background) {
                    FunFactsApp()
                }
            }
        }
    }
}

@Composable
fun FunFactsApp(modifier: Modifier = Modifier) {
    // List of fun facts
    val funFacts = listOf(
        "Honey never spoils. Archaeologists have found pots of honey in Georgian tombs that are over 5,000 years old and still edible.",
        "Bananas are berries, but strawberries aren't.",
        "A day on Venus is longer than a year on Venus.",
        "There are more possible iterations of a game of chess than there are atoms in the known universe.",
        "Octopuses have three hearts and blue blood.",
        "The Eiffel Tower can be 15 cm taller during the summer when the temperature increases.",
        "Oldest wine in the world is found in Georgia and is 8,000 years old.",
        "A single strand of spaghetti is called a 'spaghetto'."
    )

    // Helper function to get a random fact, ensuring it's different from the current one
    fun getRandomFact(facts: List<String>, current: String? = null): String {
        val availableFacts = if (current != null) {
            facts.filter { it != current }
        } else {
            facts
        }
        return if (availableFacts.isNotEmpty()) {
            availableFacts[Random.nextInt(availableFacts.size)]
        } else {
            current ?: "No fun facts available."
        }
    }

    // State to hold the current fun fact
    var currentFact by remember { mutableStateOf(getRandomFact(funFacts)) }

    // State to hold the current hue level (0f to 360f)
    var hue by remember { mutableStateOf(0f) }

    // Base saturation and value for the background color
    val baseSaturation = 0.6f // Adjust as desired for color vibrancy
    val baseValue = 0.9f       // Adjust as desired for brightness

    // Function to convert HSV to Color
    fun hsvToColor(hue: Float, saturation: Float, value: Float): Color {
        val hsv = floatArrayOf(hue, saturation, value)
        val argb = android.graphics.Color.HSVToColor(hsv)
        return Color(argb)
    }

    // Calculate the current background color based on hue
    val backgroundColor = hsvToColor(hue, baseSaturation, baseValue)

    // Determine text color based on hue for readability
    // Simple contrast logic: use black for light backgrounds and white for dark backgrounds
    val textColor = if (isColorDark(backgroundColor)) Color.White else Color.Black

    // UI Layout
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(backgroundColor), // Dynamic background color based on hue
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = currentFact,
            fontSize = 20.sp,
            color = textColor, // Dynamic text color based on background
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                // Update the fun fact, ensuring it's different from the current one
                currentFact = getRandomFact(funFacts, currentFact)
                // Update the hue: increment by 45 degrees, reset if exceeds 360
                hue = (hue + 45f) % 360f
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)) // Button color
        ) {
            Text(
                text = "New Fact",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

/**
 * Determines if a color is dark based on its luminance.
 * Returns true if the color is dark, false otherwise.
 */
fun isColorDark(color: Color): Boolean {
    // Calculate luminance using the formula:
    // https://en.wikipedia.org/wiki/Luma_(video)
    val luminance = 0.2126f * color.red + 0.7152f * color.green + 0.0722f * color.blue
    return luminance < 0.5f
}

@Preview(showBackground = true)
@Composable
fun FunFactsAppPreview() {
    MaterialTheme { // Use default MaterialTheme for preview
        Surface(color = MaterialTheme.colorScheme.background) {
            FunFactsApp()
        }
    }
}