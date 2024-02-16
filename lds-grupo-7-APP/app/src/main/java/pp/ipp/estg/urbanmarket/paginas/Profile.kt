package pp.ipp.estg.urbanmarket.paginas

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import pp.ipp.estg.urbanmarket.R
import pp.ipp.estg.urbanmarket.retrofit.feiranteAPI
import pp.ipp.estg.urbanmarket.retrofit.models.feiranteRetrofit.Feirante
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(idFeirante: Int, navController: NavHostController) {
    var loading by remember { mutableStateOf(true) }
    //var feirante by remember { mutableStateOf(Feirante(0, "", "", "", 'x', 0, 0)) }

    var edit by remember { mutableStateOf(false) }
    var clickBtn by remember { mutableStateOf(false) }

    var nome by rememberSaveable { mutableStateOf("null") }
    var email by rememberSaveable { mutableStateOf("null") }
    var dataNasc by rememberSaveable { mutableStateOf("null") }
    var sexo by rememberSaveable { mutableStateOf('x') }
    var telem by rememberSaveable { mutableIntStateOf(0) }
    var pin by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(key1 = Unit) {
        if (loading) {
            feiranteAPI.getFeirante(idFeirante)
                .enqueue(object : Callback<Feirante> {
                    override fun onResponse(
                        call: Call<Feirante>,
                        response: Response<Feirante>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            val res = response.body()
                            if (res != null) {
                                nome = res.nome
                                email = res.email
                                dataNasc = res.dataNasc
                                sexo = res.sexo
                                telem = res.telem
                                pin = res.pin
                            }
                            loading = false
                        } else {
                            Log.d("Response Feirante", response.toString())
                        }
                    }

                    override fun onFailure(call: Call<Feirante>, t: Throwable) {
                        Log.d("Profile fail -> getFeirante()", t.toString())
                    }
                })
        }
    }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {

        //ProfileImage()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "",
                modifier = Modifier.size(120.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        //Nome
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Nome",
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(6.dp))

                TextField(
                    value = nome,
                    onValueChange = { nome = it },
                    modifier = Modifier.weight(1f),
                )

            }

        }

        //Email
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Email",
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(6.dp))
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.weight(1f),
                )
            }

        }

        //Telem
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Telemóvel",
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(6.dp))
                TextField(
                    value = telem.toString(),
                    onValueChange = { telem = it.toInt() },
                    modifier = Modifier.weight(1f),
                )
            }

        }

        //Data Nascimento
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Data Nascimento",
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(6.dp))
                TextField(
                    value = dataNasc,
                    onValueChange = { dataNasc = it },
                    modifier = Modifier.weight(1f),
                )
            }
        }

        //Sexo
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Sexo",
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(6.dp))

                var isSelected by remember { mutableStateOf("") }
                var isExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = isExpanded,
                    onExpandedChange = { isExpanded = it },
                    modifier = Modifier
                        .weight(1f)
                ) {
                    TextField(
                        value = sexo.toString(),
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = "Masculino") },
                            onClick = {
                                sexo = 'M'
                                isExpanded = false
                                isSelected = "Masculino"
                            }
                        )

                        DropdownMenuItem(
                            text = { Text(text = "Feminino") },
                            onClick = {
                                sexo = 'F'
                                isExpanded = false
                                isSelected = "Feminino"
                            }
                        )

                        DropdownMenuItem(
                            text = { Text(text = "Outro") },
                            onClick = {
                                sexo = 'O'
                                isExpanded = false
                                isSelected = "Outro"
                            }
                        )
                    }
                }
            }

        }

        //PIN
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "PIN",
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(6.dp))
                TextField(
                    value = pin.toString(),
                    onValueChange = { pin = it.toInt() },
                    modifier = Modifier.weight(1f),
                )
            }

        }

        Button(
            onClick = {
                edit = !edit
            },
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
                .fillMaxWidth()
        ) {
            if (!edit)
                Text(text = "Editar Perfil")
            else {
                Text(text = "Atualizar")
                clickBtn = true
            }
        }
    }

    if (!edit && clickBtn) {

        val updatedFeirante = Feirante(idFeirante, nome, email, dataNasc, sexo, telem, pin)

        loading = true
        LaunchedEffect(key1 = Unit) {
            if (loading) {
                feiranteAPI.putFeirante(idFeirante, updatedFeirante)
                    .enqueue(object : Callback<Feirante> {
                        override fun onResponse(
                            call: Call<Feirante>,
                            response: Response<Feirante>
                        ) {
                            if (response.isSuccessful) {
                                navController.popBackStack()
                                loading = false
                            } else
                                Log.d("Reponse Feirante: ", response.toString())
                        }

                        override fun onFailure(call: Call<Feirante>, t: Throwable) {
                            Log.d("DropDown fail -> putProdutoFeirante()", t.toString())
                        }
                    })
            }
        }
    }
}

@Composable
fun ProfileImage() {
    var editImage by remember { mutableStateOf(false) }

    val imageUri = rememberSaveable { mutableStateOf("") }
    val painter = rememberImagePainter(
        if (imageUri.value.isEmpty())
            R.drawable.ic_user
        else
            imageUri.value
    )
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imageUri.value = it.toString() }
    }

    val notification = rememberSaveable { mutableStateOf("") }
    if (notification.value.isNotEmpty()) {
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (editImage) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Cancel",
                    modifier = Modifier.clickable {
                        notification.value = "Cancelled"
                        editImage = false
                    }
                )
                Text(text = "Save",
                    modifier = Modifier.clickable {
                        notification.value = "Profile updated"
                        editImage = false
                    }
                )
            }
        }

        Card(
            shape = CircleShape,
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                        launcher.launch("image/*")
                        editImage = !editImage
                    },
                contentScale = ContentScale.Crop
            )
        }
        Text(text = "Change profile picture")
    }
}

