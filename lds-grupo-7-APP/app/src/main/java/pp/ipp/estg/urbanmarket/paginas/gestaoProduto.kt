package pp.ipp.estg.urbanmarket.paginas

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.*
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import pp.ipp.estg.urbanmarket.R
import pp.ipp.estg.urbanmarket.database.unidade.UnidadeMedida
import pp.ipp.estg.urbanmarket.retrofit.feiranteAPI
import pp.ipp.estg.urbanmarket.retrofit.models.categoriaProdutoRetrofit.CategoriaProduto
import pp.ipp.estg.urbanmarket.retrofit.models.feiranteRetrofit.Feirante
import pp.ipp.estg.urbanmarket.retrofit.models.produtoFeiranteRetrofit.ProdutoFeirante
import pp.ipp.estg.urbanmarket.retrofit.models.produtoRetrofit.Produto
import pp.ipp.estg.urbanmarket.retrofit.produtoAPI
import pp.ipp.estg.urbanmarket.retrofit.produtoFeiranteAPI
import pp.ipp.estg.urbanmarket.retrofit.unidadeMedidaAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenuEdit(
    categorias: List<CategoriaProduto>,
    produtos: List<Produto>,
    productF: ProdutoFeirante,
    navController: NavHostController
) {
    var categoriaType by remember { mutableStateOf<String?>("") }
    var delete by remember { mutableStateOf(false) }
    var edit by remember { mutableStateOf(false) }
    var clickBtn by remember { mutableStateOf(false) }

    var qtd by remember { mutableFloatStateOf(productF.quantidade) }
    var precoUni by remember { mutableFloatStateOf(productF.precoUni) }

    val produto = produtos.firstOrNull { it.id == productF.produtoId }
    categoriaType = categorias.firstOrNull { it.id == produto?.categoriaProdutoId }?.categoria

    var unidadeMedida by remember { mutableStateOf<UnidadeMedida?>(null) }
    var loading by remember { mutableStateOf(false) }

    loading = true
    LaunchedEffect(key1 = Unit) {
        if (loading) {
            produto?.let {
                unidadeMedidaAPI.getUnidadeMedida(it.unidMedidaId)
                    .enqueue(object : Callback<UnidadeMedida> {
                        override fun onResponse(
                            call: Call<UnidadeMedida>,
                            response: Response<UnidadeMedida>
                        ) {
                            if (response.isSuccessful) {
                                unidadeMedida = response.body()
                                loading = false
                            } else {
                                Log.d("Response ProdutoF", response.toString())
                            }
                        }

                        override fun onFailure(call: Call<UnidadeMedida>, t: Throwable) {
                            Log.d(
                                "Response reservar produto fail -> getProdutoFeirante()",
                                t.toString()
                            )
                        }
                    })
            }
        }
    }

    Text(
        text = "Categoria do Produto",
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        modifier = Modifier.padding(top = 20.dp)
    )

    TextField(
        value = categoriaType ?: "",
        onValueChange = {},
        readOnly = true,
        colors = ExposedDropdownMenuDefaults.textFieldColors(),
        modifier = Modifier.padding(top = 10.dp)
    )

    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = "Produto",
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        modifier = Modifier.padding(bottom = 10.dp)
    )

    TextField(
        value = produto?.nome ?: "",
        onValueChange = {},
        readOnly = true,
        colors = ExposedDropdownMenuDefaults.textFieldColors(),
        modifier = Modifier.padding(bottom = 10.dp)
    )

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = produto?.linkImagem,
            contentDescription = null,
            modifier = Modifier.padding(top = 10.dp)
        )
    }

    Column(
        modifier = Modifier.padding(vertical = 20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .weight(1f)
            ) {
                Text(
                    text = "Quantidade ( ${unidadeMedida?.uniMedida} )",
                    fontWeight = FontWeight.Bold
                )
                TextField(
                    value = if (qtd != 0.0F) {
                        qtd.toString().trimStart('0')
                    } else {
                        "0"
                    },
                    maxLines = 1,
                    readOnly = !edit,
                    onValueChange = { newText ->
                        val isValidInput = newText.count { char -> char == '.' } <= 1 &&
                                newText.all { char -> char.isDigit() || char == '.' }

                        if (isValidInput) {
                            qtd = newText.toFloatOrNull() ?: 0.0F
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Preço Unidade (€)",
                    fontWeight = FontWeight.Bold
                )

                TextField(
                    value = if (precoUni != 0.0F) {
                        precoUni.toString().trimStart('0')
                    } else {
                        "0"
                    },
                    maxLines = 1,
                    readOnly = !edit,
                    onValueChange = { newText ->
                        val isValidInput = newText.count { char -> char == '.' } <= 1 &&
                                newText.all { char -> char.isDigit() || char == '.' }

                        if (isValidInput) {
                            precoUni = newText.toFloatOrNull() ?: 0.0F
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
            }
        }

    }

    Spacer(modifier = Modifier.height(20.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = {
                edit = !edit
            },
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
                .weight(1f)
        ) {
            if (!edit)
                Text(text = "Editar Produto")
            else {
                Text(text = "Atualizar")
                clickBtn = true
            }
        }

        Button(
            onClick = {
                delete = !delete
            },
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
                .weight(1f)
        ) {
            Text(text = "Eliminar produto")
        }
    }

    if (!edit && clickBtn) {
        val updateProdutoFeirante = ProdutoFeirante(
            productF.id,
            productF.produtoId,
            productF.feiranteId,
            qtd,
            precoUni
        )

        loading = true
        LaunchedEffect(key1 = Unit) {
            if (loading) {
                produtoFeiranteAPI.putProdutoFeirante(productF.id, updateProdutoFeirante)
                    .enqueue(object : Callback<ProdutoFeirante> {
                        override fun onResponse(
                            call: Call<ProdutoFeirante>,
                            response: Response<ProdutoFeirante>
                        ) {
                            if (response.isSuccessful) {
                                loading = false
                                navController.popBackStack()
                            }
                        }

                        override fun onFailure(call: Call<ProdutoFeirante>, t: Throwable) {
                            Log.d("DropDown fail -> putProdutoFeirante()", t.toString())
                        }
                    })
            }
        }
    }

    if (delete) {
        loading = true
        LaunchedEffect(key1 = Unit) {
            if (loading) {
                produtoFeiranteAPI.deleteProdutoFeirante(productF.id)
                    .enqueue(object : Callback<ProdutoFeirante> {
                        override fun onResponse(
                            call: Call<ProdutoFeirante>,
                            response: Response<ProdutoFeirante>
                        ) {
                            if (response.isSuccessful) {
                                navController.popBackStack()
                                loading = false
                                delete = false
                            } else {
                                Log.d("Response ProdutoF", response.toString())
                            }
                        }

                        override fun onFailure(call: Call<ProdutoFeirante>, t: Throwable) {
                            Log.d(
                                "Response add produto fail -> getProdutoFeirante()",
                                t.toString()
                            )
                        }
                    })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProdutoPagina(
    categorias: List<CategoriaProduto>,
    produtos: List<Produto>,
    idFeirante: Int,
    navController: NavHostController
) {

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {

        var isCategoriaSelected by remember { mutableStateOf<Int?>(0) }
        var isCategoriaExpanded by remember { mutableStateOf(false) }
        var categoriaType by remember { mutableStateOf<String?>("") }
        var unidademedida by remember { mutableStateOf<Int?>(null) }
        var loading by remember { mutableStateOf(false) }

        Text(
            text = "Categoria do Produto",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 20.dp)
        )

        ExposedDropdownMenuBox(
            expanded = isCategoriaExpanded,
            onExpandedChange = { isCategoriaExpanded = it },
            modifier = Modifier.padding(top = 10.dp)
        ) {
            categoriaType?.let {
                TextField(
                    value = it,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoriaExpanded)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier.menuAnchor()
                )
            }

            ExposedDropdownMenu(
                expanded = isCategoriaExpanded,
                onDismissRequest = { isCategoriaExpanded = false }
            ) {
                categorias.forEach() {
                    DropdownMenuItem(
                        text = { Text(text = it.categoria) },
                        onClick = {
                            categoriaType = it.categoria
                            isCategoriaExpanded = false
                            isCategoriaSelected = it.id
                        }
                    )
                }
            }
        }

        val productsByCategoria = produtos.filter { it.categoriaProdutoId == isCategoriaSelected }

        var isProdutoSelected by remember { mutableStateOf<Produto?>(Produto(0, "", "", 0, 0)) }
        var isProdutoExpanded by remember { mutableStateOf(false) }
        var produtoType by remember { mutableStateOf(isProdutoSelected?.nome) }

        var qtd by remember { mutableFloatStateOf(0.0F) }
        var precoUni by remember { mutableFloatStateOf(0.0F) }


        Column(
            modifier = Modifier.padding(vertical = 20.dp)
        ) {
            Text(
                text = "Produto",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            ExposedDropdownMenuBox(
                expanded = isProdutoExpanded,
                onExpandedChange = { isProdutoExpanded = it },
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                produtoType?.let {
                    TextField(
                        value = it,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isProdutoExpanded)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier.menuAnchor()
                    )
                }

                ExposedDropdownMenu(
                    expanded = isProdutoExpanded,
                    onDismissRequest = { isProdutoExpanded = false }
                ) {
                    productsByCategoria.forEach {
                        DropdownMenuItem(
                            text = { Text(text = it.nome) },
                            onClick = {
                                loading = true
                                unidademedida = it.unidMedidaId
                                produtoType = it.nome
                                isProdutoExpanded = false
                                isProdutoSelected = it
                            }
                        )
                    }
                }
            }


            var unidade by remember { mutableStateOf<UnidadeMedida?>(null) }
            if (loading) {
                LaunchedEffect(key1 = Unit) {
                    unidademedida?.let {
                        unidadeMedidaAPI.getUnidadeMedida(it)
                            .enqueue(object : Callback<UnidadeMedida> {
                                override fun onResponse(
                                    call: Call<UnidadeMedida>,
                                    response: Response<UnidadeMedida>
                                ) {
                                    if (response.isSuccessful) {
                                        unidade = response.body()
                                        loading = false
                                    } else {
                                        Log.d("Response Unidademedida", response.toString())
                                    }
                                }

                                override fun onFailure(call: Call<UnidadeMedida>, t: Throwable) {
                                    Log.d(
                                        "Response addproduto fail -> getunidademedida()",
                                        t.toString()
                                    )
                                }
                            })
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                AsyncImage(
                    model = isProdutoSelected?.linkImagem,
                    contentDescription = null,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .weight(1f)
                ) {
                    Text(

                        text = "Quantidade (" + unidade?.uniMedida + ")",
                        fontWeight = FontWeight.Bold
                    )
                    TextField(
                        value = if (qtd != 0.0F) {
                            qtd.toString().trimStart('0')
                        } else {
                            "0"
                        },
                        maxLines = 1,
                        onValueChange = { newText ->
                            val isValidInput = newText.count { char -> char == '.' } <= 1 &&
                                    newText.all { char -> char.isDigit() || char == '.' }

                            if (isValidInput) {
                                qtd = newText.toFloatOrNull() ?: 0.0F
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Preço Unidade (€)",
                        fontWeight = FontWeight.Bold
                    )
                    TextField(
                        value = if (precoUni != 0.0F) {
                            precoUni.toString().trimStart('0')
                        } else {
                            "0"
                        },
                        maxLines = 1,
                        onValueChange = { newText ->
                            val isValidInput = newText.count { char -> char == '.' } <= 1 &&
                                    newText.all { char -> char.isDigit() || char == '.' }

                            if (isValidInput) {
                                precoUni = newText.toFloatOrNull() ?: 0.0F
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )
                }
            }

        }

        Spacer(modifier = Modifier.height(20.dp))

        var add by remember { mutableStateOf(false) }
        Button(
            onClick = {
                add = !add
            },
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Adicionar Produto")
        }

        if (add) {
            var loading by remember { mutableStateOf(true) }
            val newProdutoFeirante = isProdutoSelected?.let {
                ProdutoFeirante(
                    it.id,
                    it.id,
                    idFeirante,
                    qtd,
                    precoUni
                )
            }

            LaunchedEffect(key1 = Unit) {
                if (newProdutoFeirante != null && loading) {
                    produtoFeiranteAPI.postProdutoFeirante(newProdutoFeirante)
                        .enqueue(object : Callback<ProdutoFeirante> {
                            override fun onResponse(
                                call: Call<ProdutoFeirante>,
                                response: Response<ProdutoFeirante>
                            ) {
                                if (response.isSuccessful) {
                                    Log.d("New ProdutoFeirante", response.body().toString())
                                    loading = false
                                    navController.popBackStack()
                                } else {
                                    Log.d("Response NewProdutosF", response.toString())
                                }
                            }

                            override fun onFailure(call: Call<ProdutoFeirante>, t: Throwable) {
                                Log.d("DropDownADD fail -> postProdutoFeirante()", t.toString())
                            }
                        })
                }
            }
        }
    }
}

@Composable
fun ViewProductPage(
    categorias: List<CategoriaProduto>,
    produtos: List<Produto>,
    idProdutoFeirante: Int,
    navController: NavHostController
) {
    var loading by remember { mutableStateOf(true) }
    var produtoF by remember { mutableStateOf<ProdutoFeirante?>(null) }

    LaunchedEffect(key1 = Unit) {
        if (loading) {
            produtoFeiranteAPI.getProdutoFeirante(idProdutoFeirante)
                .enqueue(object : Callback<ProdutoFeirante> {
                    override fun onResponse(
                        call: Call<ProdutoFeirante>,
                        response: Response<ProdutoFeirante>
                    ) {
                        if (response.isSuccessful) {
                            produtoF = response.body()
                            loading = false
                        } else {
                            Log.d("Response Produtos", produtoF.toString())
                        }
                    }

                    override fun onFailure(call: Call<ProdutoFeirante>, t: Throwable) {
                        Log.d("gestaoProduto fail -> getProdutoFeirante()", t.toString())
                    }
                })
        }
    }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.pantry),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            produtoF?.let { DropDownMenuEdit(categorias, produtos, it, navController) }

        }
    }
}

@Composable
fun ReserveProductPage(idProduto: Int, idFeirante: Int, navController: NavHostController) {
    var loading by remember { mutableStateOf(true) }

    var produto by remember { mutableStateOf<Produto?>(null) }
    var produtoF by remember { mutableStateOf<ProdutoFeirante?>(null) }
    var feirante by remember { mutableStateOf<Feirante?>(null) }
    var unidadeMedida by remember { mutableStateOf<UnidadeMedida?>(null) }

    var qtd by remember { mutableFloatStateOf(0.0f) }

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
                        Log.d("Response reservar produto fail -> getFeirante()", t.toString())
                    }
                })
        }
    }

    loading = true
    LaunchedEffect(key1 = Unit) {
        if (loading) {
            produtoAPI.getProduct(idProduto)
                .enqueue(object : Callback<Produto> {
                    override fun onResponse(
                        call: Call<Produto>,
                        response: Response<Produto>
                    ) {
                        if (response.isSuccessful) {
                            produto = response.body()
                            loading = false
                            Log.d("PRODUTO Unidade Medida API", produto.toString())


                            produto?.let {
                                unidadeMedidaAPI.getUnidadeMedida(it.unidMedidaId)
                                    .enqueue(object : Callback<UnidadeMedida> {
                                        override fun onResponse(
                                            call: Call<UnidadeMedida>,
                                            response: Response<UnidadeMedida>
                                        ) {
                                            if (response.isSuccessful) {
                                                unidadeMedida = response.body()
                                                loading = false
                                                Log.d(
                                                    "Response UNIDADE MEDIDA",
                                                    unidadeMedida.toString()
                                                )
                                            } else {
                                                Log.d(
                                                    "Response UNIDADE MEDIDA",
                                                    response.toString()
                                                )
                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<UnidadeMedida>,
                                            t: Throwable
                                        ) {
                                            Log.d(
                                                "Response reservar produto fail -> getUnidadeMedida()",
                                                t.toString()
                                            )
                                        }
                                    })
                            }

                        } else {
                            Log.d("Response Produto", response.toString())
                        }
                    }

                    override fun onFailure(call: Call<Produto>, t: Throwable) {
                        Log.d(" Response reservar produto fail -> getProduct()", t.toString())
                    }
                })
        }
    }

    loading = true
    LaunchedEffect(key1 = Unit) {
        if (loading) {
            produtoFeiranteAPI.getProdutosByFeirante(idFeirante)
                .enqueue(object : Callback<List<ProdutoFeirante>> {
                    override fun onResponse(
                        call: Call<List<ProdutoFeirante>>,
                        response: Response<List<ProdutoFeirante>>
                    ) {
                        if (response.isSuccessful) {
                            produtoF = response.body()?.firstOrNull { it.produtoId == idProduto }
                            loading = false

                        } else {
                            Log.d("Response ProdutoF", response.toString())
                        }
                    }

                    override fun onFailure(call: Call<List<ProdutoFeirante>>, t: Throwable) {
                        Log.d(
                            "Response reservar produto fail -> getProdutoFeirante()",
                            t.toString()
                        )
                    }
                })
        }
    }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            AsyncImage(
                model = produto?.linkImagem,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(10.dp)
            ) {

                Text(
                    text = "${produto?.nome}  (${produtoF?.precoUni}€ p/${unidadeMedida?.uniMedida})",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Quantidade disponível: ${produtoF?.quantidade} ${unidadeMedida?.uniMedida}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Feirante:",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Card(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            IconButton(
                                onClick = {},
                                modifier = Modifier.size(60.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = feirante?.nome ?: "null",
                                fontSize = 18.sp
                            )
                        }


                        var isPermissionGranted by remember { mutableStateOf(false) }
                        val context = rememberContext()

                        val requestPermissionLauncher = rememberLauncher {
                            isPermissionGranted = it
                        }
                        Button(onClick = {
                            val phoneNumber =
                                "tel:${feirante?.telem}" // Substitua com o número de telefone do feirante
                            val intent = Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber))

                            // Verifique se a permissão CALL_PHONE está concedida antes de realizar a chamada
                            if (isPermissionGranted || ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CALL_PHONE
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                context.startActivity(intent)
                            } else {
                                // Solicite permissão ao usuário se a permissão ainda não estiver concedida
                                requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                            }
                        }) {
                            Icon(imageVector = Icons.Default.Call, contentDescription = "")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Quantidade:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(start = 6.dp)

                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            Button(
                                onClick = { qtd = (qtd - 0.1f).coerceAtLeast(0.0f) },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(text = "-")
                            }

                            Text(
                                text = "%.1f".format(qtd),
                                modifier = Modifier.padding(horizontal = 6.dp)
                            )

                            Button(
                                onClick = {
                                    qtd = (qtd + 0.1f).coerceAtMost(produtoF?.quantidade ?: 0f)
                                },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(text = "+")
                            }
                        }
                    }
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Total: ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                )

                Text(
                    text = "%.1f".format((produtoF?.precoUni ?: 0f) * qtd) + "€",
                    fontSize = 22.sp,
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = { navController.popBackStack() },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Reservar")
                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "")
            }
        }
    }
}


@Composable
fun rememberContext(): ComponentActivity {
    val context = LocalContext.current
    return (context as? ComponentActivity)
        ?: error("CompositionLocal LocalContext not found in the hierarchy. Make sure to use this composable within a Compose hierarchy hosted by a ComponentActivity.")
}

@Composable
fun rememberLauncher(onResult: (Boolean) -> Unit): ActivityResultLauncher<String> {
    return rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
        onResult(result)
    }
}