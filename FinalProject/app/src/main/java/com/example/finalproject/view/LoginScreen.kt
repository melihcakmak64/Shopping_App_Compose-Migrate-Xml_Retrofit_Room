import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finalproject.R
import com.example.finalproject.configs.LocalStorage
import com.example.finalproject.viewmodel.LoginViewModel
import com.example.finalproject.viewmodel.LoginViewModelFactory

@Composable
fun LoginScreen(onPressed: () -> Unit) {
    val context = LocalContext.current
    val localStorage = LocalStorage(context)
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(localStorage))

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth(0.8f)
            )

            OutlinedTextField(
                value = viewModel.username,
                onValueChange = { viewModel.username = it },
                modifier = Modifier.fillMaxWidth(0.8f),
                label = { Text("Username") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    capitalization = KeyboardCapitalization.None
                ),
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Password Field
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                modifier = Modifier.fillMaxWidth(0.8f),
                label = { Text("Password") },
                visualTransformation = if (viewModel.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (viewModel.isPasswordVisible) Icons.Filled.Lock else Icons.Filled.Done
                    IconButton(
                        onClick = { viewModel.isPasswordVisible = !viewModel.isPasswordVisible },
                        content = {
                            Icon(icon, contentDescription = "Toggle password visibility")
                        }
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                viewModel.login {
                    onPressed()
                }
            }) {
                Text("Login")
            }

            if (viewModel.loginError) {
                Text(
                    text = "Login failed. Please check your username and password.",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
