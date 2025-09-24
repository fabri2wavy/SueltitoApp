package com.example.suletitoapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RecargaSaldoScreen(
    saldoActual: Double,
    onRecargar: (Double) -> Unit,
    onCancelar: () -> Unit
) {
    var montoTexto by remember { mutableStateOf("") }
    var errorMensaje by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var montoConfirmado by remember { mutableStateOf(0.0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¡Hola! Tu Saldo Actual:",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Mostrar saldo actual
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Bs. ${String.format("%.2f", saldoActual)}",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Campo para ingresar monto
        OutlinedTextField(
            value = montoTexto,
            onValueChange = {
                montoTexto = it
                errorMensaje = null
            },
            label = { Text("Monto a recargar") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = { Text("Bs.") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Botones rápidos
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf(10, 20, 50).forEach { monto ->
                OutlinedButton(onClick = {
                    montoTexto = monto.toString()
                }) {
                    Text("+$monto")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar error
        errorMensaje?.let { mensaje ->
            Text(mensaje, color = Color.Red, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Botones
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = { onCancelar() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
            ) {
                Text("Atrás")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    val monto = montoTexto.toDoubleOrNull()
                    when {
                        montoTexto.isBlank() -> errorMensaje = "Por favor ingrese un monto"
                        monto == null || monto <= 0 -> errorMensaje = "Ingrese un monto válido"
                        monto < 1 -> errorMensaje = "El monto mínimo es Bs. 1"
                        monto > 1000 -> errorMensaje = "El monto máximo es Bs. 1000"
                        else -> {
                            montoConfirmado = monto
                            showDialog = true
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Recargar")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Información adicional con íconos
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Información importante:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ArrowDownward, contentDescription = null, tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Monto mínimo: Bs. 1", fontSize = 12.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Monto máximo: Bs. 1000", fontSize = 12.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Timer, contentDescription = null, tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("La recarga se procesa al instante", fontSize = 12.sp)
                }
            }
        }
    }

    // Dialog de confirmación
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmar Recarga") },
            text = { Text("¿Deseas recargar Bs. $montoConfirmado?\nSaldo final: Bs. ${saldoActual + montoConfirmado}") },
            confirmButton = {
                Button(onClick = {
                    onRecargar(montoConfirmado)
                    showDialog = false
                }) {
                    Text("Sí, recargar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
