package pp.ipp.estg.urbanmarket.paginas

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import pp.ipp.estg.urbanmarket.retrofit.feiranteAPI
import pp.ipp.estg.urbanmarket.retrofit.models.feiranteRetrofit.Feirante
import pp.ipp.estg.urbanmarket.retrofit.models.produtoRetrofit.Produto
import pp.ipp.estg.urbanmarket.retrofit.produtoAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun CardProduto(navController: NavHostController, produto: Produto) {

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFDAE1E7),
        modifier = Modifier
            .height(210.dp)
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2f),
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.wrapContentSize(),
                    color = Color(0xFFD1D5E1)
                ) {
                    Text(
                        text = "Recomendado",
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = produto.nome,
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedButton(
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color.White
                    ),
                    onClick = { navController.navigate("Feirantes Proximos/${produto.id}") }
                ) {
                    Text(
                        text = "Feirantes Próximos",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.size(width = 100.dp, height = 140.dp)
            ) {
                AsyncImage(
                    model = produto.linkImagem,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .height(130.dp)
                )
            }
        }
    }
}

@Composable
fun ScreenPrincipal(idFeirante: Int, navController: NavHostController) {
    var loading by remember { mutableStateOf(true) }
    var feirante by remember { mutableStateOf<Feirante?>(null) }
    var products by remember { mutableStateOf<List<Produto>>(emptyList()) }

    LaunchedEffect(key1 = Unit) {
        if (loading) {
            feiranteAPI.getFeirante(idFeirante)
                .enqueue(object : Callback<Feirante> {
                    override fun onResponse(
                        call: Call<Feirante>,
                        response: Response<Feirante>
                    ) {
                        if (response.isSuccessful) {
                            feirante = response.body()
                            loading = false
                        } else {
                            Log.d("Response Feirante", response.toString())
                        }
                    }

                    override fun onFailure(call: Call<Feirante>, t: Throwable) {
                        Log.d("Screen Principal fail -> getFeirante()", t.toString())
                    }
                })
        }
    }

    loading = true
    LaunchedEffect(key1 = Unit) {
        if (loading) {
            produtoAPI.getProducts()
                .enqueue(object : Callback<List<Produto>> {
                    override fun onResponse(
                        call: Call<List<Produto>>,
                        response: Response<List<Produto>>
                    ) {
                        if (response.isSuccessful) {
                            products = response.body() ?: emptyList()
                            loading = false
                        } else {
                            Log.d("Response Produtos", response.toString())
                        }
                    }

                    override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                        Log.d("Screen Principal fail -> getProdutos()", t.toString())
                    }
                })
        }
    }

    var searchText by remember { mutableStateOf("") }

    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxSize()
    ) {
        val filteredProducts = products.filter {
            it.nome.contains(searchText, ignoreCase = true)
        }
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { navController.navigate("Perfil/${feirante?.id}") },
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp)
                ) {
                    Text(
                        text = "Olá,\n${feirante?.nome}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    IconButton(
                        onClick = {  },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "",
                        )
                    }
                }
            }


            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp)
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Pesquisar produtos...") },
                    trailingIcon = {
                        IconButton(
                            onClick = { searchText = "" },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(imageVector = Icons.Filled.Clear, contentDescription = null)
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            filteredProducts.forEach { produto ->
                CardProduto(navController = navController, produto = produto)
            }

        }
    }
}