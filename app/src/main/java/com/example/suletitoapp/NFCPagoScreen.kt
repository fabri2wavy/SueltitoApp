package com.example.suletitoapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Payment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NFCPagoScreen(
    pasajeroNombre: String,
    saldoActual: Double,
    onPagar: (Double) -> Unit,
    onCancelar: () -> Unit,
    isProcessing: Boolean = false,
    mensaje: String = ""
) {
    var montoTexto by remember { mutableStateOf("") }
    var errorMensaje by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pago con NFC", style = MaterialTheme.typography.titleLarge) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Información del pasajero
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Pasajero: $pasajeroNombre",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Saldo disponible: Bs. ${String.format("%.2f", saldoActual)}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            // Campo para ingresar monto
            OutlinedTextField(
                value = montoTexto,
                onValueChange = {
                    montoTexto = it
                    errorMensaje = null
                },
                label = { Text("Monto a pagar") },
                placeholder = { Text("Ingrese el monto en Bs.") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                enabled = !isProcessing,
                singleLine = true,
                isError = errorMensaje != null,
                leadingIcon = { Text("Bs.") },
                supportingText = {
                    if (errorMensaje != null) {
                        Text(
                            text = errorMensaje!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            )

            // Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { onCancelar() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Atrás")
                }

                Button(
                    onClick = {
                        val monto = montoTexto.toDoubleOrNull()
                        when {
                            montoTexto.isBlank() -> errorMensaje = "Por favor ingrese un monto"
                            monto == null || monto <= 0 -> errorMensaje = "Ingrese un monto válido mayor a 0"
                            monto > saldoActual -> errorMensaje = "Saldo insuficiente"
                            monto > 100 -> errorMensaje = "El monto máximo es Bs. 100"
                            else -> onPagar(monto)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !isProcessing
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Procesando...")
                    } else {
                        Icon(Icons.Default.Payment, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Pagar")
                    }
                }
            }

            // Mensajes del sistema
            if (mensaje.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (mensaje.contains("exitoso"))
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = mensaje,
                        modifier = Modifier.padding(16.dp),
                        color = if (mensaje.contains("exitoso"))
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else
                            MaterialTheme.colorScheme.onErrorContainer,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Instrucciones
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Instrucciones:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text("1. Ingresa el monto a pagar")
                    Text("2. Presiona 'Pagar'")
                    Text("3. Acerca tu teléfono a la etiqueta NFC del conductor")
                    Text("4. El pago se procesará automáticamente")
                }
            }
        }
    }
}
