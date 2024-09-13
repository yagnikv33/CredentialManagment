package com.example.credentialmanagment.ui.view.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.credentialmanagment.R
import com.example.credentialmanagment.data.db.Password
import com.example.credentialmanagment.ui.viewmodel.PasswordViewModel

@Composable
fun PasswordItem(
    password: Password,
    onPasswordClick: (Password) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onPasswordClick(password) }
            .background(Color.White, shape = RoundedCornerShape(32.dp))
            .padding(16.dp)
    ) {
        Text(text = password.accountName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(
            text = "*".repeat(8), modifier = Modifier
                .weight(1f)
                .padding(start = 6.dp, top = 3.dp), color = Color.LightGray
        )
        Icon(
            painterResource(id = R.drawable.arrow),
            contentDescription = "View Details",
            modifier = Modifier.padding(top = 3.dp)
        )
    }
}