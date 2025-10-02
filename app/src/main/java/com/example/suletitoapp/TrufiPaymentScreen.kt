package com.example.suletitoapp

// Imports básicos de Compose
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
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.WbSunny

// Para los botones con colores personalizados
import androidx.compose.material3.ButtonDefaults

// Para el Divider en el resumen
import androidx.compose.material3.Divider

// Para el Switch
import androidx.compose.material3.Switch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrufiPaymentScreen(
    pasajeroNombre: String,
    saldoActual: Double,
    onPagar: (Double) -> Unit,
    onCancelar: () -> Unit,
    isProcessing: Boolean = false,
    mensaje: String = ""
) {
    var tarifaPreferencial by remember { mutableStateOf(false) }
    var totalAcumulado by remember { mutableStateOf(0.0) }
    var contadores by remember { mutableStateOf(listOf(0, 0, 0, 0)) }
    val esNocturno = remember { TarifasManager.esHorarioNocturno() }

    // Tarifas normales y preferenciales para trufi
    val tarifas = if (tarifaPreferencial) {
        listOf(
            TarifasManager.Trufi.ZONAL_PREFERENCIAL,
            TarifasManager.Trufi.CORTO_PREFERENCIAL,
            TarifasManager.Trufi.LARGO_PREFERENCIAL,
            TarifasManager.Trufi.EXTRA_LARGO_PREFERENCIAL
        )
    } else {
        listOf(
            TarifasManager.Trufi.getZonal(),
            TarifasManager.Trufi.getCorto(),
            TarifasManager.Trufi.getLargo(),
            TarifasManager.Trufi.getExtraLargo()
        )
    }

    val zonas = listOf("Zonal", "Corto", "Largo", "Extra Largo")
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pago Trufi", style = MaterialTheme.typography.titleLarge) },
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
                colors = CardDefaults.cardColors(
                    containerColor = if (esNocturno)
                        MaterialTheme.colorScheme.tertiaryContainer
                    else
                        MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        imageVector = if (esNocturno) Icons.Default.DarkMode else Icons.Default.WbSunny,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = TarifasManager.getHorarioTexto(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
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
                        onCheckedChange = {
                            tarifaPreferencial = it
                            // Recalcular total con nuevas tarifas
                            val nuevasTarifas = if (it) {
                                listOf(
                                    TarifasManager.Trufi.ZONAL_PREFERENCIAL,
                                    TarifasManager.Trufi.CORTO_PREFERENCIAL,
                                    TarifasManager.Trufi.LARGO_PREFERENCIAL,
                                    TarifasManager.Trufi.EXTRA_LARGO_PREFERENCIAL
                                )
                            } else {
                                listOf(
                                    TarifasManager.Trufi.getZonal(),
                                    TarifasManager.Trufi.getCorto(),
                                    TarifasManager.Trufi.getLargo(),
                                    TarifasManager.Trufi.getExtraLargo()
                                )
                            }
                            totalAcumulado = contadores.foldIndexed(0.0) { index, acc, count ->
                                acc + (count * nuevasTarifas[index])
                            }
                        },
                        enabled = !isProcessing
                    )
                }
            }

            // Botones de pago por zonas
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                tarifas.forEachIndexed { index, tarifa ->
                    Button(
                        onClick = {
                            totalAcumulado += tarifa
                            contadores = contadores.toMutableList().also { it[index] = it[index] + 1 }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp),
                        enabled = !isProcessing,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (index) {
                                0 -> MaterialTheme.colorScheme.primary
                                1 -> MaterialTheme.colorScheme.secondary
                                2 -> MaterialTheme.colorScheme.tertiary
                                else -> MaterialTheme.colorScheme.primaryContainer
                            }
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = zonas[index],
                                    style = MaterialTheme.typography.titleMedium,
                                    color = when (index) {
                                        0 -> MaterialTheme.colorScheme.onPrimary
                                        1 -> MaterialTheme.colorScheme.onSecondary
                                        2 -> MaterialTheme.colorScheme.onTertiary
                                        else -> MaterialTheme.colorScheme.onPrimaryContainer
                                    }
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    if (contadores[index] > 0) {
                                        Text(
                                            text = "Agregado: ${contadores[index]}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = when (index) {
                                                0 -> MaterialTheme.colorScheme.onPrimary
                                                1 -> MaterialTheme.colorScheme.onSecondary
                                                2 -> MaterialTheme.colorScheme.onTertiary
                                                else -> MaterialTheme.colorScheme.onPrimaryContainer
                                            }
                                        )
                                    }
                                    if (!tarifaPreferencial && esNocturno && contadores[index] == 0) {
                                        Text(
                                            text = "Nocturna",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = when (index) {
                                                0 -> MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                                                1 -> MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.8f)
                                                2 -> MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.8f)
                                                else -> MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                            }
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "Bs. $tarifa",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = when (index) {
                                    0 -> MaterialTheme.colorScheme.onPrimary
                                    1 -> MaterialTheme.colorScheme.onSecondary
                                    2 -> MaterialTheme.colorScheme.onTertiary
                                    else -> MaterialTheme.colorScheme.onPrimaryContainer
                                }
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

                        tarifas.forEachIndexed { index, tarifa ->
                            if (contadores[index] > 0) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("${zonas[index]} (${contadores[index]}):")
                                    Text("Bs. ${String.format("%.2f", contadores[index] * tarifa)}")
                                }
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

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
                                    contadores = listOf(0, 0, 0, 0)
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
                    Text("1. Presiona los botones para agregar pasajes por zona")
                    Text("2. Revisa el total acumulado")
                    Text("3. Presiona 'Pagar Todo' y acerca el teléfono al NFC")
                }
            }
        }
    }
}