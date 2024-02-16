package pp.ipp.estg.urbanmarket.paginas

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pp.ipp.estg.urbanmarket.R
import pp.ipp.estg.urbanmarket.retrofit.feiranteAPI
import pp.ipp.estg.urbanmarket.retrofit.models.feiranteRetrofit.Feirante
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


@Composable
fun Buttonsignup(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        ClickableText(
            text = AnnotatedString("Sign up here"),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            onClick = { navController.navigate("signup") },
            style = TextStyle(
                fontSize = 20.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = Color.Companion.Blue
            )
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupPage(navController: NavHostController) {
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Login") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Login"
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.pantry),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Ajuste a altura conforme necessário
            )

            var name by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }
            var dataNasc by remember { mutableStateOf("") }
            var sexo by remember { mutableStateOf('m') }
            var phoneNumber by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            Text(
                text = "Signup",
                style = TextStyle(fontSize = 40.sp, fontFamily = FontFamily.Cursive)
            )

            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "Nome") },
                value = name,
                onValueChange = { name = it })

            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "Email") },
                value = email,
                onValueChange = { email = it })

            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "Data de Nascimento\n(YYYY-MM-DD)") },
                value = dataNasc,
                onValueChange = { dataNasc = it })

            Spacer(modifier = Modifier.height(20.dp))

            GenderSelector() { s -> sexo = s }

            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "Número de telemóvel") },
                value = phoneNumber,
                onValueChange = {
                    phoneNumber = it
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            var passwordVisibility by remember { mutableStateOf(false) }

            var isPasswordError by remember { mutableStateOf(false) }

            TextField(
                label = { Text(text = "PIN (4 números)") },
                value = password.toString(),
                isError = isPasswordError,
                onValueChange = {
                    password = it
                    isPasswordError = it.length != 4 || !it.isDigitsOnly()
                },
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisibility = !passwordVisibility },
                        modifier = Modifier
                            .padding(8.dp)
                            .size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = null
                        )
                    }
                }
            )

            if (isPasswordError) {
                Text(text = "Por favor, preencha um valor válido para a senha", color = Color.Red)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    // Adicionando validação de e-mail

                    // Verifique se o número de telefone já existe
                    feiranteAPI.getFeiranteByTelem(phoneNumber.toInt()).enqueue(object : Callback<Feirante> {
                        override fun onResponse(call: Call<Feirante>, response: Response<Feirante>) {
                            if (response.isSuccessful) {
                                // O número de telefone já existe, exiba um aviso
                                errorMessage = "Número de telemóvel já cadastrado. Por favor, escolha outro número."
                            } else {
                                // O número de telefone não existe, continue com as outras verificações
                                val currentDate = LocalDateTime.now()
                                val dataNascDate = try {
                                    LocalDate.parse(dataNasc, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay()
                                } catch (e: DateTimeParseException) {
                                    errorMessage = "Formato de data inválido. Use o formato yyyy-MM-dd."
                                    return
                                }
                                if (!isValidEmail(email)) {
                                    errorMessage = "Email do feirante inválido!"
                                }
                                else if (dataNascDate.isAfter(currentDate)) {
                                    errorMessage = "Data de nascimento do feirante inválida!"
                                } else if (!(sexo.equals('M') || sexo.equals('F'))) {
                                    errorMessage = "Sexo do feirante inválido!"
                                } else if (phoneNumber.length != 9 || !phoneNumber.isDigitsOnly()) {
                                    errorMessage = "Número de telefone do feirante inválido!"
                                } else if (password.length != 4 || !password.isDigitsOnly()) {
                                    errorMessage = "A senha deve ter 4 números!"
                                } else {
                                    // Todas as verificações passaram, continue com o cadastro
                                    PostNewFeirante(
                                        Feirante(
                                            0,
                                            name,
                                            email,
                                            dataNasc,
                                            sexo,
                                            phoneNumber.toInt(),
                                            password.toInt()
                                        )
                                    )
                                    navController.navigate("login")
                                }
                            }
                        }

                        override fun onFailure(call: Call<Feirante>, t: Throwable) {
                            Log.e("GET_FEIRANTE", "Error checking phone number existence", t)
                        }
                    })
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Criar Conta")
            }

            // Exibe a mensagem de erro, se houver
            errorMessage?.let {
                Text(text = it, color = Color.Red)
            }
        }
    }
}

// Função para validar o e-mail
fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
    return emailRegex.matches(email)
}

