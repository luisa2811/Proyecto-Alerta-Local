package uniquindio.proyectoalertalocal.features.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.ui.theme.ProyectoAlertaLocalThemem

private val AzulOscuro = Color(0xFF1E3043)
private val FondoPantalla = Color(0xFFFAF9F6)
private val TextoSecundario = Color(0xFF78818B)
private val Naranja = Color(0xFFFF5722)
private val BordeCampo = Color(0xFFE4E1DC)
private val FondoSelector = Color(0xFFF0EDE8)

@Composable
fun PantallaInicioSesion(
    onForgotPasswordClick: (String) -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
    modeloVista: InicioSesionViewModel = viewModel()
) {
    val estado = modeloVista.estado.collectAsState().value
    val estadoSnackbar = remember { SnackbarHostState() }

    LaunchedEffect(estado.mensajeSnackbar) {
        estado.mensajeSnackbar?.let { mensaje ->
            estadoSnackbar.showSnackbar(mensaje)
            modeloVista.limpiarMensajeSnackbar()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = FondoPantalla,
        snackbarHost = {
            SnackbarHost(hostState = estadoSnackbar) { datos ->
                Snackbar(
                    containerColor = AzulOscuro,
                    contentColor = Color.White
                ) {
                    Text(text = datos.visuals.message)
                }
            }
        }
    ) { espacioInterior ->
        ContenidoInicioSesion(
            estado = estado,
            alCambiarCorreo = modeloVista::cambiarCorreo,
            alCambiarContrasena = modeloVista::cambiarContrasena,
            alCambiarRol = modeloVista::cambiarRol,
            alIntentarInicioSesion = onLoginSuccess,
            alOlvidarContrasena = { onForgotPasswordClick(estado.correo) },
            alCrearCuenta = onRegisterClick,
            modificador = Modifier.padding(espacioInterior)
        )
    }
}

@Composable
private fun ContenidoInicioSesion(
    estado: InicioSesionUiState,
    alCambiarCorreo: (String) -> Unit,
    alCambiarContrasena: (String) -> Unit,
    alCambiarRol: (String) -> Unit,
    alIntentarInicioSesion: () -> Unit,
    alOlvidarContrasena: () -> Unit,
    alCrearCuenta: () -> Unit,
    modificador: Modifier = Modifier
) {
    Column(
        modifier = modificador
            .fillMaxSize()
            .background(FondoPantalla)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 48.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoAlertaLocal()
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "Alerta Local",
            color = AzulOscuro,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Reportes ciudadanos de tu barrio",
            color = TextoSecundario,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(38.dp))
        SelectorDeRol(
            rolSeleccionado = estado.rolSeleccionado,
            alSeleccionarRol = alCambiarRol
        )

        Spacer(modifier = Modifier.height(20.dp))
        CampoDeFormulario(
            etiqueta = "Correo electrónico",
            valor = estado.correo,
            alCambiar = alCambiarCorreo,
            indicacion = "nombre@correo.com",
            tipoTeclado = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(16.dp))
        CampoDeFormulario(
            etiqueta = "Contraseña",
            valor = estado.contrasena,
            alCambiar = alCambiarContrasena,
            indicacion = "password123",
            tipoTeclado = KeyboardType.Password,
            ocultarTexto = true
        )

        Text(
            text = "¿Olvidaste tu contraseña?",
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 22.dp)
                .clickable(onClick = alOlvidarContrasena),
            color = AzulOscuro,
            fontSize = 12.sp
        )

        Button(
            onClick = alIntentarInicioSesion,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 26.dp)
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulOscuro,
                contentColor = Color.White
            )
        ) {
            Text(text = "Iniciar sesión", fontSize = 14.sp)
        }

        SeparadorDecorativo(modifier = Modifier.padding(top = 25.dp))

        val textoRegistro = buildAnnotatedString {
            withStyle(SpanStyle(color = AzulOscuro)) {
                append("¿Eres nuevo? ")
            }
            withStyle(
                SpanStyle(
                    color = Naranja,
                    fontWeight = FontWeight.SemiBold
                )
            ) {
                append("Crear una cuenta")
            }
        }
        Text(
            text = textoRegistro,
            modifier = Modifier
                .padding(top = 24.dp)
                .clickable(onClick = alCrearCuenta),
            fontSize = 13.sp
        )
    }
}

