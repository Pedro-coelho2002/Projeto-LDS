package pp.ipp.estg.urbanmarket.paginas

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import pp.ipp.estg.urbanmarket.database.unidade.UnidadeMedida
import pp.ipp.estg.urbanmarket.retrofit.models.produtoFeiranteRetrofit.ProdutoFeirante
import pp.ipp.estg.urbanmarket.retrofit.models.produtoRetrofit.Produto
import pp.ipp.estg.urbanmarket.retrofit.produtoAPI
import pp.ipp.estg.urbanmarket.retrofit.produtoFeiranteAPI
import pp.ipp.estg.urbanmarket.retrofit.unidadeMedidaAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun CardItem(navController: NavHostController, produtoF: ProdutoFeirante, produto: Produto) {

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
                        text = "Novo Produto",
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

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedButton(
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color.White
                    ),
                    onClick = { navController.navigate("ViewProduct/${produtoF.id}") }
                ) {
                    Text(
                        text = "Read More",
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
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }
}

@Composable
fun MeusProdutos(navController: NavHostController, idFeirante: Int) {
    var loading by remember { mutableStateOf(true) }
    var productsF by remember { mutableStateOf<List<ProdutoFeirante>>(emptyList()) }
    var products by remember { mutableStateOf<List<Produto>>(emptyList()) }

    LaunchedEffect(key1 = Unit) {
        if (productsF.isEmpty() && loading) {
            produtoFeiranteAPI.getProdutosByFeirante(idFeirante)
                .enqueue(object : Callback<List<ProdutoFeirante>> {
                    override fun onResponse(
                        call: Call<List<ProdutoFeirante>>,
                        response: Response<List<ProdutoFeirante>>
                    ) {
                        if (response.isSuccessful) {
                            productsF = response.body() ?: emptyList()
                            loading = false
                        } else {
                            Log.d("Response ProdutosF", response.toString())
                        }
                    }

                    override fun onFailure(call: Call<List<ProdutoFeirante>>, t: Throwable) {
                        Log.d("Screen Principal fail -> getProdutosByFeirante()", t.toString())
                    }
                })
        }
    }

    val uniqueProducts = mutableSetOf<Produto>()
    productsF.forEach { produtoFeirante ->
        loading = true
        LaunchedEffect(key1 = produtoFeirante.produtoId) {
            if (loading) {
                produtoAPI.getProduct(produtoFeirante.produtoId)
                    .enqueue(object : Callback<Produto> {
                        override fun onResponse(
                            call: Call<Produto>,
                            response: Response<Produto>
                        ) {
                            if (response.isSuccessful) {
                                val product = response.body()
                                product?.let {
                                    uniqueProducts.add(it)
                                    products = uniqueProducts.toList()
                                }
                                loading = false
                            } else {
                                Log.d("Response Produtos", response.toString())
                            }
                        }

                        override fun onFailure(call: Call<Produto>, t: Throwable) {
                            Log.d("Screen Principal fail -> getProduct()", t.toString())
                        }
                    })
            }
        }
    }

    var searchText by remember { mutableStateOf("") }

    LazyColumn {
        val filteredProducts = products.filter {
            it.nome.contains(searchText, ignoreCase = true)
        }
        item {
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

            Spacer(modifier = Modifier.height(10.dp))

            // O primeiro item é um botão especial
            AddProductButton(
                onClick = { navController.navigate("Adicionar Produto/$idFeirante") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            filteredProducts.forEach { produto ->
                val prodF = productsF.find { it.produtoId == produto.id }
                if (prodF != null) {
                    CardItem(navController = navController, produtoF = prodF, produto = produto)
                }
            }
        }
    }
}

@Composable
fun AddProductButton(onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(16.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Adicionar Produto", fontWeight = FontWeight.Bold)
        }
    }
}