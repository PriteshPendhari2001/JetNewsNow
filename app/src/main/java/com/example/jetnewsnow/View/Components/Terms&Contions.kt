package com.example.jetnewsnow.View.Components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jetnewsnow.ui.theme.poppinsBold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsAndConditionsScreen(navcontroller: NavHostController) {
    var isChecked by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Terms&Contions", fontWeight = FontWeight.Bold, fontFamily = poppinsBold) },
                navigationIcon = {
                    IconButton(onClick = { navcontroller.popBackStack() }) {
                        Icon(
                            Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Back",
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding)
        ) {
            Text(
                "Terms and Conditions",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Scrollable Terms text
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp)) // keep inner content clipped to rounded corners
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()) // scroll the inner content
                        .padding(12.dp) // <-- INNER padding: space between border and text
                ) {
                    Text(
                        """
            Last Updated: 12 Aug 2025

            1. Acceptance of Terms
            By using this app, you agree to these Terms. If you do not agree, please do not use the app.

            2. User Accounts
            You are responsible for maintaining the confidentiality of your login credentials and for all activities under your account.

            3. Use of the App
            You agree not to use the app for unlawful purposes or distribute harmful content.

            4. Intellectual Property
            All content, designs, and logos are our property or licensed to us.

            5. Privacy
            We collect and use your information according to our Privacy Policy.

            6. Limitation of Liability
            We are not liable for damages resulting from your use of the app.

            7. Changes to Terms
            We may update these Terms. Continued use means you accept the changes.

            8. Contact Us
            Email: support@yourapp.com
            """.trimIndent(),
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Checkbox to accept terms
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it }
                )
                Text("I agree to the Terms and Conditions")
            }
        }
    }


}
