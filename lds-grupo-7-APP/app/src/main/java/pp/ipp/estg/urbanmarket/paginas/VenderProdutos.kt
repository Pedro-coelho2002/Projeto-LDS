package pp.ipp.estg.urbanmarket.paginas

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import pp.ipp.estg.urbanmarket.retrofit.models.produtoRetrofit.Produto


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardElevationVender(navController: NavHostController, produtos: List<Produto>) {
    var searchText by remember { mutableStateOf("") }
    var productList by remember { mutableStateOf(produtos) }

    LaunchedEffect(productList) {
        productList = productList
    }

    // Filtrar a lista de produtos com texto de pesquisa
    val filteredProducts = productList.filter {
        it.nome.contains(searchText, ignoreCase = true)
    }


    LazyColumn {
        item {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp)
            ) {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    maxLines = 1,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    },
                    placeholder = { Text(text = "Search...") },
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = Color.Gray,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            filteredProducts.forEach { product ->
                CardItemVender(product = product, navController)
            }
        }
    }
}

@Composable
fun CardItemVender(product: Produto, navController: NavHostController) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFDAE1E7),
        modifier = Modifier
            .height(210.dp)
            .padding(10.dp)
        //.elevation(10.dp)
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
                    text = product.nome,
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                //TODO - passar a informação do produtoFeirante selecionado

                OutlinedButton(
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color.White
                    ),
                    onClick = { navController.navigate("ReserveProduct/${product.id}") }
                ) {
                    Text(
                        text = "Comprar",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Icon(
                        Icons.Rounded.ShoppingCart,
                        tint = Color(0xFF0F0F0E),
                        contentDescription = null
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.size(width = 150.dp, height = 140.dp)
            ) {
                AsyncImage(
                    model = product.linkImagem,
                    contentDescription = null,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }
}