@Composable
private fun LogoAlertaLocal() {
    Box(
        modifier = Modifier
            .size(80.dp)
            .shadow(12.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_alerta_local),
            contentDescription = "Logo de Alerta Local",
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentScale = ContentScale.Fit

        )
    }
}



@Composable
private fun SelectorDeRol(
    rolSeleccionado: String,
    alSeleccionarRol: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(FondoSelector)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OpcionDeRol(
            nombre = "Residente",
            seleccionado = rolSeleccionado == "Residente",
            alSeleccionar = { alSeleccionarRol("Residente") },
            modificador = Modifier.weight(1f)
        )
        OpcionDeRol(
            nombre = "Moderador",
            seleccionado = rolSeleccionado == "Moderador",
            alSeleccionar = { alSeleccionarRol("Moderador") },
            modificador = Modifier.weight(1f)
        )
    }
}

@Composable
private fun OpcionDeRol(
    nombre: String,
    seleccionado: Boolean,
    alSeleccionar: () -> Unit,
    modificador: Modifier = Modifier
) {
    val colorIcono = when {
        nombre == "Moderador" -> Naranja
        seleccionado -> Color.White
        else -> AzulOscuro
    }

    Row(
        modifier = modificador
            .fillMaxSize()
            .clip(RoundedCornerShape(13.dp))
            .background(if (seleccionado) AzulOscuro else Color.Transparent)
            .clickable(onClick = alSeleccionar),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (nombre == "Residente") {
                Icons.Outlined.PersonOutline
            } else {
                Icons.Outlined.Shield
            },
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = colorIcono
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = nombre,
            color = if (seleccionado) Color.White else AzulOscuro,
            fontSize = 13.sp,
            fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun CampoDeFormulario(
    etiqueta: String,
    valor: String,
    alCambiar: (String) -> Unit,
    indicacion: String,
    tipoTeclado: KeyboardType,
    ocultarTexto: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = etiqueta,
            modifier = Modifier.padding(start = 2.dp),
            color = AzulOscuro,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(7.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(BorderStroke(1.dp, BordeCampo), RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = valor,
                onValueChange = alCambiar,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = AzulOscuro,
                    fontSize = 14.sp,
                    letterSpacing = if (ocultarTexto) 1.sp else 0.sp
                ),
                keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado),
                visualTransformation = if (ocultarTexto) {
                    PasswordVisualTransformation()
                } else {
                    androidx.compose.ui.text.input.VisualTransformation.None
                },
                decorationBox = { campo ->
                    Box {
                        if (valor.isEmpty()) {
                            Text(
                                text = indicacion,
                                color = AzulOscuro,
                                fontSize = 14.sp,
                                letterSpacing = if (ocultarTexto) 1.sp else 0.sp
                            )
                        }
                        campo()
                    }
                }
            )
        }
    }
}

@Composable
private fun SeparadorDecorativo(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(BordeCampo)
        )
        Spacer(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(6.dp)
                .background(Color(0xFFC9C6C0), CircleShape)
        )
        Spacer(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(BordeCampo)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF9F6)
@Composable
private fun VistaPreviaInicioSesion() {
    ProyectoAlertaLocalTheme {
        ContenidoInicioSesion(
            estado = InicioSesionUiState(),
            alCambiarCorreo = {},
            alCambiarContrasena = {},
            alCambiarRol = {},
            alIntentarInicioSesion = {},
            alOlvidarContrasena = {},
            alCrearCuenta = {}
        )
    }
}
