package com.example.credentialmanagment.ui.view.ui

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.credentialmanagment.R
import com.example.credentialmanagment.data.db.Password
import com.example.credentialmanagment.ui.view.ui.theme.red
import com.example.credentialmanagment.ui.view.ui.theme.shadeBlack
import com.example.credentialmanagment.ui.view.ui.theme.skyBlue
import com.example.credentialmanagment.ui.viewmodel.PasswordViewModel
import com.example.credentialmanagment.utils.UserCredUstils.isValidEmail
import com.example.credentialmanagment.utils.UserCredUstils.isValidPassword

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordManager(
    viewModel: PasswordViewModel,
    act: Activity
) {
    val passwords by viewModel.passwords.observeAsState(emptyList())
    var isPwdToggle by remember { mutableStateOf(false) }
    var accountName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isEditMode by remember { mutableStateOf(false) }
    var editingPasswordId by remember { mutableStateOf<Int?>(null) }
    val focusManager = LocalFocusManager.current
    val showAddItemSheetState = rememberModalBottomSheetState()
    var showAddItemBottomSheet by remember { mutableStateOf(false) }
    val showViewItemSheetState = rememberModalBottomSheetState()
    var showViewItemBottomSheet by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            Text(
                text = "Password Manager",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(16.dp)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                isEditMode = false // Reset to add mode
                accountName = ""
                username = ""
                password = ""
                showAddItemBottomSheet = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Password")
            }
        }
    ) { p ->
        LazyColumn(modifier = Modifier.padding(vertical = 52.dp, horizontal = 12.dp)) {
            items(passwords) { pwdItem ->
                if (passwords.isEmpty()) {
                    Text(
                        "Not saved any credentials click '+' to add.",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                } else {
                    PasswordItem(
                        password = pwdItem,
                        onPasswordClick = {
                            showViewItemBottomSheet = true

                            pwdItem.apply {
                                accountName = this.accountName
                                username = this.username
                                password = this.password
                            }
                        }
                    )
                }

            }
        }

        //Add/Edit Item Bottom Sheet
        if (showAddItemBottomSheet) {
            ModalBottomSheet(
                sheetState = showAddItemSheetState,
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        //Account Name
                        OutlinedTextField(
                            value = accountName,
                            onValueChange = { accountName = it },
                            label = { Text("Account Name") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Next)
                                }
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        //Username
                        OutlinedTextField(
                            value = username,
                            onValueChange = {
                                username = it
                                // emailError = if (isValidEmail(it)) null else "Invalid email format"
                            },
                            label = { Text("Username/Email") },
                            isError = emailError != null,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    focusManager.moveFocus(FocusDirection.Next)
                                }
                            )
                        )

                        if (emailError != null) {
                            Text(
                                text = emailError ?: "",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        //Password
                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                //  passwordError = if (isValidPassword(it)) null else "Password must be at least 8 characters, include a letter, a number, and a special character."
                            },
                            label = { Text("Password") },
                            isError = passwordError != null,
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                }
                            )
                        )

                        if (passwordError != null) {
                            Text(
                                text = passwordError ?: "",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                emailError =
                                    if (isValidEmail(username)) null else "Invalid email format"
                                passwordError =
                                    if (isValidPassword(password)) null else "Password must be at least 8 characters, include a letter, a number, and a special character."
                                if (isEditMode) {
                                    // Update password
                                    if (emailError == null && passwordError == null) {
                                        editingPasswordId?.let { id ->
                                            viewModel.updatePassword(
                                                Password(
                                                    id = id,
                                                    accountName = accountName,
                                                    username = username,
                                                    password = password
                                                )
                                            )
                                        }
                                        showAddItemBottomSheet = false
                                    } else {
                                        Toast.makeText(
                                            act.baseContext,
                                            "Enter valid Data",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                } else {
                                    //Add new password
                                    if (emailError == null && passwordError == null) {
                                        viewModel.addPassword(
                                            Password(
                                                accountName = accountName,
                                                username = username,
                                                password = password
                                            )
                                        )
                                        showAddItemBottomSheet = false
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = shadeBlack)
                        ) {
                            Text(
                                if (isEditMode) "Update Password" else "Add Password",
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                },
                onDismissRequest = { showAddItemBottomSheet = false }
            )
        }

        //View Item Bottom Sheet
        if (showViewItemBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showViewItemBottomSheet = false },
                sheetState = showViewItemSheetState,
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            "Account Details",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = skyBlue
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Account Type")
                        Text(text = accountName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(18.dp))

                        Text("Username/Email")
                        Text(text = username, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(18.dp))

                        Text("Password")
                        Row {
                            Text(
                                text = if (isPwdToggle) "*".repeat(password.length) else password,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { isPwdToggle = !isPwdToggle }) {
                                Icon(
                                    painter = painterResource(id = if (isPwdToggle) R.drawable.hide else R.drawable.show),
                                    contentDescription = if (isPwdToggle) "Hide password" else "Show password"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Row {
                            Button(
                                onClick = {
                                    isEditMode = true
                                    editingPasswordId =
                                        passwords.first { it.accountName == accountName }.id // Assuming Password has an id field
                                    showViewItemBottomSheet = false
                                    showAddItemBottomSheet = true
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = shadeBlack)
                            ) {
                                Text(
                                    "Edit",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Button(
                                onClick = {
                                    showAddItemBottomSheet = false
                                    showViewItemBottomSheet = false
                                    viewModel.deletePassword(passwords.first { it.accountName == accountName })
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = red)
                            ) {
                                Text(
                                    "Delete",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}