@Composable
fun GenderSelector(onGenderSelected: (Char) -> Unit) {
    var selectedGender by remember { mutableStateOf<Char?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Selecione seu gênero:")
        Spacer(modifier = Modifier.height(8.dp))
        RadioGroup(
            selectedOption = selectedGender,
            options = listOf('M', 'F'),
            onOptionSelected = {
                selectedGender = it
                onGenderSelected(selectedGender!!)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Gênero selecionado: ${selectedGender ?: "Nenhum"}")
    }
}

@Composable
fun RadioGroup(
    selectedOption: Char?,
    options: List<Char>,
    onOptionSelected: (Char) -> Unit
) {
    Column {
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                RadioButton(
                    selected = option == selectedOption,
                    onClick = { onOptionSelected(option) }
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = option.toString())
            }
        }
    }
}

fun PostNewFeirante(newFeirante: Feirante) {

    feiranteAPI.postFeirante(newFeirante).enqueue(object : Callback<Feirante> {
            override fun onResponse(call: Call<Feirante>, response: Response<Feirante>) {
                if (response.isSuccessful) {
                    val createdFeirante = response.body()
                    Log.d("POST_FEIRANTE", "Feirante created successfully: $createdFeirante")
                } else {
                    Log.d("POST_FEIRANTE", "Failed to create Feirante. Response: $response")
                }
            }

            override fun onFailure(call: Call<Feirante>, t: Throwable) {
                Log.e("POST_FEIRANTE", "Error creating Feirante", t)
            }
        })
}

@Composable
fun ValidateLogin(numTelem: String, pin: String, navController: NavHostController) {
    var loading by remember { mutableStateOf(true) }
    var feirante by remember { mutableStateOf<Feirante?>(null) }
    var showAlert by remember { mutableStateOf(false) }

    var alertText by remember { mutableStateOf("") }

    if (numTelem.toIntOrNull() == null|| numTelem.toInt() !in 900000000..999999999) {
        alertText = "Formato do Número de telemóvel incorreto.\n(ex.: 911222333)"
        showAlert = true
    } else if (pin.toIntOrNull() == null|| pin.toInt() < 1000) {
        alertText = "Formato de PIN incorreto.\n(ex.: 1111)"
        showAlert = true
    } else {
        LaunchedEffect(key1 = Unit) {
            if (feirante == null && loading) {
                feiranteAPI.getFeiranteByTelem(numTelem.toInt()).enqueue(object : Callback<Feirante> {
                    override fun onResponse(
                        call: Call<Feirante>,
                        response: Response<Feirante>
                    ) {
                        if (response.isSuccessful) {
                            feirante = response.body()
                            loading = false

                            if (feirante?.pin == pin.toInt()) {
                                navController.navigate("Pagina Principal/${feirante?.id}")
                            }

                        } else {
                            Log.d("Response Login", response.toString())
                            // Se a resposta não for bem-sucedida, definimos showAlert como true
                            alertText = "Feirante não encontrado... \nTente novamente."
                            showAlert = true
                        }
                    }

                    override fun onFailure(call: Call<Feirante>, t: Throwable) {
                        Log.d("Login Fail -> getFeiranteByTelem()", t.toString())
                    }
                })
            }
        }
    }

    // Exibe o AlertDialog se showAlert for true
    if (showAlert) {
        AlertDialog(
            onDismissRequest = {
                showAlert = false
            },
            title = { Text("AVISO") },
            text = {
                Text(alertText)
            },
            confirmButton = {
                Button(
                    onClick = {
                        showAlert = false
                        navController.navigate("login")
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}


@Preview
@Composable
fun PreviewLoginPage() {
    LoginPage(navController = rememberNavController())
}

@Composable
fun LoginPage(navController: NavHostController) {
    var validate by remember { mutableStateOf(false) }

    Buttonsignup(navController)

    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.pantry),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        var telem by remember { mutableStateOf("") }
        var pin by remember { mutableStateOf("") }

        Text(text = "Login", style = TextStyle(fontSize = 40.sp, fontFamily = FontFamily.Cursive))

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = { Text(text = "Nº Telemóvel") },
            value = telem,
            onValueChange = { newValue ->
                telem = newValue
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(20.dp))
        var passwordVisibility by remember { mutableStateOf(false) }

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = { Text(text = "PIN") },
            value = pin,
            onValueChange = { newValue ->
                pin = newValue
            },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            trailingIcon = {
                IconButton(
                    onClick = { passwordVisibility = !passwordVisibility },
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp)
                ) {
                    Icon(
                        imageVector = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))


        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                enabled = telem.toIntOrNull() != 0 && pin.toIntOrNull() != 0,
                onClick = {
                    validate = true
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Login")
            }
        }

        if (validate) {
            ValidateLogin(numTelem = telem, pin = pin, navController = navController)
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

