package com.example.suletitoapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

// Iconos
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

// Para los botones con colores personalizados
import androidx.compose.material3.ButtonDefaults

// Para el Divider en el resumen
import androidx.compose.material3.Divider

// Para el Switch
import androidx.compose.material3.Switch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinibusPaymentScreen(
    pasajeroNombre: String,
    saldoActual: Double,
    onPagar: (Double) -> Unit,
    onCancelar: () -> Unit,
    isProcessing: Boolean = false,
    mensaje: String = ""
) {
    var tarifaPreferencial by remember { mutableStateOf(false) }
    var totalAcumulado by remember { mutableStateOf(0.0) }
    var contadorCorto by remember { mutableStateOf(0) }
    var contadorLargo by remember { mutableStateOf(0) }
    val esNocturno = remember { TarifasManager.esHorarioNocturno() }

    // Tarifas normales y preferenciales
    val pasajeCorto = if (tarifaPreferencial) 2.0 else 2.4
    val pasajeLargo = if (tarifaPreferencial) 2.60 else 3.0
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pago Minibus", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onCancelar) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
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

            // Switch para tarifa preferencial
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Tarifa Preferencial",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = if (tarifaPreferencial) "Activada" else "Desactivada",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = tarifaPreferencial,
                        onCheckedChange = { tarifaPreferencial = it },
                        enabled = !isProcessing
                    )
                }
            }

            // Botones de pago
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Botón Pasaje Corto
                Button(
                    onClick = {
                        totalAcumulado += pasajeCorto
                        contadorCorto++
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    enabled = !isProcessing,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "PASAJE CORTO",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "Bs. $pasajeCorto",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        if (contadorCorto > 0) {
                            Text(
                                text = "Agregado: $contadorCorto",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }

                // Botón Pasaje Largo
                Button(
                    onClick = {
                        totalAcumulado += pasajeLargo
                        contadorLargo++
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    enabled = !isProcessing,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "PASAJE LARGO",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                        Text(
                            text = "Bs. $pasajeLargo",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                        if (contadorLargo > 0) {
                            Text(
                                text = "Agregado: $contadorLargo",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                }
            }

            // Resumen del total acumulado
            if (totalAcumulado > 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Resumen del Pago",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        if (contadorCorto > 0) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Pasajes Cortos ($contadorCorto):")
                                Text("Bs. ${String.format("%.2f", contadorCorto * pasajeCorto)}")
                            }
                        }

                        if (contadorLargo > 0) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Pasajes Largos ($contadorLargo):")
                                Text("Bs. ${String.format("%.2f", contadorLargo * pasajeLargo)}")
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "TOTAL:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Bs. ${String.format("%.2f", totalAcumulado)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    totalAcumulado = 0.0
                                    contadorCorto = 0
                                    contadorLargo = 0
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Limpiar")
                            }

                            Button(
                                onClick = {
                                    if (totalAcumulado <= saldoActual) {
                                        onPagar(totalAcumulado)
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                enabled = !isProcessing && totalAcumulado <= saldoActual && totalAcumulado > 0
                            ) {
                                if (isProcessing) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text("Pagar Todo")
                                }
                            }
                        }

                        if (totalAcumulado > saldoActual) {
                            Text(
                                text = "Saldo insuficiente",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
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
                    Text("1. Presiona los botones para agregar pasajes")
                    Text("2. Revisa el total acumulado")
                    Text("3. Presiona 'Pagar Todo' y acerca el teléfono al NFC")
                }
            }
        }
    }
}